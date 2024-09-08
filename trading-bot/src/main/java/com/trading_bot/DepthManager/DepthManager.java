package com.trading_bot.DepthManager;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class DepthManager {
    private static final Logger logger = LoggerFactory.getLogger(DepthManager.class); // Logger instance
    private String market;
    private RestTemplate restTemplate;
    private HashMap<String, String> bids;
    private HashMap<String, String> asks;

    public DepthManager(String market) {
        this.market = market;
        this.restTemplate = new RestTemplate();
        this.bids = new HashMap<>();
        this.asks = new HashMap<>();

        // Set up a timer to run pollMarket every 3 seconds
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                pollMarket();
            }
        }, 0, 3000); // 0 initial delay, 3000ms (3 seconds) interval
    }

    public void pollMarket() {
        try {
            String url = String.format("https://public.coindcx.com/market_data/orderbook?pair=" + this.market);
            String response = restTemplate.getForObject(url, String.class);
//            logger.info("Market response: {}", response); // Logging market response

            // Parse the response and update bids and asks
            parseMarketResponse(response);

        } catch (Exception e) {
            logger.error("Error in pollMarket: ", e);
        }
    }

    private void parseMarketResponse(String response) {
        try {
            // Create an ObjectMapper instance
            ObjectMapper objectMapper = new ObjectMapper();

            // Parse the response into a JsonNode
            JsonNode rootNode = objectMapper.readTree(response);

            // Get the bids and asks from the JSON response
            JsonNode bidsNode = rootNode.path("bids");
            JsonNode asksNode = rootNode.path("asks");

            // Clear the current bids and asks
            bids.clear();
            asks.clear();

            // Populate bids map
            bidsNode.fieldNames().forEachRemaining(price -> {
                String quantity = bidsNode.path(price).asText();
                bids.put(price, quantity);
            });

            // Populate asks map
            asksNode.fieldNames().forEachRemaining(price -> {
                String quantity = asksNode.path(price).asText();
                asks.put(price, quantity);
            });

            // Log the updated bids and asks
//            logger.info("Bids: {}", bids);
//            logger.info("Asks: {}", asks);

        } catch (Exception e) {
            logger.error("Error parsing market response: ", e);
        }
    }

    public HashMap<String, String> getBids() {
        return bids;
    }

    public HashMap<String, String> getAsks() {
        return asks;
    }

    float getRelevantDepth1() {
        // Initialize variables to store the highest bid
        float highestBid = Float.NEGATIVE_INFINITY;

        // Find the highest bid
        for (String price : bids.keySet()) {
            float bidPrice = Float.parseFloat(price);
            if (bidPrice > highestBid) {
                highestBid = bidPrice;
            }
        }

        return highestBid;
    }

    float getRelevantDepth2() {
        // Initialize variables to store the lowest ask
        float lowestAsk = Float.POSITIVE_INFINITY;

        // Find the lowest ask
        for (String price : asks.keySet()) {
            float askPrice = Float.parseFloat(price);
            if (askPrice < lowestAsk) {
                lowestAsk = askPrice;
            }
        }

        return lowestAsk;
    }
}
