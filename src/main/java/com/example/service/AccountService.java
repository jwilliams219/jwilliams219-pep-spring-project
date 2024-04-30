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

    /**
     * Create a new account in JPA repository
     * @param newAccount
     * @return the account if created
     */
    public Account register(Account newAccount) {
        Optional<Account> existingAccount = accountRepository.findByUsername(newAccount.getUsername());
        if (existingAccount.isPresent()) {
            throw new DataIntegrityViolationException("Username already exists");
        }
        return accountRepository.save(newAccount);
    }

    /**
     * @param username
     * @param password
     * @return account if authenticated
     * @throws AuthenticationException if not authenticated properly
     */
    public Account login(String username, String password) throws AuthenticationException {
        Optional<Account> account = accountRepository.findByUsernameAndPassword(username, password);
        if (account.isPresent()) {
            return account.get();
        }
        throw new AuthenticationException("Invalid credentials");
    }

    /**
     * Check if account with param id exists in the db
     * @param id
     * @return true if exists, else false
     */
    public boolean accountExists(int id) {
        if (accountRepository.findById(id).isPresent()) {
            return true;
        }
        return false;
    }
}
