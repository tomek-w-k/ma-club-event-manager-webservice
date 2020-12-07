package com.app.em.persistence.entity.registration;

import com.app.em.persistence.entity.event.Event;
import com.app.em.persistence.entity.user.User;

import javax.persistence.*;


@Entity
@Table(name = "registration")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Registration
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Event event;

    @Column(name = "fee_received")
    private Boolean feeReceived;


    public Registration() {  }

    public Registration(Long id, User user, Event event, Boolean feeReceived)
    {
        this.id = id;
        this.user = user;
        this.event = event;
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

    public Event getEvent()
    {
        return event;
    }

    public void setEvent(Event event)
    {
        this.event = event;
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
