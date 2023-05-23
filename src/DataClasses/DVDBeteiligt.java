package DataClasses;

public class DVDBeteiligt {
    private String name;
    private DVDBeteiligtenTitel titel;

    public DVDBeteiligt(String name, DVDBeteiligtenTitel titel) {
        this.name = name;
        this.titel = titel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DVDBeteiligtenTitel getTitel() {
        return titel;
    }

    public void setTitel(DVDBeteiligtenTitel titel) {
        this.titel = titel;
    }

    @Override
    public String toString() {
        return "DataClasses.DVDBeteiligt{" +
                "name='" + name + '\'' +
                ", titel=" + titel +
                '}';
    }
}
