package DataClasses;

import java.time.LocalDate;
import java.util.List;

public class Buch extends Produkt{
    private List<String> authors;
    private int seitenZahl;
    private LocalDate erscheinungsJahr;
    private String isbn;
    private List<String> verlag;

    public Buch() {
    }

    @Override
    public String toString() {
        String produktString = super.toString();
        return produktString + " DataClasses.Buch{" +
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

    public List<String> getVerlag() {
        return verlag;
    }

    public void setVerlag(List<String> verlag) {
        this.verlag = verlag;
    }
}
