package ru.fias.updates.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Dmitry Teslya on 25.04.2015.
 */
public class DownloadFile {

    private static final Logger log = LoggerFactory.getLogger(DownloadFile.class.getName());

    private static final int BUFFER_SIZE = 1024 * 4;
    private static final int END_OF_STREAM = -1;

    public static final int CONNECTION_TIMEOUT = 30 * 1000;
    public static final int READ_TIMEOUT = 10 * 1000;

    public void download(URL source, File target) throws IOException {
        long downloaded = target.exists() ? target.length() : 0;
        HttpURLConnection connection = (HttpURLConnection) source.openConnection();
        log.info("URL: {}", source);
        if (connection.getContentLength() == downloaded) {
            connection.disconnect();
            log.info("{} is downloaded", source);
            return;
        } else {
            connection.disconnect();
            connection = (HttpURLConnection) source.openConnection();
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setRequestProperty("Range", "bytes=" + downloaded + "-");
            log.info("Content-Type: {}", connection.getContentType());
            log.info("Content-Length: {}", connection.getContentLength());
            log.info("Range: bytes={}-", downloaded);
        }
        InputStream input = null;
        OutputStream output = null;
        try {
            input = connection.getInputStream();
            output = new FileOutputStream(target, downloaded != 0);
            int length = 0;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((length = input.read(buffer)) != END_OF_STREAM) {
                output.write(buffer, 0, length);
            }
        } finally {
            closeStream(input);
            closeStream(output);
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
