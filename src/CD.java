import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CD extends Produkt{
    private String label;
    private Date erscheinungsdatum;

    private List<String> künstler = new ArrayList<>();

    private List<String> tracks = new ArrayList<>();

    public CD() {
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Date getErscheinungsdatum() {
        return erscheinungsdatum;
    }

    public void setErscheinungsdatum(Date erscheinungsdatum) {
        this.erscheinungsdatum = erscheinungsdatum;
    }

    public List<String> getKünstler() {
        return künstler;
    }

    public void setKünstler(List<String> künstler) {
        this.künstler = künstler;
    }

    public List<String> getTracks() {
        return tracks;
    }

    public void setTracks(List<String> tracks) {
        this.tracks = tracks;
    }

    @Override
    public String toString() {
        return "CD{" +
                "label='" + label + '\'' +
                ", erscheinungsdatum=" + erscheinungsdatum +
                ", künstler=" + künstler +
                ", tracks=" + tracks +
                '}';
    }
}
