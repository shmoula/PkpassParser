package cz.shmoula.pkpassparser.model;

public class Field {
    private String key;
    private String label;
    private String dateStyle;
    private String timeStyle;
    private boolean ignoresTimeZone;
    private String value;

    public String getKey() {
        return key;
    }

    public String getLabel() {
        return label;
    }

    public String getDateStyle() {
        return dateStyle;
    }

    public String getTimeStyle() {
        return timeStyle;
    }

    public boolean isIgnoresTimeZone() {
        return ignoresTimeZone;
    }

    public String getValue() {
        return value;
    }
}
