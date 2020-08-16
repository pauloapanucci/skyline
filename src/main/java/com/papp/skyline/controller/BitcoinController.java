package com.papp.skyline.controller;

import com.papp.skyline.dto.TransactionDTO;
import com.papp.skyline.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.Objects;

@Controller
@RequestMapping("api/bitcoins")
public class BitcoinController {

    @Autowired
    private WalletService walletService;

    @PostMapping("buy")
    public ResponseEntity<BigDecimal> buy(@RequestBody TransactionDTO transaction){

        if(Objects.isNull(transaction.getCpf()) || Objects.isNull(transaction.getAmount())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        BigDecimal currentBtcBalance = walletService.buyBtcInBrl(transaction.getCpf(), transaction.getAmount());
        if(Objects.isNull(currentBtcBalance)) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(currentBtcBalance, HttpStatus.OK);
    }
}
