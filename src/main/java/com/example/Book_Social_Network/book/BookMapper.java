package com.example.Book_Social_Network.book;

import org.springframework.stereotype.Service;

@Service

public class BookMapper {
    public Book toBook(BookRequest bookRequest){
        return Book.builder().id(bookRequest.id())
                .title(bookRequest.title())
                .authorName(bookRequest.authorName())
                .synopsis(bookRequest.synopsis())
                .archived(true)
                .sharable(bookRequest.sharable())
                .build();
    }
}
