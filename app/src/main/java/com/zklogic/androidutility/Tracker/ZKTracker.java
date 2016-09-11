package com.zklogic.androidutility.Tracker;

import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ArfanMirza on 12/6/2015.
 */
public class ZKTracker {
    private SimpleDateFormat dateFormat;
    private boolean isWriteLog = false;
    private static Date date;

    private String nameOfFile = "ZKTracker";

    private static ZKTracker instanace;

    private ZKTracker()
    {
        nameOfFile = "ZKTracker";
        dateFormat = new SimpleDateFormat("yyyy/MM/dd_HH/mm/ss");
    }

    public static ZKTracker getInstanace() {
        if (instanace == null) {
            instanace = new ZKTracker();
        }
        return instanace;
    }

    public void setTrackerFileName(String nameOfFile) {
        this.nameOfFile = nameOfFile;
    }

    public void trackExLog(Object object) {
        // writeLog(object.toString());
        trackExLog(object.toString());
    }

    public void trackExLog(String log) {
        writeLog(log);
    }

    public void trackExLog(Throwable exception) {

        try {
            exception.printStackTrace();
            StringWriter writer = new StringWriter();
            exception.printStackTrace(new PrintWriter(writer));
            writeLog(writer.toString());
        } catch (Exception ee) {
            ee.printStackTrace();
        }

    }

    public String getLogFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "/"+nameOfFile);
        if (!file.exists()) {
            file.mkdirs();
        }
        ///Logs
        file = new File(Environment.getExternalStorageDirectory().getPath(), "/"+nameOfFile+"/Logs");
        if (!file.exists()) {
            file.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
        String uriSting = (file.getAbsolutePath() + "/"+nameOfFile+"Logs" + timeStamp + ".zklogic");
        return uriSting;
    }

    public void writeLog(String log) {
        if (isWriteLog) {
            try {
                File logFile = new File(getLogFilename());
                if (!logFile.exists()) {
                    try {
                        logFile.createNewFile();
                    } catch (IOException e) {
                        // e.printStackTrace();
                    }
                }
                try {
                    // BufferedWriter for performance, true to set append to
                    // file flag
                    BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
                    buf.newLine();
                    date = new Date();
                    buf.append(dateFormat.format(date) + " ==> " + log);
                    buf.newLine();
                    buf.close();
                } catch (IOException e) {
                    // e.printStackTrace();
                }
            } catch (Exception e) {
                // e.printStackTrace();
            }
        }
    }

    public boolean isWriteLog() {
        return isWriteLog;
    }

    public void setWriteLog(boolean isWriteLog) {
        this.isWriteLog = isWriteLog;
    }
}
