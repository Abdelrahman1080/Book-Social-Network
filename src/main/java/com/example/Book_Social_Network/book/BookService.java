package com.example.Book_Social_Network.book;

import com.example.Book_Social_Network.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;


    public Integer save(BookRequest bookRequest, Authentication conndectedUser) {
        User user = (User) conndectedUser.getPrincipal();
        Book book=bookMapper.toBook(bookRequest);
        book.setOwner(user);
        return bookRepository.save(book).getId();
    }
}
