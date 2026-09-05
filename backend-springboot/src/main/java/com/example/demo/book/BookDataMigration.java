package com.example.demo.book;

import com.example.demo.book.model.Book;
import com.example.demo.book.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * One-time migration: assign sequential bookIds (BK-001, BK-002, ...)
 * to any existing books that were saved without a bookId.
 * Safe to run multiple times — only updates books where bookId is null/blank.
 */
@Component
@Order(10)
public class BookDataMigration implements CommandLineRunner {

    private final BookRepository bookRepository;

    public BookDataMigration(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) {
        List<Book> allBooks = bookRepository.findAll();

        // Find the highest existing BK-NNN number
        int maxNum = allBooks.stream()
            .filter(b -> b.getBookId() != null && b.getBookId().matches("BK-\\d+"))
            .mapToInt(b -> {
                try { return Integer.parseInt(b.getBookId().substring(3)); }
                catch (NumberFormatException e) { return 0; }
            })
            .max()
            .orElse(0);

        AtomicInteger counter = new AtomicInteger(maxNum);
        int seeded = 0;

        for (Book book : allBooks) {
            if (book.getBookId() == null || book.getBookId().isBlank()) {
                book.setBookId(String.format("BK-%03d", counter.incrementAndGet()));
                bookRepository.save(book);
                seeded++;
            }
        }

        if (seeded > 0) {
            System.out.println("[BookDataMigration] Assigned bookIds to " + seeded + " existing book(s).");
        }
    }
}
