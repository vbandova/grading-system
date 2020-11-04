package com.interview.roiti.gradingsystem.service;

import com.interview.roiti.gradingsystem.model.User;

public interface TokenService {

    String generateToken(User user);

    User parseToken(String token);
}
