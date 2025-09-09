// src/components/PayrollForm.jsx

import React, { useState } from 'react';

export default function PayrollForm({
  batches,
  selectedBatch,
  setSelectedBatch,
  paymentType,
  setPaymentType,
  debitAccount,
  setDebitAccount,
  accountType,
  setAccountType,
  date,
  setDate,
  rows,
  handleInputChange,
  removeRow,
  addRow,
  totalAmount,
  onSave,
  onSubmit
}) {
  // Currency is static for now
  const currency = 'INR (India)';
  
  // State for file upload
  const [file, setFile] = useState(null);
  const [uploadStatus, setUploadStatus] = useState('');
  const [isUploading, setIsUploading] = useState(false);

  // Handle file selection
  const handleFileChange = (event) => {
    setFile(event.target.files[0]);
    setUploadStatus('');
  };

  // Handle Excel upload
  const handleExcelUpload = async () => {
    if (!file) {
      alert('Please select an Excel file first');
      return;
    }

    if (!selectedBatch) {
      alert('Please select a batch first');
      return;
    }

    const formData = new FormData();
    formData.append('file', file);

    try {
      setIsUploading(true);
      setUploadStatus('Uploading Excel file...');
      
      const response = await fetch(`http://localhost:8080/api/batches/${selectedBatch}/upload/excel`, {
        method: 'POST',
        body: formData
      });

      const result = await response.text();
      
      if (response.ok) {
        setUploadStatus('✅ ' + result);
        // Refresh the page to show new data
        setTimeout(() => {
          window.location.reload();
        }, 2000);
      } else {
        setUploadStatus('❌ Error: ' + result);
      }
      
    } catch (error) {
      console.error('Upload error:', error);
      setUploadStatus('❌ Upload failed: ' + error.message);
    } finally {
      setIsUploading(false);
    }
  };

  // Safely render batch options
  const batchOptions = Array.isArray(batches) ? batches.map((batch) => (
    <option key={batch.id} value={batch.id}>
      {batch.name}
    </option>
  )) : [];

  // Safely render rows
  const renderRows = Array.isArray(rows) ? rows.map((row) => (
    <tr key={row.id}>
      <td>
        <select value={row.method} onChange={(e) => handleInputChange(row.id, 'method', e.target.value)}>
          <option value="NEFT">NEFT</option>
          <option value="RTGS">RTGS</option>
          <option value="IMPS">IMPS</option>
          <option value="SWIFT">SWIFT</option>
          <option value="AUTO">AUTO</option>
        </select>
      </td>
      <td>
        <select value={row.payeeDetails} onChange={(e) => handleInputChange(row.id, 'payeeDetails', e.target.value)}>
          <option value="Empl">Empl</option>
          <option value="Vendor">Vendor</option>
        </select>
      </td>
      <td>
        <input
          type="text"
          value={row.payeeName}
          onChange={(e) => handleInputChange(row.id, 'payeeName', e.target.value)}
          placeholder="Enter name"
        />
      </td>
      <td>
        <input
          type="text"
          value={row.bankDetails}
          onChange={(e) => handleInputChange(row.id, 'bankDetails', e.target.value)}
          placeholder="Enter bank details"
        />
      </td>
      <td>
        <input
          type="text"
          value={row.yourReference}
          onChange={(e) => handleInputChange(row.id, 'yourReference', e.target.value)}
          placeholder="Your Reference"
        />
      </td>
      <td>
        <input
          type="text"
          value={row.paymentReference}
          onChange={(e) => handleInputChange(row.id, 'paymentReference', e.target.value)}
          placeholder="Payment Reference"
        />
      </td>
      <td>
        <input
          type="number"
          value={row.amount}
          onChange={(e) => handleInputChange(row.id, 'amount', e.target.value)}
          placeholder="Pay Amount"
        />
      </td>
      <td>
        <input
          type="text"
          value={row.notes}
          onChange={(e) => handleInputChange(row.id, 'notes', e.target.value)}
          placeholder="Notes to Payee"
        />
      </td>
      <td className="action-cell">
        <button className="delete-btn" onClick={() => removeRow(row.id)}>✕</button>
      </td>
    </tr>
  )) : (
    <tr>
      <td colSpan="9">No data available</td>
    </tr>
  );

  return (
    <>
      {/* Instruction Details */}
      <div className="instruction-section">
        <h2>INSTRUCTION DETAILS</h2>
        <div className="instruction-form">
          {/* Left Side */}
          <div className="left-group">
            <div className="form-group">
              <label>PAYMENT TYPE</label>
              <select value={paymentType} onChange={(e) => setPaymentType(e.target.value)}>
                <option value="Domestic">Domestic</option>
                <option value="International">International</option>
              </select>
            </div>
            <div className="form-group">
              <label>CURRENCY</label>
              <button className="currency-btn">{currency}</button>
            </div>
            <div className="form-group">
              <label>CHOOSE A DEBIT ACCOUNT</label>
              <select value={debitAccount} onChange={(e) => setDebitAccount(e.target.value)}>
                <option value="">Select Account Number</option>
                <option value="1234567890">1234567890</option>
                <option value="2345678901">2345678901</option>
              </select>
            </div>
          </div>

          {/* Right Side */}
          <div className="right-group">
            <div className="form-group">
              <label>ACCOUNT TYPE</label>
              <select value={accountType} onChange={(e) => setAccountType(e.target.value)}>
                <option value="">Select Type</option>
                <option value="Savings">Savings</option>
                <option value="Current">Current</option>
              </select>
            </div>
            <div className="form-group">
              <label>DATE</label>
              <input type="date" value={date} onChange={(e) => setDate(e.target.value)} />
            </div>
            <div className="form-group">
              <label>UPLOAD</label>
              <input 
                type="file" 
                accept=".xlsx,.xls" 
                onChange={handleFileChange} 
                style={{ display: 'none' }}
                id="file-upload"
              />
              <button 
                className="upload-btn" 
                onClick={() => document.getElementById('file-upload').click()}
                disabled={isUploading}
              >
                {isUploading ? 'UPLOADING...' : 'UPLOAD EXCEL'}
              </button>
            </div>
          </div>
        </div>
      </div>

      {/* Upload Status */}
      {uploadStatus && (
        <div className={`upload-status ${uploadStatus.includes('✅') ? 'success' : 'error'}`}>
          <p>{uploadStatus}</p>
        </div>
      )}

      {/* Payment Details */}
      <div className="payment-table-section">
        <h2>Payment Details</h2>

        {/* Batch Selector */}
        <div className="batch-selection">
          <label>Select Batch:</label>
          <select value={selectedBatch} onChange={(e) => setSelectedBatch(e.target.value)}>
            <option value="">Select a Batch</option>
            {batchOptions}
          </select>
        </div>

        {/* Payment Table */}
        <table className="payment-table">
          <thead>
            <tr>
              <th>Payment Method</th>
              <th>Payee Details</th>
              <th>Payee Name</th>
              <th>Bank Details</th>
              <th>Your Reference</th>
              <th>Payment Reference</th>
              <th>Amount</th>
              <th>Notes to Payee (Optional)</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {renderRows}
          </tbody>
        </table>

        {/* Submit Buttons & Total */}
        <button className="add-row-btn" onClick={addRow}>
          Add Row
        </button>
        <div className="total-amount">
          <strong>Total Amount:</strong> ₹{totalAmount.toFixed(2)}
        </div>
        <div className="submit-buttons">
          <button className="save-draft-btn" onClick={(e) => {
            e.preventDefault();
            onSave();
          }}>
            Save as Draft
          </button>
          <button className="submit-btn" onClick={(e) => {
            e.preventDefault();
            onSubmit();
          }}>
            Submit for Processing
          </button>
        </div>
      </div>
    </>
  );
}
