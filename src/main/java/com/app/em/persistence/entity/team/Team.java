package com.app.em.persistence.entity.team;

import com.app.em.persistence.entity.event.TournamentEvent;
import com.app.em.persistence.entity.registration.TournamentRegistration;
import com.app.em.persistence.entity.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "team")
public class Team
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private User trainer;

    @JsonManagedReference(value = "tournament_registration_team_id")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "team_id")
    private List<TournamentRegistration> tournamentRegistrations;

    @JsonBackReference(value = "team_tournament_event_id")
    @ManyToOne
    @JoinColumn(name = "tournament_event_id")
    TournamentEvent tournamentEvent;


    public Team() {  }

    public Team(Long id, User trainer, List<TournamentRegistration> tournamentRegistrations, TournamentEvent tournamentEvent)
    {
        this.id = id;
        this.trainer = trainer;
        this.tournamentRegistrations = tournamentRegistrations;
        this.tournamentEvent = tournamentEvent;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public User getTrainer()
    {
        return trainer;
    }

    public void setTrainer(User trainer)
    {
        this.trainer = trainer;
    }

    public List<TournamentRegistration> getTournamentRegistrations()
    {
        return tournamentRegistrations;
    }

    public void setTournamentRegistrations(List<TournamentRegistration> tournamentRegistrations)
    {
        this.tournamentRegistrations = tournamentRegistrations;
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
