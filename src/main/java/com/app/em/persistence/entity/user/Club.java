package com.app.em.persistence.entity.user;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "club")
public class Club
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "club_name", unique = true)
    private String clubName;

    @JsonBackReference(value = "user-club")
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    private List<User> users;


    public Club() {  }

    public Club(int id, String clubName, List<User> users)
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

    public List<User> getUsers()
    {
        return users;
    }

    public void setUsers(List<User> users)
    {
        this.users = users;
    }
}
