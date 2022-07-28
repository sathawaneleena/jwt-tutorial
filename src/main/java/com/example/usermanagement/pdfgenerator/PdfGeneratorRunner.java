package com.example.usermanagement.pdfgenerator;

import com.example.usermanagement.entity.UserM;
import com.example.usermanagement.repo.UserMRepo;
import com.example.usermanagement.service.UserMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//generate pdf
@Component
public class PdfGeneratorRunner implements CommandLineRunner {

    @Autowired
    private UserMService userMService;
    @Autowired
    private UserMRepo userMRepo;


    @Override
    public void run(String... args) throws Exception {
        Map<String, Object> data = new HashMap<>();

      /*  List<UserM> userList = new ArrayList<>();
        UserM users = new UserM();
        users.setId(1L);
        users.setUserName("leena");
        users.setEmail("leena@humancloud.biz");
        users.setDesignation("developer");
        users.setAddress("123 Street Baner");
        userList.add(users);

        UserM user1 = new UserM();
        user1.setId(2L);
        user1.setUserName("payal");
        user1.setEmail("payal@humancloud.biz");
        user1.setDesignation("developer");
        user1.setAddress("123 Street Baner");
        userList.add(user1);

*/
//        data.put("customerList", customerList);

        List<UserM> userList = new ArrayList<>();
        userList=userMRepo.findAll();
        for(UserM u: userList){
            data.put("user", u);
            userMService.generatePdfFile("userm", data,
                    u.getUserName()+".pdf");
        }

    }
}

