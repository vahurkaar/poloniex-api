package com.poloniex;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poloniex.model.PoloniexOrder;
import com.poloniex.model.PoloniexOrderBook;
import com.poloniex.model.OrderResult;
import com.poloniex.model.PoloniexBalance;
import com.poloniex.model.PoloniexChartData;
import com.poloniex.model.PoloniexCurrency;
import com.poloniex.model.PoloniexOrderType;
import com.poloniex.model.PoloniexTickerData;
import com.poloniex.model.Trade;
import com.poloniex.util.HmacSha1Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 26/03/2017
 */
@Service
public class PoloniexRestService {

    private final Logger logger = LoggerFactory.getLogger(PoloniexRestService.class);

    private static final String PUBLIC_API_URL = "https://poloniex.com/public";
    private static final String TRADING_API_URL = "https://poloniex.com/tradingApi";

    @Value("${poloniex.api.secret}")
    private String secret;

    @Value("${poloniex.api.key}")
    private String apiKey;

    private ObjectMapper objectMapper = new ObjectMapper();

    public List<PoloniexChartData> returnChartData(String currencyPair,
                                                   long startTimeInSeconds,
                                                   int candlePeriodInMinutes) {
        long time = System.currentTimeMillis();
        logger.debug("Requesting chart data for " + currencyPair +
                " (startTimeInSeconds - " + startTimeInSeconds + ", candlePeriodInMinutes - " + candlePeriodInMinutes);
        RestTemplate restTemplate = createRestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        long currentTime = System.currentTimeMillis() / 1000;
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(PUBLIC_API_URL + "?command=returnChartData")
                .queryParam("currencyPair", currencyPair)
                .queryParam("period", candlePeriodInMinutes * 60)
                .queryParam("start", startTimeInSeconds)
                .queryParam("end", currentTime);

        logger.debug("GetChartData url: " + builder.build().encode().toUriString());
        HttpEntity<?> entity = new HttpEntity<>(headers);

        HttpEntity<String> response = restTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET, entity, String.class);

        List<PoloniexChartData> result = new ArrayList<>();
        try {
            result = objectMapper.readValue(response.getBody(), new TypeReference<List<PoloniexChartData>>() {
            });
            logger.debug("CharData: numberOfResults {}", result.size());
        } catch (IOException e) {
            logger.error("Exception when convertin result: ", e);
        }

