package com.example.odm_viewer_backend.service;

import com.example.odm.generated.ODM;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.*;
import org.xml.sax.helpers.XMLFilterImpl;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;
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

            if (rootObject instanceof ODM) {
                return (ODM) rootObject;
            } else {
                throw new JAXBException("Korenski element fajla nije validan ODM objekat.");
            }
        } catch (SAXException | javax.xml.parsers.ParserConfigurationException e) {
            throw new JAXBException("Greška prilikom parsiranja XML-a: " + e.getMessage(), e);
        }
    }
}
