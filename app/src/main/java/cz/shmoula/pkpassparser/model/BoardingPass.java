package cz.shmoula.pkpassparser.model;

public class BoardingPass {
    private String transitType;

    private Field[] headerFields;
    private Field[] primaryFields;
    private Field[] secondaryFields;
    private Field[] backFields;
    private Field[] auxiliaryFields;

    public String getTransitType() {
        return transitType;
    }

    public Field[] getHeaderFields() {
        return headerFields;
    }

    public Field[] getPrimaryFields() {
        return primaryFields;
    }

    public Field[] getSecondaryFields() {
        return secondaryFields;
    }

    public Field[] getBackFields() {
        return backFields;
    }

    public Field[] getAuxiliaryFields() {
        return auxiliaryFields;
    }
}
