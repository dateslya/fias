package ru.fias.updates.http

import groovy.util.logging.Log
import spock.lang.Specification

/**
 * Created by Dmitry Teslya on 25.04.2015.
 */
@Log
class DownloadFileTest extends Specification {

    def "download file by url"() {
        given:
        def url = new URL("http://fias.nalog.ru/Public/Downloads/20150423/BASE.7Z")
//        def url = new URL("http://fias.nalog.ru/Public/Downloads/20150423/fias_xml.rar")
//        def url = new URL("http://fias.nalog.ru/Public/Downloads/20150423/fias_delta_xml.rar")
        def path = "c:/tmp/file.rar"

        when:
        new DownloadFile().download(url, new File(path))

        then:
        log.info("success")
    }
}