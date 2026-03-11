package com.example.Book_Social_Network.feedback;

import com.example.Book_Social_Network.common.PageResponce;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("feedbacks")
@RequiredArgsConstructor
@Tag(name="feedback")
public class FeedbackController {
    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<Integer> saveFeedback(
            @Valid @RequestBody FeedbackRequest feedbackRequest,
            Authentication authentication) {


        return ResponseEntity.ok(feedbackService.save(feedbackRequest,authentication));

    }

    @GetMapping("/book/{book-id}")
    public ResponseEntity<PageResponce<FeedbackResponce>> findFeedbackByBookId(
            @PathVariable("book-id") Integer bookId,
            @RequestParam(name="page",defaultValue = "0",required = false) int page,
            @RequestParam(name="size",defaultValue = "10",required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(feedbackService.findFeedbackByBookId(bookId,page,size,connectedUser));
    }




}
