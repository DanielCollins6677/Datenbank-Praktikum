import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class Buch extends Produkt{
    private List<String> authors;
    private int seitenZahl;
    private LocalDate erscheinungsJahr;
    private String isbn;
    private String verlag;

    public Buch() {
    }

    @Override
    public String toString() {
        String produktString = super.toString();
        return produktString + " Buch{" +
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

    public LocalDate getErscheinungsJahr() {
        return erscheinungsJahr;
    }

    public void setErscheinungsJahr(LocalDate erscheinungsJahr) {
        this.erscheinungsJahr = erscheinungsJahr;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getVerlag() {
        return verlag;
    }

    public void setVerlag(String verlag) {
        this.verlag = verlag;
    }
}
