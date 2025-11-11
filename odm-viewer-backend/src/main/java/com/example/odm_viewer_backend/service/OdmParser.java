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
import org.xml.sax.*;
import org.xml.sax.helpers.XMLFilterImpl;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;
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

            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(true);
            SAXParser saxParser = factory.newSAXParser();
            XMLReader reader = saxParser.getXMLReader();

            XMLFilterImpl filter = new XMLFilterImpl(reader) {
                @Override
                public void startElement(String uri, String localName, String qName, Attributes atts)
                        throws SAXException {
                    if (!"http://www.cdisc.org/ns/odm/v2.0".equals(uri)) {
                        super.startElement("http://www.cdisc.org/ns/odm/v2.0", localName, qName, atts);
                    } else {
                        super.startElement(uri, localName, qName, atts);
                    }
                }

                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    if (!"http://www.cdisc.org/ns/odm/v2.0".equals(uri)) {
                        super.endElement("http://www.cdisc.org/ns/odm/v2.0", localName, qName);
                    } else {
                        super.endElement(uri, localName, qName);
                    }
                }
            };

            InputSource source = new InputSource(inputStream);
            SAXSource saxSource = new SAXSource(filter, source);

            Object rootObject = unmarshaller.unmarshal(saxSource);

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

        } catch (ParserConfigurationException | SAXException e) {
            throw new JAXBException("Greška prilikom parsiranja XML-a: " + e.getMessage(), e);
        }
    }
}
