package com.ravi.BlogApplication.dao;

import com.ravi.BlogApplication.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;


public interface PostRepository extends JpaRepository<Post,Integer> {

    Page<Post> findAll(Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.isPublished = 1")
    Page<Post> findAllPublished(Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.author=:author AND p.isPublished = 0")
    Page<Post> findAllDrafted(@Param("author") String author,Pageable pageable);

    @Query("SELECT DISTINCT p FROM Post p LEFT JOIN p.tags t " +
            "WHERE (p.title LIKE %:query% OR p.content LIKE %:query% OR p.author LIKE %:query% OR t.name LIKE %:query%) AND (p.isPublished=1)")
    Page<Post> search(@Param("query") String query,Pageable pageable);

    @Query("SELECT DISTINCT p FROM Post p LEFT JOIN p.tags t " +
            "WHERE (p.title LIKE %:query% OR p.content LIKE %:query% OR p.author LIKE %:query% OR t.name LIKE %:query%) AND (p.isPublished=1) order by p.publishedAt desc")
    Page<Post> searchSorted(@Param("query") String query,Pageable pageable);
    @Query("SELECT DISTINCT p.author FROM Post p WHERE p.isPublished = 1")
    List<String> findAllDistinctAuthors();
    @Query("SELECT MIN(p.createdAt) FROM Post p")
    LocalDateTime findOldestCreatedAt();
    @Query("SELECT DISTINCT p FROM Post p WHERE p.author LIKE %:author% AND p.isPublished=1 ORDER BY p.publishedAt ASC")
    Page<Post> filterByAuthor(@Param("author") String author,Pageable pageable);

    @Query("SELECT DISTINCT p FROM Post p WHERE p.author LIKE %:author% AND p.isPublished=1 ORDER BY p.publishedAt DESC")
    Page<Post> filterByAuthorSorted(@Param("author") String author,Pageable pageable);

    @Query("SELECT DISTINCT p FROM Post p " +
            "LEFT JOIN p.tags t " +
            "WHERE t.name LIKE %:tagName%")
    Page<Post> filterByTagName(@Param("tagName") String tagName,Pageable pageable);
   @Query("SELECT p FROM Post p WHERE p.isPublished = 1 ORDER BY p.publishedAt DESC")
   Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query("SELECT p FROM Post p WHERE (p.createdAt BETWEEN :startDate AND :endDate) AND (p.author IN :authors " +
            "OR EXISTS (SELECT t FROM p.tags t WHERE t.name IN :tags)) AND (p.isPublished = 1) order by p.publishedAt ASC")
    Page<Post> findByAuthorsInAndTagsIn(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("authors") Set<String> authors,
            @Param("tags") Set<String> tags,
            Pageable pageable
    );

    @Query("SELECT p FROM Post p WHERE (p.createdAt BETWEEN :startDate AND :endDate) AND (p.author IN :authors " +
            "OR EXISTS (SELECT t FROM p.tags t WHERE t.name IN :tags)) AND (p.isPublished = 1) order by p.createdAt DESC")
    Page<Post> findByAuthorsInAndTagsInSorted(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("authors") Set<String> authors,
            @Param("tags") Set<String> tags,
            Pageable pageable
    );

}
