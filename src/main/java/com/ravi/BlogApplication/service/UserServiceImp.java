package com.ravi.BlogApplication.service;

import com.ravi.BlogApplication.dao.PostRepository;
import com.ravi.BlogApplication.dao.UserRepository;
import com.ravi.BlogApplication.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImp implements UserService{

    private UserRepository userRepository;
    @Autowired
    public UserServiceImp(UserRepository userRepository) {
        this.userRepository=userRepository;
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }
}
