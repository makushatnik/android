package com.softdesign.vkmusic.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by Ageev Evgeny on 31.07.2016.
 */
public class Common {
    public static String getDurationAsString(int time) {
        String result = "00:00";

        if (time > 0) {
            int whole = time / 60;
            int fract = time % 60;
            result = (whole < 10 ? "0" + whole : whole) + ":" + (fract < 10 ? "0" + fract : fract);
        }

        return result;
    }

    public static boolean fileExists(String fileName) {
        if (!isExternalStorageReadable()) return false;
//        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getPath();
//        File file = new File(path + fileName);
//        if (file.exists()) {
//            return true;
//        }
        return false;
    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
