package org.example.Assignment;

// Custom exception class for requirement 3.
public class FailToSendSAPInvoiceException extends RuntimeException {
    public FailToSendSAPInvoiceException(String message) {
        super(message);
    }
}
