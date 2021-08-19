package com.example.football.models.dto;

import com.example.football.models.Position;

import com.example.football.models.entity.Team;


public class PlayerExportDto {
    private String firstName;
    private String lastName;
    private Position position;
    private Team team;

    public PlayerExportDto() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
