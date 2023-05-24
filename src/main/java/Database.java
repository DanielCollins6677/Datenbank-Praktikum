import DataClasses.Buch;
import DataClasses.Produkt;

import java.sql.*;

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

    public void addProdukt(Produkt produkt) throws SQLException {
        try {
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
}
