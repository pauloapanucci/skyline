package com.papp.skyline.service;

import com.papp.skyline.entity.User;
import com.papp.skyline.entity.Wallet;
import com.papp.skyline.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

@Service
public class UserService {

    private final static Logger LOG = LoggerFactory.getLogger(UserService.class);


    private final BigDecimal INITIAL_REAL_BALANCE = BigDecimal.ZERO;
    private final BigDecimal INITIAL_BITCOIN_BALANCE = BigDecimal.ZERO;

    @Autowired
    private UserRepository userRepository;

    public User create(String name, String cpf) {
        User userAlreadyExists = userRepository.findByCpf(cpf);
        if (Objects.nonNull(userAlreadyExists)) {
            LOG.info("User " + "[id " + userAlreadyExists.getId() + "]" + " with this cpf already exists");
            return null;
        }
        Wallet wallet = new Wallet(INITIAL_REAL_BALANCE, INITIAL_BITCOIN_BALANCE);

        User user = new User.Builder(name, cpf, wallet).build();
        return userRepository.save(user);
    }

}
