package com.example.service;

import java.util.*;

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
    private MessageService messageService;

    @Autowired
    public AccountService(MessageService messageService, AccountRepository accountRepository) {
        this.messageService = messageService;
        this.accountRepository = accountRepository;
    }

    public void register(Account newAccount) {
        accountRepository.save(newAccount);
    }

    public void login(String username, String password) throws AuthenticationException {
        List<Account> accountList = accountRepository.findAll();
        if (accountList.isEmpty()) {
            throw new AuthenticationException("No accounts found");
        }
        for (Account account: accountList) {
            if (account.getUsername().equals(username) && account.getPassword().equals(password)) {
                return;
            }
            throw new AuthenticationException("Invalid credentials");
        }
    }

    //
    //public Account addMessageToAccount(long accountId, Message message){
    //    Account account = accountRepository.findById(accountId).get();
    //    account.getMessages().add(message);
    //    accountRepository.save(account);
    //    return account;
    //}
}
