package com.app.em.persistence.entity.event;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;


@Entity
@Table(name = "stay_period")
public class StayPeriod
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Column(name = "stay_period_name")
    private String stayPeriodName;


    public StayPeriod() {  }

    public StayPeriod(Integer id, @NotBlank String stayPeriodName)
    {
        this.id = id;
        this.stayPeriodName = stayPeriodName;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getStayPeriodName()
    {
        return stayPeriodName;
    }

    public void setStayPeriodName(String stayPeriodName)
    {
        this.stayPeriodName = stayPeriodName;
    }
}
