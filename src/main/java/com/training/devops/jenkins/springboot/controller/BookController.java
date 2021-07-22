package com.training.devops.jenkins.springboot.controller;

import com.training.devops.jenkins.springboot.model.Book;
import com.training.devops.jenkins.springboot.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;

import java.util.List;

@RestController
public class BookController {

    @Autowired
    private BookService bookservice;

    @GetMapping("/")
    public String home() {
        return "Hello World!";
    }
    
    @GetMapping("/perftest")
    public String sqrt() {
        double x = 0.0001;
        
        for (double i = 0; i <= 1000000; i++) {
            x += Math.sqrt(x);
        }

        String ip = null;

        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            System.out.println(e);
        }
        return ip + "\n";
    }

    @GetMapping("/books")
    public List<Book> getAllBooks() {
        return bookservice.getAllBooks();
    }

    @GetMapping("/books/{id}")
    public Book getBook(@PathVariable String bookId) {
        return bookservice.getBook(bookId);
    }

    @PostMapping("/books")
    public void addBook(@RequestBody Book book) {
        bookservice.addBook(book);
    }

    @PutMapping("/books/{id}")
    public void updateBook(@PathVariable String bookId, @RequestBody Book book) {
        bookservice.updateBook(bookId, book);
    }

    @DeleteMapping("/books/{id}")
    public void deleteBook(@PathVariable String bookId) {
        bookservice.deleteBook(bookId);
    }
}
