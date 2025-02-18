package org.example.AssignmentTests;

import org.example.Assignment.FilterInvoice;
import org.example.Assignment.Invoice;
import org.example.Assignment.SAP;
import org.example.Assignment.SAP_BasedInvoiceSender;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Arrays;

import static org.mockito.Mockito.*;

public class SAPBasedInvoiceSenderTest {

    // Test case for when there are low invoices present. It stubs the FilterInvoice's lowValueInvoices() method to return
    // a non-empty list. Then, it verifies that SAP sends the invoice once.
    @Test
    void testWhenLowInvoicesSend() {
        // Creates the mocks of FilterInvoice and SAP
        FilterInvoice fakeFilterInvoice = mock(FilterInvoice.class);
        SAP fakeSAP = mock(SAP.class);

        // Creates the fake invoices with the fake low invoice data.
        Invoice invoice1 = new Invoice("Bob Ross", 50);
        Invoice invoice2 = new Invoice("Emilia Clarke", 90);
        List<Invoice> fakeInvoices = Arrays.asList(invoice1, invoice2);

        // Stubs the lowValueInvoices method to return the fake data list.
        when(fakeFilterInvoice.lowValueInvoices()).thenReturn(fakeInvoices);

        // Injects the mocked dependencies into SAP_BasedInvoiceSender since it requires both FilterInvoice and SAP.
        SAP_BasedInvoiceSender sender = new SAP_BasedInvoiceSender(fakeFilterInvoice, fakeSAP);

        // Calls the method in SAP_BasedInvoiceSender and verifies to check whether send was called for each invoice.
        sender.sendLowValuedInvoices();
        verify(fakeSAP).send(invoice1);
        verify(fakeSAP).send(invoice2);
    }

    // Test case for when no invoices are present. It replicates the same as the above test case
    // except this time, it should not send any invoices anywhere.
    @Test
    void testWhenNoInvoices() {
        // Creates the mocks of FilterInvoice and SAP
        FilterInvoice fakeFilterInvoice = mock(FilterInvoice.class);
        SAP fakeSAP = mock(SAP.class);

        // Stubs the lowValueInvoices method into returning an empty list when called.
        when(fakeFilterInvoice.lowValueInvoices()).thenReturn(Collections.emptyList());

        // Injects the mocked dependencies into SAP_BasedInvoiceSender since it requires both FilterInvoice and SAP.
        SAP_BasedInvoiceSender sender = new SAP_BasedInvoiceSender(fakeFilterInvoice, fakeSAP);

        // Calls the method in SAP_BasedInvoiceSender and verifies that send was never called.
        sender.sendLowValuedInvoices();
        verify(fakeSAP, never()).send(any(Invoice.class));
    }
}
