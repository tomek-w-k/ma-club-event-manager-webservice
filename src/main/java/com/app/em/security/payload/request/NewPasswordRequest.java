package com.app.em.security.payload.request;

import javax.validation.constraints.NotBlank;


public class NewPasswordRequest
{
    @NotBlank
    private String token;

    @NotBlank
    private String password;


    public NewPasswordRequest() {  }

    public NewPasswordRequest(@NotBlank String token, @NotBlank String password)
    {
        this.token = token;
        this.password = password;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
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
