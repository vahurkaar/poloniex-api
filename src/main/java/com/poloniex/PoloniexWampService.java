package com.poloniex;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.poloniex.model.PoloniexOrderBookTickerData;
import com.poloniex.model.PoloniexTickerData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Subscription;
import ws.wamp.jawampa.WampClient;
import ws.wamp.jawampa.WampClientBuilder;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Vahur Kaar (<a href="mailto:vahur.kaar@tieto.com">vahur.kaar@tieto.com</a>)
 * @since 10.04.2017.
 */
public abstract class PoloniexWampService {

    private final Logger logger = LoggerFactory.getLogger(PoloniexWampService.class);

    private static final String HOST = "wss://api.poloniex.com";
    private static final String REALM = "realm1";
    private static final int MIN_RECONNECT_INTERVAL = 5; // sec

    private WampClient client;
    private ObjectMapper objectMapper = new ObjectMapper();

    private Subscription tickerSubscription;
    private Map<String, Subscription> orderbookSubscriptions = new HashMap<>();

    @PostConstruct
    public void init() {
        initializePoloniexWampClient();
    }

    private void initializePoloniexWampClient() {
        logger.debug("Initializing Poloniex Wamp Client...");
        try {
            // Create a builder and configure the client
            WampClientBuilder builder = new WampClientBuilder();
            builder.withUri(HOST)
                    .withRealm(REALM)
                    .withInfiniteReconnects()
                    .withReconnectInterval(MIN_RECONNECT_INTERVAL, TimeUnit.SECONDS);
            // Create a client through the builder. This will not immediately start
            // a connection attempt
            client = builder.build();

            // subscribe for session status changes
            client.statusChanged().subscribe(status -> {
                logger.info("Session status changed to " + status);

                if (status == WampClient.Status.Connected) {
                    // once it's connected, subscribe to Add events and request the comments.
                    onConnected();
                }
            });

            // request to open the connection with the server
            client.open();
        } catch (Exception ignored) {
        }
    }

    protected void onConnected() {
        listenTicker();
    }

    protected void listenTicker() {
        logger.debug("Starting ticker process...");
        tickerSubscription = client.makeSubscription("ticker").subscribe(next -> {
            PoloniexTickerData tickerData = convertToTickerData(next.arguments());
            onTickerData(tickerData);
        }, error -> {
            if (error != null) {
                logger.error("ticker call got exception: ", error);
            }
        });
    }

    protected void stopTicker() {
        if (tickerSubscription != null && !tickerSubscription.isUnsubscribed()) {
            tickerSubscription.unsubscribe();
        }
    }

    protected void listenOrderbook(String currencyPair) {
        if (!orderbookSubscriptions.containsKey(currencyPair)) {
            logger.debug("Subscribing to {} order book", currencyPair);
            Subscription orderbookSubscription = client.makeSubscription(currencyPair).subscribe(next -> {
                List<PoloniexOrderBookTickerData> orderBookTicker = convertToOrderBook(next.arguments());
                onOrderBookTicker(currencyPair, orderBookTicker);
            }, error -> {
                if (error != null) {
                    logger.error("orderbook " + currencyPair + " call got exception", error);
                }
            });

            orderbookSubscriptions.put(currencyPair, orderbookSubscription);
        }
    }

    protected abstract void onOrderBookTicker(String currencyPair, List<PoloniexOrderBookTickerData> orderBookTickerData);

    public void stopOrderbook(String currencyPair) {
        logger.debug("Unsubscribing from {} order book", currencyPair);
        if (orderbookSubscriptions.containsKey(currencyPair)) {
            Subscription orderbookSubscription = orderbookSubscriptions.remove(currencyPair);
            orderbookSubscription.unsubscribe();
        }
    }

    protected List<PoloniexOrderBookTickerData> convertToOrderBook(ArrayNode data) {
        List<PoloniexOrderBookTickerData> result = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            result.add(objectMapper.convertValue(data.get(i), PoloniexOrderBookTickerData.class));
        }
        return result;
    }

    private PoloniexTickerData convertToTickerData(ArrayNode data) {
        return new PoloniexTickerData(
                null,
                data.get(0).asText(),
                new BigDecimal(data.get(1).asText()),
                new BigDecimal(data.get(2).asText()),
                new BigDecimal(data.get(3).asText()),
                new BigDecimal(data.get(4).asText()),
                new BigDecimal(data.get(5).asText()),
                new BigDecimal(data.get(6).asText()),
                data.get(7).intValue() > 0,
                new BigDecimal(data.get(8).asText()),
                new BigDecimal(data.get(9).asText())
        );
    }

    protected abstract void onTickerData(PoloniexTickerData tickerData);
}
