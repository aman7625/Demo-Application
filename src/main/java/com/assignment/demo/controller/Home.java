package com.assignment.demo.controller;

import com.assignment.demo.helper.JwtUtil;
import com.assignment.demo.model.User;
import com.assignment.demo.repo.UserRepository;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class Home {

    @Autowired
    private UserRepository repo;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    ServletContext servletContext;

    private final TemplateEngine templateEngine;

    public Home(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @RequestMapping("/welcome")
    public String welcome(){
        String text = "this is private page ";
        text +="this is not allowed to unauthorized users";
        return text;
    }

    @RequestMapping("/getusers")
    public Object getUser(@RequestHeader(name="Authorization") String token){
        System.out.println("Get Users "+ token);

        if(token!=null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);

            try {
                String username = this.jwtUtil.getUsernameFromToken(jwtToken);
                User me = repo.findByEmail(username);
                return me;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return "Token not found. Kindly Sign up for Sign in";

    }

    @RequestMapping(path = "/download")
    public ResponseEntity<?> getPDF(HttpServletRequest request, HttpServletResponse response, @RequestHeader(name="Authorization") String token) throws IOException {

        User user = (User) getUser(token);
        System.out.println(user.getEmail());
        //System.out.println(user.getPassword());

        /* Create HTML using Thymeleaf template Engine */
        WebContext context = new WebContext(request, response, servletContext);
        context.setVariable("userEntry", user);
        String userHtml = templateEngine.process("user", context);

        /* Setup Source and target I/O streams */
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setBaseUri("http://localhost:8080");

        /* Call convert method */
        HtmlConverter.convertToPdf(userHtml, target, converterProperties);

        /* extract output as bytes */
        byte[] bytes = target.toByteArray();

        /* Send the response as downloadable PDF */
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=user.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(bytes);

    }
}

