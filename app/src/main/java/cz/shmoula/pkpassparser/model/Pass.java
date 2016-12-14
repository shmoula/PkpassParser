package cz.shmoula.pkpassparser.model;


import java.util.Date;

public class Pass {
    private int formatVersion;
    private String serialNumber;
    private String teamIdentifier;

    private String passTypeIdentifier;
    private String organizationName;
    private String description;

    private String backgroundColor;
    private String foregroundColor;
    private String labelColor;

    private Date expirationDate;
    private Date relevantDate;

    @Override
    public String toString() {
        return "Pass{" +
                "formatVersion=" + formatVersion +
                ", serialNumber='" + serialNumber + '\'' +
                ", teamIdentifier='" + teamIdentifier + '\'' +
                ", passTypeIdentifier='" + passTypeIdentifier + '\'' +
                ", organizationName='" + organizationName + '\'' +
                ", description='" + description + '\'' +
                ", backgroundColor='" + backgroundColor + '\'' +
                ", foregroundColor='" + foregroundColor + '\'' +
                ", labelColor='" + labelColor + '\'' +
                ", expirationDate=" + expirationDate +
                ", relevantDate=" + relevantDate +
                '}';
    }
}
