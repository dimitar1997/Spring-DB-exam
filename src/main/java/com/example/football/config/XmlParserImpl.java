package com.example.football.config;

import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
@Component
public class XmlParserImpl implements XmlParser {
    private JAXBContext jaxbContext;

    @Override
    @SuppressWarnings("unchecked")
    public <T> T fromFile(String path, Class<T> tClass) throws JAXBException, FileNotFoundException {
        jaxbContext = JAXBContext.newInstance(tClass);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (T) unmarshaller.unmarshal(new FileReader(path));
    }

    @Override
    public <T> void writeToFile(String path, T entity) throws JAXBException {
        jaxbContext = JAXBContext.newInstance(entity.getClass());
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(entity, new File(path));
    }
}
