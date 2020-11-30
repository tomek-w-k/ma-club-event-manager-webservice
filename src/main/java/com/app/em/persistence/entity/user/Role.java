package com.app.em.persistence.entity.user;

import javax.persistence.*;


@Entity
@Table(name = "role")
public class Role
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", length = 20)
    private RoleEnum roleName;


    public Role() {  }

    public Role(RoleEnum roleName)
    {
        this.roleName = roleName;
    }

    public Role(int id, RoleEnum roleName)
    {
        this.id = id;
        this.roleName = roleName;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public RoleEnum getRoleName()
    {
        return roleName;
    }

    public void setRoleName(RoleEnum roleName)
    {
        this.roleName = roleName;
    }
}
