package com.app.em.persistence.entity.event;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;


@Entity
@Table(name = "fee")
public class Fee
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Column(name = "title")
    private String title;

    @NotBlank
    @Column(name = "amount")
    private Float amount;


    public Fee() {  }

    public Fee(Integer id, @NotBlank String title, @NotBlank Float amount)
    {
        this.id = id;
        this.title = title;
        this.amount = amount;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public Float getAmount()
    {
        return amount;
    }

    public void setAmount(Float amount)
    {
        this.amount = amount;
    }
}
