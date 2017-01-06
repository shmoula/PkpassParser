package cz.shmoula.pkpassparser.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility to load and access strings in language file.
 */
public class LanguageAccessor {
    private Pattern regex = Pattern.compile("\\\"(.*?)\\\"");
    private Map<String, String> stringMap = new HashMap<>();

    public LanguageAccessor(String language, PkpassParser parser) {
        File file = parser.getFile(language + ".lproj/pass.strings");

        // Wizzair passes have no extra languages.
        // Also if there is no language, we'll be returning just keys.
        if (!file.exists())
            return;

        Properties prop = new Properties();
        try {
            InputStream input = new FileInputStream(file);
            prop.load(input);

            for (Object key : prop.keySet()) {
                String stringKey = extractValue(key.toString());
                String stringValue = extractValue(prop.getProperty(key.toString()));

                stringMap.put(stringKey, stringValue);
            }

            input.close();
        } catch (IOException e) {
            // Nothing to report, file probably does not exist.
            // TODO maybe add some fallback to .en
        }
    }

    /**
     * Returns translated value for requested resource string.
     * If there is no language loaded, returns key itself.
     */
    public String getString(String resourceId) {
        if (stringMap.isEmpty())
            return resourceId;

        return stringMap.get(resourceId);
    }

    /**
     * Extract value inside of "", omits all possible characters outside.
     * If nothing found, returns empty string.
     */
    private String extractValue(String value) {
        Matcher regexMatcher = regex.matcher(value);

        return (regexMatcher.find()) ? regexMatcher.group(1) : "";
    }
}
