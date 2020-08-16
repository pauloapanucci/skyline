package com.papp.skyline.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TransferBalanceControllerTest extends SkylineTestController{
    @Before
    public void init() throws Exception {
        createUser();
    }

    @Test
    public void shouldSuccessfullyTransferBrlAndReturnBtcBalance() throws Exception {
        MvcResult result = testPost(API_TRANSFER_BALANCE, getTransacttionJsonWithAmount("1000"), status().isOk());
        BigDecimal btcBalanceReturned = new BigDecimal(result.getResponse().getContentAsString());
        Assert.assertEquals(new BigDecimal("1000.00"), btcBalanceReturned);
    }

    @Test
    public void shouldSuccessfullyTransferBrlAndReturnBrlBalanceAccumulated() throws Exception {
        MvcResult result = testPost(API_TRANSFER_BALANCE, getTransacttionJsonWithAmount("1000"), status().isOk());
        BigDecimal brlBalanceReturned = new BigDecimal(result.getResponse().getContentAsString());
        Assert.assertEquals(new BigDecimal("1000.00"), brlBalanceReturned);

        result = testPost(API_TRANSFER_BALANCE, getTransacttionJsonWithAmount("1000"), status().isOk());
        brlBalanceReturned = new BigDecimal(result.getResponse().getContentAsString());
        Assert.assertEquals(new BigDecimal("2000.00"), brlBalanceReturned);
    }

    @Test
    public void shouldNotSuccessfullyBTransferBrlDueToNegativeAmount() throws Exception {
        testPost(API_TRANSFER_BALANCE, getTransacttionJsonWithAmount("-1"), status().isInternalServerError());
    }

    @Test
    public void shouldNotSuccessfullyTransferBrlDueZeroTransactionAmount() throws Exception {
        testPost(API_TRANSFER_BALANCE, getTransacttionJsonWithAmount("0"), status().isInternalServerError());
    }

    @Test
    public void shouldReturnBadRequestWhenNotPassCpfToRequest() throws Exception {
        testPost(API_TRANSFER_BALANCE, BRL_TRANSACTION_WITHOUT_CPF, status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestWhenNotPassAmountToRequest() throws Exception {
        testPost(API_TRANSFER_BALANCE, BRL_TRANSACTION_WITHOUT_AMOUNT, status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestWhenNotPassCpfAndToRequest() throws Exception {
        mockMvc.perform(post(API_TRANSFER_BALANCE))
                .andExpect(status().isBadRequest());
    }
}
