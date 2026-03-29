package com.lib.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lib.dto.BookDTO;
import com.lib.dto.BookResponseDTO;
import com.lib.response.ApiResponse;
import com.lib.service.BookService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BookController {

    private final BookService bookService;

    // ✅ Add new book
    // POST http://localhost:8080/api/books
    @PostMapping
    public ResponseEntity<ApiResponse<BookResponseDTO>> addBook(
            @Valid @RequestBody BookDTO dto) {

        BookResponseDTO savedBook = bookService.addBook(dto);
        return ResponseEntity.ok(ApiResponse.success("Book added successfully", savedBook));
    }

    // ✅ Get all books
    // GET http://localhost:8080/api/books
    @GetMapping
    public ResponseEntity<ApiResponse<List<BookResponseDTO>>> getAllBooks() {

        List<BookResponseDTO> books = bookService.getAllBooks();
        return ResponseEntity.ok(ApiResponse.success("Books fetched successfully", books));
    }

    // ✅ Get single book by ID
    // GET http://localhost:8080/api/books/1
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookResponseDTO>> getBookById(
            @PathVariable Long id) {

        BookResponseDTO book = bookService.getBookById(id);
        return ResponseEntity.ok(ApiResponse.success("Book fetched successfully", book));
    }

    // ✅ Update book
    // PUT http://localhost:8080/api/books/1
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BookResponseDTO>> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookDTO dto) {

        BookResponseDTO updatedBook = bookService.updateBook(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Book updated successfully", updatedBook));
    }

    // ✅ Delete book
    // DELETE http://localhost:8080/api/books/1
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBook(
            @PathVariable Long id) {

        bookService.deleteBook(id);
        return ResponseEntity.ok(ApiResponse.success("Book deleted successfully", null));
    }
}
