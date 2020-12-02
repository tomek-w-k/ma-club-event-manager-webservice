package com.app.em.security.payload.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Set;


public class SignUpRequest
{
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    private Set<String> roles;


    public SignUpRequest() {  }

    public SignUpRequest(@Email @NotBlank String email, @NotBlank String password, Set<String> roles)
    {
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public Set<String> getRoles()
    {
        return roles;
    }

    public void setRoles(Set<String> roles)
    {
        this.roles = roles;
    }
}
