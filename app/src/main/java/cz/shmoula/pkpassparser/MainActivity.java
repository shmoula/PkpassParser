package cz.shmoula.pkpassparser;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.zxing.WriterException;

import org.apache.commons.compress.archivers.ArchiveException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import cz.shmoula.pkpassparser.model.Pass;
import cz.shmoula.pkpassparser.util.BarcodeEncoder;
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

                BarcodeEncoder barcodeEncoder = new BarcodeEncoder(getApplicationContext());
                Bitmap bitmap = barcodeEncoder.getBitmap(pass.getBarcode().getMessage(), pass.getBarcode().getFormat());

                ImageView imageView = (ImageView) findViewById(R.id.barcode);
                imageView.setImageBitmap(bitmap);

                RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.activity_main);
                relativeLayout.setBackgroundColor(pass.getBackgroundColor());
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
