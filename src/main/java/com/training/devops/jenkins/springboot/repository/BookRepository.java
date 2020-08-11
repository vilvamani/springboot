package com.training.devops.jenkins.springboot.repository;

import com.training.devops.jenkins.springboot.model.Book;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends CrudRepository<Book, String> {
}