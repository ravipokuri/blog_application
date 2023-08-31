package com.ravi.BlogApplication.service;

import com.ravi.BlogApplication.entity.Tag;
import java.util.*;
public interface TagService {
    Tag getOrCreateTagByName(String tagName);

    List<String> getTags();
}
