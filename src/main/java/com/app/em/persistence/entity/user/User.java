package com.app.em.persistence.entity.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


@Entity
@Table(name = "user")
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Column(name = "full_name")
    private String fullName;

    @Email
    @NotBlank
    @Column(name = "email", unique = true)
    private String email;

    @NotBlank
    @Column(name = "password")
    private String password;

    @Column(name = "country")
    private String country;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "user_role",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

//    @JsonManagedReference(value = "user-rank")
    @ManyToOne
    @JoinColumn(name = "rank_id")
    private Rank rank;

//    @JsonManagedReference(value = "user-club")
    @ManyToOne( fetch = FetchType.EAGER,
                cascade = {
//                        CascadeType.PERSIST,
//                        CascadeType.REFRESH,
                        CascadeType.MERGE
                })
    @JoinColumn(name = "club_id")
    private Club club;

//    @JsonManagedReference(value = "user-branch-chief")
    @ManyToOne( fetch = FetchType.EAGER,
                cascade = {
//                        CascadeType.PERSIST,
//                        CascadeType.REFRESH,
                        CascadeType.MERGE
                })
    @JoinColumn(name = "branch_chief_id")
    private BranchChief branchChief;


    public User() {  }

    public User(long id,
                @NotBlank String fullName,
                @Email @NotBlank String email,
                @NotBlank String password,
                String country,
                Set<Role> roles,
                Rank rank,
                Club club,
                BranchChief branchChief)
    {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.country = country;
        this.roles = roles;
        this.rank = rank;
        this.club = club;
        this.branchChief = branchChief;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getFullName()
    {
        return fullName;
    }

    public void setFullName(String fullName)
    {
        this.fullName = fullName;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public Set<Role> getRoles()
    {
        return roles;
    }

    public void setRoles(Set<Role> roles)
    {
        this.roles = roles;
    }

    public Rank getRank()
    {
        return rank;
    }

    public void setRank(Rank rank)
    {
        this.rank = rank;
    }

    public Club getClub()
    {
        return club;
    }

    public void setClub(Club club)
    {
        this.club = club;
    }

    public BranchChief getBranchChief()
    {
        return branchChief;
    }

    public void setBranchChief(BranchChief branchChief)
    {
        this.branchChief = branchChief;
    }

    @Override
    public String toString()
    {
        return "User {" +
                "id : " + id + "\n" +
                "fullName : " + fullName + "\n" +
                "email : " + email + "\n" +
                "password : " + password + "\n" +
                "country : " + country + "\n" +
                "roles : " + roles.stream().map(role -> role.getRoleName().toString()).collect(Collectors.joining(", ")) + "\n" +
                "rank : " + (rank != null ? ("id: " + rank.getId() + " rankName: " + rank.getRankName()) : "null") + "\n" +
                "club : " + (club != null ? ("id: " + club.getId() + " clubName: " + club.getClubName()) : "null") + "\n" +
                "branchChief : " + (branchChief != null ? ("id: " + branchChief.getId() + " branchChiefName: " + branchChief.getBranchChiefName()) : "null") + "\n" +
                '}';
    }
}
