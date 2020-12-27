package com.app.em.persistence.entity.registration;

import com.app.em.persistence.entity.event.CampEvent;
import com.app.em.persistence.entity.event.ClothingSize;
import com.app.em.persistence.entity.event.Event;
import com.app.em.persistence.entity.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;


@Entity
@Table(name = "camp_registration")
public class CampRegistration extends Registration
{
    @Column(name = "sayonara_meeting_participation")
    private Boolean sayonaraMeetingParticipation;

    @OneToOne(fetch = FetchType.EAGER)
    //@MapsId
    @JoinColumn(name = "clothing_size_id")
    private ClothingSize clothingSize;

    @Column(name = "accommodation")
    private Boolean accommodation;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "camp_event_id")
    CampEvent campEvent;


    public CampRegistration() {  }

    public CampRegistration(Long id,
                            User user,
                            Boolean feeReceived,
                            Boolean sayonaraMeetingParticipation,
                            ClothingSize clothingSize,
                            Boolean accommodation,
                            CampEvent campEvent)
    {
        super(id, user, feeReceived);
        this.sayonaraMeetingParticipation = sayonaraMeetingParticipation;
        this.clothingSize = clothingSize;
        this.accommodation = accommodation;
        this.campEvent = campEvent;
    }

    public Boolean getSayonaraMeetingParticipation()
    {
        return sayonaraMeetingParticipation;
    }

    public void setSayonaraMeetingParticipation(Boolean sayonaraMeetingParticipation)
    {
        this.sayonaraMeetingParticipation = sayonaraMeetingParticipation;
    }

    public ClothingSize getClothingSize()
    {
        return clothingSize;
    }

    public void setClothingSize(ClothingSize clothingSize)
    {
        this.clothingSize = clothingSize;
    }

    public Boolean getAccommodation()
    {
        return accommodation;
    }

    public void setAccommodation(Boolean accommodation)
    {
        this.accommodation = accommodation;
    }

    public CampEvent getCampEvent()
    {
        return campEvent;
    }

    public void setCampEvent(CampEvent campEvent)
    {
        this.campEvent = campEvent;
    }
}
