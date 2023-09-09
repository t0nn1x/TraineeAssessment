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

    @GetMapping
    public ResponseEntity<List<AccountingEntry>> getAllEntries(Principal principal) {
        if (principal.getName().equals("ADMIN")) {
            return ResponseEntity.ok(accountingEntryService.findAll());
        } else {
            User user = userService.findByEmail(principal.getName());
            return ResponseEntity.ok(accountingEntryService.findByUser(user));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountingEntry> getEntry(@PathVariable Long id) {
        return accountingEntryService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AccountingEntry> createEntry(@Valid @RequestBody AccountingEntry entry) {
        return ResponseEntity.ok(accountingEntryService.save(entry));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountingEntry> updateEntry(@PathVariable Long id,
            @Valid @RequestBody AccountingEntry updatedEntry) {
        return accountingEntryService.updateEntry(id, updatedEntry);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntity(@PathVariable Long id) {
        accountingEntryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
