package com.papp.skyline.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.papp.skyline.bitcoinApis.BtcPrice;
import com.papp.skyline.dto.ValueDTO;
import com.papp.skyline.entity.Transaction;
import com.papp.skyline.entity.TransactionType;
import com.papp.skyline.entity.User;
import com.papp.skyline.entity.Wallet;
import com.papp.skyline.repository.TransactionRepository;
import com.papp.skyline.repository.UserRepository;
import com.papp.skyline.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private void addBrlBalance(Wallet wallet, BigDecimal amount) {
        wallet.setBrlBalance(wallet.getBrlBalance().add(amount));
    }

    private void subtractBrlBalance(Wallet wallet, BigDecimal amount) {
        wallet.setBrlBalance(wallet.getBrlBalance().subtract(amount));
    }

    private void addBtcBalance(Wallet wallet, BigDecimal amount) {
        wallet.setBtcBalance(wallet.getBtcBalance().add(amount));
    }

    private boolean haveEnoughBalanceToBuy(Wallet wallet, BigDecimal amount) {
        return wallet.getBrlBalance().compareTo(amount) >= 0;
    }

    public BigDecimal transferBalance(String cpf, BigDecimal amount) {
        if(amount.compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }
        User user = userRepository.findByCpf(cpf);
        Wallet wallet = user.getWallet();
        addBrlBalance(wallet, amount);
        walletRepository.save(wallet);
        transactionRepository.save(
                new Transaction.Builder(user, amount, TransactionType.MONEY_TRANSFER).build());
        return wallet.getBrlBalance();
    }

    public BigDecimal buyBtcInBrl(String cpf, BigDecimal amount) {
        if(amount.compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }
        BtcPrice btcPrice;
        try {
            btcPrice = BtcPrice.inBrl();
        } catch (JsonProcessingException e) {
            return null;
        }
        if(Objects.isNull(btcPrice)){
            return null;
        }
        BigDecimal priceOfTransactionInBrl = btcPrice.getAmount().multiply(amount);
        User user = userRepository.findByCpf(cpf);
        Wallet wallet = user.getWallet();
        if (!haveEnoughBalanceToBuy(wallet, priceOfTransactionInBrl)) {
            return null;
        }
        addBtcBalance(wallet, amount);
        subtractBrlBalance(wallet, priceOfTransactionInBrl);
        walletRepository.save(wallet);
        transactionRepository.save(
                new Transaction.Builder(user, amount, TransactionType.BITCOIN_ACQUISITION).brlAmount(priceOfTransactionInBrl).build());
        return wallet.getBtcBalance();
    }

    public BigDecimal getCurrentBrlBalanceValue(String cpf) {
        return userRepository.findByCpf(cpf).getWallet().getBrlBalance();
    }

    public BigDecimal getCurrentBtcBalanceValue(String cpf) {
        return userRepository.findByCpf(cpf).getWallet().getBtcBalance();
    }

    private BigDecimal getAmountOfBrlInvestedInBtc(User user) {
        return user.getTransactions()
                .stream()
                .filter(transaction ->
                        TransactionType.BITCOIN_ACQUISITION.equals(transaction.getTransactionType()))
                .map(transaction -> transaction.getBrlAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getAmountOfBrlInvestedInBtc(String cpf) {
        return getAmountOfBrlInvestedInBtc(userRepository.findByCpf(cpf));
    }

    public BigDecimal getBtcPriceInBrl() {
        try {
            return BtcPrice.inBrl().getAmount();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal calcProfit(BigDecimal amountOfBrlInvestedInBtc, BigDecimal curretnBtcPriceInBrl, BigDecimal amountOfBtc) {
        if(amountOfBrlInvestedInBtc.equals(BigDecimal.ZERO)) {
            return BigDecimal.ZERO;
        }
        BigDecimal currentPrice = amountOfBtc.multiply(curretnBtcPriceInBrl);
        return currentPrice.subtract(amountOfBrlInvestedInBtc);
    }

    public BigDecimal getBtcProfitSoFar(String cpf) {
        User user = userRepository.findByCpf(cpf);
        BigDecimal amountOfBrlInvestedInBtc = getAmountOfBrlInvestedInBtc(user);
        BigDecimal curretnBtcPriceInBrl = getBtcPriceInBrl();
        BigDecimal amountOfBtc = user.getWallet().getBtcBalance();
        return calcProfit(amountOfBrlInvestedInBtc, curretnBtcPriceInBrl, amountOfBtc);
    }

    public List<Transaction> getLast5Transactions(String cpf, int quantity) {
        List<Transaction> transactions =
            userRepository.findByCpf(cpf).getTransactions()
                .stream()
                .sorted(Comparator.comparingLong(Transaction::getId).reversed())
                .collect(Collectors.toList());

        return transactions.size() <= quantity ? transactions : transactions.subList(0, quantity);
    }

    public ValueDTO toValueDTO(BigDecimal value) {
        return new ValueDTO(value);
    }
}
