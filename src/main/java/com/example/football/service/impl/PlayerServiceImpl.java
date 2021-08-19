package com.example.football.service.impl;

import com.example.football.config.XmlParser;
import com.example.football.models.Position;
import com.example.football.models.dto.PlayerExportDto;
import com.example.football.models.dto.PlayerRootSeedDto;
import com.example.football.models.entity.Player;
import com.example.football.repository.PlayerRepository;
import com.example.football.repository.StatRepository;
import com.example.football.repository.TeamRepository;
import com.example.football.repository.TownRepository;
import com.example.football.service.PlayerService;
import com.example.football.util.ValidationUtil;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;


@Service
public class PlayerServiceImpl implements PlayerService {
    private final String PATH_OF_PLAYERS = "src/main/resources/files/xml/players.xml";
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final TeamRepository teamRepository;
    private final TownRepository townRepository;
    private final StatRepository statRepository;
    private final PlayerRepository playerRepository;
    private final XmlParser xmlParser;

    public PlayerServiceImpl(ModelMapper modelMapper, ValidationUtil validationUtil, TeamRepository teamRepository, TownRepository townRepository, StatRepository statRepository, PlayerRepository playerRepository, XmlParser xmlParser) {
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.teamRepository = teamRepository;
        this.townRepository = townRepository;
        this.statRepository = statRepository;
        this.playerRepository = playerRepository;
        this.xmlParser = xmlParser;
    }


    @Override
    public boolean areImported() {
        return playerRepository.count() > 0;
    }

    @Override
    public String readPlayersFileContent() throws IOException {
        return Files.readString(Path.of(PATH_OF_PLAYERS));
    }

    @Override
    public String importPlayers() throws JAXBException, FileNotFoundException {
        StringBuilder sb = new StringBuilder();
        xmlParser.fromFile(PATH_OF_PLAYERS, PlayerRootSeedDto.class)
                .getPlayerSeedDtos().stream()
                .filter(playerSeedDto -> {
                    boolean isValid = validationUtil.isValid(playerSeedDto);
                    sb.append(isValid ? String.format("Successfully imported Player %s %s - %s",
                            playerSeedDto.getFirstName(),playerSeedDto.getLastName(),
                            playerSeedDto.getPosition()): "Invalid Player");
                    sb.append(System.lineSeparator());
                    return isValid;
                }).map(playerSeedDto -> {
            Player player = modelMapper.map(playerSeedDto,Player.class);
            player.setPosition(Position.valueOf(playerSeedDto.getPosition()));
            player.setTown(townRepository.findByName(playerSeedDto.getTown().getName()));
            player.setTeam(teamRepository.findByName(playerSeedDto.getTeam().getName()));
            player.setStat(statRepository.findById(playerSeedDto.getStat().getId()));

            return player;
        }).forEach(playerRepository::save);

        return sb.toString().trim();
    }

    @Override
    public String exportBestPlayers() {
        StringBuilder sb = new StringBuilder();
        LocalDate date1 = LocalDate.of(1995,01,01);
       LocalDate date2 = LocalDate.of(2003,01,01);
       List<Player> players = playerRepository.findAllByBirthDateBetweenOrderByStatStatDescLastNameAsc(date1,date2);
        for (Player player : players) {
            PlayerExportDto playerExportDto = modelMapper.map(player,PlayerExportDto.class);
            sb.append(String.format("Player - %s %s\n" +
                    "\tPosition - %s\n" +
                    "Team - %s\n" +
                    "\tStadium - %s\n",
                    playerExportDto.getFirstName(),
                    playerExportDto.getLastName(),
                    playerExportDto.getPosition().toString(),
                    playerExportDto.getTeam().getName(),
                    playerExportDto.getTeam().getStadiumName()));
        }


        return sb.toString().trim();
    }
}
