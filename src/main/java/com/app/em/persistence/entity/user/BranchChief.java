package com.app.em.persistence.entity.user;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "branch_chief")
public class BranchChief
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "branch_chief_name", unique = true)
    private String branchChiefName;

    @JsonBackReference(value = "user-branch-chief")
    @OneToMany(mappedBy = "branchChief", cascade = CascadeType.ALL)
    private List<User> users;


    public BranchChief() {  }

    public BranchChief(int id, String branchChiefName, List<User> users)
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

    public List<User> getUsers()
    {
        return users;
    }

    public void setUsers(List<User> users)
    {
        this.users = users;
    }
}
