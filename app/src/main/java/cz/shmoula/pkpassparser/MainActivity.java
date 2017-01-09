package cz.shmoula.pkpassparser;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.WriterException;

import org.apache.commons.compress.archivers.ArchiveException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import cz.shmoula.pkpassparser.model.Field;
import cz.shmoula.pkpassparser.model.Pass;
import cz.shmoula.pkpassparser.util.BarcodeEncoder;
import cz.shmoula.pkpassparser.util.ImageEncoder;
import cz.shmoula.pkpassparser.util.LanguageAccessor;
import cz.shmoula.pkpassparser.util.PkpassParser;

public class MainActivity extends Activity {
    private static final String FILENAME_PKPASS = "letenka3.pkpass";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            PkpassParser parser = new PkpassParser(getApplicationContext(), FILENAME_PKPASS);

            if (parser.isManifestValid()) {
                Pass pass = parser.readFile("pass.json", Pass.class);

                // Load and show logo.
                Bitmap logoBitmap = ImageEncoder.readBitmap("logo.png", parser);

                ImageView logoImage = (ImageView) findViewById(R.id.logo);
                logoImage.setImageBitmap(logoBitmap);

                // Load and show barcode.
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder(getApplicationContext());
                Bitmap barcodeBitmap = barcodeEncoder.getBitmap(pass.getBarcode().getMessage(), pass.getBarcode().getFormat());

                ImageView imageView = (ImageView) findViewById(R.id.barcode);
                imageView.setImageBitmap(barcodeBitmap);

                // Set background color.
                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.root_view);
                linearLayout.setBackgroundColor(pass.getBackgroundColor());

                // Attempt to load translation for "en" language.
                LanguageAccessor languageAccessor = new LanguageAccessor("en", parser);

                // Render a few fields.
                Field[] auxiliaryFields = pass.getBoardingPass().getAuxiliaryFields();
                for (Field field : auxiliaryFields) {
                    TextView label = generateTextView(pass.getLabelColor(), languageAccessor.getString(field.getLabel()));
                    TextView value = generateTextView(pass.getForegroundColor(), field.getValue());

                    linearLayout.addView(label);
                    linearLayout.addView(value);
                }
            }

        } catch (ArchiveException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generates TextView with specified text color and content.
     */
    private TextView generateTextView(int color, String text) {
        TextView textView = new TextView(getApplicationContext());

        textView.setText(text);
        textView.setTextColor(color);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);

        return textView;
    }
}
