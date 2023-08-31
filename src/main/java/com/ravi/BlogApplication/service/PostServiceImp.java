package com.ravi.BlogApplication.service;

import com.ravi.BlogApplication.dao.PostRepository;
import com.ravi.BlogApplication.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class PostServiceImp implements PostService{
    private PostRepository postRepository;
    @Autowired
    public PostServiceImp(PostRepository postRepository) {
        this.postRepository=postRepository;
    }
    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Post getPostById(Integer id) {
        return postRepository.findById(id).get();
    }

    @Override
    public void savePost(Post post) {
        postRepository.save(post);
    }

    @Override
    public void updatePost(Post post) {
        postRepository.save(post);
    }

    @Override
    public void deletePostById(Integer id) {
        postRepository.deleteById(id);
    }

    @Override
    public Page<Post> getPostsByAuthor(String author,Pageable pageable) {
        return postRepository.filterByAuthor(author,pageable);
    }
    @Override
    public Page<Post> getPostsByAuthorSorted(String author,Pageable pageable) {
        return postRepository.filterByAuthorSorted(author,pageable);
    }
    @Override
    public List<Post> getPostsByPublishedDate() {
        return null;
    }

    @Override
    public List<Post> getPostsByTagName(String tagName) {
        return null;
    }

    @Override
    public Page<Post> searchPosts(String searchTerm,Pageable pageable) {
        return postRepository.search(searchTerm,pageable);
    }

    @Override
    public Page<Post> searchPostsSorted(String searchTerm, Pageable pageable) {
        return postRepository.searchSorted( searchTerm,  pageable);
    }

    @Override
    public List<String> getAuthors() {
        List<String> authors= postRepository.findAllDistinctAuthors();
        return authors;
    }

    @Override
    public Page<Post> filterPostsByAuthor(String author,Pageable pageable) {
        return postRepository.filterByAuthor(author,pageable);
    }

    @Override
    public Page<Post> filterPostsByTagName(String tag,Pageable pageable) {
        return postRepository.filterByTagName(tag,pageable);
    }


    @Override
    public Page<Post> getSortedPostsPage(Pageable pageable) {
        return postRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    @Override
    public Page<Post> getPostsPage(Pageable pageable) {
        return postRepository.findAllPublished(pageable);
    }

    @Override
    public Page<Post> getPostsPageDrafts(String author,Pageable pageable) {
        return postRepository.findAllDrafted(author,pageable);
    }

    @Override
    public Page<Post> getPostsByAuthorsAndTags(LocalDateTime startDate,LocalDateTime endDate,Set<String> authors, Set<String> tags,Pageable pageable) {

        return postRepository.findByAuthorsInAndTagsIn(startDate,endDate,authors, tags, pageable);
    }

    @Override
    public Page<Post> filterPostsByAuthorsAndTagsSorted(LocalDateTime startDate,LocalDateTime endDate,Set<String> authors, Set<String> tags, Pageable pageable) {

        return postRepository.findByAuthorsInAndTagsInSorted(startDate,endDate,authors,tags,pageable);
    }

    @Override
    public LocalDateTime getStartDate() {
        return postRepository.findOldestCreatedAt();
    }


}
