package com.example.odm_viewer_backend.service;

import com.example.odm.generated.ODM;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.IOException;

@Service
public class OdmParser {

    private final JAXBContext jaxbContext;

    public OdmParser() throws JAXBException {
        this.jaxbContext = JAXBContext.newInstance(ODM.class);
    }

    public ODM parseOdmFile(MultipartFile file) throws JAXBException, IOException {
        try (InputStream inputStream = file.getInputStream()) {

            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            Object rootObject = unmarshaller.unmarshal(inputStream);

            if (rootObject instanceof ODM) {
                return (ODM) rootObject;
            } else {
                throw new JAXBException("Korenski element fajla nije validan ODM objekat.");
            }
        }
    }

}
