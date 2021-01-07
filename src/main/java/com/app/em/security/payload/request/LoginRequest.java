package com.app.em.security.payload.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


public class LoginRequest
{
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;


    public LoginRequest() {  }

    public LoginRequest(@Email @NotBlank String email, @NotBlank String password)
    {
        this.email = email;
        this.password = password;
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
}
