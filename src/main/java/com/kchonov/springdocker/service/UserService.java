package com.kchonov.springdocker.service;

import com.kchonov.springdocker.entity.Authorities;
import com.kchonov.springdocker.entity.User;
import com.kchonov.springdocker.entity.Users;
import com.kchonov.springdocker.entity.constants.Messages;
import com.kchonov.springdocker.exception.UserAlreadyExistException;
import com.kchonov.springdocker.interfaces.IUserService;
import com.kchonov.springdocker.messages.ResponseMessage;
import com.kchonov.springdocker.repository.AuthorityRepository;
import com.kchonov.springdocker.repository.UserRepository;
import com.kchonov.springdocker.utils.CSVHelper;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Krasi
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements IUserService {
    
    Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    
    private final AuthorityRepository authorityRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @PostConstruct
    public void init() {
        userRepository.save(new Users("admin", passwordEncoder.encode("pass"), 1));
        authorityRepository.save(new Authorities("admin", "ROLE_ADMIN"));
    }

    @Override
    public Users registerNewUserAccount(User user) throws UserAlreadyExistException {
        if (userExist(user.getUsername())) {
            throw new UserAlreadyExistException("There is an account with that username address: " + user.getUsername());
        }
        
        Users newUser = new Users();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setEnabled(user.getEnabled());
        userRepository.save(newUser);
        
        Authorities authority = new Authorities();
        authority.setUsername(user.getUsername());
        authority.setAuthority(user.getRole());
        authorityRepository.save(authority);
        
        return newUser;
    }
    
    public void registerNewUserAccounts(List<User> users) throws UserAlreadyExistException {
        users.forEach(u -> {
            registerNewUserAccount(u);
        });
    }

    private boolean userExist(String username) {
        return userRepository.findByUsername(username) != null;
    }
    
    @Modifying
    @Transactional
    public ResponseEntity<ResponseMessage> importUsers(MultipartFile file) {
        logger.info("Begin file upload: " + file.getOriginalFilename());
        String message;
        if (CSVHelper.hasCSVFormat(file)) {
            try {
                List<User> users = CSVHelper.csvToUsers(file.getInputStream());
                registerNewUserAccounts(users);
                message = Messages.FILE_UPLOAD_SUCCESSFUL;
                logger.info(message);
                return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage(message));
            } catch (Exception e) {
                logger.error("Error occured during file upload");
                message = Messages.FILE_UPLOAD_ERROR(file.getOriginalFilename());
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
            }
        }
        message = Messages.FILE_NOT_A_CSV;
        logger.error(message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
    }
}
