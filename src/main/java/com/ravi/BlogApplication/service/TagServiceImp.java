package com.ravi.BlogApplication.service;

import com.ravi.BlogApplication.dao.PostRepository;
import com.ravi.BlogApplication.dao.TagRepository;
import com.ravi.BlogApplication.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImp implements TagService{

    private TagRepository tagRepository;
    @Autowired
    public TagServiceImp(TagRepository tagRepository) {
        this.tagRepository=tagRepository;
    }

    @Override
    public Tag getOrCreateTagByName(String tagName) {
        Tag tag= tagRepository.findByName(tagName);
        if (tag == null) {
            tag = new Tag();
            tag.setName(tagName);
            tagRepository.save(tag);
        }
        return tag;
    }

    @Override
    public List<String> getTags() {
        List<String> tags=tagRepository.findAllDistinctTags();
        return tags;
    }
}
