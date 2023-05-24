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

            PreparedStatement newBook = db.prepareStatement(
                    "INSERT INTO Buch (prodnr,seitenzahl,erscheinungsjahr,isbn) " +
                            "VALUES (?,?,?,?)"
            );


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            db.setAutoCommit(true);
        }
    }

}
