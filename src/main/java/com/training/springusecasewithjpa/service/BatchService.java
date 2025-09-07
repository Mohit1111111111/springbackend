package com.training.springusecasewithjpa.service;

import com.training.springusecasewithjpa.entities.Batch;
import com.training.springusecasewithjpa.entities.PayrollEntry;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BatchService {
    List<Batch> getAllBatches();
    Batch getBatchById(String id);
    Batch createBatch(Batch batch);
    Batch updateBatch(String id, Batch batch);
    void deleteBatch(String id);

    List<PayrollEntry> getPayrollEntries(String batchId);
    PayrollEntry addPayrollEntry(String batchId, PayrollEntry entry);
    PayrollEntry updatePayrollEntry(Long id, PayrollEntry entry);
    void deletePayrollEntry(Long id);

    void uploadPayrollEntriesFromExcel(String batchId, MultipartFile file) throws IOException;
}