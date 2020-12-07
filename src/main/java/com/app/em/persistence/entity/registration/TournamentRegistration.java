package com.app.em.persistence.entity.registration;

import com.app.em.persistence.entity.event.*;
import com.app.em.persistence.entity.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import liquibase.pro.packaged.J;

import javax.persistence.*;


@Entity
@Table(name = "tournament_registration")
public class TournamentRegistration extends Registration
{
    @Column(name = "sayonara_meeting_participation")
    private Boolean sayonaraMeetingParticipation;

    @Column(name = "accomodation")
    private Boolean accomodation;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private RoomType roomType;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private StayPeriod stayPeriod;

    @Column(name = "as_judge_participation")
    private Boolean asJudgeParticipation;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private WeightAgeCategory weightAgeCategory;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "tournament_event_id")
    TournamentEvent tournamentEvent;


    public TournamentRegistration() {  }

    public TournamentRegistration(Long id,
                                  User user,
                                  Event event,
                                  Boolean feeReceived,
                                  Boolean sayonaraMeetingParticipation,
                                  Boolean accomodation,
                                  RoomType roomType,
                                  StayPeriod stayPeriod,
                                  Boolean asJudgeParticipation,
                                  WeightAgeCategory weightAgeCategory,
                                  TournamentEvent tournamentEvent)
    {
        super(id, user, event, feeReceived);
        this.sayonaraMeetingParticipation = sayonaraMeetingParticipation;
        this.accomodation = accomodation;
        this.roomType = roomType;
        this.stayPeriod = stayPeriod;
        this.asJudgeParticipation = asJudgeParticipation;
        this.weightAgeCategory = weightAgeCategory;
        this.tournamentEvent = tournamentEvent;
    }

    public Boolean getSayonaraMeetingParticipation()
    {
        return sayonaraMeetingParticipation;
    }

    public void setSayonaraMeetingParticipation(Boolean sayonaraMeetingParticipation)
    {
        this.sayonaraMeetingParticipation = sayonaraMeetingParticipation;
    }

    public Boolean getAccomodation()
    {
        return accomodation;
    }

    public void setAccomodation(Boolean accomodation)
    {
        this.accomodation = accomodation;
    }

    public RoomType getRoomType()
    {
        return roomType;
    }

    public void setRoomType(RoomType roomType)
    {
        this.roomType = roomType;
    }

    public StayPeriod getStayPeriod()
    {
        return stayPeriod;
    }

    public void setStayPeriod(StayPeriod stayPeriod)
    {
        this.stayPeriod = stayPeriod;
    }

    public Boolean getAsJudgeParticipation()
    {
        return asJudgeParticipation;
    }

    public void setAsJudgeParticipation(Boolean asJudgeParticipation)
    {
        this.asJudgeParticipation = asJudgeParticipation;
    }

    public WeightAgeCategory getWeightAgeCategory()
    {
        return weightAgeCategory;
    }

    public void setWeightAgeCategory(WeightAgeCategory weightAgeCategory)
    {
        this.weightAgeCategory = weightAgeCategory;
    }

    public TournamentEvent getTournamentEvent()
    {
        return tournamentEvent;
    }

    public void setTournamentEvent(TournamentEvent tournamentEvent)
    {
        this.tournamentEvent = tournamentEvent;
    }
}
