package com.app.em.persistence.entity.user;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "rank")
public class Rank
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "rank_name", unique = true)
    private String rankName;

    @JsonBackReference(value = "user-rank")
    @OneToMany(mappedBy = "rank", cascade = CascadeType.ALL)
    private List<User> users;


    public Rank() {  }

    public Rank(int id, String rankName, List<User> users)
    {
        this.id = id;
        this.rankName = rankName;
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

    public String getRankName()
    {
        return rankName;
    }

    public void setRankName(String rankName)
    {
        this.rankName = rankName;
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
