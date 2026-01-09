package com.campusmind.app.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {

    // Method untuk konversi Bitmap ke File
    public static File bitmapToFile(Context context, Bitmap bitmap, String fileName) {
        File file = new File(context.getCacheDir(), fileName); // simpan di cache
        try (FileOutputStream out = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return file;
    }

    // Method untuk dapat File dari Uri (galeri)
    public static File getFile(Context context, Uri uri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            return bitmapToFile(context, bitmap, "temp_image.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
