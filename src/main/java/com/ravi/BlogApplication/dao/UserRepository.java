package com.ravi.BlogApplication.dao;

import com.ravi.BlogApplication.entity.Post;
import com.ravi.BlogApplication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {

}
