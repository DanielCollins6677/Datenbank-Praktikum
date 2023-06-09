package DataClasses;

import java.util.ArrayList;
import java.util.List;

public class DVD extends Produkt{
    private List<String> format = new ArrayList<>();
    private int laufzeit;
    private int regionCode;
    private List<DVDBeteiligt> dvdBeteiligte = new ArrayList<>();

    public DVD() {
    }

    public List<String> getFormat() {
        return format;
    }

    public void setFormat(List<String> format) {
        this.format = format;
    }

    public int getLaufzeit() {
        return laufzeit;
    }

    public void setLaufzeit(int laufzeit) {
        this.laufzeit = laufzeit;
    }

    public int getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(int regionCode) {
        this.regionCode = regionCode;
    }

    public List<DVDBeteiligt> getDvdBeteiligte() {
        return dvdBeteiligte;
    }

    public void setDvdBeteiligt(List<DVDBeteiligt> dvdBeteiligte) {
        this.dvdBeteiligte = dvdBeteiligte;
    }

    @Override
    public String toString() {
        String produktString = super.toString();
        return produktString + " DVD{" +
                "format='" + format + '\'' +
                ", laufzeit=" + laufzeit +
                ", regionCode=" + regionCode +
                ", dvdBeteiligt=" + dvdBeteiligte +
                '}';
    }
}
