package com.hust.vitech.Service;

import com.hust.vitech.Model.Comment;
import com.hust.vitech.Request.CommentRequest;
import org.springframework.data.domain.Page;

public interface CommentService {
    Comment createComment(CommentRequest commentRequest);

    Comment deleteComment(Long commentId);

    Comment findByCommentId(Long commentId);

    Page<Comment> getCommentPagination(Long filmId, int page, int size, String sortBy, String orderBy);
}
