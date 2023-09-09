package com.testapi.accounting.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.testapi.accounting.entity.AccountingEntry;
import com.testapi.accounting.entity.User;
import com.testapi.accounting.service.AccountingEntryService;
import com.testapi.accounting.service.UserService;

/**
 * Controller for accounting entry operations.
 */
@RestController
@RequestMapping("/entries")
public class AccountingEntryController {
    @Autowired
    private UserService userService;

    private final AccountingEntryService accountingEntryService;

    /**
     * Constructor for AccountingEntryController.
     * 
     * @param accountingEntryService AccountingEntryService object.
     */
    @Autowired
    public AccountingEntryController(AccountingEntryService accountingEntryService) {
        this.accountingEntryService = accountingEntryService;
    }

    /**
     * Get all accounting entries.
     * 
     * @param principal Current logged-in user.
     * @return ResponseEntity containing a list of AccountingEntry objects.
     */
    @GetMapping
    public ResponseEntity<List<AccountingEntry>> getAllEntries(Principal principal) {
        if (principal.getName().equals("ADMIN")) {
            return ResponseEntity.ok(accountingEntryService.findAll());
        } else {
            User user = userService.findByEmail(principal.getName());
            return ResponseEntity.ok(accountingEntryService.findByUser(user));
        }
    }

    /**
     * Get a specific accounting entry by ID.
     * 
     * @param id ID of the accounting entry.
     * @return ResponseEntity containing the AccountingEntry object.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AccountingEntry> getEntry(@PathVariable Long id) {
        return accountingEntryService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create a new accounting entry.
     * 
     * @param entry AccountingEntry object to be created.
     * @return ResponseEntity containing the created AccountingEntry object.
     */
    @PostMapping
    public ResponseEntity<AccountingEntry> createEntry(@Valid @RequestBody AccountingEntry entry) {
        return ResponseEntity.ok(accountingEntryService.save(entry));
    }

    /**
     * Update an existing accounting entry.
     * 
     * @param id           ID of the accounting entry to be updated.
     * @param updatedEntry Updated AccountingEntry object.
     * @return ResponseEntity containing the updated AccountingEntry object.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AccountingEntry> updateEntry(@PathVariable Long id,
            @Valid @RequestBody AccountingEntry updatedEntry) {
        return accountingEntryService.updateEntry(id, updatedEntry);
    }

    /**
     * Delete a specific accounting entry by ID.
     * 
     * @param id ID of the accounting entry to be deleted.
     * @return ResponseEntity indicating the result of the deletion.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntity(@PathVariable Long id) {
        accountingEntryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
