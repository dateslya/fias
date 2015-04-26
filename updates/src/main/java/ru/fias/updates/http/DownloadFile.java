package ru.fias.updates.http;

import org.apache.commons.io.IOUtils;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

/**
 * Created by Dmitry Teslya on 25.04.2015.
 */
public class DownloadFile {

    private static final Logger log = Logger.getLogger(DownloadFile.class.getName());

    public static final int CONNECTION_TIMEOUT = 30 * 1000;
    public static final int READ_TIMEOUT = 10 * 1000;

    public void download(URL source, File target) throws IOException {
        long downloaded = target.exists() ? target.length() : 0;
        HttpURLConnection connection = (HttpURLConnection) source.openConnection();
        log.info("URL: " + source);
        if (connection.getContentLength() == downloaded) {
            connection.disconnect();
            log.info(source + " is downloaded");
            return;
        } else {
            connection.disconnect();
            connection = (HttpURLConnection) source.openConnection();
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setRequestProperty("Range", "bytes=" + downloaded + "-");
            log.info("Content-Type: " + connection.getContentType());
            log.info("Content-Length: " + connection.getContentLength());
            log.info("Range: bytes=" + downloaded + "-");
        }
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = connection.getInputStream();
            outputStream = new FileOutputStream(target, downloaded != 0);
            IOUtils.copyLarge(inputStream, outputStream);
        } finally {
            closeStream(inputStream);
            closeStream(outputStream);
            connection.disconnect();
        }
    }

    private void closeStream(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
