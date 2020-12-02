package com.app.em.security.payload.response;

import java.util.List;


public class JwtResponse
{
    private static final String TYPE = "Bearer";

    private String accessToken;
    private Long id;
    private String email;
    private List<String> roles;


    public JwtResponse() {  }

    public JwtResponse(String accessToken, Long id, String email, List<String> roles)
    {
        this.accessToken = accessToken;
        this.id = id;
        this.email = email;
        this.roles = roles;
    }

    public static String getTYPE()
    {
        return TYPE;
    }

    public String getAccessToken()
    {
        return accessToken;
    }

    public void setAccessToken(String accessToken)
    {
        this.accessToken = accessToken;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public List<String> getRoles()
    {
        return roles;
    }

    public void setRoles(List<String> roles)
    {
        this.roles = roles;
    }
}
