package com.example.football.service.impl;

import com.example.football.models.dto.TeamSeedDto;
import com.example.football.models.entity.Team;
import com.example.football.repository.TeamRepository;
import com.example.football.repository.TownRepository;
import com.example.football.service.TeamService;
import com.example.football.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class TeamServiceImpl implements TeamService {
    private final String PATH_OF_TEAMS = "src/main/resources/files/json/teams.json";
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;
    private final TeamRepository teamRepository;
    private final TownRepository townRepository;

    public TeamServiceImpl(ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson, TeamRepository teamRepository, TownRepository townRepository) {
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
        this.teamRepository = teamRepository;
        this.townRepository = townRepository;
    }

    @Override
    public boolean areImported() {
        return this.teamRepository.count() > 0;
    }

    @Override
    public String readTeamsFileContent() throws IOException {
        return Files.readString(Path.of(PATH_OF_TEAMS));
    }

    @Override
    public String importTeams() throws IOException {
        StringBuilder sb = new StringBuilder();
        Arrays.stream(gson.fromJson(readTeamsFileContent(), TeamSeedDto[].class))
                .filter(teamSeedDto -> {
                    boolean isValid = validationUtil.isValid(teamSeedDto);
                    sb.append(isValid ? String.format("Successfully imported Team %s - %d",
                            teamSeedDto.getName(), teamSeedDto.getFanBase()) : "Invalid Team");
                    sb.append(System.lineSeparator());
                    return isValid;
                }).map(teamSeedDto -> {
            Team team = modelMapper.map(teamSeedDto,Team.class);
            team.setTown(townRepository.findByName(teamSeedDto.getTownName()));
            return team;
        }).forEach(teamRepository::save);
        return sb.toString().trim();
    }
}
