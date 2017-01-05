package cz.shmoula.pkpassparser.model;


import android.graphics.Color;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Pass {
    private int formatVersion;
    private String serialNumber;
    private String teamIdentifier;

    private String passTypeIdentifier;
    private String organizationName;
    private String description;
    private long[] associatedStoreIdentifiers;

    private String backgroundColor;
    private String foregroundColor;
    private String labelColor;

    private Date expirationDate;
    private Date relevantDate;

    private Barcode barcode;
    private Location[] locations;
    private BoardingPass boardingPass;

    public int getFormatVersion() {
        return formatVersion;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getTeamIdentifier() {
        return teamIdentifier;
    }

    public String getPassTypeIdentifier() {
        return passTypeIdentifier;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public String getDescription() {
        return description;
    }

    public long[] getAssociatedStoreIdentifiers() {
        return associatedStoreIdentifiers;
    }

    public int getBackgroundColor() {
        return parseColor(backgroundColor);
    }

    public int getForegroundColor() {
        return parseColor(foregroundColor);
    }

    public int getLabelColor() {
        return parseColor(labelColor);
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public Date getRelevantDate() {
        return relevantDate;
    }

    public Barcode getBarcode() {
        return barcode;
    }

    public Location[] getLocations() {
        return locations;
    }

    public BoardingPass getBoardingPass() {
        return boardingPass;
    }

    /**
     * Parses string with color written like rgb(128,32,212) into a color-int.
     */
    private int parseColor(String input) {
        Pattern c = Pattern.compile("rgb *\\( *([0-9]+), *([0-9]+), *([0-9]+) *\\)");
        Matcher m = c.matcher(input);

        if (m.matches()) {
            Integer r = Integer.valueOf(m.group(1));
            Integer g = Integer.valueOf(m.group(2));
            Integer b = Integer.valueOf(m.group(3));

            System.out.println("Color " + input + " to " + Color.rgb(r, g, b));

            return Color.rgb(r, g, b);
        }

        return 0;
    }
}
