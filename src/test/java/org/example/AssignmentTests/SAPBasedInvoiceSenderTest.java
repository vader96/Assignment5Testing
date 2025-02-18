package org.example.AssignmentTests;

import org.example.Assignment.*;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

// Requirement #2 and Requirement #3 are located in this file.
public class SAPBasedInvoiceSenderTest {

    // Requirement #2
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

    // Requirement #3
    // This test case is used to show that the custom exception was thrown when a failed invoice happened.
    // It also verifies that the failedInvoices were in the list.
    @Test
    void testThrowExceptionWhenBadInvoice() {
        // Creates the mocks of FilterInvoice and SAP
        FilterInvoice fakeFilterInvoice = mock(FilterInvoice.class);
        SAP fakeSAP = mock(SAP.class);

        // Creates the fake invoices and turns them into a list.
        Invoice invoice1 = new Invoice("Bob Ross", 50);
        Invoice invoice2 = new Invoice("Emilia Clarke", 90);    // This will be used to simulate failure.
        Invoice invoice3 = new Invoice("Christian Bale", 80);
        List<Invoice> fakeInvoices = Arrays.asList(invoice1, invoice2, invoice3);

        // Stubs the filter method lowValueInvoices to return the list of fake invoices.
        when(fakeFilterInvoice.lowValueInvoices()).thenReturn(fakeInvoices);

        // Stubs the send method to throw an exception for invoice2
        doThrow(new FailToSendSAPInvoiceException("Caused failure for invoice #2 : Emilia Clarke"))
                .when(fakeSAP).send(invoice2);

        // Injects the mocked dependencies into SAP_BasedInvoiceSender since it requires both FilterInvoice and SAP.
        SAP_BasedInvoiceSender sender = new SAP_BasedInvoiceSender(fakeFilterInvoice, fakeSAP);

        // Gets the list of failedInvoices and asserts that there should be one result, and that it contains invoice2.
        List<Invoice> failedInvoices = sender.sendLowValuedInvoices();
        assertEquals(1, failedInvoices.size());
        assertTrue(failedInvoices.contains(invoice2));

        // This verifies to check that send was called for every invoice, in particular, invoice3 since the exception
        // occurred for invoice2 and this checks that invoice3 was called.
        verify(fakeSAP).send(invoice1);
        verify(fakeSAP).send(invoice2);
        verify(fakeSAP).send(invoice3);
    }
}
