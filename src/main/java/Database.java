import DataClasses.*;

import java.sql.*;
import java.util.List;
import java.util.Map;

public class Database {
    private Connection db;

    public Database(String ip,int port,String dbName,String user,String password) {
        try {
            String dbConnection = String.format("jdbc:postgresql://%s:%d/%s",ip,port,dbName);
            this.db = DriverManager.getConnection(dbConnection,user,password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean produktAlreadyInDatabase(Produkt produkt) throws SQLException {
        PreparedStatement inDatenbank = db.prepareStatement(
                "SELECT prodnr from produkt where prodnr = ?"
        );

        inDatenbank.setString(1,produkt.getProdNr());

        ResultSet resultSet = inDatenbank.executeQuery();
        boolean produktAlreadyInDatabase = false;
        if(resultSet.next()){
            produktAlreadyInDatabase = true;
        }
        resultSet.close();
        inDatenbank.close();
        return produktAlreadyInDatabase;
    }

    private void addFiliale(Filiale filiale) throws SQLException {
        PreparedStatement newFiliale = db.prepareStatement(
                "INSERT INTO Filiale (name,plz,straße) " +
                        "VALUES (?,?,?)"
        );

        //Parameter

        newFiliale.setString(1, filiale.getName());
        newFiliale.setInt(2, filiale.getPlz());
        newFiliale.setString(3, filiale.getStraße());
        newFiliale.executeUpdate();

        PreparedStatement filialeAngebot = db.prepareStatement(
                "INSERT INTO filiale_Angebot (fname, prodnr,preis,zustand)" +
                        "VALUES (?,?,?,?)"
        );

        filialeAngebot.setString(1, filiale.getName());


        Map<Produkt, List<PreisZustand>> produktPreis = filiale.getProduktPreis();
        for(Produkt prod : produktPreis.keySet()){

            //Add Produkt into Database
            addProdukt(prod);

            List<PreisZustand> produktAngebote = produktPreis.get(prod);
            for(PreisZustand einzelnesProduktAngebot : produktAngebote) {
                filialeAngebot.setString(2, prod.getProdNr());
                filialeAngebot.setDouble(3,einzelnesProduktAngebot.getPreis());
                filialeAngebot.setString(4,einzelnesProduktAngebot.getZustand());
                filialeAngebot.executeUpdate();
            }
        }
    }

    public void addProdukt(Produkt produkt) throws SQLException {


        try {


            if(produktAlreadyInDatabase(produkt)){
                System.out.println(produkt.getProdNr() + " ist schon in der Datenbank");
                return;
            }

            db.setAutoCommit(false);

            PreparedStatement newProdukt = db.prepareStatement(
                    "INSERT INTO Produkt (prodnr,titel,rating,verkaufsrang,bild) " +
                        "VALUES (?,?,?,?,?)"
            );
            newProdukt.setString(1, produkt.getProdNr());
            newProdukt.setString(2, produkt.getTitel());
            newProdukt.setDouble(3,produkt.getRating());
            newProdukt.setLong(4,produkt.getVerkaufsRank());
            newProdukt.setString(5, produkt.getBild());

            newProdukt.executeUpdate();

            if(produkt instanceof Buch){
                //System.out.println("Ein neues Buch!");
                addBuch((Buch) produkt);
            } else if(produkt instanceof CD){
                addCD((CD) produkt);
            } else if(produkt instanceof DVD){
                addDVD((DVD) produkt);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            db.setAutoCommit(true);
        }
    }
    private void addBuch(Buch buch) throws SQLException {
        PreparedStatement newBook = db.prepareStatement(
                "INSERT INTO Buch (prodnr,seitenzahl,erscheinungsjahr,isbn) " +
                        "VALUES (?,?,?,?)"
        );

        //Parameter

        newBook.setString(1, buch.getProdNr());
        newBook.setInt(2, buch.getSeitenZahl());
        newBook.setDate(3, Date.valueOf(buch.getErscheinungsJahr()));
        newBook.setString(4, buch.getIsbn());


        newBook.executeUpdate();

        PreparedStatement buchAutor = db.prepareStatement(
                "INSERT INTO buch_autor (prodnr,autor)" +
                        "VALUES (?,?)"
        );

        buchAutor.setString(1, buch.getProdNr());

        for (String autor : buch.getAuthors()) {
            buchAutor.setString(2, autor);
            buchAutor.executeUpdate();
        }

        PreparedStatement buchVerlag = db.prepareStatement(
                "INSERT INTO buch_verlag (prodnr,verlag)" +
                        "VALUES (?,?)"
        );

        buchVerlag.setString(1, buch.getProdNr());

        for (String verlag : buch.getVerlag()) {
            buchVerlag.setString(2, verlag);
            buchVerlag.executeUpdate();
        }

        // JDBC Erweiterung
    }
    private void addCD(CD cd) throws SQLException {
            PreparedStatement newCD = db.prepareStatement(
                    "INSERT INTO CD (prodnr,erscheinungsdatum) " +
                            "VALUES (?,?)"
            );

            //Parameter

            newCD.setString(1, cd.getProdNr());
            newCD.setDate(2, Date.valueOf(cd.getErscheinungsdatum()));


            newCD.executeUpdate();

            PreparedStatement cdKünstler = db.prepareStatement(
                    "INSERT INTO cd_künstler (prodnr,name)" +
                            "VALUES (?,?)"
            );

            cdKünstler.setString(1, cd.getProdNr());

            for(String name : cd.getKünstler()){
                cdKünstler.setString(2,name);
                cdKünstler.executeUpdate();
            }

            PreparedStatement cdLabel = db.prepareStatement(
                    "INSERT INTO cd_label (prodnr,label)" +
                            "VALUES (?,?)"
            );

            cdLabel.setString(1, cd.getProdNr());

            for(String label : cd.getLabels()){
                cdLabel.setString(2,label);
                cdLabel.executeUpdate();
            }

            PreparedStatement cdWerke = db.prepareStatement(
                    "INSERT INTO cd_werke (prodnr,titel)" +
                            "VALUES (?,?)"
            );

            cdWerke.setString(1, cd.getProdNr());

            for(String titel : cd.getTracks()){
                cdWerke.setString(2,titel);
                cdWerke.executeUpdate();
            }

        }
    private void addDVD(DVD dvd) throws SQLException {
        PreparedStatement newDVD = db.prepareStatement(
                "INSERT INTO DVD (prodnr,laufzeit,regioncode) " +
                        "VALUES (?,?,?)"
        );

        //Parameter

        newDVD.setString(1, dvd.getProdNr());
        newDVD.setInt(2, dvd.getLaufzeit());
        newDVD.setInt(3, dvd.getRegionCode());


        newDVD.executeUpdate();


        //newDVD.setString(2, dvd.getFormat());

        PreparedStatement dvdFormat = db.prepareStatement(
                "INSERT INTO dvd_format (prodnr,format)" +
                        "VALUES (?,?)"
        );
        dvdFormat.setString(1,dvd.getProdNr());

        for(String format : dvd.getFormat()){
            dvdFormat.setString(2,format);
            dvdFormat.executeUpdate();
        }


        PreparedStatement dvdBeteiligt = db.prepareStatement(
                "INSERT INTO dvd_beteiligt (prodnr,name,titel)" +
                        "VALUES (?,?,?)"
        );

        dvdBeteiligt.setString(1, dvd.getProdNr());

        for(DVDBeteiligt beteiligt : dvd.getDvdBeteiligte()){
            dvdBeteiligt.setString(2,beteiligt.getName());
            dvdBeteiligt.setString(3,beteiligt.getTitel().toString());
            dvdBeteiligt.executeUpdate();
        }
    }
    private void addCategories(List<Category> categories) throws SQLException {
            PreparedStatement newKategorie = db.prepareStatement(
                    "INSERT INTO Kategorie (name) " +
                            "VALUES (?)"
            );
            PreparedStatement kategorieOrdnung = db.prepareStatement(
                    "INSERT INTO kategorieOrdnung (oberkategorie,unterkategorie)" +
                            "VALUES (?,?)"
            );

            //Parameter

            for(Category category : categories) {
                newKategorie.setString(1, category.getName());

                newKategorie.executeUpdate();

                kategorieOrdnung.setString(1, category.getParentCategory());
                kategorieOrdnung.setString(2, category.getName());
                kategorieOrdnung.executeUpdate();
            }
        }

        /*private void addKunde(Kunde kunde) throws SQLException {
            PreparedStatement newKunde = db.prepareStatement(
                    "INSERT INTO Kunde (name) " +
                            "VALUES (?)"
            );

            //Parameter

            newKunde.setString(1, kunde.getName());

            newKunde.executeUpdate();

            PreparedStatement kundeAdresse = db.prepareStatement(
                    "INSERT INTO kunde_adresse (name,adresse)" +
                            "VALUES (?,?)"
            );

            kundeAdresse.setString(1, kunde.getName());

            for(String adresse : kunde.getAdresse()){
                kundeAdresse.setString(2,adresse);
                kundeAdresse.executeUpdate();
            }

            PreparedStatement kundeKonto = db.prepareStatement(
                    "INSERT INTO kunde_konto (name,kontoname, kontonummer)" +
                            "VALUES (?,?,?)"
            );

            kundeKonto.setString(1, kunde.getName());

            for(String kontoname : kunde.getKontoname()){
                kunde.setString(2,kontoname);
                kundeKonto.executeUpdate();
            }
            for(String kontonummer : kunde.getKontonummer()){
                kunde.setString(3,kontonummer);
                kundeKonto.executeUpdate();
            }

        }
    }
         */
}
