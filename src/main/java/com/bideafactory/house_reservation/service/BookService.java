package com.bideafactory.house_reservation.service;

import com.bideafactory.house_reservation.model.Book;
import com.bideafactory.house_reservation.repository.BookRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;

import java.util.List;

@Service
public class BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    @Autowired
    private BookRepository bookRepository;

    public void saveBook(Book book) {
        logger.info("Saving book with id: {}", book.getId());
        bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        logger.info("Fetching all books from the database.");
        return bookRepository.findAll();
    }
}
