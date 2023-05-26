import DataClasses.Buch;
import DataClasses.Category;
import DataClasses.Produkt;

import java.sql.*;
import java.util.Collections;
import java.util.List;

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
                System.out.println("Ein neues Buch!");
                addBuch((Buch) produkt);
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

        for(String autor : buch.getAuthors()){
            buchAutor.setString(2,autor);
            buchAutor.executeUpdate();
        }

        PreparedStatement buchVerlag = db.prepareStatement(
                "INSERT INTO buch_verlag (prodnr,verlag)" +
                        "VALUES (?,?)"
        );

        buchVerlag.setString(1, buch.getProdNr());

        for(String verlag : buch.getVerlag()){
            buchVerlag.setString(2,verlag);
            buchVerlag.executeUpdate();
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

        } catch (Exception e){
            System.err.println("Fehler beim Einlesen der Kategorien");
            e.printStackTrace();
        } finally {
            db.setAutoCommit(true);
        }

    }
}
