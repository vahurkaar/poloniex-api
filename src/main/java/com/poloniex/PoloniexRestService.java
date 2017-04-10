package com.poloniex;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poloniex.model.PoloniexChartData;
import com.poloniex.model.PoloniexCurrency;
import com.poloniex.model.PoloniexTickerData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
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
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://poloniex.com/public?command=returnChartData")
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
            result = objectMapper.readValue(response.getBody(), new TypeReference<List<PoloniexChartData>>() { });
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
        Map tickerData = restTemplate.getForObject("https://poloniex.com/public?command=returnTicker", Map.class);

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
        Map currencies = restTemplate.getForObject("https://poloniex.com/public?command=returnCurrencies", Map.class);

        for (Object currency : currencies.entrySet()) {
            Map.Entry currencyEntry = (Map.Entry) currency;
            PoloniexCurrency poloniexCurrency = objectMapper.convertValue(currencyEntry.getValue(), PoloniexCurrency.class);

            if (!ignoreCurrency(poloniexCurrency)) {
                result.add(currencyEntry.getKey().toString());
            }
        }

        return result;
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
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return restTemplate;
    }
}
