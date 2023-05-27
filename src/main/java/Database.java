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

    private boolean produktAlreadyInDatabase(String prodnr) throws SQLException {
        PreparedStatement inDatenbank = db.prepareStatement(
                "SELECT prodnr from produkt where prodnr = ?"
        );

        inDatenbank.setString(1,prodnr);

        ResultSet resultSet = inDatenbank.executeQuery();
        boolean produktAlreadyInDatabase = false;
        if(resultSet.next()){
            produktAlreadyInDatabase = true;
        }
        resultSet.close();
        inDatenbank.close();
        return produktAlreadyInDatabase;
    }

    private boolean kategorieAlreadyInDatabase(String kategorieName) throws SQLException {
        PreparedStatement inDatenbank = db.prepareStatement(
                "SELECT name from kategorie where name = ?"
        );

        inDatenbank.setString(1,kategorieName);

        ResultSet resultSet = inDatenbank.executeQuery();
        boolean kategorieAlreadyInDatabase = false;
        if(resultSet.next()){
            kategorieAlreadyInDatabase = true;
        }
        resultSet.close();
        inDatenbank.close();
        return kategorieAlreadyInDatabase;
    }

    private boolean kategorieProduktAlreadyInDatabase(String prodnr,String kategorieName) throws SQLException {
        PreparedStatement inDatenbank = db.prepareStatement(
                "SELECT Kat_Name from produkt_kategorie where Kat_Name = ? AND prodnr = ?"
        );

        inDatenbank.setString(1,kategorieName);
        inDatenbank.setString(2,prodnr);

        ResultSet resultSet = inDatenbank.executeQuery();
        boolean kategorieAlreadyInDatabase = false;
        if(resultSet.next()){
            kategorieAlreadyInDatabase = true;
        }
        resultSet.close();
        inDatenbank.close();
        return kategorieAlreadyInDatabase;
    }
    private boolean kategorieOrdnungAlreadyInDatabase(String oberKategorie, String unterKategorie) throws SQLException {
        PreparedStatement inDatenbank = db.prepareStatement(
                "SELECT Oberkategorie from Kategorie_Ordnung where Oberkategorie = ? AND Unterkategorie = ?"
        );

        inDatenbank.setString(1,oberKategorie);
        inDatenbank.setString(2,unterKategorie);

        ResultSet resultSet = inDatenbank.executeQuery();
        boolean kategorieAlreadyInDatabase = false;
        if(resultSet.next()){
            kategorieAlreadyInDatabase = true;
        }
        resultSet.close();
        inDatenbank.close();
        return kategorieAlreadyInDatabase;
    }

    private boolean kundeAlreadyInDatabase(String kundenName) throws SQLException {
        PreparedStatement inDatenbank = db.prepareStatement(
                "SELECT name from Kunde where name = ?"
        );

        inDatenbank.setString(1,kundenName);

        ResultSet resultSet = inDatenbank.executeQuery();
        boolean kategorieAlreadyInDatabase = false;
        if(resultSet.next()){
            kategorieAlreadyInDatabase = true;
        }
        resultSet.close();
        inDatenbank.close();
        return kategorieAlreadyInDatabase;
    }

    private boolean rezensionAlreadyInDatabase(String kundenName, String prodnr) throws SQLException {
        PreparedStatement inDatenbank = db.prepareStatement(
                "SELECT Kname from Rezension where Kname = ? AND ProdNr = ?"
        );

        inDatenbank.setString(1,kundenName);
        inDatenbank.setString(2,prodnr);

        ResultSet resultSet = inDatenbank.executeQuery();
        boolean kategorieAlreadyInDatabase = false;
        if(resultSet.next()){
            kategorieAlreadyInDatabase = true;
        }
        resultSet.close();
        inDatenbank.close();
        return kategorieAlreadyInDatabase;
    }

    private boolean produktÄhnlichAlreadyInDatabase(String prodnr1, String prodnr2) throws SQLException {
        PreparedStatement inDatenbank = db.prepareStatement(
                "SELECT ProdNr1 from Rezension where ProdNr1 = ? AND ProdNr2 = ?"
        );

        inDatenbank.setString(1,prodnr1);
        inDatenbank.setString(2,prodnr2);

        ResultSet resultSet = inDatenbank.executeQuery();
        boolean kategorieAlreadyInDatabase = false;
        if(resultSet.next()){
            kategorieAlreadyInDatabase = true;
        }
        resultSet.close();
        inDatenbank.close();
        return kategorieAlreadyInDatabase;
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

            db.commit();

        }catch (SQLException e){
            System.err.println("Konnte Filiale nicht in Datenbank hinzufügen");
            e.printStackTrace();
        } finally {
            db.setAutoCommit(true);
        }
    }

    private void addProdukt(Produkt produkt) {

        try (
            PreparedStatement newProdukt = db.prepareStatement(
                    "INSERT INTO Produkt (prodnr,titel,rating,verkaufsrang,bild) " +
                            "VALUES (?,?,?,?,?)"
            );
        ) {
            if(produktAlreadyInDatabase(produkt)){
                //System.out.println(produkt.getProdNr() + " ist schon in der Datenbank");
                return;
            }

            if(produkt.getProdNr().length() > 30) System.err.print("ProdNr zu groß");
            if(produkt.getTitel().length() > 255) System.err.print("Titel zu groß");
            newProdukt.setString(1, produkt.getProdNr());
            newProdukt.setString(2, produkt.getTitel());
            newProdukt.setDouble(3,produkt.getRating());
            newProdukt.setLong(4,produkt.getVerkaufsRank());
            newProdukt.setString(5, produkt.getBild());

            newProdukt.executeUpdate();

            if(produkt instanceof Buch){
                addBuch((Buch) produkt);
            } else if(produkt instanceof CD){
                addCD((CD) produkt);
            } else if(produkt instanceof DVD){
                addDVD((DVD) produkt);
            }
            db.commit();

        } catch (SQLException e){
            System.err.println("Konnte Produkt nicht in Datenbank hinzufügen");
            e.printStackTrace();
        }
    }
    private void addBuch(Buch buch) {
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
                if((verlag).length() > 50) System.err.print("Verlag zu groß");
            }

            // JDBC Erweiterung
        }catch (SQLException e){
            System.err.println("Konnte Buch nicht in Datenbank hinzufügen");
            e.printStackTrace();
        }
    }
    private void addCD(CD cd)  {
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
                if(name.length() > 60) System.err.println("Name zu groß:" + name.length());
                cdKünstler.executeUpdate();
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

        }catch (SQLException e){
            System.err.println("Konnte CD nicht in Datenbank hinzufügen");
            e.printStackTrace();
        }

    }
    private void addDVD(DVD dvd) {
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
                if(beteiligt.getTitel().toString().length() > 20) System.err.print("Titel zu groß");
            }

        }catch (SQLException e){
            System.err.println("Konnte DVD nicht in Datenbank hinzufügen");
            e.printStackTrace();
        }
    }
    public void addKategorien(List<Category> kategorien) throws SQLException {
        try(
                PreparedStatement addKategorie = db.prepareStatement(
                        "INSERT INTO Kategorie (Name) VALUES (?)"
                );
                PreparedStatement addKategorieOrdnung = db.prepareStatement(
                        "INSERT INTO Kategorie_Ordnung (Oberkategorie,Unterkategorie) VALUES (?,?)"
                );
                PreparedStatement addKategorieProdukt = db.prepareStatement(
                        "INSERT INTO Produkt_Kategorie (ProdNr,Kat_Name) VALUES (?,?)"
                );
        ){

            Collections.reverse(kategorien);

            db.setAutoCommit(false);

            for (Category kategorie : kategorien){
                if(!kategorieAlreadyInDatabase(kategorie.getName())){
                    addKategorie.setString(1,kategorie.getName());
                    addKategorie.executeUpdate();
                }

                if( !(kategorie.getParentCategory() == null) &
                        !kategorieOrdnungAlreadyInDatabase(kategorie.getParentCategory(),kategorie.getName())){
                    addKategorieOrdnung.setString(1,kategorie.getParentCategory());
                    addKategorieOrdnung.setString(2, kategorie.getName());
                    addKategorieOrdnung.executeUpdate();
                }

                for(String produkt : kategorie.getItems()){
                    if(!kategorieProduktAlreadyInDatabase(produkt,kategorie.getName()) && produktAlreadyInDatabase(produkt)){
                        addKategorieProdukt.setString(1,produkt);
                        addKategorieProdukt.setString(2,kategorie.getName());
                        addKategorieProdukt.executeUpdate();
                    }
                }
            }

        } catch (SQLException e){
            System.err.println("Konnte Kategorie nicht in Datenbank hinzufügen");
            e.printStackTrace();
        } finally {
            db.setAutoCommit(true);
        }

    }

    public void addRezension(List<Review> reviews) throws SQLException {
        try(
                PreparedStatement addRezension = db.prepareStatement(
                        "INSERT INTO Rezension (Kname,ProdNr,Rating,Helpful,Zeitpunkt,Kommentar)" +
                                "VALUES (?,?,?,?,?,?)"
                )
        ){
            db.setAutoCommit(false);
            for(Review review : reviews){
                String kundenName = review.getUser();
                if(!kundeAlreadyInDatabase(kundenName)){
                    addKunde(kundenName);
                }
                if(!produktAlreadyInDatabase(review.getProdID())){
                    Ablehner.ablehnen(review.toString(),"Produkt der Review nicht in der Datenbank");
                    continue;
                }

                if(rezensionAlreadyInDatabase(review.getUser(),review.getProdID())){
                    continue;
                }


                addRezension.setString(1,kundenName);
                addRezension.setString(2,review.getProdID());
                addRezension.setDouble(3,review.getRating());
                addRezension.setInt(4,review.getHelpful());
                addRezension.setDate(5, Date.valueOf(review.getDate()));
                addRezension.setString(6, review.getContent());

                if(review.getContent().length() > 10000) System.err.println("Kommentar zu lang: " + review.getContent().length());

                addRezension.executeUpdate();


            }

        } catch (Exception e){
            System.err.println("Konnte Rezensionen nicht in Datenbank hinzufügen");
            e.printStackTrace();
        } finally {
            db.setAutoCommit(true);
        }
    }
    private void addKunde(String kundeName) {
        try(
                PreparedStatement addKunde = db.prepareStatement(
                        "INSERT INTO kunde (name)" +
                                "VALUES (?)"
                )
        ){
            addKunde.setString(1,kundeName);
            addKunde.executeUpdate();
        } catch (SQLException e){
            System.err.println("Konnte Kunden nicht in Datenbank hinzufügen");
            e.printStackTrace();
        }
    }

    public void addSimilars(Map<String, String> similars) throws SQLException{
        try(
            PreparedStatement addProduktÄhnlich = db.prepareStatement(
                    "INSERT INTO produkt_ähnlich (ProdNr1,ProdNr2)" +
                        "VALUES (?,?)"
            )
        ){
            db.setAutoCommit(false);

            for(String prodnr1 : similars.keySet()){
                String prodnr2 = similars.get(prodnr1);

                //Falls eins der beiden Produkte nicht in der DB
                if( ! (produktAlreadyInDatabase(prodnr1) && produktAlreadyInDatabase(prodnr2) ) ){
                    Ablehner.ablehnen(prodnr1 + " ähnlich zu " + prodnr2, "Eins oder beide Produkte nicht in der Datenbank");
                    continue;
                }

                addProduktÄhnlich.setString(1,prodnr1);
                addProduktÄhnlich.setString(2,prodnr2);
                addProduktÄhnlich.executeUpdate();

            }

            db.commit();

        } catch (SQLException e) {
            System.err.println("Konnte Produkt_Ähnlich nicht in Datenbank hinzufügen");
            e.printStackTrace();
        } finally {
            db.setAutoCommit(true);
        }
    }

    public void clearDB() throws SQLException {
        try{
            db.setAutoCommit(false);

            clearRezensionDB();
            clearKundeDB();
            clearKategorieDB();
            clearFilialeDB();
            clearBuchDB();
            clearCDDB();
            clearDVDDB();
            clearProdukt();

            db.commit();

        } catch (Exception e) {
            System.err.println("Could not clear Database!");
            e.printStackTrace();
        } finally {
            db.setAutoCommit(true);
        }
    }

    public void clearKundeDB() {
        try(
                PreparedStatement clearBuch = db.prepareStatement(
                        "Delete from kunde_konto;" +
                                "Delete from kunde_adresse;" +
                                "Delete from kunde;"
                );
        ){
            clearBuch.executeUpdate();
        }catch (SQLException e) {
            System.err.println("Could not clear Kunde");
            throw new RuntimeException(e);
        }
    }

    public void clearRezensionDB() {
        try(
                PreparedStatement clearRezension = db.prepareStatement(
                        "Delete from rezension;"
                );
        ){
            clearRezension.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Could not clear Rezension");
            throw new RuntimeException(e);
        }
    }

    public void clearBuchDB() {
        try(
            PreparedStatement clearBuch = db.prepareStatement(
                    "Delete from buch_autor;" +
                        "Delete from buch_verlag;" +
                        "Delete from buch;"
            );
        ){
            clearBuch.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Could not clear Buch");
            throw new RuntimeException(e);
        }
    }

    public void clearCDDB() {
        try(
                PreparedStatement clearCD = db.prepareStatement(
                        "Delete from cd_künstler;" +
                            "Delete from cd_label;" +
                            "Delete from cd_werke;" +
                            "Delete from cd;"
                );
        ){
            clearCD.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Could not clear CD");
            throw new RuntimeException(e);
        }
    }

    public void clearDVDDB() {
        try(
            PreparedStatement clearDVD = db.prepareStatement(
                    "Delete from dvd_beteiligt;" +
                        "Delete from dvd_format;" +
                        "Delete from dvd;"
            );
        ){
            clearDVD.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Could not clear DVD");
            throw new RuntimeException(e);
        }
    }

    public void clearProdukt() {
        try(
            PreparedStatement clearProdukt = db.prepareStatement(
                    "Delete from produkt_kategorie;" + 
                        "Delete from produkt_ähnlich;" +
                        "Delete from produkt;"
            );
        ){
            clearProdukt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Could not clear Produkt");
            throw new RuntimeException(e);
        }
    }

    public void clearFilialeDB() {
        try(
            PreparedStatement clearFiliale = db.prepareStatement(
                    "Delete from filiale_angebot;" +
                        "Delete from filiale;"
            );
        ){
            clearFiliale.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Could not clear Filiale");
            throw new RuntimeException(e);
        }
    }

    public void clearKategorieDB() {
        try(
            PreparedStatement clearKategorie = db.prepareStatement(
                    "Delete from produkt_kategorie;" +
                        "Delete from kategorie_ordnung;" +
                        "Delete from kategorie;"
            );
        ){
            clearKategorie.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Could not clear Kategorie");
            throw new RuntimeException(e);
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
}
