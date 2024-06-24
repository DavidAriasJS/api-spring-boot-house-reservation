package com.bideafactory.house_reservation.controller;

import com.bideafactory.house_reservation.dto.DiscountRequest;
import com.bideafactory.house_reservation.dto.DiscountResponse;
import com.bideafactory.house_reservation.dto.ErrorResponse;
import com.bideafactory.house_reservation.dto.SuccessResponse;
import com.bideafactory.house_reservation.model.Book;
import com.bideafactory.house_reservation.service.BookService;
import com.bideafactory.house_reservation.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/bideafactory/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private DiscountService discountService;

    @GetMapping
    public ResponseEntity<List<Book>> getAll() {
        List<Book> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody Book book) {
        if (book.getHouseId() == null) {
            return new ResponseEntity<>(new ErrorResponse(400, "Bad Request", "required property 'houseId'"), HttpStatus.BAD_REQUEST);
        }

        if (book.getDiscountCode() != null && !book.getDiscountCode().isEmpty()) {
            DiscountRequest discountRequest = new DiscountRequest();
            discountRequest.setUserId(book.getId());
            discountRequest.setHouseId(book.getHouseId());
            discountRequest.setDiscountCode(book.getDiscountCode());

            try {
                DiscountResponse discountResponse = discountService.validateDiscount(discountRequest);
                if (!discountResponse.isStatus()) {
                    return new ResponseEntity<>(new ErrorResponse(409, "Conflict", "invalid discount"), HttpStatus.CONFLICT);
                }
            } catch (Exception e) {
                return new ResponseEntity<>(new ErrorResponse(409, "Conflict", "Discount API validation failed"), HttpStatus.CONFLICT);
            }
        }

        bookService.saveBook(book);
        return new ResponseEntity<>(new SuccessResponse(200, "Book Accepted"), HttpStatus.OK);
    }
}