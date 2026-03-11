package com.example.Book_Social_Network.history;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface BookTransactionHistoryRepository extends JpaRepository<BookTransactionHistory,Integer> {
    @Query("""
    select history
    from BookTransactionHistory history
    where history.user.id= :id

""")
    Page<BookTransactionHistory> findAllBorrowedBooks(Pageable pageable, Integer id);

    @Query("""
    select history
    from BookTransactionHistory history
    where history.book.owner.id= :id  

""")
    Page<BookTransactionHistory> findAllReturnedBooks(Pageable pageable, Integer id);

    @Query("""
    select (COUNT (*)>0) AS isborrowed
    from BookTransactionHistory booktransactionHistory
    where booktransactionHistory.book.id= :bookId AND
          booktransactionHistory.user.id= :id AND
          booktransactionHistory.returnApproved=false             
    """)
    boolean isAlreadyBorrowedByTheUser(Integer bookId, Integer id);

    @Query("""
    select transaction
    from BookTransactionHistory transaction
    where transaction.book.id= :bookId AND
            transaction.user.id= :id  AND 
            transaction.returned=false 
          AND transaction.returnApproved=false
""")
    Optional<BookTransactionHistory> findByBookIdAndUserId(Integer bookId, Integer id);

    @Query("""
  select transaction
    from BookTransactionHistory transaction
    where transaction.book.id= :bookId AND
            transaction.book.owner= :id  AND 
            transaction.returned=true 
          AND transaction.returnApproved=false
""")
    Optional<BookTransactionHistory> findByBookIdAndOwnerId(Integer bookId, Integer id);
}
