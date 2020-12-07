package com.app.em.persistence.entity.event;

import com.app.em.persistence.entity.registration.CampRegistration;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;


@Entity
@Table(name = "camp_event")
public class CampEvent extends Event
{
    @Column(name = "sayonara_meeting")
    private Boolean sayonaraMeeting;

    @Column(name = "clothing_type")
    private String clothingType;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "camp_event_id")
    private Set<ClothingSize> clothingSizes;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "camp_event_id")
    private Set<Fee> fees;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "camp_event_id")
    private Set<CampRegistration> campRegistrations;


    public CampEvent() {  }

    public CampEvent(Long id,
                     String eventName,
                     String eventDescription,
                     String eventPicturePath,
                     Date dateCreated,
                     Date startDate,
                     Date endDate,
                     Boolean sayonaraMeeting,
                     String clothingType,
                     Set<ClothingSize> clothingSizes,
                     Set<Fee> fees,
                     Set<CampRegistration> campRegistrations)
    {
        super(id, eventName, eventDescription, eventPicturePath, dateCreated, startDate, endDate);
        this.sayonaraMeeting = sayonaraMeeting;
        this.clothingType = clothingType;
        this.clothingSizes = clothingSizes;
        this.fees = fees;
        this.campRegistrations = campRegistrations;
    }

    public Boolean getSayonaraMeeting()
    {
        return sayonaraMeeting;
    }

    public void setSayonaraMeeting(Boolean sayonaraMeeting)
    {
        this.sayonaraMeeting = sayonaraMeeting;
    }

    public String getClothingType()
    {
        return clothingType;
    }

    public void setClothingType(String clothingType)
    {
        this.clothingType = clothingType;
    }

    public Set<ClothingSize> getClothingSizes()
    {
        return clothingSizes;
    }

    public void setClothingSizes(Set<ClothingSize> clothingSizes)
    {
        this.clothingSizes = clothingSizes;
    }

    public Set<Fee> getFees()
    {
        return fees;
    }

    public void setFees(Set<Fee> fees)
    {
        this.fees = fees;
    }

    public Set<CampRegistration> getCampRegistrations()
    {
        return campRegistrations;
    }

    public void setCampRegistrations(Set<CampRegistration> campRegistrations)
    {
        this.campRegistrations = campRegistrations;
    }
}
