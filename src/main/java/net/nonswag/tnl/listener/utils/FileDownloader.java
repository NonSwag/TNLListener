package net.nonswag.tnl.listener.utils;

import net.nonswag.tnl.listener.api.logger.Logger;

import javax.annotation.Nonnull;
import javax.net.ssl.HttpsURLConnection;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class FileDownloader {

    public static void downloadFile(@Nonnull URL url, @Nonnull File directory) throws IOException {
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            String fileName = null;
            String disposition = connection.getHeaderField("Content-Disposition");
            String contentType = connection.getContentType();
            int contentLength = connection.getContentLength();
            if (disposition != null) {
                int index = disposition.indexOf("filename=");
                if (index > 0) fileName = disposition.substring(index + 10, disposition.length() - 1);
            } else fileName = url.toString().substring(url.toString().lastIndexOf("/") + 1);
            if (fileName != null) {
                InputStream inputStream = connection.getInputStream();
                FileOutputStream outputStream = new FileOutputStream(new File(directory.getAbsolutePath(), fileName));
                int bytesRead;
                byte[] buffer = new byte[4096];
                Logger.debug.println("Starting download of file <'" + fileName + "'>");
                while ((bytesRead = inputStream.read(buffer)) != -1) outputStream.write(buffer, 0, bytesRead);
                outputStream.close();
                inputStream.close();
                Logger.debug.println("Successfully downloaded file <'" + fileName + "'>");
            } else Logger.debug.println("No file was found");
        } else {
            Logger.error.println("No file was found. Server replied HTTP code: " + responseCode);
        }
        connection.disconnect();
    }

    public static void downloadFile(@Nonnull String url, @Nonnull String directory) throws IOException {
        downloadFile(new URL(url), new File(directory));
    }
}
