package com.example.Book_Social_Network.book;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("books")
@RequiredArgsConstructor
@Tag(name = "book")
public class BookController {
    private final BookService bookService;

    @PostMapping("")
    public ResponseEntity<Integer> saveBook(@Valid @RequestBody BookRequest bookRequest, Authentication conndectedUser) {

        return ResponseEntity.ok(bookService.save(bookRequest,conndectedUser));
    }

}
