package com.training.springusecasewithjpa.service;

import com.training.springusecasewithjpa.dao.BatchRepository;
import com.training.springusecasewithjpa.entities.Batch;
import com.training.springusecasewithjpa.entities.PayrollEntry;
import com.training.springusecasewithjpa.exceptions.BatchNotFoundException;
import com.training.springusecasewithjpa.exceptions.DataNotFoundException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service
public class BatchServiceImpl implements BatchService {

    @Autowired
    private BatchRepository batchRepository;

    @Override
    public List<Batch> getAllBatches() {
        return batchRepository.findAll();
    }

    @Override
    public Batch getBatchById(String id) {
        return batchRepository.findById(id)
                .orElseThrow(() -> new BatchNotFoundException("Batch not found with id: " + id));
    }

    @Override
    public Batch createBatch(Batch batch) {
        return batchRepository.save(batch);
    }

    @Override
    public Batch updateBatch(String id, Batch batchDetails) {
        Batch batch = getBatchById(id);
        batch.setName(batchDetails.getName());
        batch.setStatus(batchDetails.getStatus());
        batch.setPaymentDate(batchDetails.getPaymentDate());
        batch.setPaymentType(batchDetails.getPaymentType());
        batch.setDebitAccount(batchDetails.getDebitAccount());
        batch.setAccountType(batchDetails.getAccountType());
        return batchRepository.save(batch);
    }

    @Override
    public void deleteBatch(String id) {
        Batch batch = getBatchById(id);
        batchRepository.delete(batch);
    }

    @Override
    public List<PayrollEntry> getPayrollEntries(String batchId) {
        Batch batch = getBatchById(batchId);
        return batch.getEntries();
    }

    @Override
    public PayrollEntry addPayrollEntry(String batchId, PayrollEntry entry) {
        Batch batch = getBatchById(batchId);
        entry.setBatch(batch);
        PayrollEntry savedEntry = batchRepository.save(batch).getEntries().stream()
                .filter(e -> e.getBatch().getId().equals(batchId))
                .findFirst()
                .orElse(null);
        return savedEntry;
    }

    @Override
    public PayrollEntry updatePayrollEntry(Long id, PayrollEntry entryDetails) {
        // This is a simplified implementation
        // In a real app, you'd need proper entry retrieval and update logic
        throw new UnsupportedOperationException("Update payroll entry not implemented");
    }

    @Override
    public void deletePayrollEntry(Long id) {
        // This is a simplified implementation
        throw new UnsupportedOperationException("Delete payroll entry not implemented");
    }

    @Override
    public void uploadPayrollEntriesFromExcel(String batchId, MultipartFile file) throws IOException {
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) { // Skip header row
            Row row = sheet.getRow(i);
            if (row != null) {
                String payeeName = getStringCellValue(row.getCell(0));
                String bankDetails = getStringCellValue(row.getCell(1));
                String yourReference = getStringCellValue(row.getCell(2));
                String paymentReference = getStringCellValue(row.getCell(3));
                String amountStr = getStringCellValue(row.getCell(4));

                BigDecimal amount = new BigDecimal(amountStr);

                PayrollEntry entry = new PayrollEntry();
                entry.setPayeeName(payeeName);
                entry.setBankDetails(bankDetails);
                entry.setYourReference(yourReference);
                entry.setPaymentReference(paymentReference);
                entry.setAmount(amount);
                entry.setMethod("NEFT"); // Default method
                entry.setPayeeDetails("Empl"); // Default type

                // Add to batch (simplified)
                Batch batch = getBatchById(batchId);
                entry.setBatch(batch);
            }
        }

        workbook.close();
    }

    private String getStringCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
}