package com.app.em.persistence.entity.event;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;


@Entity
@Table(name = "weight_age_category")
public class WeightAgeCategory
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Column(name = "category_name")
    private String categoryName;


    public WeightAgeCategory() {  }

    public WeightAgeCategory(Integer id, @NotBlank String categoryName)
    {
        this.id = id;
        this.categoryName = categoryName;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getCategoryName()
    {
        return categoryName;
    }

    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
    }
}
