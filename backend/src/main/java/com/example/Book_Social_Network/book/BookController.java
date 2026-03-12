package com.example.Book_Social_Network.book;

import com.example.Book_Social_Network.common.PageResponce;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Pageable; // <- correct import

@RestController
@RequestMapping("books")
@RequiredArgsConstructor
@Tag(name = "book")
public class BookController {
    private final BookService bookService;

    @PostMapping
    public ResponseEntity<Integer> saveBook(@Valid @RequestBody BookRequest bookRequest, Authentication conndectedUser) {

        return ResponseEntity.ok(bookService.save(bookRequest,conndectedUser));
    }
    @GetMapping("{book-id}")
    public ResponseEntity<BookResponce> findBookById(@PathVariable("book-id") Integer bookId) {
        return  ResponseEntity.ok(bookService.findBookById(bookId));
    }

    @GetMapping
    public ResponseEntity<PageResponce<BookResponce>> findAllBooks(@RequestParam(name="page",defaultValue = "0") int page,
                                                                   @RequestParam(name="size",defaultValue = "10") int size,
                                                                   Authentication conndectedUser) {
        return  ResponseEntity.ok(bookService.findAllBooks(page,size,conndectedUser));
    }

    @GetMapping("/owner")
    public ResponseEntity<PageResponce<BookResponce>> findAllBooksByOwner(@RequestParam(name="page",defaultValue = "0") int page,
                                                                          @RequestParam(name="size",defaultValue = "10") int size,
                                                                          Authentication conndectedUser) {
        return  ResponseEntity.ok(bookService.findAllBooksByOwner(page,size,conndectedUser));
    }

    @GetMapping("/borrowed")
    public ResponseEntity<PageResponce<BorrowedBookResponse>> findAllBorrowedBooks(@RequestParam(name="page",defaultValue = "0") int page,
                                                                          @RequestParam(name="size",defaultValue = "10") int size,
                                                                          Authentication conndectedUser) {
        return  ResponseEntity.ok(bookService.findAllBorrowedBooks(page,size,conndectedUser));
    }


    @GetMapping("/returned")
    public ResponseEntity<PageResponce<BorrowedBookResponse>> findAllReturnedBooks(@RequestParam(name="page",defaultValue = "0") int page,
                                                                            @RequestParam(name="size",defaultValue = "10") int size,
                                                                                   Authentication conndectedUser) {
        return  ResponseEntity.ok(bookService.findAllReturneddBooks(page,size,conndectedUser));
    }

    @PatchMapping("/shaeabled/{book-id}")
    public ResponseEntity<Integer> updateSHarableStatus(@PathVariable("book-id") Integer bookId,
                                                        Authentication conndectedUser) {
        return  ResponseEntity.ok(bookService.updateSharableStatus(bookId,conndectedUser));

    }

    @PatchMapping("/archived/{book-id}")
    public ResponseEntity<Integer> updateArchivedStatus(@PathVariable("book-id") Integer bookId,
                                                        Authentication conndectedUser) {
        return  ResponseEntity.ok(bookService.updateArchivedStatus(bookId,conndectedUser));

    }

    @PostMapping("/borrow/{book-id}")
    public ResponseEntity<Integer> borrowBook(@PathVariable("book-id") Integer bookId,Authentication conndectedUser) {
        return  ResponseEntity.ok(bookService.borrowBook(bookId,conndectedUser));
    }

    @PatchMapping("/borrow/return/{book-id}")
    public ResponseEntity<Integer> returnBook(@PathVariable("book-id") Integer bookId,Authentication conndectedUser) {
        return  ResponseEntity.ok(bookService.returnBorrowedBook(bookId,conndectedUser));
    }

    @PatchMapping("/borrow/return/approve/{book-id}")
    public ResponseEntity<Integer> approveReturnBook(@PathVariable("book-id") Integer bookId,Authentication conndectedUser) {
        return  ResponseEntity.ok(bookService.approveReturnBorrowedBook(bookId,conndectedUser));
    }

    @PostMapping(value = "cover/{book-id}",consumes = "multipart/form-data")
    public ResponseEntity<?> uploadBookCover(@PathVariable("book-id") Integer bookId,
                                             @Parameter()
                                             @RequestParam("file") MultipartFile file,
                                             Authentication conndectedUser) {
        bookService.uploadBookCover(bookId,file,conndectedUser);
        return ResponseEntity.ok().build();
    }






}