        logger.debug("returnChartData: execution time: {} sec", (double) (System.currentTimeMillis() - time) / 1000);
        return result;
    }

    public Map<String, PoloniexTickerData> getTickerData() {
        Map<String, PoloniexTickerData> result = new TreeMap<>();
        RestTemplate restTemplate = createRestTemplate();
        Map tickerData = restTemplate.getForObject(PoloniexRestService.PUBLIC_API_URL + "?command=returnTicker", Map.class);

        for (Object entry : tickerData.entrySet()) {
            Map.Entry tickerEntry = (Map.Entry) entry;
            if (!tickerEntry.getKey().toString().startsWith("BTC_")) continue;

            try {
                result.put((String) ((Map.Entry) entry).getKey(), convertTickerData(tickerEntry));
            } catch (IOException e) {
                logger.error("Failed to deserialize ticker JSON data: " + tickerEntry.getValue(), e);
            }
        }

        return result;
    }

    public List<String> getCurrencies() {
        List<String> result = new ArrayList<>();
        RestTemplate restTemplate = createRestTemplate();
        Map currencies = restTemplate.getForObject(PoloniexRestService.PUBLIC_API_URL + "?command=returnCurrencies", Map.class);

        for (Object currency : currencies.entrySet()) {
            Map.Entry currencyEntry = (Map.Entry) currency;
            PoloniexCurrency poloniexCurrency = objectMapper.convertValue(currencyEntry.getValue(), PoloniexCurrency.class);

            if (!ignoreCurrency(poloniexCurrency)) {
                result.add(currencyEntry.getKey().toString());
            }
        }

        return result;
    }

    public Map<String, PoloniexBalance> returnCompleteBalances() {
        Map<String, PoloniexBalance> balanceData = new HashMap<>();
        RestTemplate restTemplate = createRestTemplate();

        String requestData = "command=returnCompleteBalances&nonce=" + generateNonce();
        HttpHeaders requestHeaders = new HttpHeaders();
        addSecurityHeaders(requestData, requestHeaders);
        requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>(requestData, requestHeaders);
        Map responseData = restTemplate.postForObject(TRADING_API_URL, entity, Map.class);
        for (Object entry : responseData.entrySet()) {
            Map.Entry entryObject = (Map.Entry) entry;
            PoloniexBalance value = objectMapper.convertValue(entryObject.getValue(), PoloniexBalance.class);

            if (value.getAvailable().compareTo(BigDecimal.ZERO) > 0) {
                String currency = entryObject.getKey().toString();

                value.setCurrency(currency);
                balanceData.put(currency, value);
            }
        }

        return balanceData;
    }

    public Map<String, List<PoloniexOrder>> returnOpenOrders() {
        return returnOpenOrders(null);
    }

    public Map<String, List<PoloniexOrder>> returnOpenOrders(String currencyPair) {
        Map<String, List<PoloniexOrder>> openOrders = new HashMap<>();
        RestTemplate restTemplate = createRestTemplate();

        if (currencyPair == null) currencyPair = "all";
        String requestData = "command=returnOpenOrders&currencyPair=" + currencyPair + "&nonce=" + generateNonce();
        HttpHeaders requestHeaders = new HttpHeaders();
        addSecurityHeaders(requestData, requestHeaders);
        requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>(requestData, requestHeaders);
        Map responseData = restTemplate.postForObject(TRADING_API_URL, entity, Map.class);
        for (Object entry : responseData.entrySet()) {
            Map.Entry entryObject = (Map.Entry) entry;
            PoloniexOrder[] openOrdersArray = objectMapper.convertValue(entryObject.getValue(), PoloniexOrder[].class);
            openOrders.put(entryObject.getKey().toString(), Arrays.asList(openOrdersArray));
        }

        return openOrders;
    }

    public PoloniexOrderBook returnOrderBook(String currencyPair, Integer depth) {
        logger.debug("Requesting order book data for " + currencyPair + " (depth - " + depth + ")");
        RestTemplate restTemplate = createRestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(PUBLIC_API_URL)
                .queryParam("command", "returnOrderBook")
                .queryParam("currencyPair", currencyPair)
                .queryParam("depth", depth);

        logger.debug("ReturnOrderBook url: " + builder.build().encode().toUriString());
        HttpEntity<?> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET, entity, PoloniexOrderBook.class).getBody();
    }

    public Map<String, PoloniexOrderBook> returnOrderBook(Integer depth) {
        logger.debug("Requesting order book data for all currencies (depth - " + depth + ")");
        long startTime = System.currentTimeMillis();
        RestTemplate restTemplate = createRestTemplate();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(PUBLIC_API_URL)
                .queryParam("command", "returnOrderBook")
                .queryParam("currencyPair", "all")
                .queryParam("depth", depth);

        logger.debug("ReturnOrderBook url: " + builder.build().encode().toUriString());

        Map responseData = restTemplate.getForObject(builder.build().encode().toUriString(), Map.class);
        Map<String, PoloniexOrderBook> result = new HashMap<>();
        for (Object entry : responseData.entrySet()) {
            Map.Entry entryObject = (Map.Entry) entry;
            PoloniexOrderBook value = objectMapper.convertValue(entryObject.getValue(), PoloniexOrderBook.class);
            value.setCurrencyPair(entryObject.getKey().toString());
            result.put(entryObject.getKey().toString(), value);
        }

        logger.debug("Request took {} ms", System.currentTimeMillis() - startTime);
        return result;
    }

    public OrderResult buy(String currencyPair, BigDecimal rate, BigDecimal amount, PoloniexOrderType orderType) {
        RestTemplate restTemplate = createRestTemplate();
        if (currencyPair == null) throw new IllegalArgumentException("Currency pair cannot be null");

        String requestData = "command=buy&currencyPair=" + currencyPair +
                "&rate=" + rate.setScale(10, RoundingMode.HALF_UP).toPlainString() +
                "&amount=" + amount.setScale(10, RoundingMode.HALF_UP).toPlainString();
        if (orderType != null) {
            requestData += String.format("&%s=1", orderType.getParam());
        }
        requestData += "&nonce=" + generateNonce();

        HttpHeaders requestHeaders = new HttpHeaders();
        addSecurityHeaders(requestData, requestHeaders);
        requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>(requestData, requestHeaders);
        OrderResult orderResult;
        try {
            orderResult = restTemplate.postForObject(TRADING_API_URL, entity, OrderResult.class);
            logger.debug("Buy response: {}", orderResult);
        } catch (RestClientException e) {
            orderResult = new OrderResult();
            logger.error("Error when buying {} {} with rate {}", amount, currencyPair.split("_")[1], rate);
            logger.error("Got exception", e);
        }

        return orderResult;
    }

    public OrderResult sell(String currencyPair, BigDecimal rate, BigDecimal amount, PoloniexOrderType orderType) {
        RestTemplate restTemplate = createRestTemplate();
        if (currencyPair == null) throw new IllegalArgumentException("Currency pair cannot be null");

        String requestData = "command=sell&currencyPair=" + currencyPair +
                "&rate=" + rate.setScale(10, RoundingMode.HALF_UP).toPlainString() +
                "&amount=" + amount.setScale(10, RoundingMode.HALF_UP).toPlainString();
        if (orderType != null) {
            requestData += String.format("&%s=1", orderType.getParam());
        }
        requestData += "&nonce=" + generateNonce();

        HttpHeaders requestHeaders = new HttpHeaders();
        addSecurityHeaders(requestData, requestHeaders);
        requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>(requestData, requestHeaders);
        OrderResult orderResult;
        try {
            orderResult = restTemplate.postForObject(TRADING_API_URL, entity, OrderResult.class);
            logger.debug("Sell response: {}", orderResult);
            return orderResult;
        } catch (RestClientException e) {
            orderResult = new OrderResult();
            logger.error("Error when selling {} {} with rate {}", amount, currencyPair.split("_")[1], rate);
            logger.error("Got exception", e);
        }

        return orderResult;
    }

    public Trade[] returnOrderTrades(String orderNumber) {
        RestTemplate restTemplate = createRestTemplate();
        if (orderNumber == null) throw new IllegalArgumentException("Order number cannot be null");

        String requestData = "command=returnOrderTrades&orderNumber=" + orderNumber + "&nonce=" + generateNonce();

        HttpHeaders requestHeaders = new HttpHeaders();
        addSecurityHeaders(requestData, requestHeaders);
        requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>(requestData, requestHeaders);
        Trade[] trades;
        try {
            trades = restTemplate.postForObject(TRADING_API_URL, entity, Trade[].class);
            logger.debug("Open trades response: {}", trades);
            return trades;
        } catch (RestClientException e) {
            trades = new Trade[] {};
            logger.debug("Failed to find any trades for order {}", orderNumber);
        }

        return trades;
    }

    private Long generateNonce() {
        return System.currentTimeMillis();
    }

    private void addSecurityHeaders(String data, HttpHeaders requestHeaders) {
        try {
            requestHeaders.set("Key", apiKey);
            requestHeaders.set("Sign", HmacSha1Signature.calculateRFC2104HMAC(data, secret));
        } catch (Exception e) {
            logger.error("Failed to sign data!");
        }
    }

    private PoloniexTickerData convertTickerData(Map.Entry tickerEntry) throws IOException {
        String tickerName = (String) tickerEntry.getKey();
        PoloniexTickerData tickerData = objectMapper.convertValue(tickerEntry.getValue(), PoloniexTickerData.class);
        tickerData.setCurrencyPair(tickerName);

        logger.debug("TickerEntry (" + tickerName + "): " + tickerData);
        return tickerData;
    }

    private boolean ignoreCurrency(PoloniexCurrency currency) {
        return currency.getDisabled().equals(1) || currency.getDelisted().equals(1) || currency.getFrozen().equals(1);
    }

    private RestTemplate createRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return restTemplate;
    }


    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
