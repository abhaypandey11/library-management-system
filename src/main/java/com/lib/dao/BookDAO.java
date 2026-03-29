package com.lib.dao;


import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.lib.entity.Book;
import com.lib.repository.BookRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BookDAO {

    private final BookRepository bookRepository;

    public Book save(Book book) {
        return bookRepository.save(book);
    }

    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public void delete(Book book) {
        bookRepository.delete(book);
    }

    public boolean existsByIsbn(String isbn) {
        return bookRepository.existsByIsbn(isbn);
    }

    public boolean existsByIsbnAndIdNot(String isbn, Long id) {
        return bookRepository.existsByIsbnAndIdNot(isbn, id);
    }
}
