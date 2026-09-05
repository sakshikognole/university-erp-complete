package com.example.demo.book.controller;

import com.example.demo.book.dto.ApiResponse;
import com.example.demo.book.dto.PageResponse;
import com.example.demo.book.model.Book;
import com.example.demo.book.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<Book>>> getAll(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success("Books fetched", bookService.getAllBooks(search, page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Book>> getById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success("Book fetched", bookService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Book book) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Book created", bookService.create(book)));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(ex.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Book>> update(
            @PathVariable String id, @Valid @RequestBody Book book) {
        return ResponseEntity.ok(ApiResponse.success("Book updated", bookService.update(id, book)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable String id) {
        bookService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Book deleted", null));
    }
}
