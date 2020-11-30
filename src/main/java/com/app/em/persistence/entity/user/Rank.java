package com.app.em.persistence.entity.user;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "rank")
public class Rank
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    @Column(name = "rank_name", unique = true)
    private String rankName;

    @JsonBackReference(value = "user-rank")
    @OneToMany(mappedBy = "rank", cascade = CascadeType.ALL)
    private Set<User> users = new HashSet<>();


    public Rank() {  }

    public Rank(int id, @NotBlank String rankName, Set<User> users)
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

    public Set<User> getUsers()
    {
        return users;
    }

    public void setUsers(Set<User> users)
    {
        this.users = users;
    }
}
