package com.papp.skyline.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.papp.skyline.dto.ValueDTO;
import com.papp.skyline.entity.Transaction;
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
@RequestMapping("wallets")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/currentbrlbalance")
    public ResponseEntity<ValueDTO> getCurrentBrlValue(@RequestParam String cpf) {
        if(Objects.isNull(cpf) || cpf.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        BigDecimal value = walletService.getCurrentBrlBalanceValue(cpf);
        return ResponseEntity.ok(walletService.toValueDTO(value));
    }

    @GetMapping("/currentbtcbalance")
    public ResponseEntity<ValueDTO> getCurrentBtcValue(@RequestParam String cpf) {
        if(Objects.isNull(cpf) || cpf.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        BigDecimal value = walletService.getCurrentBtcBalanceValue(cpf);
        return ResponseEntity.ok(walletService.toValueDTO(value));
    }

    @GetMapping("/totalbrlamountinvestedinbtc")
    public ResponseEntity<ValueDTO> getAmountOfBrlInvestedInBtc(@RequestParam String cpf) {
        if(Objects.isNull(cpf) || cpf.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        BigDecimal value = walletService.getAmountOfBrlInvestedInBtc(cpf);
        return ResponseEntity.ok(walletService.toValueDTO(value));
    }

    @GetMapping("/profitsincefirstbtctransaction")
    public ResponseEntity<ValueDTO> getBtcProfitSoFar(@RequestParam String cpf) {
        if(Objects.isNull(cpf) || cpf.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        BigDecimal value = walletService.getBtcProfitSoFar(cpf);
        return ResponseEntity.ok(walletService.toValueDTO(value));
    }

    @GetMapping("/currentbtcvalue")
    public ResponseEntity<ValueDTO> getBtcPriceInBrl() {
        BigDecimal value = walletService.getBtcPriceInBrl();
        return ResponseEntity.ok(walletService.toValueDTO(value));
    }

    @GetMapping("/last5transactions")
    public ResponseEntity<List<Transaction>> getLast5Transactions(@RequestParam String cpf) {
        if(Objects.isNull(cpf) || cpf.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(walletService.getLast5Transactions(cpf, 5));
    }
}
