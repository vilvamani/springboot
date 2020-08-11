package com.training.devops.jenkins.springboot.service;

import com.training.devops.jenkins.springboot.model.Book;
import com.training.devops.jenkins.springboot.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        bookRepository.findAll().forEach(books::add);
        return books;
    }

    public Book getBook(String id) {
        return bookRepository.findById(id).orElseGet(Book::new);
    }

    public void addBook(Book whiskey) {
        bookRepository.save(whiskey);
    }

    public void updateBook(String id, Book whiskey) {
        bookRepository.save(whiskey);
    }

    public void deleteBook(String id) {
        bookRepository.deleteById(id);
    }

}