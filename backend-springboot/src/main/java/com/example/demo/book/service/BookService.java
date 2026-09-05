package com.example.demo.book.service;

import com.example.demo.book.dto.PageResponse;
import com.example.demo.book.exception.ResourceNotFoundException;
import com.example.demo.book.model.Book;
import com.example.demo.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Collation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final MongoTemplate  mongoTemplate;

    // ── Defect #6: Case-insensitive sort using MongoDB collation ────────────
    // Sort.by("bookTitle") is case-sensitive — "apple" comes after "Zebra".
    // We use MongoTemplate with a locale-aware collation (strength=2 ignores case).
    public PageResponse<Book> getAllBooks(String search, int page, int size) {
        Collation collation = Collation.of(Locale.ENGLISH).strength(Collation.ComparisonLevel.secondary());

        Query query = new Query();

        // ── Defect #5: search by bookTitle, authorName, OR bookId ──
        if (search != null && !search.isBlank()) {
            String escaped = search.trim().replace("(", "\\(").replace(")", "\\)");
            query.addCriteria(new Criteria().orOperator(
                Criteria.where("bookTitle").regex(escaped, "i"),
                Criteria.where("authorName").regex(escaped, "i"),
                Criteria.where("bookId").regex(escaped, "i")
            ));
        }

        // Total count for pagination (without collation — count doesn't need it)
        long total = mongoTemplate.count(query, Book.class);

        // Apply sort with collation + pagination
        query.with(Sort.by(Sort.Direction.ASC, "bookTitle"))
             .skip((long) page * size)
             .limit(size);
        query.collation(collation);

        List<Book> content = mongoTemplate.find(query, Book.class);

        Pageable pageable = PageRequest.of(page, size);
        Page<Book> pageResult = PageableExecutionUtils.getPage(content, pageable, () -> total);
        return toPageResponse(pageResult);
    }

    public Book getById(String id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }

    // ── Defect #2 + Book ID: user provides bookId, validate uniqueness ──────
    public Book create(Book book) {
        String bookId = (book.getBookId() != null) ? book.getBookId().trim() : "";
        if (bookId.isBlank()) {
            throw new IllegalArgumentException("Book ID is required.");
        }
        // Check for duplicate bookId
        if (bookRepository.existsByBookId(bookId)) {
            throw new IllegalArgumentException(
                "Book ID \"" + bookId + "\" is already in use. Please enter a different ID.");
        }
        // Check for duplicate title + author
        boolean exists = bookRepository.existsByBookTitleIgnoreCaseAndAuthorNameIgnoreCase(
                book.getBookTitle().trim(), book.getAuthorName().trim());
        if (exists) {
            throw new IllegalArgumentException(
                "A book titled \"" + book.getBookTitle() + "\" by " + book.getAuthorName() + " already exists.");
        }
        book.setId(null);
        book.setBookId(bookId);
        return bookRepository.save(book);
    }

    public Book update(String id, Book book) {
        Book existing = getById(id);
        existing.setBookTitle(book.getBookTitle());
        existing.setAuthorName(book.getAuthorName());
        existing.setTotalCopies(book.getTotalCopies());
        existing.setBookLocation(book.getBookLocation());
        existing.setDepartment(book.getDepartment());
        return bookRepository.save(existing);
    }

    public void delete(String id) {
        getById(id); // throws if not found
        bookRepository.deleteById(id);
    }

    private PageResponse<Book> toPageResponse(Page<Book> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }
}
