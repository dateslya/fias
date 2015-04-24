package ru.fias.ws

import groovy.util.logging.Log
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle
import spock.lang.Specification

import javax.xml.namespace.QName
import javax.xml.ws.BindingProvider

/**
 * Created by Dmitry Teslya on 24.04.2015.
 */
@Log
class DownloadServiceTest extends Specification {

    def static final ENDPOINT_ADDRESS = "http://fias.nalog.ru/WebServices/Public/DownloadService.asmx"

    def "get last download file info"() {
        given:
        URL url = new URL(this.getClass().getResource("."), "../../../../../src/main/resources/DownloadService.wsdl")
        QName qName = new QName("http://fias.nalog.ru/WebServices/Public/DownloadService.asmx", "DownloadService")
        DownloadService service = new DownloadService(url, qName)
        DownloadServiceSoap port = service.getDownloadServiceSoap()
        // override endpoint address
        Map<String, Object> requestContext = ((BindingProvider) port).getRequestContext();
        requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, ENDPOINT_ADDRESS);

        when:
        DownloadFileInfo result = port.getLastDownloadFileInfo()

        then:
        log.info(ToStringBuilder.reflectionToString(result, ToStringStyle.MULTI_LINE_STYLE))
    }
}