package org.example.AssignmentTests;

import org.example.Assignment.Database;
import org.example.Assignment.FilterInvoice;
import org.example.Assignment.Invoice;
import org.example.Assignment.QueryInvoicesDAO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilterInvoiceTest {

    // This test case verifies that the lowValueInvoices() correctly filters invoices with a value less than 100.
    @Test
    void filterInvoiceTest() {
        // No stubbing at this moment so need to create the actual database and DAO.
        Database db = new Database();
        QueryInvoicesDAO dao = new QueryInvoicesDAO(db);
        FilterInvoice filterInvoice = new FilterInvoice();

        // Calling the method lowValueInvoices in FilterInvoice.java
        List<Invoice> filteredInvoices = filterInvoice.lowValueInvoices();

        // Verifying that all invoices returned have value < 100
        for (Invoice invoice : filteredInvoices) {
            assertTrue(invoice.getValue() < 100, "Invoice value should be less than 100");
        }
    }
}
