package org.example.Assignment;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class FilterInvoice {
    private QueryInvoicesDAO dao;

    // This code has been refactored to use dependency injection, which allows me to mock/stub the database in tests.
    // Instead of creating a new Database instance inside, we pass the Database from outside.
    public FilterInvoice(Database db) {
        this.dao = new QueryInvoicesDAO(db);
    }

    // Method that filters out invoices with a value less than 100.
    public List<Invoice> lowValueInvoices() {
            List<Invoice> all = dao.all();

            return all.stream()
                    .filter(invoice -> invoice.getValue() < 100)
                    .collect(toList());
    }
}
