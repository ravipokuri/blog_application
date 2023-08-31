package com.ravi.BlogApplication.service;

import com.ravi.BlogApplication.entity.Post;
import com.ravi.BlogApplication.dao.CommentRepository;
import com.ravi.BlogApplication.entity.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImp implements CommentService {
    private CommentRepository commentRepository;
    @Autowired
    public CommentServiceImp(CommentRepository commentRepository) {
        this.commentRepository=commentRepository;
    }


    @Override
    public List<Comment> getCommentsByPost(Post post) {
        return commentRepository.findCommentsByPost(post);
    }

    @Override
    public void saveComment(Comment comment) {
        commentRepository.save(comment);
    }

    @Override
    public Comment getCommentById(Integer id) {
        return commentRepository.getById(id);
    }

    @Override
    public void updateComment(Comment comment) {
        commentRepository.save(comment);
    }

    @Override
    public void deleteCommentById(Integer id) {
        commentRepository.deleteById(id);
    }


}