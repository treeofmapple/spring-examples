package com.tom.example.graphql.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tom.example.graphql.common.SystemUtils;
import com.tom.example.graphql.model.Book;
import com.tom.example.graphql.repository.BookRepository;

import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService extends SystemUtils implements GraphQLMutationResolver, GraphQLQueryResolver {

	private final BookRepository repository;

	public List<Book> findAll() {
		return repository.findAll();
	}

	public Book findById(Integer id) {
		return repository.findById(id).orElseThrow(() -> new RuntimeException(""));
	}

	public Book createBook(String title, String author) {
		Book book = new Book(null, title, author);
		return repository.save(book);
	}

	public Book updateBook(Integer id, String title, String author) {
		return repository.findById(id).map(book -> {
			mergeData(book, title, author);
			return repository.save(book);
		}).orElseThrow(() -> new RuntimeException(""));
	}

	public boolean deleteBook(Integer id) {
		if (repository.existsById(id)) {
			repository.deleteById(id);
			return true;
		}
		return false;
	}

}


