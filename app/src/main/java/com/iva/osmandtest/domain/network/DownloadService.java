package com.iva.osmandtest.domain.network;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by iva on 11.05.17.
 */

public class DownloadService extends IntentService {

    public static final String UPDATE_PROGRESS = "22848";

    private final String MAP_FOLDER = "maps";
    public static final String URL = "url";
    public static final String PROGRESS = "progress";
    public static final String NAME = "name";

    private Intent registrationComplete;

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String urlToDownload = intent.getStringExtra(URL);
        String fileName = intent.getStringExtra(NAME);
        try {
            URL url = new URL("http://download.osmand.net/download.php?standard=yes&file=Albania_europe_2.obf.zip");
            URLConnection connection = url.openConnection();
            connection.connect();
            // this will be useful so that you can show a typical 0-100% progress bar
            int fileLength = connection.getContentLength();

            // download the file
            InputStream input = new BufferedInputStream(connection.getInputStream());
            OutputStream output = new FileOutputStream(getApplicationContext().getFilesDir().toString()
                    + File.separator
            + MAP_FOLDER
            + fileName);

            byte data[] = new byte[1024];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;
//                Bundle resultData = new Bundle();
//                resultData.putInt("progress", (int) (total * 100 / fileLength));
                registrationComplete = new Intent(UPDATE_PROGRESS);
                registrationComplete.putExtra(PROGRESS, (int) (total * 100 / fileLength));
                registrationComplete.putExtra(NAME, fileName);
                LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bundle resultData = new Bundle();
        resultData.putInt(PROGRESS, 100);
        Intent registrationComplete = new Intent(UPDATE_PROGRESS);
        registrationComplete.putExtra(PROGRESS, registrationComplete);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
