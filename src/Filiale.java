import java.util.List;
import java.util.Map;

public class Filiale {
    private String name;
    private String straße;
    private int plz;
    private Map<Produkt, List<PreisZustand>> produktPreis;

    public Filiale() {
    }

    public Filiale(String name, String straße, int plz, Map<Produkt,List<PreisZustand>> produktPreis) {
        this.name = name;
        this.plz = plz;
        this.straße = straße;
        this.produktPreis = produktPreis;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPlz() {
        return plz;
    }

    public void setPlz(int plz) {
        this.plz = plz;
    }

    public String getStraße() {
        return straße;
    }

    public void setStraße(String straße) {
        this.straße = straße;
    }

    public Map<Produkt, List<PreisZustand>> getProduktPreis() {
        return produktPreis;
    }

    public void setProduktPreis(Map<Produkt, List<PreisZustand>> produktPreis) {
        this.produktPreis = produktPreis;
    }

    @Override
    public String toString() {
        return "Filiale{" +
                "name='" + name + '\'' +
                ", plz=" + plz +
                ", straße='" + straße + '\'' +
                ", produktPreis=" + produktPreis +
                '}';
    }
}
