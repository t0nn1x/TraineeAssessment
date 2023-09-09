package com.testapi.accounting.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.testapi.accounting.entity.AccountingEntry;
import com.testapi.accounting.entity.User;
import com.testapi.accounting.repository.AccountingEntryRepository;

@Service
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

    public List<AccountingEntry> findByUser(User user) {
        return accountingEntryRepository.findByOwner(user);
    }

    public ResponseEntity<AccountingEntry> updateEntry(Long id, AccountingEntry updatedEntry) {
        return accountingEntryRepository.findById(id).map(entry -> {
            entry.setEntryText(updatedEntry.getEntryText());
            entry.setRecordType(updatedEntry.getRecordType());
            entry.setAmount(updatedEntry.getAmount());

            return ResponseEntity.ok(accountingEntryRepository.save(entry));
        }).orElse(ResponseEntity.notFound().build());
    }
}
