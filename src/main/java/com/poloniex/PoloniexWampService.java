package com.poloniex;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.poloniex.model.PoloniexTickerData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.schedulers.ImmediateScheduler;
import ws.wamp.jawampa.PubSubData;
import ws.wamp.jawampa.WampClient;
import ws.wamp.jawampa.WampClientBuilder;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.HashMap;
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
    private Observable<PubSubData> tickerSubscription;
    private Map<String, Observable<PubSubData>> orderbookSubscriptions = new HashMap<>();

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
        tickerSubscription = client.makeSubscription("ticker");
        tickerSubscription.subscribe(next -> {
            PoloniexTickerData tickerData = convertToData(next.arguments());
            onTickerData(tickerData);
        }, error -> {
            if (error != null) {
                logger.error("ticker call got exception: ", error);
            }
        });
    }

    protected void listenOrderbook(String currencyPair) {
        if (!orderbookSubscriptions.containsKey(currencyPair)) {
            Observable<PubSubData> orderbookSubscription = client.makeSubscription(currencyPair);
            orderbookSubscriptions.put(currencyPair, orderbookSubscription);

            orderbookSubscription.subscribe(next -> {
                logger.debug(currencyPair + ": " + next.arguments().toString());
            }, error -> {
                if (error != null) {
                    logger.error("orderbook " + currencyPair + " call got exception", error);
                }
            });
        }
    }

//    protected void stopListeningOnOrderbook(String currencyPair) {
//        if (orderbookSubscriptions.containsKey(currencyPair)) {
//            Observable<PubSubData> orderbookSubscription = orderbookSubscriptions.remove(currencyPair);
//            orderbookSubscription.unsubscribeOn();
//        }
//    }

    private PoloniexTickerData convertToData(ArrayNode data) {
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
