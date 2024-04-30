package com.example.service;

import java.util.*;

import org.springframework.dao.DataIntegrityViolationException;
import javax.naming.AuthenticationException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.entity.*;
import com.example.repository.AccountRepository;


@Service
@Transactional
public class AccountService {

    private AccountRepository accountRepository;

    @Autowired
    public AccountService(MessageService messageService, AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account register(Account newAccount) {
        Optional<Account> existingAccount = accountRepository.findByUsername(newAccount.getUsername());
        if (existingAccount.isPresent()) {
            throw new DataIntegrityViolationException("Username already exists");
        }
        return accountRepository.save(newAccount);
    }

    public Account login(String username, String password) throws AuthenticationException {
        Optional<Account> account = accountRepository.findByUsernameAndPassword(username, password);
        if (account.isPresent()) {
            return account.get();
        }
        throw new AuthenticationException("Invalid credentials");
    }

    public boolean accountExists(int id) {
        if (accountRepository.findById(id).isPresent()) {
            return true;
        }
        return false;
    }
}
