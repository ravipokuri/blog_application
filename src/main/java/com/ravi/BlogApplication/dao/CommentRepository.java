package com.ravi.BlogApplication.dao;

import com.ravi.BlogApplication.entity.Comment;
import com.ravi.BlogApplication.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Integer> {
    @Query("SELECT c FROM Comment c WHERE c.post = :post")
    List<Comment> findCommentsByPost(Post post);


}
