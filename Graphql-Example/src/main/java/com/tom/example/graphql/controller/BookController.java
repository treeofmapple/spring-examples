package com.tom.example.graphql.controller;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.tom.example.graphql.model.Book;
import com.tom.example.graphql.service.BookService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BookController {

	private final BookService service;
	
    @QueryMapping
    public List<Book> books() {
        return service.findAll();
    }
	
    @QueryMapping
    public Book book(@Argument Integer id) {
        return service.findById(id);
    }
    
    @MutationMapping
    public Book addBook(@Argument String title, @Argument String author) {
        return service.createBook(title, author);
    }

    @MutationMapping
    public Book updateBook(@Argument Integer id, @Argument String title, @Argument String author) {
        return service.updateBook(id, title, author);
    }

    @MutationMapping
    public Boolean deleteBook(@Argument Integer id) {
        return service.deleteBook(id);
    }
	
}
