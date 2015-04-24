package ru.fias.xml

import groovy.util.logging.Log
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle
import spock.lang.Specification

import javax.xml.namespace.QName

/**
 * Created by Dmitry Teslya on 21.04.2015.
 */
@Log
class XmlReaderTest extends Specification {

    def PATH = "C:\\Users\\Dmitry Teslya\\Downloads\\AS_ADDROBJ_20150419_8043fea9-30b0-4bb7-ae04-0bf933c4cb71.xml";
    InputStream inputStream

    def setup() {
        inputStream = new FileInputStream(PATH)
    }

    def "read first address object"() {
        when:
        XmlReader reader = new XmlReader(inputStream, AddressObjects.Object.class, QName.valueOf("Object"))

        then:
        log.info("has next: " + reader.hasNext())
        log.info("item: " + ToStringBuilder.reflectionToString(reader.next(), ToStringStyle.MULTI_LINE_STYLE))
    }

    def "address object count"() {
        given:
        long count = 0

        when:
        XmlReader reader = new XmlReader(inputStream, AddressObjects.Object.class, QName.valueOf("Object"))
        while (reader.hasNext()) {
            reader.next()
            count++
        }

        then:
        log.info("count: " + count)
    }

    def cleanup() {
        inputStream.close()
    }
}
