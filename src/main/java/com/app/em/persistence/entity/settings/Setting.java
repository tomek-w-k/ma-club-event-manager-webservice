package com.app.em.persistence.entity.settings;

import javax.persistence.*;


@Entity
@Table(name = "setting")
public class Setting
{
    @Id
    @Column(name = "`key`")
    private String key;

    @Lob
    @Column(name = "`value`")
    private String value;


    public Setting() {  }

    public Setting(String key, String value)
    {
        this.key = key;
        this.value = value;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }
}
