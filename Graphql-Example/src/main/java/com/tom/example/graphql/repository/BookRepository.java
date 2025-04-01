package com.tom.example.graphql.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tom.example.graphql.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

}