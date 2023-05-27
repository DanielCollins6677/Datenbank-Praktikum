import DataClasses.*;
import Reader.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class Main {

    //Globale Listen aller Filialen
    public static List<Filiale> filialen = new ArrayList<>();

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {

        String basePath = new File("").getAbsolutePath();

        String dataPath = basePath + "/src/main/java/dbpraktikum-mediastore-master/data";

        String categoriesPath = dataPath + "/categories.xml";
        String shopAndItemsPath = dataPath + "/dresden.xml";
        String leipzigTransformed = dataPath + "/leipzig_transformed.xml";
        String reviews = dataPath + "/reviews.csv";

        //dresden
        Filiale f1 = null;
        //leipzig
        Filiale f2 = null;

        List<Category> categories = new ArrayList<>();
        List<Review> reviewList = new ArrayList<>();
        try {
            //lese die Kategorien aus
            categories = XMLCategoryReader.readCategories(new File(categoriesPath));

            //Lese die Filialen aus
            f1 = XMLReader.readFilialeXML(shopAndItemsPath);
            f2 = XMLReader.readFilialeXML(leipzigTransformed);


            //System.out.printf("name: %s, straße: %s, plz: %s\n",f2.getName(),f2.getStraße(),f2.getPlz());
            /*for (Produkt i: f2.getProduktPreis().keySet()){
                if(i instanceof DVD){
                    if(((DVD) i).getFormat().size() > 1)
                    System.out.println(i);
                }
            }*/

            //Lese die Reviews aus
            reviewList = ReviewReader.readReviews(new File(reviews));

            ReviewLogik.removeFaultyReviews(reviewList);

            KategorieLogik.prüfeAufKategorie(f1,categories);
            KategorieLogik.prüfeAufKategorie(f2,categories);

            ReviewLogik.setProductRatings(f1,reviewList);
            ReviewLogik.setProductRatings(f2,reviewList);

            /*for (String prodnr : Ablehner.abgelehnt.keySet()){
                if(Ablehner.abgelehnt.get(prodnr).equals("Produkt hat keine zugehörige Kategorie")){
                    System.out.println(prodnr);
                }
            }*/



        } catch (Exception e) {
            e.printStackTrace();
        }

        /*for(String key : abgelehnt.keySet()){
            System.out.println(key + " wurde abgelehnt wegen:");
            System.out.println(abgelehnt.get(key) + "\n");
        }*/

        Database db = new Database("localhost",5432,"postgres","postgres","DBPraktikum#2023");
        db.clearDB();
        db.testAddFilialeDB();
/*
        Database db = new Database("localhost",5432,"dbpraktikum","postgres","1234");

        //ReviewLogik.calculateRatingTest();



        try {
            db.addFiliale(f1);
            db.addFiliale(f2);
        } catch (SQLException e) {
            //db.addKategorien(categories);
            db.addRezension(reviewList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


 */

    }


}
