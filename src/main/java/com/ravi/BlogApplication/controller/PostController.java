package com.ravi.BlogApplication.controller;


import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import com.ravi.BlogApplication.entity.Comment;
import com.ravi.BlogApplication.entity.Post;
import com.ravi.BlogApplication.entity.Tag;
import com.ravi.BlogApplication.service.CommentService;
import com.ravi.BlogApplication.service.PostService;
import com.ravi.BlogApplication.service.TagService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PostController {
    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private TagService tagService;

    @GetMapping("/posts")
    public String listPosts(@RequestParam(value = "page", defaultValue = "0") int page,
                            @RequestParam(value = "size", defaultValue = "10") int size, HttpServletRequest request, Model model) {

        boolean isPostsPage = request.getRequestURI().contains("/posts");
        model.addAttribute("isPostsPage", isPostsPage);

        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postsPage = postService.getPostsPage(pageable);

        model.addAttribute("posts", postsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", postsPage.getTotalPages());
        model.addAttribute("size", size);



        List<String> authors=new ArrayList<String>();
        List<String> tags=new ArrayList<String>();

        authors.addAll(postService.getAuthors());
        tags.addAll(tagService.getTags());

        model.addAttribute("authors", authors);
        model.addAttribute("tags", tags);

        return "postsList";
    }

    @GetMapping("/drafts")
    public String draftsPosts(@RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "size", defaultValue = "10") int size,Model model,Principal principal) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postsPage = postService.getPostsPageDrafts(principal.getName(),pageable);
        model.addAttribute("posts",postsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", postsPage.getTotalPages());
        model.addAttribute("size", size);

        return "draftsLists";
    }

    @GetMapping("/publish")
    public String publishPost(@RequestParam("postId") Integer theId,Model model) {
        Post existingPost = postService.getPostById(theId);

        existingPost.setPublishedAt(LocalDateTime.now());
        existingPost.setIsPublished(1);
        postService.updatePost(existingPost);
        return "redirect:/posts";
    }

    @GetMapping("/postForm")
    public String showPostForm(Model model, Principal principal) {
        Post post=new Post();
        model.addAttribute("post",post);

        return "postForm";
    }
    @PostMapping("/create")
    public String createPost(@ModelAttribute("post") Post post,@RequestParam("tagInput") String tagInput,
                             Principal principal,
                             @RequestParam(value = "publish", required = false) String publishButton,
                             @RequestParam(value = "draft", required = false) String draftButton,
                             @RequestParam(value = "update", required = false) String updateButton) {
        List<String> tagNames = Arrays.asList(tagInput.split("\\s*,\\s*"));

        Set<Tag> newTags = new HashSet<Tag>();
        for (String tagName : tagNames) {
            Tag tag = tagService.getOrCreateTagByName(tagName.trim());
            newTags.add(tag);
        }

        post.setTags(newTags);

        if (updateButton != null && post.getId() != null) {
            Post existingPost = postService.getPostById(post.getId());

            existingPost.setTitle(post.getTitle());
            existingPost.setExcerpt(post.getExcerpt());
            existingPost.setContent(post.getContent());
            existingPost.setAuthor(post.getAuthor());
            existingPost.setTags(post.getTags());

            postService.updatePost(existingPost);
        } else {
            post.setAuthor(principal.getName());
            if (publishButton != null) {
                post.setPublishedAt(LocalDateTime.now());
                post.setIsPublished(1);
            }

            postService.savePost(post);
        }

        return "redirect:/posts";
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("postId") int theId,
                                    Model theModel) {

        Post post = postService.getPostById(theId);

        String existingTags = post.getTags().stream().map(Tag::getName).collect(Collectors.joining(", "));

        theModel.addAttribute("post", post);
        theModel.addAttribute("existingTags", existingTags);


        return "postForm";
    }

    @GetMapping("/delete")
    public String deletePost(@RequestParam("postId") Integer theId) {
        postService.deletePostById(theId);
        return "redirect:/posts";
    }

    @GetMapping("/view")
    public String viewPost(@RequestParam("postId") int theId,Model model,Principal principal) {
        Post post=postService.getPostById(theId);
        List<Comment> comments=commentService.getCommentsByPost(post);

        model.addAttribute("post" ,post);
        model.addAttribute("comments",comments);

        if (principal != null) {
            model.addAttribute("name", principal.getName());
        } else {
            model.addAttribute("name", "Anonymous");
        }

        return "viewPost";
    }

    @GetMapping("/search")
    public String searchPosts(@RequestParam("query") String query,@RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "size", defaultValue = "10") int size, HttpServletRequest request, Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> searchResults = postService.searchPosts(query,pageable);

        model.addAttribute("posts", searchResults.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", searchResults.getTotalPages());
        model.addAttribute("size", size);

        boolean isSearchPage = request.getRequestURI().contains("/search");
        model.addAttribute("isSearchPage", isSearchPage);

        List<String> authorsList=new ArrayList<String>();
        List<String> tagsList=new ArrayList<String>();

        for (int pageIndex = 0; pageIndex < searchResults.getTotalPages(); pageIndex++) {

            Pageable newPageable = PageRequest.of(pageIndex, searchResults.getSize());
            Page<Post> pageResults = postService.searchPosts(query, newPageable);
            for (Post post : pageResults.getContent()) {
                String author = post.getAuthor();
                Set<Tag> postTags = post.getTags();

                authorsList.add(author);

                for (Tag tag : postTags) {
                    tagsList.add(tag.getName());
                }
            }
        }

        model.addAttribute("authors", authorsList);
        model.addAttribute("tags", tagsList);

        return "postsList";
    }


    @GetMapping("/sort")
    public String sortPosts(@RequestParam(value = "query", required = false) String query,
                            @RequestParam(value = "page", defaultValue = "0") int page,
                            @RequestParam(value = "size", defaultValue = "10") int size,
                            @RequestParam(value = "authors", required = false) Set<String> selectedAuthors,
                            @RequestParam(value = "tags", required = false) Set<String> selectedTags,
                            @RequestParam(value="startDate",required = false) LocalDateTime startDate,
                            @RequestParam(value="endDate",required = false) LocalDateTime endDate,
                            HttpServletRequest request,Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postsPage;

        if (query!=null & !query.isEmpty()) {
            postsPage = postService.searchPostsSorted(query, pageable);
        } else if (selectedAuthors != null && !selectedAuthors.isEmpty() || selectedTags != null && !selectedTags.isEmpty()) {
            Set<String> authorsSet = selectedAuthors != null ? selectedAuthors : new HashSet<>();
            Set<String> tagsSet = selectedTags != null ? selectedTags : new HashSet<>();


            LocalDateTime startDateTime = startDate != null ? startDate : postService.getStartDate();
            LocalDateTime endDateTime = endDate != null ? endDate : LocalDateTime.now();

            postsPage = postService.filterPostsByAuthorsAndTagsSorted(startDateTime,endDateTime,authorsSet, tagsSet, pageable);
        } else {
            postsPage  = postService.getSortedPostsPage(pageable);
        }

        model.addAttribute("posts", postsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", postsPage.getTotalPages());
        model.addAttribute("size", size);

        boolean isSortPage = request.getRequestURI().contains("/sort");
        model.addAttribute("isSortPage", isSortPage);

        List<String> authorsList=new ArrayList<String>();
        List<String> tagsList=new ArrayList<String>();

        if (query!=null & !query.isEmpty()) {
            for (int pageIndex = 0; pageIndex < postsPage.getTotalPages(); pageIndex++) {
                Pageable newPageable = PageRequest.of(pageIndex, postsPage.getSize());
                Page<Post> pageResults = postService.searchPosts(query, newPageable);

                for (Post post : pageResults.getContent()) {
                    String author = post.getAuthor();
                    Set<Tag> postTags = post.getTags();

                    authorsList.add(author);

                    for (Tag tag : postTags) {
                        tagsList.add(tag.getName());
                    }
                }
            }
        } else if (selectedAuthors != null && !selectedAuthors.isEmpty() || selectedTags != null && !selectedTags.isEmpty()) {
            Set<String> authorsSet = selectedAuthors != null ? selectedAuthors : new HashSet<>();
            Set<String> tagsSet = selectedTags != null ? selectedTags : new HashSet<>();


            LocalDateTime startDateTime = startDate != null ? startDate : postService.getStartDate();
            LocalDateTime endDateTime = endDate != null ? endDate : LocalDateTime.now();

            for (int pageIndex = 0; pageIndex < postsPage.getTotalPages(); pageIndex++) {
                Pageable newPageable = PageRequest.of(pageIndex, postsPage.getSize());

                Page<Post> pageResults = postService.filterPostsByAuthorsAndTagsSorted(startDateTime,endDateTime,authorsSet,tagsSet,newPageable);

                for (Post post : pageResults.getContent()) {
                    String author = post.getAuthor();
                    Set<Tag> postTags = post.getTags();

                    authorsList.add(author);

                    for (Tag tag : postTags) {
                        tagsList.add(tag.getName());
                    }
                }
            }
        } else {
            authorsList.addAll(postService.getAuthors());
            tagsList.addAll(tagService.getTags());
        }

        model.addAttribute("authors", authorsList);
        model.addAttribute("tags", tagsList);

        return "postsList";
    }

    @GetMapping("/filter")
    public String filterPosts(@RequestParam(name="authors",required = false) String[] authors,
                              @RequestParam(name="tags",required = false) String[] tags,
                              @RequestParam(value="startDate",required=false) LocalDateTime startDate,
                              @RequestParam(value="endDate",required=false) LocalDateTime endDate,
                              @RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "size", defaultValue = "10") int size,
                              HttpServletRequest request, Model model) {
        Pageable pageable = PageRequest.of(page, size);
        if((authors == null) && (tags == null)) {
            return "redirect:/posts";
        }
        else {
            Set<String> authorsSet;
            Set<String> tagsSet;

            if(authors==null && tags!=null) {
                 authorsSet = new HashSet<>();
                 tagsSet = new HashSet<>(Arrays.asList(tags));
            }
            else if (tags==null && authors!=null) {
                authorsSet=new HashSet<>(Arrays.asList(authors));
                tagsSet=new HashSet<>();
            }
            else {
                authorsSet = new HashSet<>(Arrays.asList(authors));
                tagsSet =new HashSet<>(Arrays.asList(tags));
            }

            LocalDateTime startDateTime = startDate != null ? startDate : postService.getStartDate();
            LocalDateTime endDateTime = endDate != null ? endDate : LocalDateTime.now();


            Page<Post> postsPage = postService.getPostsByAuthorsAndTags(startDateTime,endDateTime,authorsSet,tagsSet,pageable);

            model.addAttribute("posts", postsPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", postsPage.getTotalPages());
            model.addAttribute("size", size);

            boolean isFilterPage = request.getRequestURI().contains("/filter");
            model.addAttribute("isFilterPage", isFilterPage);

            model.addAttribute("selectedAuthors", authors);
            model.addAttribute("selectedTags", tags);

            List<String> authorsList=new ArrayList<String>();
            List<String> tagsList=new ArrayList<String>();

            for (int pageIndex = 0; pageIndex < postsPage.getTotalPages(); pageIndex++) {
                Pageable newPageable = PageRequest.of(pageIndex, postsPage.getSize());
                Page<Post> pageResults = postService.getPostsByAuthorsAndTags(startDateTime,endDateTime,authorsSet,tagsSet,newPageable);

                for (Post post : pageResults.getContent()) {
                    String author = post.getAuthor();
                    Set<Tag> postTags = post.getTags();

                    authorsList.add(author);

                    for (Tag tag : postTags) {
                        tagsList.add(tag.getName());
                    }
                }
            }

            model.addAttribute("authors", authorsList);
            model.addAttribute("tags", tagsList);

            return "postsList";
        }
    }

    @GetMapping("/myPosts")
    public String getAuthorPosts(@RequestParam(value = "page", defaultValue = "0") int page,
                                 @RequestParam(value = "size", defaultValue = "10") int size,
                                 Model model,Principal principal) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Post> postsPage = postService.getPostsByAuthor(principal.getName(),pageable);

        model.addAttribute("posts", postsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", postsPage.getTotalPages());
        model.addAttribute("size", size);



        return "authorPosts";
    }


    @GetMapping("/authorPostsSort")
    public String getAuthorPostsSorted(@RequestParam(value = "page", defaultValue = "0") int page,
                                       @RequestParam(value = "size", defaultValue = "10") int size,
                                       Model model,Principal principal) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Post> postsPage = postService.getPostsByAuthorSorted(principal.getName(),pageable);

        model.addAttribute("posts", postsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", postsPage.getTotalPages());
        model.addAttribute("size", size);

        return "authorPostsSorted";
    }
}
