package com.app.em.persistence.entity.registration;

import com.app.em.persistence.entity.event.Event;
import com.app.em.persistence.entity.event.ExamEvent;
import com.app.em.persistence.entity.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;


@Entity
@Table(name = "exam_registration")
public class ExamRegistration extends Registration
{
    @Column(name = "participation_accepted")
    private Boolean participationAccepted;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "exam_event_id")
    private ExamEvent examEvent;


    public ExamRegistration() {  }

    public ExamRegistration(Long id, User user, Event event, Boolean feeReceived, Boolean participationAccepted, ExamEvent examEvent)
    {
        super(id, user, event, feeReceived);
        this.participationAccepted = participationAccepted;
        this.examEvent = examEvent;
    }

    public Boolean getParticipationAccepted()
    {
        return participationAccepted;
    }

    public void setParticipationAccepted(Boolean participationAccepted)
    {
        this.participationAccepted = participationAccepted;
    }

    public ExamEvent getExamEvent()
    {
        return examEvent;
    }

    public void setExamEvent(ExamEvent examEvent)
    {
        this.examEvent = examEvent;
    }
}
