package cz.shmoula.pkpassparser;

import android.app.Activity;
import android.os.Bundle;

import org.apache.commons.compress.archivers.ArchiveException;

import java.io.IOException;

import cz.shmoula.pkpassparser.model.Pass;
import cz.shmoula.pkpassparser.util.PkpassParser;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            PkpassParser parser = new PkpassParser(getApplicationContext(), "letenka.pkpass");
            Pass pass = parser.readFile("pass.json", Pass.class);

        } catch (ArchiveException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
