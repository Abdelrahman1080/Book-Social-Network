package com.example.Book_Social_Network.book;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;

public interface BookRepository extends JpaRepository<Book, Integer>, JpaSpecificationExecutor<Book> {
    @Query("""
SELECT Book 
FROM Book Book
WHERE Book.archived = false AND (Book.owner.id != :id and Book.sharable = true)
""")
    Page<Book> findAllDisplayableBooks(Pageable pageable, Integer id);

    @Query("""
SELECT books
FROM Book books
WHERE books.owner.id = :id 
""")
    Page<Book> findAllByOwnerId(Pageable pageable, Integer id);
}
