package com.example.Book_Social_Network.feedback;

import com.example.Book_Social_Network.book.Book;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class FeedBackMapper {

    public Feedback toFeedback(FeedbackRequest feedbackRequest){
        return Feedback.builder()
                .note(feedbackRequest.note())
                .comment(feedbackRequest.comment())
                .book(Book.builder()
                        .id(feedbackRequest.bookId())
                        .archived(false)
                        .sharable(false)
                        .build())

                .build();
    }

    public FeedbackResponce toFeedBackResponce(Feedback f, Integer id) {
        return FeedbackResponce.builder().
                note(f.getNote())
                .comment(f.getComment())
                .ownFeedback(Objects.equals(f.getCreatedBy(), id))
        .build();
    }
}
