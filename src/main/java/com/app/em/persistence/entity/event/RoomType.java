package com.app.em.persistence.entity.event;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;


@Entity
@Table(name = "room_type")
public class RoomType
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Column(name = "room_type_name")
    private String roomTypeName;

    @Column(name = "room_type_picture_path")
    private String roomTypePicturePath;


    public RoomType() {  }

    public RoomType(Integer id, @NotBlank String roomTypeName, String roomTypePicturePath)
    {
        this.id = id;
        this.roomTypeName = roomTypeName;
        this.roomTypePicturePath = roomTypePicturePath;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getRoomTypeName()
    {
        return roomTypeName;
    }

    public void setRoomTypeName(String roomTypeName)
    {
        this.roomTypeName = roomTypeName;
    }

    public String getRoomTypePicturePath()
    {
        return roomTypePicturePath;
    }

    public void setRoomTypePicturePath(String roomTypePicturePath)
    {
        this.roomTypePicturePath = roomTypePicturePath;
    }
}
