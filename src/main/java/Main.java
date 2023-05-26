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

        try {
            //lese die Kategorien aus
            //List<Category> categories = CategoryReader.getCategories(new File(categoriesPath));


            /*for(Category category : categories){
                System.out.println(category);
            }*/

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
            //List<Review> reviewList = ReviewReader.readReviews(new File(reviews));

        /*for (DataClasses.Review review : reviewList) {
            System.out.println(review);
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
        try {
            db.addFiliale(f1);
            db.addFiliale(f2);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


 */
    }


}
