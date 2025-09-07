package com.training.springusecasewithjpa.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "payroll_entries")
public class PayrollEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id", nullable = false)
    private Batch batch;

    @Column(name = "method")
    private String method;

    @Column(name = "payee_details")
    private String payeeDetails;

    @Column(name = "payee_name")
    private String payeeName;

    @Column(name = "bank_details")
    private String bankDetails;

    @Column(name = "your_reference")
    private String yourReference;

    @Column(name = "payment_reference")
    private String paymentReference;

    @Column(name = "amount")
    private BigDecimal amount = BigDecimal.ZERO;

    @Column(name = "notes")
    private String notes;

    // Constructors
    public PayrollEntry() {}

    public PayrollEntry(Batch batch, String payeeName, BigDecimal amount) {
        this.batch = batch;
        this.payeeName = payeeName;
        this.amount = amount;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Batch getBatch() { return batch; }
    public void setBatch(Batch batch) { this.batch = batch; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public String getPayeeDetails() { return payeeDetails; }
    public void setPayeeDetails(String payeeDetails) { this.payeeDetails = payeeDetails; }

    public String getPayeeName() { return payeeName; }
    public void setPayeeName(String payeeName) { this.payeeName = payeeName; }

    public String getBankDetails() { return bankDetails; }
    public void setBankDetails(String bankDetails) { this.bankDetails = bankDetails; }

    public String getYourReference() { return yourReference; }
    public void setYourReference(String yourReference) { this.yourReference = yourReference; }

    public String getPaymentReference() { return paymentReference; }
    public void setPaymentReference(String paymentReference) { this.paymentReference = paymentReference; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
