package BMS.boutHouse.form.field.type;

import java.io.Serializable;
import java.util.Objects;

public class BoatType implements Serializable {
    String boatClassification;
    String widthType;
    String rowingEnvironment;

    public BoatType(String boatClassification, String widthType, String rowingEnvironment) {
        this.boatClassification = boatClassification;
        this.widthType = widthType;
        this.rowingEnvironment = rowingEnvironment;
    }

    public String getBoatClassification() {
        return boatClassification;
    }

    public String getWidthType() {
        return widthType;
    }

    public String getRowingEnvironment() {
        return rowingEnvironment;
    }

    private Integer getMaximumNumberOfRowers(String boatClassification) {
        return Character.getNumericValue(boatClassification.charAt(0));
    }

    @Override
    public String toString() {
        String theString = boatClassification.toString();

        if (widthType.equals("Wide")) {
            theString += " " + widthType;
        }

        if (rowingEnvironment.equals("Coastal")) {
            theString += " " + rowingEnvironment;
        }

        return theString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoatType boatType = (BoatType) o;
        return Objects.equals(boatClassification, boatType.boatClassification) &&
                Objects.equals(widthType, boatType.widthType) &&
                Objects.equals(rowingEnvironment, boatType.rowingEnvironment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(boatClassification, widthType, rowingEnvironment);
    }
}