package com.example.usermanagement.service;

import com.example.usermanagement.entity.UserM;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Map;


public interface UserMService {
    public void createUser(UserM userM);
    public UserM getUser(Long id);
    public boolean loginUser(UserM userM);
    public void getUserWithNotification(String email);
    //pdf exporter
    public List<UserM> listAll();
    //jwt
    public UserDetails loadUserByUsername(String username);

    //pdf generator
    void generatePdfFile(String templateName, Map<String, Object> data, String pdfFileName);




}
