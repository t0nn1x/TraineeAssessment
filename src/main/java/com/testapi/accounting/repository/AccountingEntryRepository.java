package com.testapi.accounting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.testapi.accounting.entity.AccountingEntry;

public interface AccountingEntryRepository extends JpaRepository<AccountingEntry, Long> {
    
}
