package org.example.AssignmentTests;

import org.example.Assignment.Database;
import org.example.Assignment.FilterInvoice;
import org.example.Assignment.Invoice;
import org.example.Assignment.QueryInvoicesDAO;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FilterInvoiceTest {

    // Test Number #1 With Real Database
    // This test case verifies that the lowValueInvoices() correctly filters invoices with a value less than 100.
    @Test
    void filterInvoiceTest() {
        // No stubbing at this moment so need to create the actual database and DAO.
        Database db = new Database();
        QueryInvoicesDAO dao = new QueryInvoicesDAO(db);
        FilterInvoice filterInvoice = new FilterInvoice(db);

        // Calling the method lowValueInvoices in FilterInvoice.java
        List<Invoice> filteredInvoices = filterInvoice.lowValueInvoices();

        // Verifying that all invoices returned have value < 100
        for (Invoice invoice : filteredInvoices) {
            assertTrue(invoice.getValue() < 100, "Invoice value should be less than 100");
        }
    }

    // This test case verifies that the lowValueInvoices() correctly filters invoices with a value less than 100.
    // Additionally, since FilterInvoice.java has been modified to use dependency injection,
    // this means that a stubbed database can be used.
    @Test
    void filterInvoiceStubbedTest() {
        // Create the mocked database.
        Database fakeDatabase = mock(Database.class);
        // Create the fake list of invoices to use, which is stubbed behavior to use.
        List<Invoice> fakeInvoices = Arrays.asList(
                new Invoice("Bob Ross", 50),                // Should be in result.
                new Invoice("Christian Bale", 200),         // Should not be in result.
                new Invoice("Hugh Jackman", 100),           // Should not be in result.
                new Invoice("Emilia Clarke", 90));          // Should be in result.
        // Stub the withSql method with the fake Invoices.
        when(fakeDatabase.withSql(any())).thenReturn(fakeInvoices);

        // Injecting the fake Database into FilterInvoice.
        FilterInvoice filterInvoice = new FilterInvoice(fakeDatabase);

        // Telling the filteredInvoice to get the values less than 100 from the fake database.
        List<Invoice> filteredInvoices = filterInvoice.lowValueInvoices();

        // Verifies to see whether there are 2 invoices left after filtering.
        assertEquals(2, filteredInvoices.size());
    }
}
