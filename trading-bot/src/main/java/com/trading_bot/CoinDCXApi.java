package com.trading_bot;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.google.gson.Gson;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import io.github.cdimascio.dotenv.Dotenv;


public class CoinDCXApi {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String BASE_URL = "https://api.coindcx.com";
    private static final String API_KEY = dotenv.get("API_KEY"); // replace with your key
    private static final String API_SECRET = dotenv.get("API_SECRET");; // replace with your secret
    private static final Gson gson = new Gson();

    public static void createOrder(String side, String market, double price, double quantity, String clientOrderId) throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("side", side);
        body.put("order_type", "limit_order");
        body.put("market", market);
        body.put("price_per_unit", price);
        body.put("total_quantity", quantity);
        body.put("timestamp", System.currentTimeMillis());
        body.put("client_order_id", clientOrderId);

        String payload = gson.toJson(body);
        String signature = generateSignature(payload);

        HttpPost post = new HttpPost(BASE_URL + "/exchange/v1/orders/create");
        post.setHeader("X-AUTH-APIKEY", API_KEY);
        post.setHeader("X-AUTH-SIGNATURE", signature);
        post.setEntity(new StringEntity(payload, StandardCharsets.UTF_8));
        post.setHeader("Content-Type", "application/json");

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {
            String responseBody = EntityUtils.toString(response.getEntity());
            System.out.println("Create Order Response: " + responseBody);
        }
    }

    public static void cancelAll(String market) throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("market", market);
        body.put("timestamp", System.currentTimeMillis());

        String payload = gson.toJson(body);
        String signature = generateSignature(payload);

        HttpPost post = new HttpPost(BASE_URL + "/exchange/v1/orders/cancel_all");
        post.setHeader("X-AUTH-APIKEY", API_KEY);
        post.setHeader("X-AUTH-SIGNATURE", signature);
        post.setEntity(new StringEntity(payload, StandardCharsets.UTF_8));
        post.setHeader("Content-Type", "application/json");

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {
            String responseBody = EntityUtils.toString(response.getEntity());
            System.out.println("Cancel All Response: " + responseBody);
        }
    }

    private static String generateSignature(String payload) throws Exception {
        Mac sha256HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(API_SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256HMAC.init(secretKey);
        byte[] hash = sha256HMAC.doFinal(payload.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
