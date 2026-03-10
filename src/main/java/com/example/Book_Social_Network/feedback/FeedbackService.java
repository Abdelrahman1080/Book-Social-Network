package com.example.Book_Social_Network.feedback;

import com.example.Book_Social_Network.book.Book;
import com.example.Book_Social_Network.book.BookRepository;
import com.example.Book_Social_Network.common.PageResponce;
import com.example.Book_Social_Network.exception.OperationNotPermittedException;
import com.example.Book_Social_Network.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final BookRepository bookRepository;
    private final FeedBackMapper feedBackMaooer;
    private final FeedBackRepository feedBackRepository;

    public Integer save(@Valid FeedbackRequest feedbackRequest, Authentication authentication) {
        Book book = bookRepository.findById(feedbackRequest.bookId())
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + feedbackRequest.bookId()));

        User user = (User) authentication.getPrincipal();
        if(book.getOwner().getId().equals(user.getId())) {
            throw new OperationNotPermittedException("You Cannot give feedback to your own book");
        }
            if(!book.isSharable()||book.isArchived()) {
                throw new OperationNotPermittedException("You Cannot give feedback to a book that not sharable or archived");
            }

            Feedback feedback=feedBackMaooer.toFeedback(feedbackRequest );
            return feedBackRepository.save(feedback).getId();
        }

    public PageResponce<FeedbackResponce> findFeedbackByBookId(Integer bookId, int page, int size, Authentication connectedUser) {
        Pageable pageble = PageRequest.of(page, size);
        User user= (User) connectedUser.getPrincipal();
        Page<Feedback> feedbacks=feedBackRepository.findAllByBookId(pageble,bookId );
        List<FeedbackResponce> feedbackResponces= feedbacks.stream()
                .map(f->feedBackMaooer.toFeedBackResponce(f,user.getId()))
                .toList();

        return new PageResponce<>(
                feedbackResponces,
                feedbacks.getNumber(),
                feedbacks.getSize(),
                feedbacks.getTotalElements(),
                feedbacks.getTotalPages(),
                feedbacks.isFirst(),
                feedbacks.isLast()
        );
    }
}
