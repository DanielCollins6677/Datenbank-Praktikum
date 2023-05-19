import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DVD extends Produkt{
    private String format;
    private int laufzeit;
    private int regionCode;
    private List<DVDBeteiligt> dvdBeteiligte = new ArrayList<>();

    public DVD() {
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
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
                "prodNr='" + getProdNr() + '\'' +
                "format='" + format + '\'' +
                ", laufzeit=" + laufzeit +
                ", regionCode=" + regionCode +
                ", dvdBeteiligt=" + dvdBeteiligte +
                '}';
    }
}
