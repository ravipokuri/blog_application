package com.ravi.BlogApplication.dao;

import com.ravi.BlogApplication.entity.Post;
import com.ravi.BlogApplication.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag,Integer> {
    Tag findByName(String name);

    @Query("SELECT DISTINCT t.name FROM Tag t")
    List<String> findAllDistinctTags();
}
