package com.example.usermanagement.controller;

import com.example.usermanagement.entity.UserM;
import com.example.usermanagement.exporter.UserPDFExporter;
import com.example.usermanagement.model.User;
import com.example.usermanagement.service.UserMService;
import com.example.usermanagement.util.JwtUtil;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserMController {
    @Autowired
    UserMService userMService;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/signup")
    public String createUser(@RequestBody UserM userM){
        userMService.createUser(userM);
        return "registered successfully";
    }
    @GetMapping("/{id}")
    public UserM getUser(@PathVariable Long id){
        return userMService.getUser(id);
    }
    @GetMapping("/login")
    public boolean loginUser(@RequestBody UserM userM){
        return userMService.loginUser(userM);
    }
    @GetMapping("/email")
    public void getUserWithEmail(@RequestBody String email){
     userMService.getUserWithNotification(email);
    }
    // pdf exporter
    @GetMapping("/export/pdf")
    public void exportToPDF(HttpServletResponse response) throws DocumentException, IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        List<UserM> listUsers = userMService.listAll();

        UserPDFExporter exporter = new UserPDFExporter(listUsers);
        exporter.export(response);

/*
        for (UserM emp : listUsers){
            UserPDFExporter exporter = new UserPDFExporter(Arrays.asList(emp));
            exporter.export(response);
        }*/

    }
    //jwt generate token
   @PostMapping("/authenticate")
    public String generateToken(@RequestBody User user) throws Exception {

        try{

           this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword())
            );
        }catch (Exception ex){
            throw new Exception("Invalid username/password");
        }
        // userDetails = this.userMService.loadUserByUsername(user.getUserName());
        return jwtUtil.generateToken(user.getUserName());

    }

}
