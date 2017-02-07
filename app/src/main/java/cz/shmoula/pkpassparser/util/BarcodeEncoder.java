package cz.shmoula.pkpassparser.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.DimenRes;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.EnumMap;
import java.util.Map;

/**
 * Utility for encoding text to barcode bitmap.
 */
public class BarcodeEncoder {
    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;
    private Context context;

    public BarcodeEncoder(Context context) {
        this.context = context;
    }

    /**
     * Creates bitmap with barcode of specified format from input message.
     */
    public Bitmap getBitmap(String toEncode, String format, @DimenRes int sizeDp) throws WriterException {
        BarcodeFormat zxingFormat = null;

        switch (format) {
            case "PKBarcodeFormatPDF417":
                zxingFormat = BarcodeFormat.PDF_417;
                break;
            case "PKBarcodeFormatQR":
                zxingFormat = BarcodeFormat.QR_CODE;
                break;
            case "PKBarcodeFormatAztec":
                zxingFormat = BarcodeFormat.AZTEC;
                break;
        }

        // Remove barcode margin.
        Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
        hints.put(EncodeHintType.MARGIN, 0);

        Writer writer = new MultiFormatWriter();

        int barcodeSizePx = context.getResources().getDimensionPixelSize(sizeDp);
        BitMatrix result = writer.encode(toEncode, zxingFormat, barcodeSizePx, barcodeSizePx, hints);

        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap barcode = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        barcode.setPixels(pixels, 0, width, 0, 0, width, height);
        return barcode;
    }
}
