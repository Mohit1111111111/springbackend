// src/App.js
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import PayrollForm from './components/PayrollForm';
import './App.css';

// Update this to point to your backend
const API_BASE = 'http://localhost:8080/api';

function App() {
  const [batches, setBatches] = useState([]);
  const [selectedBatch, setSelectedBatch] = useState('');
  const [paymentType, setPaymentType] = useState('Domestic');
  const [currency] = useState('INR (India)');
  const [debitAccount, setDebitAccount] = useState('');
  const [accountType, setAccountType] = useState('');
  const [date, setDate] = useState('');
  const [rows, setRows] = useState([]);

  // Load batches on mount
  useEffect(() => {
    axios.get(`${API_BASE}/batches`)
      .then(res => {
        setBatches(res.data);
        if (res.data.length > 0) {
          setSelectedBatch(res.data[0].id);
        }
      })
      .catch(err => {
        console.error("Failed to fetch batches", err);
        setBatches([]);
      });
  }, []);

  // Load payroll entries when batch changes
  useEffect(() => {
    if (!selectedBatch) return;

    axios.get(`${API_BASE}/batches/${selectedBatch}/entries`)
      .then(res => setRows(res.data))
      .catch(err => {
        console.error("Failed to fetch payroll entries", err);
        setRows([]);
      });

    // Load batch-level fields
    axios.get(`${API_BASE}/batches/${selectedBatch}`)
      .then(res => {
        const batch = res.data;
        setPaymentType(batch.paymentType || 'Domestic');
        setDebitAccount(batch.debitAccount || '');
        setAccountType(batch.accountType || '');
        setDate(batch.paymentDate || '');
      })
      .catch(err => console.error("Failed to fetch batch details", err));
  }, [selectedBatch]);

  // Add new row
  const addRow = () => {
    const newRow = {
      method: 'NEFT',
      payeeDetails: 'Empl',
      payeeName: `Employee ${rows.length + 1}`,
      bankDetails: '',
      yourReference: `Salary-${selectedBatch}-${rows.length + 1}`,
      paymentReference: `PAY-${Date.now()}`,
      amount: 0,
      notes: ''
    };

    axios.post(`${API_BASE}/batches/${selectedBatch}/entries`, newRow)
      .then(res => setRows([...rows, res.data]))
      .catch(err => {
        console.error("Failed to add row", err);
        alert("Failed to add payroll entry");
      });
  };

  // Update row
  const handleInputChange = (id, field, value) => {
    const updatedRows = rows.map(r => r.id === id ? { ...r, [field]: value } : r);
    setRows(updatedRows);

    // Update on backend
    if (id) {
      axios.put(`${API_BASE}/batches/entries/${id}`, updatedRows.find(r => r.id === id))
        .catch(err => console.error("Failed to update row", err));
    }
  };

  // Remove row
  const removeRow = (id) => {
    if (!id) return;
    
    axios.delete(`${API_BASE}/batches/entries/${id}`)
      .then(() => setRows(rows.filter(r => r.id !== id)))
      .catch(err => {
        console.error("Failed to delete row", err);
        alert("Failed to delete payroll entry");
      });
  };

  // Save batch details
  const saveBatchDetails = () => {
    if (!selectedBatch) return;
    
    axios.put(`${API_BASE}/batches/${selectedBatch}`, {
      paymentType,
      debitAccount,
      accountType,
      paymentDate: date,
    }).catch(err => console.error("Failed to save batch", err));
  };

  // Submit for processing
  const submitForProcessing = () => {
    if (!selectedBatch) return;
    
    axios.put(`${API_BASE}/batches/${selectedBatch}`, {
      status: 'submitted',
      paymentType,
      debitAccount,
      accountType,
      paymentDate: date,
    }).then(() => alert("Submitted successfully!"))
     .catch(err => {
       console.error("Submit failed", err);
       alert("Failed to submit batch");
     });
  };

  const totalAmount = rows.reduce((sum, row) => sum + (parseFloat(row.amount) || 0), 0);

  return (
    <div className="App">
      <main className="main-content">
        <PayrollForm
          batches={batches}
          selectedBatch={selectedBatch}
          setSelectedBatch={setSelectedBatch}
          paymentType={paymentType}
          setPaymentType={setPaymentType}
          debitAccount={debitAccount}
          setDebitAccount={setDebitAccount}
          accountType={accountType}
          setAccountType={setAccountType}
          date={date}
          setDate={setDate}
          rows={rows}
          handleInputChange={handleInputChange}
          removeRow={removeRow}
          addRow={addRow}
          totalAmount={totalAmount}
          onSave={saveBatchDetails}
          onSubmit={submitForProcessing}
        />
      </main>
    </div>
  );
}

export default App;
