package com.app.em.persistence.entity.registration;

import com.app.em.persistence.entity.event.Event;
import com.app.em.persistence.entity.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;


@Entity
@Table(name = "registration")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Registration
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@JsonBackReference
    @ManyToOne( fetch = FetchType.EAGER,
                cascade = {
                  //  CascadeType.PERSIST,
//                    CascadeType.REFRESH,
                    CascadeType.MERGE
                })
    private User user;

    @Column(name = "fee_received")
    private Boolean feeReceived = false;


    public Registration() {  }

    public Registration(Long id, User user, Boolean feeReceived)
    {
        this.id = id;
        this.user = user;
        this.feeReceived = feeReceived;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public Boolean getFeeReceived()
    {
        return feeReceived;
    }

    public void setFeeReceived(Boolean feeReceived)
    {
        this.feeReceived = feeReceived;
    }
}
