package com.example.Book_Social_Network.book;

import com.example.Book_Social_Network.common.PageResponce;
import com.example.Book_Social_Network.exception.OperationNotPermittedException;
import com.example.Book_Social_Network.file.FileStorageService;
import com.example.Book_Social_Network.history.BookTransactionHistory;
import com.example.Book_Social_Network.history.BookTransactionHistoryRepository;
import com.example.Book_Social_Network.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Pageable; // <- correct import

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookTransactionHistoryRepository bookTransactionHistoryRepository;
    private final FileStorageService fileStorageService;

    public Integer save(BookRequest bookRequest, Authentication conndectedUser) {
        User user = (User) conndectedUser.getPrincipal();
        Book book=bookMapper.toBook(bookRequest);
        book.setOwner(user);
        return bookRepository.save(book).getId();
    }

    public BookResponce findBookById(Integer bookId) {
        return bookRepository.findById(bookId)
                .map(bookMapper::toBookResponce)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + bookId));
    }

    public PageResponce<BookResponce> findAllBooks(int page, int size, Authentication conndectedUser) {
        User user = (User) conndectedUser.getPrincipal();
        Pageable pageable= (Pageable) PageRequest.of(page,size, Sort.by("creationDate").descending());
        Page<Book> bookPage=bookRepository.findAllDisplayableBooks(pageable,user.getId());
        List<BookResponce> bookResponces=bookPage.getContent().stream()
                .map(bookMapper::toBookResponce)
                .collect(Collectors.toList());
        return PageResponce.<BookResponce>builder()
                .content(bookResponces)
                .number(bookPage.getNumber())
                .size(bookPage.getSize())
                .totalElements(bookPage.getTotalElements())
                .totalPages(bookPage.getTotalPages())
                .last(bookPage.isLast())
                .first(bookPage.isFirst())
                .build();

    }

    public PageResponce<BookResponce> findAllBooksByOwner2(int page, int size, Authentication conndectedUser) {
        User user=(User)conndectedUser.getPrincipal();
        Pageable pageable= (Pageable) PageRequest.of(page,size, Sort.by("creationDate").descending());
        Page<Book>bookpage=bookRepository.findAll(BookSpecification.withOwnerId(user.getId()), (org.springframework.data.domain.Pageable) pageable);
        List<BookResponce> bookResponces=bookpage.getContent().stream()
                .map(bookMapper::toBookResponce)
                .toList();
        return PageResponce.<BookResponce>builder()
                .content(bookResponces)
                .number(bookpage.getNumber())
                .size(bookpage.getSize())
                .totalElements(bookpage.getTotalElements())
                .totalPages(bookpage.getTotalPages())
                .last(bookpage.isLast())
                .first(bookpage.isFirst())
                .build();
    }

    public PageResponce<BookResponce> findAllBooksByOwner(int page, int size, Authentication conndectedUser) {
        User user=(User)conndectedUser.getPrincipal();
        Pageable pageable= (Pageable) PageRequest.of(page,size, Sort.by("creationDate").descending());
        Page<Book>bookpage=bookRepository.findAllByOwnerId(pageable,user.getId());
        List<BookResponce> bookResponces=bookpage.getContent().stream()
                .map(bookMapper::toBookResponce)
                .toList();
        return PageResponce.<BookResponce>builder()
                .content(bookResponces)
                .number(bookpage.getNumber())
                .size(bookpage.getSize())
                .totalElements(bookpage.getTotalElements())
                .totalPages(bookpage.getTotalPages())
                .last(bookpage.isLast())
                .first(bookpage.isFirst())
                .build();
    }

    public PageResponce<BorrowedBookResponse> findAllBorrowedBooks(int page, int size, Authentication conndectedUser) {
        User user=(User)conndectedUser.getPrincipal();
         Pageable pageable= (Pageable) PageRequest.of(page,size, Sort.by("creationDate").descending());
            Page<BookTransactionHistory> allBorrowedBooks=bookTransactionHistoryRepository.findAllBorrowedBooks(pageable,user.getId());
            List<BorrowedBookResponse> bookResponces=allBorrowedBooks.getContent().stream()
                    .map(bookMapper::toBorrowedBookResponce)
                    .toList();
        return new PageResponce<>(
                bookResponces,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    public PageResponce<BorrowedBookResponse> findAllReturneddBooks(int page, int size, Authentication conndectedUser) {

        User user=(User)conndectedUser.getPrincipal();
        Pageable pageable=  PageRequest.of(page,size, Sort.by("creationDate").descending());
        Page<BookTransactionHistory> allBorrowedBooks=bookTransactionHistoryRepository.findAllReturnedBooks(pageable,user.getId());
        List<BorrowedBookResponse> bookResponces=allBorrowedBooks.getContent().stream()
                .map(bookMapper::toBorrowedBookResponce)
                .toList();
        return new PageResponce<>(
                bookResponces,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );

    }

    public Integer updateSharableStatus(Integer bookId, Authentication conndectedUser) {
        Book book=bookRepository.findById(bookId)
                .orElseThrow(()->new RuntimeException("Book not found with id: "+bookId));
        User user=(User)conndectedUser.getPrincipal();
        if(!book.getOwner().getId().equals(user.getId()))
            throw new OperationNotPermittedException("You are not the owner of this book");

            book.setSharable(!book.isSharable());
            bookRepository.save(book);
            return book.getId();

    }

    public Integer updateArchivedStatus(Integer bookId, Authentication conndectedUser) {

        Book book=bookRepository.findById(bookId)
                .orElseThrow(()->new RuntimeException("Book not found with id: "+bookId));
        User user=(User)conndectedUser.getPrincipal();
        if(!book.getOwner().getId().equals(user.getId()))
            throw new OperationNotPermittedException("You are not the owner of this book");

        book.setArchived(!book.isArchived());
        bookRepository.save(book);
        return book.getId();
    }

    public Integer borrowBook(Integer bookId, Authentication conndectedUser) {
        Book book=bookRepository.findById(bookId).orElseThrow(()->new RuntimeException("Book not found with id: "+bookId));
        User user=(User)conndectedUser.getPrincipal();

        if(book.getOwner().getId().equals(user.getId()))
            throw new OperationNotPermittedException("You cannot borrow your own book");

        if(!book.isSharable()||book.isArchived())
            throw new OperationNotPermittedException("This book is not available for borrowing(either not sharable or archived)");
        final boolean isAlreadyBorrowed=bookTransactionHistoryRepository.isAlreadyBorrowedByTheUser(bookId,user.getId());

        if(isAlreadyBorrowed)
            throw new OperationNotPermittedException("The requested book is already borrowed");

        BookTransactionHistory bookTransactionHistory= BookTransactionHistory.builder()
                .user(user)
                .book(book)
                .returned(false)
                .returnApproved(false)
                .build();
        bookTransactionHistoryRepository.save(bookTransactionHistory);
        return bookTransactionHistory.getId();

    }

    public Integer returnBorrowedBook(Integer bookId, Authentication conndectedUser) {
        Book book=bookRepository.findById(bookId).orElseThrow(()->new RuntimeException("Book not found with id: "+bookId));
        User user=(User)conndectedUser.getPrincipal();

        if(book.getOwner().getId().equals(user.getId()))
            throw new OperationNotPermittedException("You cannot return your own book");

        if(!book.isSharable()||book.isArchived())
            throw new OperationNotPermittedException("This book is not available for returning(either not sharable or archived)");
        final boolean isAlreadyBorrowed=bookTransactionHistoryRepository.isAlreadyBorrowedByTheUser(bookId,user.getId());

        BookTransactionHistory bookTransactionHistory=bookTransactionHistoryRepository.findByBookIdAndUserId(bookId,user.getId())
                .orElseThrow(()->new OperationNotPermittedException("The requested book is not borrowed by you"));

        bookTransactionHistory.setReturned(true);
        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();


    }

    public Integer approveReturnBorrowedBook(Integer bookId, Authentication conndectedUser) {
        Book book=bookRepository.findById(bookId).orElseThrow(()->new RuntimeException("Book not found with id: "+bookId));
        User user=(User)conndectedUser.getPrincipal();


        if(!book.isSharable()||book.isArchived())
            throw new OperationNotPermittedException("This book is not available for returning(either not sharable or archived)");

        BookTransactionHistory bookTransactionHistory=bookTransactionHistoryRepository.findByBookIdAndOwnerId(bookId,user.getId())
                .orElseThrow(()->new OperationNotPermittedException("The requested book is Yours Or The requested book is not returned yet"));

        bookTransactionHistory.setReturnApproved(true);
        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();

    }

    public void uploadBookCover(Integer bookId, MultipartFile file, Authentication conndectedUser) {
        Book book=bookRepository.findById(bookId).orElseThrow(()->new RuntimeException("Book not found with id: "+bookId));
        User user=(User)conndectedUser.getPrincipal();
        var bookCover=fileStorageService.saveFile(file, user.getId());

        book.setBookCover(bookCover);
        bookRepository.save(book);
    }
}
