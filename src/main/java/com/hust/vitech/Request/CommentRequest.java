package com.hust.vitech.Request;

import lombok.Data;

@Data
public class CommentRequest {

    private Long productId;

    private String content;
}
