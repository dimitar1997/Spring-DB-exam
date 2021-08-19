package com.example.football.models.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
@XmlRootElement(name = "stats")
@XmlAccessorType(XmlAccessType.FIELD)
public class StatRootSeedDto {
    @XmlElement(name = "stat")
    private List<StatSeedDto> statSeedDtos;

    public StatRootSeedDto() {
    }

    public List<StatSeedDto> getStatSeedDtos() {
        return statSeedDtos;
    }

    public void setStatSeedDtos(List<StatSeedDto> statSeedDtos) {
        this.statSeedDtos = statSeedDtos;
    }
}
