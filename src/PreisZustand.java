public class PreisZustand {
    private double preis;
    private String zustand;

    public double getPreis() {
        return preis;
    }

    public void setPreis(double preis) {
        this.preis = preis;
    }

    public String getZustand() {
        return zustand;
    }

    public void setZustand(String zustand) {
        this.zustand = zustand;
    }

    @Override
    public String toString() {
        return "PreisZustand{" +
                "preis=" + preis +
                ", zustand='" + zustand + '\'' +
                '}';
    }
}
