package com.kchonov.springdocker.entity;

import com.sun.istack.NotNull;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Krasi
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User extends Users {
    
    @NotNull
    @NotEmpty
    @Pattern(regexp = "^ROLE_ADMIN$|ROLE_MERCHANT")
    private String role;
    
    @Builder
    public User(String username, String password, int enabled, String role){
      super(username, password, enabled);
      this.role = role;
    } 
}
