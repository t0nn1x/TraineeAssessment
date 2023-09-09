package com.testapi.accounting.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.testapi.accounting.entity.AccountingEntry;
import com.testapi.accounting.entity.User;
import com.testapi.accounting.repository.AccountingEntryService;
import com.testapi.accounting.service.UserService;

@RestController
@RequestMapping("/entries")
public class AccountingEntryController {
    @Autowired
    private UserService userService;

    private final AccountingEntryService accountingEntryService;

    @Autowired
    public AccountingEntryController(AccountingEntryService accountingEntryService) {
        this.accountingEntryService = accountingEntryService;
    }

    // Get all entries
    @GetMapping
    public ResponseEntity<List<AccountingEntry>> getAllEntries(Principal principal) {
        if (principal.getName().equals("ADMIN")) {
            return ResponseEntity.ok(accountingEntryService.findAll());
        } else {
            // Fetch entries only for the logged-in NORMAL user
            User user = userService.findByEmail(principal.getName());
            return ResponseEntity.ok(accountingEntryService.findByUser(user));
        }
    }

    // Get entry by id
    @GetMapping("/{id}")
    public ResponseEntity<AccountingEntry> getEntry(@PathVariable Long id) {
        return accountingEntryService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create a new entry
    @PostMapping
    public ResponseEntity<AccountingEntry> createEntry(@Valid @RequestBody AccountingEntry entry) {
        return ResponseEntity.ok(accountingEntryService.save(entry));
    }

    // Update entry
    @PutMapping("/{id}")
    public ResponseEntity<AccountingEntry> updateEntry(@PathVariable Long id,
            @Valid @RequestBody AccountingEntry updatedEntry) {
        return accountingEntryService.findById(id).map(entry -> {
            entry.setEntryText(updatedEntry.getEntryText());
            entry.setRecordType(updatedEntry.getRecordType());
            entry.setAmount(updatedEntry.getAmount());

            return ResponseEntity.ok(accountingEntryService.save(entry));
        }).orElse(ResponseEntity.notFound().build());
    }

    // Delete entry by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntity(@PathVariable Long id) {
        accountingEntryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
