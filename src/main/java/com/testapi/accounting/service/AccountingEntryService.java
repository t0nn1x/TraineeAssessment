package com.testapi.accounting.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.testapi.accounting.entity.AccountingEntry;
import com.testapi.accounting.repository.AccountingEntryRepository;

public class AccountingEntryService {
    private final AccountingEntryRepository accountingEntryRepository;

    @Autowired
    public AccountingEntryService(AccountingEntryRepository accountingEntryRepository){
        this.accountingEntryRepository = accountingEntryRepository;
    }

    public Optional<AccountingEntry> findById(Long id) {
        return accountingEntryRepository.findById(id);
    }

    public List<AccountingEntry> findAll(){
        return accountingEntryRepository.findAll();
    }

    public AccountingEntry save(AccountingEntry accountingEntry){
        return accountingEntryRepository.save(accountingEntry);
    }

    public void deleteById(Long id){
        accountingEntryRepository.deleteById(id);
    }
}
