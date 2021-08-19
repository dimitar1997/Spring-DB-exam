package com.example.football.service.impl;

import com.example.football.config.XmlParser;
import com.example.football.models.dto.StatRootSeedDto;
import com.example.football.models.entity.Stat;
import com.example.football.repository.StatRepository;
import com.example.football.service.StatService;
import com.example.football.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class StatServiceImpl implements StatService {
    private final String PATH_OF_STATS = "src/main/resources/files/xml/stats.xml";
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;
    private final StatRepository statRepository;

    public StatServiceImpl(ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser, StatRepository statRepository) {
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
        this.statRepository = statRepository;
    }

    @Override
    public boolean areImported() {
        return this.statRepository.count() > 0;
    }

    @Override
    public String readStatsFileContent() throws IOException {
        return Files.readString(Path.of(PATH_OF_STATS));
    }

    @Override
    public String importStats() throws JAXBException, FileNotFoundException {
        StringBuilder sb = new StringBuilder();
        xmlParser.fromFile(PATH_OF_STATS, StatRootSeedDto.class)
                .getStatSeedDtos().stream()
                .filter(statSeedDto -> {
                    boolean isValid = validationUtil.isValid(statSeedDto);
                    sb.append(isValid ? String.format("Successfully imported Stat %.2f - %.2f  - %.2f",
                            statSeedDto.getPassing(), statSeedDto.getShooting(), statSeedDto.getEndurance())
                            : "Invalid Stat");
                    sb.append(System.lineSeparator());
                    return isValid;
                }).map(statSeedDto -> modelMapper.map(statSeedDto, Stat.class))
                .forEach(statRepository::save);
        return sb.toString().trim();
    }
}
