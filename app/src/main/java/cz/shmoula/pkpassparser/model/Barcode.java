package cz.shmoula.pkpassparser.model;

public class Barcode {
    private String format;
    private String message;
    private String messageEncoding;

    public String getFormat() {
        return format;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageEncoding() {
        return messageEncoding;
    }
}
