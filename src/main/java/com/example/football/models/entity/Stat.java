package com.example.football.models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "stats")
public class Stat extends BaseEntity {
    private BigDecimal shooting;
    private BigDecimal passing;
    private BigDecimal endurance;

    public Stat() {
    }

    @Column
    public BigDecimal getShooting() {
        return shooting;
    }

    public void setShooting(BigDecimal shooting) {
        this.shooting = shooting;
    }

    @Column
    public BigDecimal getPassing() {
        return passing;
    }

    public void setPassing(BigDecimal passing) {
        this.passing = passing;
    }

    @Column
    public BigDecimal getEndurance() {
        return endurance;
    }

    public void setEndurance(BigDecimal endurance) {
        this.endurance = endurance;
    }
}
