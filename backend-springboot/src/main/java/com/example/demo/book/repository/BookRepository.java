package com.example.demo.book.repository;

import com.example.demo.book.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends MongoRepository<Book, String> {

    // Defect #5: search now handled in BookService via MongoTemplate
    // (kept here for any direct usage)
    @Query("{ '$or': [ " +
           "{ 'bookTitle':  { '$regex': ?0, '$options': 'i' } }, " +
           "{ 'authorName': { '$regex': ?0, '$options': 'i' } }, " +
           "{ '_id':        { '$regex': ?0, '$options': 'i' } } " +
           "] }")
    Page<Book> searchBooks(String search, Pageable pageable);

    // Defect #2: duplicate validation — case-insensitive match on title + author
    boolean existsByBookTitleIgnoreCaseAndAuthorNameIgnoreCase(String bookTitle, String authorName);

    // User-provided bookId uniqueness check
    boolean existsByBookId(String bookId);
}
