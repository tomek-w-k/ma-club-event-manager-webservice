package com.app.em.persistence.entity.event;

import com.app.em.persistence.entity.registration.CampRegistration;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "camp_event")
public class CampEvent extends Event
{
    @Column(name = "sayonara_meeting")
    private Boolean sayonaraMeeting;

    @Column(name = "accommodation")
    private Boolean accommodation;

    @NotBlank
    @Column(name = "clothing_type")
    private String clothingType;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "camp_event_id")
    private List<ClothingSize> clothingSizes;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "camp_event_id")
    private List<Fee> fees;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "camp_event_id")
    private List<CampRegistration> campRegistrations;


    public CampEvent() {  }

    public CampEvent(Long id,
                     String eventName,
                     String eventDescription,
                     String eventPicturePath,
                     Date dateCreated,
                     Date startDate,
                     Date endDate,
                     Boolean sayonaraMeeting,
                     Boolean accommodation,
                     String clothingType,
                     List<ClothingSize> clothingSizes,
                     List<Fee> fees,
                     List<CampRegistration> campRegistrations)
    {
        super(id, eventName, eventDescription, eventPicturePath, dateCreated, startDate, endDate);
        this.sayonaraMeeting = sayonaraMeeting;
        this.accommodation = accommodation;
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

    public Boolean getAccommodation()
    {
        return accommodation;
    }

    public void setAccommodation(Boolean accommodation)
    {
        this.accommodation = accommodation;
    }

    public String getClothingType()
    {
        return clothingType;
    }

    public void setClothingType(String clothingType)
    {
        this.clothingType = clothingType;
    }

    public List<ClothingSize> getClothingSizes()
    {
        return clothingSizes;
    }

    public void setClothingSizes(List<ClothingSize> clothingSizes)
    {
        this.clothingSizes = clothingSizes;
    }

    public List<Fee> getFees()
    {
        return fees;
    }

    public void setFees(List<Fee> fees)
    {
        this.fees = fees;
    }

    public List<CampRegistration> getCampRegistrations()
    {
        return campRegistrations;
    }

    public void setCampRegistrations(List<CampRegistration> campRegistrations)
    {
        this.campRegistrations = campRegistrations;
    }
}
