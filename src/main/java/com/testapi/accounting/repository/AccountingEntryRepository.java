package com.testapi.accounting.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.testapi.accounting.entity.AccountingEntry;
import com.testapi.accounting.entity.User;

public interface AccountingEntryRepository extends JpaRepository<AccountingEntry, Long> {
    List<AccountingEntry> findByOwner(User owner);
}
