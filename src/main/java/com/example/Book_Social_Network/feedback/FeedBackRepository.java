package com.example.Book_Social_Network.feedback;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Pageable;

public interface FeedBackRepository extends JpaRepository<Feedback,Integer> {

    @Query("""
select feedback
from Feedback feedback
where feedback.book.id= :bookId
""")
    Page<Feedback> findAllByBookId(Pageable pageble, Integer bookId);
}
