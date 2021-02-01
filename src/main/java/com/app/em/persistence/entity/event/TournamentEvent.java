package com.app.em.persistence.entity.event;

import com.app.em.persistence.entity.registration.ExamRegistration;
import com.app.em.persistence.entity.registration.TournamentRegistration;
import com.app.em.persistence.entity.team.Team;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;


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
    private Set<RoomType> roomTypes;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "tournament_event_id")
    private Set<StayPeriod> stayPeriods;

    @Column(name = "fee")
    private Float fee;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "tournament_event_id")
    private Set<WeightAgeCategory> weightAgeCategories;

//    @JsonManagedReference
//    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
//    @JoinColumn(name = "tournament_event_id")
//    private Set<TournamentRegistration> tournamentRegistrations;

    @JsonManagedReference(value = "team_tournament_event_id")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "tournament_event_id")
    private Set<Team> teams;


    public TournamentEvent() {  }

    public TournamentEvent(Long id,
                           String eventName,
                           String eventDescription,
                           String eventPicturePath,
                           Date dateCreated,
                           Date startDate,
                           Date endDate,
                           Boolean sayonaraMeeting,
                           Boolean accommodation,
                           Set<RoomType> roomTypes,
                           Set<StayPeriod> stayPeriods,
                           Float fee,
                           Set<WeightAgeCategory> weightAgeCategories,
    //                       Set<TournamentRegistration> tournamentRegistrations,
                           Set<Team> teams
    )
    {
        super(id, eventName, eventDescription, eventPicturePath, dateCreated, startDate, endDate);
        this.sayonaraMeeting = sayonaraMeeting;
        this.accommodation = accommodation;
        this.roomTypes = roomTypes;
        this.stayPeriods = stayPeriods;
        this.fee = fee;
        this.weightAgeCategories = weightAgeCategories;
        //this.tournamentRegistrations = tournamentRegistrations;
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

    public Set<RoomType> getRoomTypes()
    {
        return roomTypes;
    }

    public void setRoomTypes(Set<RoomType> roomTypes)
    {
        this.roomTypes = roomTypes;
    }

    public Set<StayPeriod> getStayPeriods()
    {
        return stayPeriods;
    }

    public void setStayPeriods(Set<StayPeriod> stayPeriods)
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

    public Set<WeightAgeCategory> getWeightAgeCategories()
    {
        return weightAgeCategories;
    }

    public void setWeightAgeCategories(Set<WeightAgeCategory> weightAgeCategories)
    {
        this.weightAgeCategories = weightAgeCategories;
    }

//    public Set<TournamentRegistration> getTournamentRegistrations()
//    {
//        return tournamentRegistrations;
//    }
//
//    public void setTournamentRegistrations(Set<TournamentRegistration> tournamentRegistrations)
//    {
//        this.tournamentRegistrations = tournamentRegistrations;
//    }


    public Set<Team> getTeams()
    {
        return teams;
    }

    public void setTeams(Set<Team> teams)
    {
        this.teams = teams;
    }
}
