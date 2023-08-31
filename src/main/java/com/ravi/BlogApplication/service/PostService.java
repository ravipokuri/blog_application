package com.ravi.BlogApplication.service;

import com.ravi.BlogApplication.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface PostService {
    List<Post> getAllPosts();
    Post getPostById(Integer id);
    void savePost(Post post);
    void updatePost(Post post);
    void deletePostById(Integer id);
    Page<Post> getPostsByAuthor(String author,Pageable pageable);

    Page<Post> getPostsByAuthorSorted(String author,Pageable pageable);
    List<Post> getPostsByPublishedDate();
    List<Post> getPostsByTagName(String tagName);
    Page<Post> searchPosts(String searchTerm,Pageable pageable);
    Page<Post> searchPostsSorted(String searchTerm,Pageable pageable);
    List<String> getAuthors();

    Page<Post> filterPostsByAuthor(String author,Pageable pageable);
    Page<Post> filterPostsByTagName(String tag,Pageable pageable);
    public Page<Post> getSortedPostsPage(Pageable pageable);

    Page<Post> getPostsPage(Pageable pageable);

    Page<Post> getPostsPageDrafts(String author,Pageable pageable);
    Page<Post> getPostsByAuthorsAndTags(LocalDateTime startDate,LocalDateTime endDate,Set<String> authors, Set<String> tags,Pageable pageable);
    Page<Post> filterPostsByAuthorsAndTagsSorted(LocalDateTime startDate,LocalDateTime endDate,Set<String> authors, Set<String> tags,Pageable pageable);
    LocalDateTime getStartDate();
}
