package cz.shmoula.pkpassparser.util;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
 * Helper for parsing pkpass file.
 */
public class PkpassParser {
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();
    private String pkpassDirectory;

    public PkpassParser(Context context, String assetFilename) throws IOException, ArchiveException {

        // Load pkpass from asset directory.
        InputStream pkpass = context.getAssets().open(assetFilename);

        // Unzip files from pkpass archive to temporary directory.
        pkpassDirectory = unzip(pkpass, context.getCacheDir());
    }

    /**
     * Returns file from archive.
     */
    public File getFile(String filename) {
        return new File(pkpassDirectory, filename);
    }

    /**
     * Opens JSON file in archive and returns it as object.
     */
    public <T> T readFile(String filename, Class<T> classOfT) throws FileNotFoundException, UnsupportedEncodingException {
        File file = getFile(filename);
        Reader jsonReader = new InputStreamReader(new FileInputStream(file), "UTF-8");

        return gson.fromJson(jsonReader, classOfT);
    }

    /**
     * Extracts zip from inputStream to temporary directory and returns its path.
     */
    private String unzip(InputStream is, File tempDirRoot) throws IOException, ArchiveException {
        String tempDirName = UUID.randomUUID().toString();
        File tempDir = new File(tempDirRoot, tempDirName);
        tempDir.mkdir();

        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, is);
        ZipArchiveEntry zae;
        while ((zae = (ZipArchiveEntry) ais.getNextEntry()) != null) {
            String fileName = zae.getName();
            File tempSubdir = null;

            // Check if file is in subdirectory. Create dir in that case.
            int separatorIndex = fileName.indexOf(File.separator);
            if (separatorIndex > 0) {
                String path = fileName.substring(0, separatorIndex);

                tempSubdir = new File(tempDir, path);
                tempSubdir.mkdir();

                fileName = fileName.substring(separatorIndex + 1);
            }

            // Copy extracted content to file - either in root, or in subdirectory.
            copyStreamToFile(ais, new File(tempSubdir != null ? tempSubdir : tempDir, fileName));
        }

        return tempDir.getPath();
    }

    /**
     * Saves stream from input to output file.
     * Keeps input open.
     */
    private void copyStreamToFile(InputStream input, File output) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        FileOutputStream fos = new FileOutputStream(output);
        BufferedOutputStream outputStream = new BufferedOutputStream(fos);

        try {
            while ((bytesRead = input.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } finally {
            fos.getFD().sync();
            outputStream.close();
        }
    }
}
