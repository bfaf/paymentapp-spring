package com.kchonov.springdocker.controller;

import com.kchonov.springdocker.messages.ResponseMessage;
import com.kchonov.springdocker.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Krasi
 */
@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/v1/users")
public class UserController {
    
    Logger logger = LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    private UserService userService;
    
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/import")
    public ResponseEntity<ResponseMessage> importMerchants(@CurrentSecurityContext(expression = "authentication") String username, @RequestParam("file") MultipartFile file) {
        logger.info(username + " is trying to import " + file.getOriginalFilename());
        return userService.importUsers(file);
    }
    
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MERCHANT')")
    @GetMapping("/role")
    @ResponseBody
    public ResponseEntity<String> getRole(@CurrentSecurityContext(expression = "authentication") Authentication authentication) {
        return new ResponseEntity<>(authentication.getAuthorities().toArray()[0].toString(), HttpStatus.OK);
    }
}
