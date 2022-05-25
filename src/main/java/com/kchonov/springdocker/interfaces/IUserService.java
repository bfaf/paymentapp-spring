package com.kchonov.springdocker.interfaces;

import com.kchonov.springdocker.entity.User;
import com.kchonov.springdocker.entity.Users;

/**
 *
 * @author Krasi
 */
public interface IUserService {
    Users registerNewUserAccount(User user);
}
