package com.app.em.persistence.entity.user;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "branch_chief")
public class BranchChief
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    @Column(name = "branch_chief_name", unique = true)
    private String branchChiefName;

    @JsonBackReference(value = "user-branch-chief")
    @OneToMany(mappedBy = "branchChief", cascade = CascadeType.ALL)
    private Set<User> users = new HashSet<>();


    public BranchChief() {  }

    public BranchChief(int id, @NotBlank String branchChiefName, Set<User> users)
    {
        this.id = id;
        this.branchChiefName = branchChiefName;
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

    public String getBranchChiefName()
    {
        return branchChiefName;
    }

    public void setBranchChiefName(String branchChiefName)
    {
        this.branchChiefName = branchChiefName;
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
