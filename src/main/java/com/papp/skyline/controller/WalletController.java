package com.papp.skyline.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.papp.skyline.model.Transaction;
import com.papp.skyline.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("api/wallets")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/currentbrlbalance")
    public ResponseEntity<BigDecimal> getCurrentBrlValue(@RequestParam String cpf) {
        if(Objects.isNull(cpf) || cpf.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(walletService.getCurrentBrlBalanceValue(cpf));
    }

    @GetMapping("/currentbtcbalance")
    public ResponseEntity<BigDecimal> getCurrentBtcValue(@RequestParam String cpf) {
        if(Objects.isNull(cpf) || cpf.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(walletService.getCurrentBtcBalanceValue(cpf));
    }

    @GetMapping("/totalbrlamountinvestedinbtc")
    public ResponseEntity<BigDecimal> getAmountOfBrlInvestedInBtc(@RequestParam String cpf) {
        if(Objects.isNull(cpf) || cpf.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(walletService.getAmountOfBrlInvestedInBtc(cpf));
    }

    @GetMapping("/profitsincefirstbtctransaction")
    public ResponseEntity<BigDecimal> getBtcProfitSoFar(@RequestParam String cpf) {
        if(Objects.isNull(cpf) || cpf.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(walletService.getBtcProfitSoFar(cpf));
    }

    @GetMapping("/currentbtcvalue")
    public ResponseEntity<BigDecimal> getBtcPriceInBrl() {
        return ResponseEntity.ok(walletService.getBtcPriceInBrl());
    }

    @GetMapping("/last5transactions")
    public ResponseEntity<List<Transaction>> getLast5Transactions(@RequestParam String cpf) {
        if(Objects.isNull(cpf) || cpf.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(walletService.getLast5Transactions(cpf, 5));
    }
}
