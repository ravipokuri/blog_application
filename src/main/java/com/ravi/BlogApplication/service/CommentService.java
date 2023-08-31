package com.ravi.BlogApplication.service;
import com.ravi.BlogApplication.entity.Comment;
import com.ravi.BlogApplication.entity.Post;

import java.util.List;

public interface CommentService {
    List<Comment> getCommentsByPost(Post post);
    void saveComment(Comment comment);
    Comment getCommentById(Integer id);
    void updateComment(Comment comment);

    void deleteCommentById(Integer id);

}
