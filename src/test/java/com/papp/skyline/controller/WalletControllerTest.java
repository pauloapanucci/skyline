package com.papp.skyline.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.papp.skyline.entity.Transaction;
import com.papp.skyline.entity.TransactionType;
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
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class WalletControllerTest extends SkylineTestController {
    @Autowired
    private RestTemplate restTemplate;
    private MockRestServiceServer mockServer;

    @Autowired
    private ObjectMapper mapper;

    private static boolean initIsDone = false;

    @Before
    public void init() throws Exception {
        if(initIsDone) {
            return;
        }

        mockServer = MockRestServiceServer.createServer(restTemplate);

        mockServercConfig(BTC_PRICE_API_BTC_URL_BRL_RESPONSE);

        createUserAndPerformTransactions();

        initIsDone = true;
    }

    @Test
    public void shouldGetCurrentBrlBalance() throws Exception {
        MvcResult mvcResult = testGet(API_WALLET + CURRENT_BRL_BALANCE, CPF, status().isOk());
        String brlBalanceResult = mvcResult.getResponse().getContentAsString();
        Assert.assertEquals(getValueJsonWithAmount("2000.00"), brlBalanceResult);
    }

    @Test
    public void shouldGetCurrentBtcBalance() throws Exception {
        MvcResult mvcResult = testGet(API_WALLET + CURRENT_BTC_BALANCE, CPF, status().isOk());
        String brlBalanceResult = mvcResult.getResponse().getContentAsString();
        Assert.assertEquals(getValueJsonWithAmount("2.00"), brlBalanceResult);
    }

    @Test
    public void shouldGetTotalBrlAmountInvestedInBtc() throws Exception {
        MvcResult mvcResult = testGet(API_WALLET + TOTAL_AMOUNT_INVESTED_IN_BTC, CPF, status().isOk());
        String brlAmountInvestedInBtcResult = mvcResult.getResponse().getContentAsString();
        Assert.assertEquals(getValueJsonWithAmount("2000.00"), brlAmountInvestedInBtcResult);
    }

    @Test
    public void shouldGetProfitSinceFirstBtcTransaction() throws Exception {
        mockServercConfig(BTC_PRICE_API_BTC_URL_BRL_RESPONSE_PROFIT);
        MvcResult mvcResult = testGet(API_WALLET + PROFIT_SINCE_FIRST_BTC_TRANSACTION, CPF, status().isOk());
        String profitResult = mvcResult.getResponse().getContentAsString();
        Assert.assertEquals(getValueJsonWithAmount("1000.00"), profitResult);
    }

    @Test
    public void shouldGetLossSinceFirstBtcTransaction() throws Exception {
        mockServercConfig(BTC_PRICE_API_BTC_URL_BRL_RESPONSE_LOSS);
        MvcResult mvcResult = testGet(API_WALLET + PROFIT_SINCE_FIRST_BTC_TRANSACTION, CPF, status().isOk());
        String profitResult = mvcResult.getResponse().getContentAsString();
        Assert.assertEquals(getValueJsonWithAmount("-1000.00"), profitResult);
    }

    @Test
    public void shouldGetLast5transactions() throws Exception {
        MvcResult mvcResult = testGet(API_WALLET + LAST_5_TRANSACTIONS, CPF, status().isOk());
        List<Transaction> transactionsResult = Arrays.asList(mapper.readValue(mvcResult.getResponse().getContentAsString(), Transaction[].class));

        Assert.assertEquals(5, transactionsResult.size());

        Assert.assertEquals(TransactionType.MONEY_TRANSFER, transactionsResult.get(0).getTransactionType());
        Assert.assertEquals(new BigDecimal("2000.00"), transactionsResult.get(0).getAmount());
        Assert.assertEquals(new BigDecimal("0.00"), transactionsResult.get(0).getBrlAmount());

        Assert.assertEquals(TransactionType.BITCOIN_ACQUISITION, transactionsResult.get(1).getTransactionType());
        Assert.assertEquals(new BigDecimal("0.50"), transactionsResult.get(1).getAmount());
        Assert.assertEquals(new BigDecimal("500.00"), transactionsResult.get(1).getBrlAmount());

        Assert.assertEquals(TransactionType.BITCOIN_ACQUISITION, transactionsResult.get(2).getTransactionType());
        Assert.assertEquals(new BigDecimal("0.50"), transactionsResult.get(2).getAmount());
        Assert.assertEquals(new BigDecimal("500.00"), transactionsResult.get(2).getBrlAmount());

        Assert.assertEquals(TransactionType.MONEY_TRANSFER, transactionsResult.get(3).getTransactionType());
        Assert.assertEquals(new BigDecimal("1000.00"), transactionsResult.get(3).getAmount());
        Assert.assertEquals(new BigDecimal("0.00"), transactionsResult.get(3).getBrlAmount());

        Assert.assertEquals(TransactionType.BITCOIN_ACQUISITION, transactionsResult.get(4).getTransactionType());
        Assert.assertEquals(new BigDecimal("0.50"), transactionsResult.get(4).getAmount());
        Assert.assertEquals(new BigDecimal("500.00"), transactionsResult.get(4).getBrlAmount());
    }

    private void createUserAndPerformTransactions() throws Exception {
        createUser();
        testPost(API_TRANSFER_BALANCE, getTransactionJsonWithAmount("1000"), status().isOk());
        testPost(API_BTC, getTransactionJsonWithAmount("0.5"), status().isOk());
        testPost(API_BTC, getTransactionJsonWithAmount("0.5"), status().isOk());
        testPost(API_TRANSFER_BALANCE, getTransactionJsonWithAmount("1000"), status().isOk());
        testPost(API_BTC, getTransactionJsonWithAmount("0.5"), status().isOk());
        testPost(API_BTC, getTransactionJsonWithAmount("0.5"), status().isOk());
        testPost(API_TRANSFER_BALANCE, getTransactionJsonWithAmount("2000"), status().isOk());
    }

    private void mockServercConfig(String response) throws URISyntaxException {
        if(mockServer != null) {
            mockServer.reset();
        }
        mockServer = MockRestServiceServer.createServer(restTemplate);
        mockServer.expect(ExpectedCount.manyTimes(),
                requestTo(new URI(BTC_PRICE_API_BTC_URL_BRL)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response)
                );
    }
}
