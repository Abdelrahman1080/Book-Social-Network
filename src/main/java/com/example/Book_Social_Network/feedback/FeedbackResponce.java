package com.example.Book_Social_Network.feedback;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class FeedbackResponce {
    private Double note;
    private String comment;
    private boolean ownFeedback;

}
