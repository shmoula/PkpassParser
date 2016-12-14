package cz.shmoula.pkpassparser.model;


import java.util.Arrays;
import java.util.Date;

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
}
