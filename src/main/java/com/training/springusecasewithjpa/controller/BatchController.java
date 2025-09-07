package com.training.springusecasewithjpa.controller;

import com.training.springusecasewithjpa.entities.Batch;
import com.training.springusecasewithjpa.entities.PayrollEntry;
import com.training.springusecasewithjpa.service.BatchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/batches")
@Tag(name = "Batch Management", description = "Operations related to payroll batch management")
public class BatchController {

    @Autowired
    private BatchService batchService;

    @GetMapping
    public ResponseEntity<List<Batch>> getAllBatches() {
        return ResponseEntity.ok(batchService.getAllBatches());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Batch> getBatchById(@PathVariable String id) {
        return ResponseEntity.ok(batchService.getBatchById(id));
    }

    @PostMapping
    public ResponseEntity<Batch> createBatch(@RequestBody Batch batch) {
        return ResponseEntity.ok(batchService.createBatch(batch));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Batch> updateBatch(@PathVariable String id, @RequestBody Batch batch) {
        return ResponseEntity.ok(batchService.updateBatch(id, batch));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBatch(@PathVariable String id) {
        batchService.deleteBatch(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{batchId}/entries")
    public ResponseEntity<List<PayrollEntry>> getPayrollEntries(@PathVariable String batchId) {
        return ResponseEntity.ok(batchService.getPayrollEntries(batchId));
    }

    @PostMapping("/{batchId}/entries")
    public ResponseEntity<PayrollEntry> addPayrollEntry(@PathVariable String batchId, @RequestBody PayrollEntry entry) {
        return ResponseEntity.ok(batchService.addPayrollEntry(batchId, entry));
    }

    @PutMapping("/entries/{id}")
    public ResponseEntity<PayrollEntry> updatePayrollEntry(@PathVariable Long id, @RequestBody PayrollEntry entry) {
        return ResponseEntity.ok(batchService.updatePayrollEntry(id, entry));
    }

    @DeleteMapping("/entries/{id}")
    public ResponseEntity<Void> deletePayrollEntry(@PathVariable Long id) {
        batchService.deletePayrollEntry(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/upload/excel/{batchId}")
    public ResponseEntity<String> uploadExcel(@PathVariable String batchId, @RequestParam("file") MultipartFile file) {
        try {
            batchService.uploadPayrollEntriesFromExcel(batchId, file);
            return ResponseEntity.ok("Successfully uploaded payroll entries from Excel");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error uploading Excel file: " + e.getMessage());
        }
    }
}
