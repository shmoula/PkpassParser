package cz.shmoula.pkpassparser;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.WriterException;

import org.apache.commons.compress.archivers.ArchiveException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import cz.shmoula.pkpassparser.model.Pass;
import cz.shmoula.pkpassparser.util.BarcodeEncoder;
import cz.shmoula.pkpassparser.util.ImageEncoder;
import cz.shmoula.pkpassparser.util.PkpassParser;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            PkpassParser parser = new PkpassParser(getApplicationContext(), "letenka.pkpass");

            if(parser.isManifestValid()) {
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

                // Show label example.
                TextView label = (TextView) findViewById(R.id.label);
                label.setTextColor(pass.getLabelColor());

                // Show field example.
                TextView field = (TextView) findViewById(R.id.field);
                field.setTextColor(pass.getForegroundColor());
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
}
