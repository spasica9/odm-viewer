package com.example.odm_viewer_backend.service;

import com.example.odm.generated.FileType;
import com.example.odm.generated.MetaDataVersion;
import com.example.odm.generated.ODM;
import com.example.odm.generated.Study;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
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
        this.jaxbContext = JAXBContext.newInstance(ODM.class, MetaDataVersion.class, Study.class);
    }

    public ODM parseOdmFile(MultipartFile file) throws JAXBException, IOException {
        try (InputStream inputStream = file.getInputStream()) {

            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            Object rootObject = unmarshaller.unmarshal(inputStream);

            if (rootObject instanceof JAXBElement) {
                rootObject = ((JAXBElement<?>) rootObject).getValue();
            }

            if (rootObject instanceof ODM) {
                return (ODM) rootObject;
            }

            if (rootObject instanceof MetaDataVersion) {
                MetaDataVersion mdv = (MetaDataVersion) rootObject;

                ODM wrapper = new ODM();
                wrapper.setFileOID("Partial-Upload");
                wrapper.setODMVersion(mdv.getName() != null ? mdv.getName() : "Partial");
                wrapper.setFileType(FileType.SNAPSHOT);

                Study s = new Study();
                s.setOID("PartialStudy");
                s.setStudyName("Partial Metadata Upload");
                s.getMetaDataVersions().add(mdv);
                wrapper.getStudies().add(s);
                return wrapper;
            }

            throw new JAXBException("Korenski element nije validan ODM niti MetaDataVersion (rootObject type: "
                    + (rootObject != null ? rootObject.getClass().getName() : "null") + ").");

        }
    }
}
