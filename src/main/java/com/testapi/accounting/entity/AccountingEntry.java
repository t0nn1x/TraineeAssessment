package com.testapi.accounting.entity;
import com.testapi.accounting.constant.RecordType;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


import lombok.Data;

@Entity
@Data
@Table(name = "accounting_entries")
public class AccountingEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull(message = "Owner cannot be null.")
    private User owner;

    @NotNull(message = "Entry text cannot be null.")
    @Size(min = 1, max = 255, message = "Entry text must be between 1 and 255 characters.")
    private String entryText;

    @NotNull(message = "Amount cannot be null.")
    @Min(value = 0, message = "Amount must be greater than or equal to 0.")
    private Double amount;

    @NotNull(message = "Record type cannot be null.")
    @Enumerated(EnumType.STRING)
    private RecordType recordType;
}