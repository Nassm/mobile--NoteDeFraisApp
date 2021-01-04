package com.example.notedefrais.model.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.notedefrais.R;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Helper {
    public static final String TAG = Helper.class.getSimpleName();

    private static Helper instance;

    /*-------------------------------------- Region CONSTRUCTOR ---------------------------------*/
    private Helper()
    {
    }

    public static Helper get()
    {
        if (instance == null)
        {
            instance = new Helper();
        }
        return instance;
    }

    /* --------------------------------------- Region METHODS ---------------------------------- */

    /** Replace Main fragment with another fragment B*/
    public static void replaceMainFragment(FragmentManager manager, Fragment b, String TAG)
    {
        manager.beginTransaction()
                .replace(R.id.host_fragment, b, TAG)
                .addToBackStack(TAG)
                .commit();
    }

    public static String formatDate(Date date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.FRANCE);
        String dateFormatted = sdf.format(date);
        return dateFormatted;
    }

    public static Date formatStringToDate(String df)
    {
        try
        {
            int separatorCount = 0;
            if(df.contains("/"))
            {
                df = df.replace('/', '-');
            }
            else if(df.contains("."))
            {
                df  = df.replace('.', '-');
            }
            for(int i =0; i<df.length(); ++i)
            {
                if(df.charAt(i) == '-')
                {
                    separatorCount +=1;
                    if(separatorCount == 2)
                    {
                        if(df.length() == 8)
                        {
                            Character main = df.charAt(i+1); Character sec = df.charAt(i+2);
                            df = df.replace(df.charAt(i+1), '2');
                            df = df.replace(df.charAt(i+2), '0');
                            df = df + main;
                            df = df + sec;
                        }
                    }
                }
            }
            return new SimpleDateFormat("dd-MM-yyyy", Locale.FRANCE).parse(df);
        }
        catch (ParseException e)
        {
            NdfLogger.e(TAG, e.getMessage());
        }

        return new Date();
    }

    public static String formatDecimalTwoDigits(Float value)
    {
        DecimalFormat df = new DecimalFormat();
        df.setMinimumFractionDigits(2);
        df.setMaximumFractionDigits(2);
        return df.format(value);
    }

    public static Float formatStringToFloat(String value)
    {
        return Float.valueOf(value.replace(',', '.'));
    }

    public static String formatStringToFloatString(String value)
    {
        String val = value.replace(',', '.');
        return val;
    }

    public static int generateRandomInteger()
    {
        Random random = new Random();
        String numberString = "";
        for(int i = 0; i<4; ++i)
        {
            numberString += String.valueOf(random.nextInt(9));
        }

        return Integer.valueOf(numberString);
    }

    public static void generateDialogOneAction(Context c, String msg, DialogInterface.OnClickListener listener)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setIcon(R.drawable.ic_about);
        builder.setTitle(R.string.LBL_INFORMATION);
        builder.setMessage(msg)
                .setPositiveButton(c.getString(R.string.COMPRIS), listener).show();
    }

    public static void generateDialogTwoAction(Context c, String msg, DialogInterface.OnClickListener listener)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setIcon(R.drawable.ic_about);
        builder.setTitle(R.string.LBL_INFORMATION);
        builder.setMessage(msg)
                .setPositiveButton(c.getString(R.string.OUI), listener)
                .setNegativeButton(c.getString(R.string.NON), listener).show();
    }
}
