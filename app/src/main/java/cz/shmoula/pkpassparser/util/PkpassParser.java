package cz.shmoula.pkpassparser.util;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Helper for parsing pkpass file.
 */
public class PkpassParser {
    private static final String FILENAME_MANIFEST = "manifest.json";
    private static final String CHARSET_NAME = "UTF-8";

    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();
    private String pkpassDirectory;

    /**
     * Opens pkpass file from assets directory.
     */
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
        Reader jsonReader = new InputStreamReader(new FileInputStream(file), CHARSET_NAME);

        return gson.fromJson(jsonReader, classOfT);
    }

    /**
     * Returns list of available languages.
     */
    public List<String> getLanguages() {
        File directory = new File(pkpassDirectory);
        List<String> languages = new ArrayList<>();

        String[] langDirs = directory.list(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return s.toLowerCase().endsWith(".lproj");
            }
        });

        // Omit everything after "."
        for (String languageDirectory : langDirs)
            languages.add(languageDirectory.substring(0, languageDirectory.indexOf(".")));

        return languages;
    }

    /**
     * Checks hashes in manifest file against archived files hashes.
     */
    public boolean isManifestValid() throws IOException, NoSuchAlgorithmException {
        File manifestFile = getFile(FILENAME_MANIFEST);
        Reader jsonReader = new InputStreamReader(new FileInputStream(manifestFile), CHARSET_NAME);

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(jsonReader);

        JsonObject obj = element.getAsJsonObject();

        for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
            String key = entry.getKey();
            File file = new File(pkpassDirectory, key);
            String hashFromfile = computeShaFromFile(file);
            String hashFromManifest = entry.getValue().getAsString();

            if (!hashFromfile.equals(hashFromManifest))
                return false;
        }

        return true;
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

    /**
     * Computes SHA for input file.
     */
    private String computeShaFromFile(File file) throws IOException, NoSuchAlgorithmException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
        FileInputStream fis = new FileInputStream(file);
        StringBuffer sb = new StringBuffer("");

        try {
            while ((bytesRead = fis.read(buffer)) != -1) {
                messageDigest.update(buffer, 0, bytesRead);
            }

            // Convert byte to hex.
            for (byte bb : messageDigest.digest())
                sb.append(Integer.toString((bb & 0xff) + 0x100, 16).substring(1));
        } finally {
            fis.close();
        }
        return sb.toString();
    }
}
