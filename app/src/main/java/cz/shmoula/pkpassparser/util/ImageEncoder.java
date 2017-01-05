package cz.shmoula.pkpassparser.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Utility to read file to bitmap.
 */
public class ImageEncoder {

    /**
     * Reads image file from archive and returns it as a bitmap.
     * If no file at specified path, returns null.
     */
    public static Bitmap readBitmap(String filename, PkpassParser parser) {
        File file = parser.getFile(filename);

        if (file != null && file.isFile()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            try {
                return BitmapFactory.decodeStream(new FileInputStream(file), null, options);
            } catch (FileNotFoundException e) {
                return null;
            }
        }

        return null;
    }
}
