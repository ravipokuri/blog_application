package com.ravi.BlogApplication.service;

import com.ravi.BlogApplication.entity.User;
import org.springframework.stereotype.Service;


public interface UserService {
    void saveUser(User user);
}
