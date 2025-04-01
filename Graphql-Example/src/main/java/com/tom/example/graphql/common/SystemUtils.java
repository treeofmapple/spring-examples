package com.tom.example.graphql.common;

import org.springframework.stereotype.Component;

import com.tom.example.graphql.model.Book;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SystemUtils {

	protected void mergeData(Book book, String title, String author) {
		book.setTitle(title);
		book.setAuthor(author);
	}
	
}
