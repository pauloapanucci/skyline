package com.papp.skyline.controller;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
abstract class SkylineControllerTest {
    @Autowired
    MockMvc mockMvc;

    protected static String API_USERS = "/users";
    protected static String USER_JSON = "{\"name\":\"Paulo\",\"cpf\":\"01234567890\"}";
    protected static String USER_JSON_WITHOUT_NAME = "{\"cpf\":\"01234567890\"}";
    protected static String USER_JSON_WITHOUT_CPF = "{\"name\": \"Paulo\"}";

    protected static String API_BTC = "/bitcoins/buy";
    protected static String BTC_PRICE_API_BTC_URL_BRL = "https://api.coinbase.com/v2/prices/spot?currency=BRL";
    protected static String BTC_PRICE_API_BTC_URL_BRL_RESPONSE = "{\"data\":{\"base\":\"BTC\",\"currency\":\"BRL\",\"amount\":\"1000\"}}";
    protected static String BTC_PRICE_API_BTC_URL_BRL_RESPONSE_PROFIT = "{\"data\":{\"base\":\"BTC\",\"currency\":\"BRL\",\"amount\":\"1500\"}}";
    protected static String BTC_PRICE_API_BTC_URL_BRL_RESPONSE_LOSS = "{\"data\":{\"base\":\"BTC\",\"currency\":\"BRL\",\"amount\":\"500\"}}";
    protected static String BTC_TRANSACTTION_JSON_WITHOUT_CPF = "{\"amount\":\"0.1\"}";
    protected static String TRANSACTTION_JSON_WITHOUT_AMOUNT = "{\"cpf\": \"01234567890\"}";

    protected static String API_TRANSFER_BALANCE = "/balances/transfer";
    protected static String BRL_TRANSACTION = "{\"cpf\":\"01234567890\", \"amount\":\"1000\"}";
    protected static String BRL_TRANSACTION_WITHOUT_CPF = "{\"amount\":\"1000\"}";
    protected static String BRL_TRANSACTION_WITHOUT_AMOUNT = "{\"cpf\":\"01234567890\"}";

    protected static String API_WALLET = "/wallets";
    protected static String CURRENT_BRL_BALANCE = "/currentbrlbalance";
    protected static String CURRENT_BTC_BALANCE = "/currentbtcbalance";
    protected static String TOTAL_AMOUNT_INVESTED_IN_BTC = "/totalbrlamountinvestedinbtc";
    protected static String PROFIT_SINCE_FIRST_BTC_TRANSACTION = "/profitsincefirstbtctransaction";
    protected static String CURRENT_BTC_VALUE = "/currentbtcvalue";
    protected static String LAST_5_TRANSACTIONS = "/last5transactions";
    protected static String CPF = "01234567890";

    protected MvcResult testPost(String API, String content, ResultMatcher resultMatcher) throws Exception {
        return mockMvc.perform(post(API)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(resultMatcher)
                .andReturn();
    }

    protected MvcResult testGet(String API, String param, ResultMatcher resultMatcher) throws Exception {
        return mockMvc.perform(get(API)
                .contentType(MediaType.APPLICATION_JSON)
                .param("cpf", param))
                .andExpect(resultMatcher)
                .andReturn();
    }

    protected MvcResult createUser() throws Exception {
        return testPost(API_USERS, USER_JSON, status().isCreated());
    }

    protected String getTransactionJsonWithAmount(String amount) {
        return "{\"cpf\": \"01234567890\", \"amount\":\"" + amount + "\" }";
    }

    protected String getValueJsonWithAmount(String amount) {
        return "{\"value\":" + amount + "}";
    }
}
