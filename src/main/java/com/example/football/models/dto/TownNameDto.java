package com.example.football.models.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "town")
@XmlAccessorType(XmlAccessType.FIELD)
public class TownNameDto {
    @XmlElement
    private String name;

    public TownNameDto() {
    }
    @NotNull
    @Size(min = 2)
    public String getName() {
        return name;
    }
    @NotNull
    public void setName(String name) {
        this.name = name;
    }
}
