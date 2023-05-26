import DataClasses.*;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;


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

    private boolean filialeAlreadyInDatabase(Filiale filiale) throws SQLException {
        try(
            PreparedStatement inDatenbank = db.prepareStatement(
                    "SELECT name from filiale where name = ?"
            )
        ){

            inDatenbank.setString(1,filiale.getName());

            ResultSet resultSet = inDatenbank.executeQuery();
            boolean produktAlreadyInDatabase = false;
            if(resultSet.next()){
                produktAlreadyInDatabase = true;
            }
            resultSet.close();
            inDatenbank.close();
            return produktAlreadyInDatabase;
        } catch (Exception e){
            throw new RuntimeException();
        }


    }
    private boolean produktAlreadyInDatabase(Produkt produkt) throws SQLException {
        try(
            PreparedStatement inDatenbank = db.prepareStatement(
                    "SELECT prodnr from produkt where prodnr = ?"
            )
        ){
            inDatenbank.setString(1,produkt.getProdNr());
            if(produkt.getProdNr().length() > 30) System.err.print("ProdNr zu groß");

            ResultSet resultSet = inDatenbank.executeQuery();
            boolean produktAlreadyInDatabase = false;
            if(resultSet.next()){
                produktAlreadyInDatabase = true;
            }
            resultSet.close();
            inDatenbank.close();
            return produktAlreadyInDatabase;
        } catch (Exception e){
            throw new RuntimeException();
        }

    }

    public void addFiliale(Filiale filiale) throws SQLException {
        try(
            PreparedStatement newFiliale = db.prepareStatement(
                    "INSERT INTO Filiale (name,plz,straße) " +
                            "VALUES (?,?,?)"
            );
            PreparedStatement filialeAngebot = db.prepareStatement(
                    "INSERT INTO filiale_Angebot (fname, prodnr,preis,zustand)" +
                            "VALUES (?,?,?,?)"
            );
        ) {
            if(filialeAlreadyInDatabase(filiale)){
                System.out.println(filiale.getName() + " ist schon in der Datenbank");
                return;
            }

            if(db.getAutoCommit()) db.setAutoCommit(false);

            //Parameter

            if(filiale.getName().length() > 30) System.err.print("Name zu groß");
            if(filiale.getStraße().length() > 50) System.err.print("Straße zu groß");
            newFiliale.setString(1, filiale.getName());
            newFiliale.setInt(2, filiale.getPlz());
            newFiliale.setString(3, filiale.getStraße());
            newFiliale.executeUpdate();


            filialeAngebot.setString(1, filiale.getName());
            if(filiale.getName().length() > 50) System.err.print("FName zu groß");


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
                    if((prod.getProdNr()).length() > 30) System.err.print("ProdNr zu groß");
                    if(einzelnesProduktAngebot.getZustand().length() > 30) System.err.print("Produkt Angebot zu groß");
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        } finally {
            db.setAutoCommit(true);
        }
    }

    private void addProdukt(Produkt produkt) throws SQLException {

        try (
            PreparedStatement newProdukt = db.prepareStatement(
                    "INSERT INTO Produkt (prodnr,titel,rating,verkaufsrang,bild) " +
                            "VALUES (?,?,?,?,?)"
            );
        ) {
            if(produktAlreadyInDatabase(produkt)){
                System.out.println(produkt.getProdNr() + " ist schon in der Datenbank");
                return;
            }

            if(db.getAutoCommit()) db.setAutoCommit(false);

            if(produkt.getProdNr().length() > 30) System.err.print("ProdNr zu groß");
            if(produkt.getTitel().length() > 255) System.err.print("Titel zu groß");
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
            db.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void addBuch(Buch buch) throws SQLException {
        try(
            PreparedStatement newBook = db.prepareStatement(
                    "INSERT INTO Buch (prodnr,seitenzahl,erscheinungsjahr,isbn) " +
                            "VALUES (?,?,?,?)"
            );
            PreparedStatement buchAutor = db.prepareStatement(
                    "INSERT INTO buch_autor (prodnr,autor)" +
                            "VALUES (?,?)"
            );
            PreparedStatement buchVerlag = db.prepareStatement(
                    "INSERT INTO buch_verlag (prodnr,verlag)" +
                            "VALUES (?,?)"
            );
        ){
            if(db.getAutoCommit()) db.setAutoCommit(false);


            //Parameter

            if(buch.getProdNr().length() > 30) System.err.print("ProdNr zu groß");
            newBook.setString(1, buch.getProdNr());
            newBook.setInt(2, buch.getSeitenZahl());
            newBook.setDate(3, Date.valueOf(buch.getErscheinungsJahr()));
            newBook.setString(4, buch.getIsbn());


            newBook.executeUpdate();


            buchAutor.setString(1, buch.getProdNr());

            for (String autor : buch.getAuthors()) {
                buchAutor.setString(2, autor);
                buchAutor.executeUpdate();
                if((autor).length() > 30) System.err.print("Autor zu groß");
            }


            buchVerlag.setString(1, buch.getProdNr());

            for (String verlag : buch.getVerlag()) {
                buchVerlag.setString(2, verlag);
                buchVerlag.executeUpdate();
                if((verlag).length() > 30) System.err.print("Verlag zu groß");
            }

            // JDBC Erweiterung
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void addCD(CD cd) throws SQLException {
        try(
            PreparedStatement newCD = db.prepareStatement(
                    "INSERT INTO CD (prodnr,erscheinungsdatum) " +
                            "VALUES (?,?)"
            );
            PreparedStatement cdKünstler = db.prepareStatement(
                    "INSERT INTO cd_künstler (prodnr,name)" +
                            "VALUES (?,?)"
            );
            PreparedStatement cdLabel = db.prepareStatement(
                    "INSERT INTO cd_label (prodnr,label)" +
                            "VALUES (?,?)"
            );
            PreparedStatement cdWerke = db.prepareStatement(
                    "INSERT INTO cd_werke (prodnr,titel)" +
                            "VALUES (?,?)"
            );
        ){
            if(db.getAutoCommit()) db.setAutoCommit(false);

            //Parameter

            if(cd.getProdNr().length() > 30) System.err.print("ProdNr zu groß");
            newCD.setString(1, cd.getProdNr());
            newCD.setDate(2, Date.valueOf(cd.getErscheinungsdatum()));


            newCD.executeUpdate();


            cdKünstler.setString(1, cd.getProdNr());
            if(cd.getProdNr().length() > 30) System.err.print("Name zu groß");


            //cdKünstler.setString(2, ...)
            for(String name : cd.getKünstler()){

                cdKünstler.setString(2,name);
                cdKünstler.executeUpdate();
                if((name).length() > 50) System.err.print("Name zu groß");
            }

            cdLabel.setString(1, cd.getProdNr());
            if(cd.getProdNr().length() > 30) System.err.print("ProdNr zu groß");


            for(String label : cd.getLabels()){
                cdLabel.setString(2,label);
                cdLabel.executeUpdate();
                if(label.length() > 100) System.err.print("Label zu groß");

            }


            cdWerke.setString(1, cd.getProdNr());
            if(cd.getProdNr().length() > 30) System.err.print("ProdNr zu groß");

            for(String titel : cd.getTracks()){
                cdWerke.setString(2,titel);
                cdWerke.executeUpdate();
                if(titel.length() > 255) System.err.print("Titel zu groß");
            }

        }catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    private void addDVD(DVD dvd) throws SQLException {
        try(
            PreparedStatement newDVD = db.prepareStatement(
                    "INSERT INTO DVD (prodnr,laufzeit,regioncode) " +
                            "VALUES (?,?,?)"
            );
            PreparedStatement dvdFormat = db.prepareStatement(
                    "INSERT INTO dvd_format (prodnr,format)" +
                            "VALUES (?,?)"
            );
            PreparedStatement dvdBeteiligt = db.prepareStatement(
                    "INSERT INTO dvd_beteiligt (prodnr,name,titel)" +
                            "VALUES (?,?,?)"
            );
        ) {
            if(db.getAutoCommit()) db.setAutoCommit(false);



            //Parameter

            if(dvd.getProdNr().length() > 30) System.err.print("ProdNr zu groß");
            newDVD.setString(1, dvd.getProdNr());
            newDVD.setInt(2, dvd.getLaufzeit());
            newDVD.setInt(3, dvd.getRegionCode());


            newDVD.executeUpdate();


            dvdFormat.setString(1,dvd.getProdNr());
            if(dvd.getProdNr().length() > 30) System.err.print("ProdNr zu groß");

            for(String format : dvd.getFormat()){
                dvdFormat.setString(2,format);
                dvdFormat.executeUpdate();
                if((format).length() > 50) System.err.print("Format zu groß");
            }


            dvdBeteiligt.setString(1, dvd.getProdNr());
            if(dvd.getProdNr().length() > 30) System.err.print("ProdNr zu groß");

            for(DVDBeteiligt beteiligt : dvd.getDvdBeteiligte()){
                dvdBeteiligt.setString(2,beteiligt.getName());
                dvdBeteiligt.setString(3,beteiligt.getTitel().toString());
                dvdBeteiligt.executeUpdate();
                if(beteiligt.getName().length() > 30) System.err.print("Name zu groß");
                if(beteiligt.getTitel().toString().length() > 255) System.err.print("Titel zu groß");
            }

        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void addCategories(List<Category> categories) throws SQLException {
        try(
            PreparedStatement newKategorie = db.prepareStatement(
                    "INSERT INTO Kategorie (name) " +
                            "VALUES (?)"
            );
            PreparedStatement kategorieOrdnung = db.prepareStatement(
                    "INSERT INTO kategorieOrdnung (oberkategorie,unterkategorie)" +
                            "VALUES (?,?)"
            );
        ){
            if(db.getAutoCommit()) db.setAutoCommit(false);

            //Parameter

            for(Category category : categories) {
                newKategorie.setString(1, category.getName());
                if(category.getName().length() > 30) System.err.print("Name zu groß");

                newKategorie.executeUpdate();

                kategorieOrdnung.setString(1, category.getParentCategory());
                kategorieOrdnung.setString(2, category.getName());
                kategorieOrdnung.executeUpdate();
                if(category.getParentCategory().length() > 30) System.err.print("Oberkategorie zu groß");
                if(category.getName().length() > 30) System.err.print("Unterkategorie zu groß");
            }

        }catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void clearDB() {
        try{

            clearKategorieDB();
            clearFilialeDB();
            clearBuchDB();
            clearCDDB();
            clearDVDDB();
            clearProdukt();

        } catch (Exception e) {
            System.err.println("Could not clear Database!");
            e.printStackTrace();
        }
    }

    public void clearBuchDB() throws SQLException {
        try(
            PreparedStatement clearBuch = db.prepareStatement(
                    "Delete from buch_autor;" +
                        "Delete from buch_verlag;" +
                        "Delete from buch;"
            );
        ){
            clearBuch.executeUpdate();
        }
    }

    public void clearCDDB() throws SQLException{
        try(
                PreparedStatement clearCD = db.prepareStatement(
                        "Delete from cd_künstler;" +
                            "Delete from cd_label;" +
                            "Delete from cd_werke;" +
                            "Delete from cd;"
                );
        ){
            clearCD.executeUpdate();
        }
    }

    public void clearDVDDB() throws SQLException{
        try(
            PreparedStatement clearDVD = db.prepareStatement(
                    "Delete from dvd_beteiligt;" +
                        "Delete from dvd_format;" +
                        "Delete from dvd;"
            );
        ){
            clearDVD.executeUpdate();
        }
    }

    private void clearProdukt() throws SQLException {
        try(
            PreparedStatement clearProdukt = db.prepareStatement(
                    "Delete from produkt_kategorie;" + 
                        "Delete from produkt_ähnlich;" +
                        "Delete from produkt;"
            );
        ){
            clearProdukt.executeUpdate();
        }
    }

    public void clearFilialeDB() throws SQLException{
        try(
            PreparedStatement clearFiliale = db.prepareStatement(
                    "Delete from filiale_angebot;" +
                        "Delete from filiale;"
            );
        ){
            clearFiliale.executeUpdate();
        }
    }

    public void clearKategorieDB() throws SQLException{
        try(
            PreparedStatement clearKategorie = db.prepareStatement(
                    "Delete from produkt_kategorie;" +
                        "Delete from kategorie_ordnung;" +
                        "Delete from kategorie;"
            );
        ){
            clearKategorie.executeUpdate();
        }
    }

    public void testAddFilialeDB(){

        Buch test1 = new Buch();
        {
            test1.setProdNr("1");
            test1.setTitel("test1");
            test1.setRating(4.45);
            test1.setVerkaufsRank(1);
            test1.setBild("");

            test1.setIsbn("1234");
            test1.setErscheinungsJahr(LocalDate.now());
            test1.setSeitenZahl(1);
            List<String> autoren = new ArrayList<>();
            autoren.add("Gustav");
            autoren.add("Steven");
            test1.setAuthors(autoren);
            List<String> verlag = new ArrayList<>();
            verlag.add("Carlsen");
            verlag.add("HBB");
            test1.setVerlag(verlag);
        }
        Buch test2 = new Buch();
        {
            test2.setProdNr("2");
            test2.setTitel("test2");
            test2.setRating(5.45);
            test2.setVerkaufsRank(2);
            test2.setBild("");

            test2.setIsbn("12345");
            test2.setErscheinungsJahr(LocalDate.now());
            test2.setSeitenZahl(10);
            List<String> autoren = new ArrayList<>();
            autoren.add("Lennard");
            autoren.add("John");
            test2.setAuthors(autoren);
            List<String> verlag = new ArrayList<>();
            verlag.add("MangaCult");
            verlag.add("Kaze");
            test2.setVerlag(verlag);
        }
        CD test3 = new CD();
        {
            test3.setProdNr("3");
            test3.setTitel("test3");
            test3.setRating(5.45);
            test3.setVerkaufsRank(3);
            test3.setBild("");

            test3.setErscheinungsdatum(LocalDate.now());
            List<String> künstler = new ArrayList<>();
            künstler.add("k1");
            künstler.add("k2");
            test3.setKünstler(künstler);
            Set<String> label = new HashSet<>();
            label.add("label1");
            label.add("label2");
            test3.setLabels(label);
            List<String> tracks = new ArrayList<>();
            tracks.add("track1");
            tracks.add("track2");
            test3.setTracks(tracks);
        }
        DVD test4 = new DVD();
        {
            test4.setProdNr("4");
            test4.setTitel("test4");
            test4.setRating(5.45);
            test4.setVerkaufsRank(4);
            test4.setBild("");

            test4.setLaufzeit(1);
            test4.setRegionCode(1);
            List<String> formate = new ArrayList<>();
            formate.add("format1");
            formate.add("format2");
            test4.setFormat(formate);
            List<DVDBeteiligt> beteiligte = new ArrayList<>();
            beteiligte.add(new DVDBeteiligt("beteiligter1",DVDBeteiligtenTitel.Actor));
            beteiligte.add(new DVDBeteiligt("beteiligter2",DVDBeteiligtenTitel.Creator));
            beteiligte.add(new DVDBeteiligt("beteiligter3",DVDBeteiligtenTitel.Director));
            test4.setDvdBeteiligt(beteiligte);
        }
        List<Produkt> produkts = new ArrayList<>();
        produkts.add(test1);
        produkts.add(test2);
        produkts.add(test3);
        produkts.add(test4);

        PreisZustand testPreis1 = new PreisZustand();
        PreisZustand testPreis2 = new PreisZustand();
        {
            testPreis1.setPreis(1.1);
            testPreis1.setZustand("neu");
            testPreis2.setPreis(0.5);
            testPreis2.setZustand("alt");
        }
        List<PreisZustand> preisZustands = new ArrayList<>();
        preisZustands.add(testPreis1);
        preisZustands.add(testPreis2);

        Map<Produkt, List<PreisZustand>> produktPreis = new HashMap<>();
        for(Produkt prod : produkts){
            produktPreis.put(prod,preisZustands);
        }

        Filiale testFiliale = new Filiale("test","MaxMustermannStraße",1234,produktPreis);
        try{
            addFiliale(testFiliale);
        } catch (SQLException e) {
            System.err.println("Fehler beim Testen der Datenbank");
            e.printStackTrace();
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
