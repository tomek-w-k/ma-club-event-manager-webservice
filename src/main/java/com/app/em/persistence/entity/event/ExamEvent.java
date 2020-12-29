package com.app.em.persistence.entity.event;

import com.app.em.persistence.entity.registration.ExamRegistration;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;


@Entity
@Table(name = "exam_event")
public class ExamEvent extends Event
{
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "exam_event_id")
    private Set<Fee> fees;

    /*
        Setting FetchType.EAGER here caused that exam registrations weren't removed from database
        by calling deleteById() method from ExamRegistrationRpository without any exception thrown.
     */
    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "exam_event_id")
    private Set<ExamRegistration> examRegistrations;


    public ExamEvent() {  }

    public ExamEvent(Long id,
                     String eventName,
                     String eventDescription,
                     String eventPicturePath,
                     Date dateCreated,
                     Date startDate,
                     Date endDate,
                     Set<Fee> fees,
                     Set<ExamRegistration> examRegistrations)
    {
        super(id, eventName, eventDescription, eventPicturePath, dateCreated, startDate, endDate);
        this.fees = fees;
        this.examRegistrations = examRegistrations;
    }

    public Set<Fee> getFees()
    {
        return fees;
    }

    public void setFees(Set<Fee> fees)
    {
        this.fees = fees;
    }

    public Set<ExamRegistration> getExamRegistrations()
    {
        return examRegistrations;
    }

    public void setExamRegistrations(Set<ExamRegistration> examRegistrations)
    {
        this.examRegistrations = examRegistrations;
    }
}