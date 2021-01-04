package com.example.notedefrais.model.utils;

import android.os.Environment;
import android.util.Log;

import com.example.notedefrais.App;
import com.example.notedefrais.BuildConfig;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NdfLogger {

    public static String fileLog ="Log.txt";
    public static String directory = "NoteDeFrais Logger";


    /**
     * Sends an error message to LogCat and to a log file.
     * @param TAG A tag identifying a group of log messages
     * @param msg The message to add to the log.
     */
    public static void e(String TAG, String msg)
    {
        if (!Log.isLoggable(TAG, Log.ERROR))
            return;

        int logResult = Log.e(TAG, msg);
        if (logResult > 0)
            logToFile(TAG, msg);
    }


    /**
     * Sends an error message and the exception to LogCat and to a log file.
     * @param TAG A tag identifying a group of log messages
     * @param msg The message to add to the log.
     */
    public static void e(String TAG, String msg, Throwable throwableException)
    {
        if (!Log.isLoggable(TAG, Log.ERROR))
            return;

        int logResult = Log.e(TAG, msg, throwableException);
        if (logResult > 0)
            logToFile(TAG, msg + "\r\n" + Log.getStackTraceString(throwableException));
    }


    /**
     * Sends a message to LogCat and to a log file.
     * @param msgTag A tag identifying a group of log messages.
     * @param msg The message to add to the log.
     */
    public static void v( String msgTag, String msg)
    {
        // If the build is not debug, do not try to log, the logcat be stripped at compilation.
        if (!App.DEBUG || !Log.isLoggable(msgTag, Log.VERBOSE))
            return;

        int logResult = Log.v(msgTag, msg);
        if (logResult > 0)
            logToFile(msgTag, msg);
    }


    /**
     * Sends a message to LogCat and to a log file.
     * @param msgTag A tag identifying a group of log messages.
     * @param msg The message to add to the log.
     */
    public static void i( String msgTag, String msg)
    {
        // If the build is not debug, do not try to log, the logcat be stripped at compilation.
        if (!Log.isLoggable(msgTag, Log.INFO))
            return;

        int logResult = Log.i(msgTag, msg);
        if (logResult > 0)
            logToFile(msgTag, msg);
    }


    /**
     * Sends a message to LogCat and to a log file.
     * @param msgTag A tag identifying a group of log messages.
     * @param msg The message to add to the log.
     */
    public static void d( String msgTag, String msg)
    {
        // If the build is not debug, do not try to log, the logcat be stripped at compilation.
        if (!App.DEBUG  || !Log.isLoggable(msgTag, Log.DEBUG))
            return;

        int logResult = Log.d(msgTag, msg);
        if (logResult > 0)
            logToFile(msgTag, msg);
    }


    /**
     * Sends a message and the exception to LogCat and to a log file.
     * @param msgTag A tag identifying a group of log messages.
     * @param msg The message to add to the log.
     * @param throwableException An exception to log
     */
    public static void v(String msgTag, String msg, Throwable throwableException)
    {
        // If the build is not debug, do not try to log, the logcat be
        // stripped at compilation.
        if (!BuildConfig.DEBUG || !Log.isLoggable(msgTag, Log.VERBOSE))
            return;

        int logResult = Log.v(msgTag, msg, throwableException);
        if (logResult > 0)
            logToFile(msgTag,  msg + "\r\n" + Log.getStackTraceString(throwableException));
    }


    /**
     * Gets a stamp containing the current date and time to write to the log.
     * @return The stamp for the current date and time.
     */
    private static String getDateTimeStamp()
    {
        Date dateNow = Calendar.getInstance().getTime();
        // Our locale, so all the log files have the same date and time format
        return (DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.DEFAULT, Locale.FRENCH).format(dateNow));
    }


    /**
     * Writes a message to the log file on the device.
     * @param msgTag A tag identifying a group of log messages.
     * @param msg The message to add to the log.
     */
    private static void logToFile(String msgTag, String msg)
    {
        try
        {
            File A = new File(Environment.getExternalStorageDirectory(), directory);
            File B = new File(A, fileLog);

            /** if maybe A and not B, new A*/
            if (!B.getParentFile().exists())
            {
                B.getParentFile().mkdirs();
            }

            /** if A and not B, new B*/
            if (!B.exists())
                B.createNewFile();

            // Write the message to the log with a timestamp
            BufferedWriter writer = new BufferedWriter(new FileWriter(B, true));
            writer.write(String.format("%1s [%2s]:%3s\r\n", getDateTimeStamp(), msgTag, msg));
            writer.close();
        }
        catch (IOException e)
        {
            Log.e("Ndf_project", "Unable to log exception to B.", e);
        }

    }

}
