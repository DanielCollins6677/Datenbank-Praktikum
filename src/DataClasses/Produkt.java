package DataClasses;

public abstract class Produkt {
    private String prodNr;
    private String titel;
    private double rating;
    private long verkaufsRank;
    private String bild;

    public Produkt() {
    }


    public String getProdNr() {
        return prodNr;
    }

    public void setProdNr(String prodNr) {
        this.prodNr = prodNr;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public long getVerkaufsRank() {
        return verkaufsRank;
    }

    public void setVerkaufsRank(long verkaufsRank) {
        this.verkaufsRank = verkaufsRank;
    }

    public String getBild() {
        return bild;
    }

    public void setBild(String bild) {
        this.bild = bild;
    }


    @Override
    public String toString() {
        return "DataClasses.Produkt{" +
                "prodNr='" + prodNr + '\'' +
                ", titel='" + titel + '\'' +
                ", rating=" + rating +
                ", verkaufsRank=" + verkaufsRank +
                ", bild='" + bild + '\'' +
                '}';
    }
}
