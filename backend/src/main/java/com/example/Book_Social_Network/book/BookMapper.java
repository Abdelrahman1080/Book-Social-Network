package com.example.Book_Social_Network.book;

import com.example.Book_Social_Network.file.FileUtils;
import com.example.Book_Social_Network.history.BookTransactionHistory;
import org.springframework.stereotype.Service;

@Service

public class BookMapper {
    public Book toBook(BookRequest bookRequest){
        return Book.builder()
                .title(bookRequest.title())
                .authorName(bookRequest.authorName())
                .synopsis(bookRequest.synopsis())
                .archived(true)
                .sharable(bookRequest.sharable())
                .build();
    }

    public BookResponce toBookResponce( Book book) {
        return BookResponce.builder()
                .id(book.getId())
                .title(book.getTitle())
                .authorName(book.getAuthorName())
                .isbn(book.getIsbn())
                .synopsis(book.getSynopsis())
                .owner(book.getOwner().getUsername())
                .rate(book.getRate())
                .archived(book.isArchived())
                .sharable(book.isSharable())
                .cover(FileUtils.readFileFromLocation(book.getBookCover()))
                .build();
    }

    public BorrowedBookResponse toBorrowedBookResponce(BookTransactionHistory book) {
        return BorrowedBookResponse.builder().
                 id(book.getId())
                .title(book.getBook().getTitle())
                .authorName(book.getBook().getAuthorName())
                .isbn(book.getBook().getIsbn())
                .rate(book.getBook().getRate())
                .returned(book.isReturned())
                .returnApproved(book.isReturnApproved())
                .build();
    }
}
