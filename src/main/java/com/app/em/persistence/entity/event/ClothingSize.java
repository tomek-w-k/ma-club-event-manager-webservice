package com.app.em.persistence.entity.event;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;


@Entity
@Table(name = "clothing_size")
public class ClothingSize
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Column(name = "clothing_size")
    private String clothingSizeName;


    public ClothingSize() {  }

    public ClothingSize(Integer id, @NotBlank String clothingSizeName)
    {
        this.id = id;
        this.clothingSizeName = clothingSizeName;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getClothingSizeName()
    {
        return clothingSizeName;
    }

    public void setClothingSizeName(String clothingSizeName)
    {
        this.clothingSizeName = clothingSizeName;
    }
}
