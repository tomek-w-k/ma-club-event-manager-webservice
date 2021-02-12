package com.app.em.security.payload.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


public class PasswordResetRequest
{
    @Email
    @NotBlank
    private String email;


    public PasswordResetRequest() {  }

    public PasswordResetRequest(@Email @NotBlank String email)
    {
        this.email = email;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }
}
