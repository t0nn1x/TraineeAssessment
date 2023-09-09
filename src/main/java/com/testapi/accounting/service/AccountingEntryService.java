package com.testapi.accounting.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.testapi.accounting.entity.AccountingEntry;
import com.testapi.accounting.entity.User;
import com.testapi.accounting.repository.AccountingEntryRepository;

/**
 * Service for accounting entry operations.
 */
@Service
public class AccountingEntryService {
    private final AccountingEntryRepository accountingEntryRepository;

    /**
     * Constructor for AccountingEntryService.
     * 
     * @param accountingEntryRepository AccountingEntryRepository object.
     */
    @Autowired
    public AccountingEntryService(AccountingEntryRepository accountingEntryRepository){
        this.accountingEntryRepository = accountingEntryRepository;
    }

    /**
     * Find an accounting entry by ID.
     * 
     * @param id ID of the accounting entry.
     * @return Optional containing the AccountingEntry object.
     */
    public Optional<AccountingEntry> findById(Long id) {
        return accountingEntryRepository.findById(id);
    }

    /**
     * Find all accounting entries.
     * 
     * @return List of all accounting entries.
     */
    public List<AccountingEntry> findAll(){
        return accountingEntryRepository.findAll();
    }

    /**
     * Save an accounting entry.
     * 
     * @param accountingEntry AccountingEntry object to be saved.
     * @return Saved AccountingEntry object.
     */
    public AccountingEntry save(AccountingEntry accountingEntry){
        return accountingEntryRepository.save(accountingEntry);
    }

    /**
     * Delete an accounting entry by ID.
     * 
     * @param id ID of the accounting entry to be deleted.
     */
    public void deleteById(Long id){
        accountingEntryRepository.deleteById(id);
    }

    /**
     * Find accounting entries by user.
     * 
     * @param user User object.
     * @return List of accounting entries associated with the user.
     */
    public List<AccountingEntry> findByUser(User user) {
        return accountingEntryRepository.findByOwner(user);
    }

    /**
     * Update an existing accounting entry.
     * 
     * @param id           ID of the accounting entry to be updated.
     * @param updatedEntry Updated AccountingEntry object.
     * @return ResponseEntity containing the updated AccountingEntry object.
     */
    public ResponseEntity<AccountingEntry> updateEntry(Long id, AccountingEntry updatedEntry) {
        return accountingEntryRepository.findById(id).map(entry -> {
            entry.setEntryText(updatedEntry.getEntryText());
            entry.setRecordType(updatedEntry.getRecordType());
            entry.setAmount(updatedEntry.getAmount());

            return ResponseEntity.ok(accountingEntryRepository.save(entry));
        }).orElse(ResponseEntity.notFound().build());
    }
}
