package net.nonswag.tnl.listener.utils;

import net.nonswag.tnl.listener.api.logger.Logger;

import javax.net.ssl.HttpsURLConnection;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class FileDownloader {

    public static void downloadFile(String url, String saveDir) throws IOException {
        HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
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
                if (index > 0) {
                    fileName = disposition.substring(index + 10, disposition.length() - 1);
                }
            } else {
                fileName = url.substring(url.lastIndexOf("/") + 1);
            }
            if (fileName != null) {
                InputStream inputStream = connection.getInputStream();
                String saveFilePath = saveDir + File.separator + fileName;
                FileOutputStream outputStream = new FileOutputStream(saveFilePath);
                int bytesRead;
                byte[] buffer = new byte[4096];
                Logger.info.println("§aStarting download of file §8'§6" + fileName + "§8'");
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.close();
                inputStream.close();
                Logger.info.println("§aSuccessfully downloaded file §8'§6" + fileName + "§8'");
            } else {
                Logger.error.println("§cNo file was found");
            }
        } else {
            Logger.error.println("§cNo file was found§8.§c Server replied HTTP code§8: §4" + responseCode);
        }
        connection.disconnect();
    }
}
