package com.app.em.security.payload.request;

import com.app.em.persistence.entity.user.BranchChief;
import com.app.em.persistence.entity.user.Club;
import com.app.em.persistence.entity.user.Rank;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Set;


public class SignUpRequest
{
    @NotBlank
    private String fullName;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    private Club club;

    @NotBlank
    private String country;

    private Rank rank;

    private BranchChief branchChief;

    private Boolean asTrainer;


    public SignUpRequest() {  }

    public SignUpRequest(@NotBlank String fullName, @Email @NotBlank String email, @NotBlank String password, Club club, @NotBlank String country, Rank rank, BranchChief branchChief, Boolean asTrainer)
    {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.club = club;
        this.country = country;
        this.rank = rank;
        this.branchChief = branchChief;
        this.asTrainer = asTrainer;
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

    public Club getClub()
    {
        return club;
    }

    public void setClub(Club club)
    {
        this.club = club;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public Rank getRank()
    {
        return rank;
    }

    public void setRank(Rank rank)
    {
        this.rank = rank;
    }

    public BranchChief getBranchChief()
    {
        return branchChief;
    }

    public void setBranchChief(BranchChief branchChief)
    {
        this.branchChief = branchChief;
    }

    public Boolean getAsTrainer()
    {
        return asTrainer;
    }

    public void setAsTrainer(Boolean asTrainer)
    {
        this.asTrainer = asTrainer;
    }
}
