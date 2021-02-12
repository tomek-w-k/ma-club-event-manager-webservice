package com.app.em.persistence.entity.security;

import com.app.em.persistence.entity.user.User;
import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "password_reset_token")
public class PasswordResetToken
{
    @Id
    private Long id;

    @Column(name = "token")
    private String token;

    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    private User user;

    @Column(name = "expiry_date")
    private Date expiryDate;


    public PasswordResetToken() {  }

    public PasswordResetToken(Long id, String token, User user, Date expiryDate)
    {
        this.id = id;
        this.token = token;
        this.user = user;
        this.expiryDate = expiryDate;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public Date getExpiryDate()
    {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate)
    {
        this.expiryDate = expiryDate;
    }
}