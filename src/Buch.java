import java.util.List;

public class Buch extends Produkt{
    private List<String> authors;
    private int seitenZahl;
    private String erscheinungsJahr;
    private long isbn;
    private String verlag;

    public Buch() {
    }

    @Override
    public String toString() {
        return "Buch{" +
                "prodNr='" + getProdNr() + '\'' +
                "author='" + authors + '\'' +
                ", seitenZahl=" + seitenZahl +
                ", erscheinungsJahr=" + erscheinungsJahr +
                ", isbn=" + isbn +
                ", verlag='" + verlag + '\'' +
                '}';
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> author) {
        this.authors = author;
    }

    public int getSeitenZahl() {
        return seitenZahl;
    }

    public void setSeitenZahl(int seitenZahl) {
        this.seitenZahl = seitenZahl;
    }

    public String getErscheinungsJahr() {
        return erscheinungsJahr;
    }

    public void setErscheinungsJahr(String erscheinungsJahr) {
        this.erscheinungsJahr = erscheinungsJahr;
    }

    public long getIsbn() {
        return isbn;
    }

    public void setIsbn(long isbn) {
        this.isbn = isbn;
    }

    public String getVerlag() {
        return verlag;
    }

    public void setVerlag(String verlag) {
        this.verlag = verlag;
    }
}
