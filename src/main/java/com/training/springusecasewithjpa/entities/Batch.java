package com.training.springusecasewithjpa.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "batches")
public class Batch {

    @Id
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "status")
    private String status = "draft";

    @Column(name = "total_amount")
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "payment_type")
    private String paymentType;

    @Column(name = "currency")
    private String currency = "INR";

    @Column(name = "debit_account")
    private String debitAccount;

    @Column(name = "account_type")
    private String accountType;

    @OneToMany(mappedBy = "batch", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PayrollEntry> entries;

    // Constructors
    public Batch() {}

    public Batch(String id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }

    public String getPaymentType() { return paymentType; }
    public void setPaymentType(String paymentType) { this.paymentType = paymentType; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getDebitAccount() { return debitAccount; }
    public void setDebitAccount(String debitAccount) { this.debitAccount = debitAccount; }

    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }

    public List<PayrollEntry> getEntries() { return entries; }
    public void setEntries(List<PayrollEntry> entries) { this.entries = entries; }
}
