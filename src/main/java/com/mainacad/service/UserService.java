package com.mainacad.service;

import com.mainacad.dao.UserDAO;
import com.mainacad.model.User;

public class UserService {

    public static User create(User user){
        return UserDAO.create(user);
    }

    public static User getAuthUser(String login, String password){
        User user = UserDAO.findByLogin(login);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }


}
