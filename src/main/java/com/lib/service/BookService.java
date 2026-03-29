package com.lib.service;



import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.lib.dao.BookDAO;
import com.lib.dto.BookDTO;
import com.lib.dto.BookResponseDTO;
import com.lib.entity.Book;
import com.lib.exception.DuplicateResourceException;
import com.lib.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookDAO bookDAO;
    private final ModelMapper modelMapper;

    // ✅ Add new book
    public BookResponseDTO addBook(BookDTO dto) {

        // Check duplicate ISBN
        if (bookDAO.existsByIsbn(dto.getIsbn())) {
            throw new DuplicateResourceException(
                "Book with ISBN " + dto.getIsbn() + " already exists");
        }

        // DTO → Model
        Book book = modelMapper.map(dto, Book.class);
        book.setAvailableCopies(dto.getTotalCopies());

        Book savedBook = bookDAO.save(book);

        // Model → ResponseDTO
        return modelMapper.map(savedBook, BookResponseDTO.class);
    }

    // ✅ Get all books
    public List<BookResponseDTO> getAllBooks() {
        List<Book> books = bookDAO.findAll();
        List<BookResponseDTO> responseDTOs = new ArrayList<>();
        for (Book book : books) {
            responseDTOs.add(modelMapper.map(book, BookResponseDTO.class));
        }
        return responseDTOs;
    }

    // ✅ Get single book by ID
    public BookResponseDTO getBookById(Long id) {
        Optional<Book> book = bookDAO.findById(id);
        if (book.isPresent()) {
            return modelMapper.map(book.get(), BookResponseDTO.class);
        } else {
            throw new ResourceNotFoundException("Book", id);
        }
    }

    // ✅ Update existing book
    public BookResponseDTO updateBook(Long id, BookDTO dto) {

        Optional<Book> existingBook = bookDAO.findById(id);
        if (!existingBook.isPresent()) {
            throw new ResourceNotFoundException("Book", id);
        }

        if (bookDAO.existsByIsbnAndIdNot(dto.getIsbn(), id)) {
            throw new DuplicateResourceException(
                "Another book with ISBN " + dto.getIsbn() + " already exists");
        }

        Book book = existingBook.get();
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setIsbn(dto.getIsbn());
        book.setCategory(dto.getCategory());
        book.setTotalCopies(dto.getTotalCopies());

        Book updatedBook = bookDAO.save(book);

        // Model → ResponseDTO
        return modelMapper.map(updatedBook, BookResponseDTO.class);
    }

    // ✅ Delete book
    public void deleteBook(Long id) {
        Optional<Book> book = bookDAO.findById(id);
        if (!book.isPresent()) {
            throw new ResourceNotFoundException("Book", id);
        }
        bookDAO.delete(book.get());
    }
}