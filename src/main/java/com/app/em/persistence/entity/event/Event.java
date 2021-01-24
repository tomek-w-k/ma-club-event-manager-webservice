package com.app.em.persistence.entity.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;


@Entity
@Table(name = "event")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Event
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "event_name")
    private String eventName;

    @Lob
    @Column(name = "event_description")
    private String eventDescription;

    @Column(name = "event_picture_path")
    private String eventPicturePath;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "date_created", updatable = false)
    private Date dateCreated;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "start_date")
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "end_date")
    private Date endDate;


    public Event() {  }

    public Event(Long id, String eventName, String eventDescription, String eventPicturePath, Date dateCreated, Date startDate, Date endDate)
    {
        this.id = id;
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.eventPicturePath = eventPicturePath;
        this.dateCreated = dateCreated;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getEventName()
    {
        return eventName;
    }

    public void setEventName(String eventName)
    {
        this.eventName = eventName;
    }

    public String getEventDescription()
    {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription)
    {
        this.eventDescription = eventDescription;
    }

    public String getEventPicturePath()
    {
        return eventPicturePath;
    }

    public void setEventPicturePath(String eventPicturePath)
    {
        this.eventPicturePath = eventPicturePath;
    }

    public Date getDateCreated()
    {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated)
    {
        this.dateCreated = dateCreated;
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    public Date getEndDate()
    {
        return endDate;
    }

    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }
}
