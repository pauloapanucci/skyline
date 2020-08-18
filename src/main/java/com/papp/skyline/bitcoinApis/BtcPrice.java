package com.papp.skyline.bitcoinApis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Component
public class BtcPrice {

    private final static Logger LOG = LoggerFactory.getLogger(BtcPrice.class);

    private static RestTemplate restTemplate;

    @Autowired
    public BtcPrice(RestTemplate restTemplate) {
        BtcPrice.restTemplate = restTemplate;
    }

    private static String BTC_PRICE_API_URL = "https://api.coinbase.com/v2/prices/spot?currency=";

    private String base;
    private String currency;
    private BigDecimal amount;

    private BtcPrice(String base, String currency, BigDecimal amount) {
        this.base = base;
        this.currency = currency;
        this.amount = amount;
    }

    public String getBase() {
        return base;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    private static JsonNode getContentInJson(String ulr, RestTemplate restTemplate) throws JsonProcessingException {
        ResponseEntity<String> response
                = restTemplate.getForEntity(ulr, String.class);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(response.getBody());
    }

    private static BtcPrice jsonToBtcPrice(JsonNode jsonNode) {
        String base = jsonNode.path("data").path("base").asText();
        String currency = jsonNode.path("data").path("currency").asText();
        BigDecimal amount = new BigDecimal(jsonNode.path("data").path("amount").asText());
        return new BtcPrice(base, currency, amount);
    }

    public static BtcPrice inBrl() throws JsonProcessingException {
        LOG.debug("API url user to get BtcPrice inBrl: " + BTC_PRICE_API_URL + "BRL");
        JsonNode jsonNode = getContentInJson(BTC_PRICE_API_URL + "BRL", restTemplate);
        return jsonToBtcPrice(jsonNode);
    }
}
