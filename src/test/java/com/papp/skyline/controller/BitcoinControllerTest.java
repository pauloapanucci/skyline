package com.papp.skyline.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BitcoinControllerTest extends SkylineTestController {

    @Autowired
    private RestTemplate restTemplate;
    private MockRestServiceServer mockServer;

    @Before
    public void init() throws Exception {
        mockServer = MockRestServiceServer.createServer(restTemplate);

        mockServer.expect(ExpectedCount.manyTimes(),
                requestTo(new URI(BTC_PRICE_API_BTC_URL_BRL)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BTC_PRICE_API_BTC_URL_BRL_RESPONSE)
                );

        createUserWith1000BtcAmountAtWallet();
    }

    @Test
    public void shouldSuccessfullyBuyBtcAndReturnBtcBalance() throws Exception {
        MvcResult result = testPost(API_BTC, getTransactionJsonWithAmount("0.1"), status().isOk());
        String btcBalanceReturned = result.getResponse().getContentAsString();
        Assert.assertEquals(getValueJsonWithAmount("0.10"), btcBalanceReturned);
    }

    @Test
    public void shouldSuccessfullyBuyBtcAndReturnBtcBalanceAccumulated() throws Exception {
        MvcResult result = testPost(API_BTC, getTransactionJsonWithAmount("0.1"), status().isOk());
        String btcBalanceReturned = result.getResponse().getContentAsString();
        Assert.assertEquals(getValueJsonWithAmount("0.10"), btcBalanceReturned);

        result = testPost(API_BTC, getTransactionJsonWithAmount("0.1"), status().isOk());
        btcBalanceReturned = result.getResponse().getContentAsString();
        Assert.assertEquals(getValueJsonWithAmount("0.20"), btcBalanceReturned);
    }

    @Test
    public void shouldNotSuccessfullyBuyBtcDueToLackOfBrlBalance() throws Exception {
        testPost(API_BTC, getTransactionJsonWithAmount("2"), status().isInternalServerError());
    }

    @Test
    public void shouldNotSuccessfullyBuyBtcDueZeroTransactionAmount() throws Exception {
        testPost(API_BTC, getTransactionJsonWithAmount("0"), status().isInternalServerError());
    }

    @Test
    public void shouldNotSuccessfullyBuyBtcDueNegativeTransactionAmount() throws Exception {
        testPost(API_BTC, getTransactionJsonWithAmount("-1"), status().isInternalServerError());
    }

    @Test
    public void shouldReturnBadRequestWhenNotPassCpfToRequest() throws Exception {
        testPost(API_BTC, BTC_TRANSACTTION_JSON_WITHOUT_CPF, status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestWhenNotPassAmountToRequest() throws Exception {
        testPost(API_BTC, TRANSACTTION_JSON_WITHOUT_AMOUNT, status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestWhenNotPassCpfAndToRequest() throws Exception {
        mockMvc.perform(post(API_BTC))
                .andExpect(status().isBadRequest());
    }

    private void createUserWith1000BtcAmountAtWallet() throws Exception {
        createUser();
        testPost(API_TRANSFER_BALANCE, BRL_TRANSACTION, status().isOk());
    }
}
