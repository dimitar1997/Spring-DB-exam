package com.example.football.config;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

public interface XmlParser {
    <T> T fromFile(String path, Class<T> tClass) throws JAXBException, FileNotFoundException;
    <T> void writeToFile(String path, T entity) throws JAXBException;
}
