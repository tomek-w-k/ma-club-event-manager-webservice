package com.app.em.persistence.entity.event;

import com.app.em.persistence.entity.team.Team;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "tournament_event")
public class TournamentEvent extends Event
{
    @Column(name = "sayonara_meeting")
    private Boolean sayonaraMeeting;

    @Column(name = "accommodation")
    private Boolean accommodation;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "tournament_event_id")
    private List<RoomType> roomTypes;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "tournament_event_id")
    private List<StayPeriod> stayPeriods;

    @Column(name = "fee")
    private Float fee;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "tournament_event_id")
    private List<WeightAgeCategory> weightAgeCategories;

    @JsonManagedReference(value = "team_tournament_event_id")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "tournament_event_id")
    private List<Team> teams;


    public TournamentEvent()
    {
    }

    public TournamentEvent(Long id,
                           String eventName,
                           String eventDescription,
                           String eventPicturePath,
                           Date dateCreated,
                           Date startDate,
                           Date endDate,
                           Boolean suspendRegistration,
                           Boolean sayonaraMeeting,
                           Boolean accommodation,
                           List<RoomType> roomTypes,
                           List<StayPeriod> stayPeriods,
                           Float fee,
                           List<WeightAgeCategory> weightAgeCategories,
                           List<Team> teams
    )
    {
        super(id, eventName, eventDescription, eventPicturePath, dateCreated, startDate, endDate, suspendRegistration);
        this.sayonaraMeeting = sayonaraMeeting;
        this.accommodation = accommodation;
        this.roomTypes = roomTypes;
        this.stayPeriods = stayPeriods;
        this.fee = fee;
        this.weightAgeCategories = weightAgeCategories;
        this.teams = teams;
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

    public List<RoomType> getRoomTypes()
    {
        return roomTypes;
    }

    public void setRoomTypes(List<RoomType> roomTypes)
    {
        this.roomTypes = roomTypes;
    }

    public List<StayPeriod> getStayPeriods()
    {
        return stayPeriods;
    }

    public void setStayPeriods(List<StayPeriod> stayPeriods)
    {
        this.stayPeriods = stayPeriods;
    }

    public Float getFee()
    {
        return fee;
    }

    public void setFee(Float fee)
    {
        this.fee = fee;
    }

    public List<WeightAgeCategory> getWeightAgeCategories()
    {
        return weightAgeCategories;
    }

    public void setWeightAgeCategories(List<WeightAgeCategory> weightAgeCategories)
    {
        this.weightAgeCategories = weightAgeCategories;
    }

    public List<Team> getTeams()
    {
        return teams;
    }

    public void setTeams(List<Team> teams)
    {
        this.teams = teams;
    }
}
