package com.example.usermanagement.serviceimpl;
import com.example.usermanagement.VO.EmailMessage;
import com.example.usermanagement.VO.ResponseTemplateVO;
import com.example.usermanagement.entity.UserM;
import com.example.usermanagement.repo.UserMRepo;
import com.example.usermanagement.service.UserMService;
import com.lowagie.text.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.transaction.Transactional;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class UserMServiceImpl implements UserMService, UserDetailsService {
   private Logger logger = LoggerFactory.getLogger(UserMServiceImpl.class);



    @Autowired
    TemplateEngine templateEngine;

    @Value("${pdf.directory}")
    private String pdfDirectory;


    @Autowired
    UserMRepo userMRepo;

    @Autowired
    RestTemplate restTemplate;

    @Override
    public void createUser(UserM userM) {
        userMRepo.save(userM);
        getUserWithNotification(userM.getEmail());
    }

    @Override
    public UserM getUser(Long id) {
        Optional<UserM> user = userMRepo.findById(id);
        if (user.isPresent()) {
            return user.get();
        }
        return null;
    }

    @Override
    public boolean loginUser(UserM userM) {
        UserM userFromDB = userMRepo.findByEmail(userM.getEmail());
        if (userFromDB.getPassword().equals(userM.getPassword())) {
            userM.setVerified(true);
        }
        return userM.isVerified();
    }
    //user registered notification
    @Override
    public void getUserWithNotification(String email) {
        ResponseTemplateVO vo = new ResponseTemplateVO();
        UserM userM = userMRepo.findByEmail(email);
        EmailMessage message = new EmailMessage();
        message.setTo(email);
        message.setMessage("You are registered successfully , Thank you!");
        message.setSubject("Registered");
        String emailMessage = restTemplate
                .postForObject("http://localhost:8080/send-email", message, String.class);
        vo.setUserM(userM);
        vo.setEmailMessage(message);
    }


   public List<UserM> listAll() {
       return userMRepo.findAll(Sort.by("email").ascending());
    }


    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        System.out.println(userName);
        UserM userM = userMRepo.findByUserName(userName);
        if (userM.getUserName().equals(userName)) {
            return new org.springframework.security.core.userdetails.User(userM.getUserName(),
                    userM.getPassword(), new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("user not found");
        }
    }
    //pdf generator
   @Override
    public void generatePdfFile(String templateName, Map<String, Object> data, String pdfFileName) {
        Context context = new Context();
        context.setVariables(data);

        String htmlContent = templateEngine.process(templateName, context);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(pdfDirectory + pdfFileName);
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(fileOutputStream, false);
            renderer.finishPDF();
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        } catch (DocumentException e) {
            logger.error(e.getMessage(), e);
        }
    }


}





