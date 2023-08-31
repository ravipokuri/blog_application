package com.ravi.BlogApplication.controller;

import com.ravi.BlogApplication.dao.CommentRepository;
import com.ravi.BlogApplication.entity.Comment;
import com.ravi.BlogApplication.entity.Post;
import com.ravi.BlogApplication.service.CommentService;
import com.ravi.BlogApplication.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.AttributedString;

@Controller
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;
    @PostMapping("/add")
    public String addComment(@RequestParam Integer postId, @RequestParam String name,@RequestParam String email,@RequestParam String content) {
        // Create a new Comment entity from the form data
        Post post = postService.getPostById(postId);
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setName(name);
        comment.setEmail(email);
        comment.setComment(content);

        // Save the comment
        commentService.saveComment(comment);


        return "redirect:/view?postId="+post.getId();
    }

    @GetMapping("/showFormForUpdate")
    public String showFromForUpdate(@RequestParam("commentId") int theId,
                                    Model theModel) {
      Comment comment=commentService.getCommentById(theId);

      theModel.addAttribute("Comment", comment);

      return "commentUpdateForm";
    }

    @PostMapping("/update")
    public String updateComment(@ModelAttribute("Comment") Comment comment) {
        commentService.updateComment(comment);

        return "redirect:/view?postId="+comment.getPost().getId();
    }

    @GetMapping("/delete")
    public String deleteComment(@RequestParam("commentId") Integer theId) {
        Comment comment=commentService.getCommentById(theId);
        int post_id=comment.getPost().getId();

        commentService.deleteCommentById(theId);


        return "redirect:/view?postId="+post_id;
    }
}
