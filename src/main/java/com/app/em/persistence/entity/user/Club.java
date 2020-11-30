package com.app.em.persistence.entity.user;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "club")
public class Club
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    @Column(name = "club_name", unique = true)
    private String clubName;

    @JsonBackReference(value = "user-club")
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    private Set<User> users = new HashSet<>();


    public Club() {  }

    public Club(int id, @NotBlank String clubName, Set<User> users)
    {
        this.id = id;
        this.clubName = clubName;
        this.users = users;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getClubName()
    {
        return clubName;
    }

    public void setClubName(String clubName)
    {
        this.clubName = clubName;
    }

    public Set<User> getUsers()
    {
        return users;
    }

    public void setUsers(Set<User> users)
    {
        this.users = users;
    }
}
