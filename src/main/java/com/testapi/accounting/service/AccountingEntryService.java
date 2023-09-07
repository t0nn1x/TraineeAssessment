package com.testapi.accounting.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.testapi.accounting.entity.AccountingEntry;
import com.testapi.accounting.repository.AccountingEntryRepository;

@Service
public class AccountingEntryService {
    private final AccountingEntryRepository accountingEntryRepository;

    @Autowired
    public AccountingEntryService(AccountingEntryRepository accountingEntryRepository){
        this.accountingEntryRepository = accountingEntryRepository;
    }

    /**
     * Finds an accounting entry by ID.
     *
     * @param id the ID of the accounting entry
     * @return an Optional<AccountingEntry> which contains the entry if found
     */
    public Optional<AccountingEntry> findById(Long id) {
        return accountingEntryRepository.findById(id);
    }

    /**
     * Retrieves all accounting entries from the database.
     *
     * @return a List of accounting entries
     */
    public List<AccountingEntry> findAll(){
        return accountingEntryRepository.findAll();
    }

    /**
     * Saves a new accounting entry or updates an existing one.
     *
     * @param accountingEntry the entry to be saved
     * @return the saved entry
     */
    public AccountingEntry save(AccountingEntry accountingEntry){
        return accountingEntryRepository.save(accountingEntry);
    }

    /**
     * Deletes an accounting entry by ID.
     *
     * @param id the ID of the entry to be deleted
     */
    public void deleteById(Long id){
        accountingEntryRepository.deleteById(id);
    }
}
