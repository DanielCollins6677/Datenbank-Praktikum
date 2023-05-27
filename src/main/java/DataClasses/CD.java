package DataClasses;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CD extends Produkt{
    private Set labels = new HashSet<>();
    private LocalDate erscheinungsdatum;

    private List<String> künstler = new ArrayList<>();

    private List<String> tracks = new ArrayList<>();

    public CD() {
    }

    public Set<String> getLabels() {
        return labels;
    }

    public void setLabels(Set<String> labels) {
        this.labels = labels;
    }

    public LocalDate getErscheinungsdatum() {
        return erscheinungsdatum;
    }

    public void setErscheinungsdatum(LocalDate erscheinungsdatum) {
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
        String produktString = super.toString();
        return produktString + " CD{" +
                "label='" + labels + '\'' +
                ", erscheinungsdatum=" + erscheinungsdatum +
                ", künstler=" + künstler +
                ", tracks=" + tracks +
                '}';
    }
}
