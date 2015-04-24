package ru.fias.xml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.Iterator;

/**
 * Created by Dmitry Teslya on 21.04.2015.
 */
public class XmlReader<T> implements Iterator<T> {

    private final Class<T> clazz;
    private final QName qName;
    private final Unmarshaller unmarshaller;
    private final XMLEventReader reader;

    public XmlReader(InputStream inputStream, Class<T> clazz, QName qName) throws XMLStreamException, JAXBException {
        this.clazz = clazz;
        this.qName = qName;
        JAXBContext context = JAXBContext.newInstance(clazz);
        this.unmarshaller = context.createUnmarshaller();
        XMLInputFactory factory = XMLInputFactory.newInstance();
        reader = factory.createXMLEventReader(inputStream);
    }

    public boolean hasNext() {
        try {
            XMLEvent event;
            while ((event = reader.peek()) != null) {
                if (event.isStartElement() && qName.equals(((StartElement) event).getName())) {
                    return true;
                } else {
                    reader.next();
                }
            }
            return false;
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    public T next() {
        try {
            return (T) unmarshaller.unmarshal(reader, clazz).getValue();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}
