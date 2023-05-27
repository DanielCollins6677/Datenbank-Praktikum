import DataClasses.*;
import Reader.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class Main {

    //Globale Listen aller Filialen
    public static List<Filiale> filialen = new ArrayList<>();

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {

        String basePath = new File("").getAbsolutePath();

        String dataPath = basePath + "\\src\\main\\java\\dbpraktikum-mediastore-master\\data";

        String categoriesPath = dataPath + "\\categories.xml";
        String shopAndItemsPath = dataPath + "\\dresden.xml";
        String leipzigTransformed = dataPath + "\\leipzig_transformed.xml";
        String reviews = dataPath + "\\reviews.csv";

        List<Category> categories = new ArrayList<>();
        List<Review> reviewList = new ArrayList<>();
        try {
            //lese die Kategorien aus
            categories = XMLCategoryReader.readCategories(new File(categoriesPath));

            //Lese die Filialen aus
            Filiale f1 = XMLReader.readFilialeXML(shopAndItemsPath);
            Filiale f2 = XMLReader.readFilialeXML(leipzigTransformed);

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

        Database db = new Database("localhost",5432,"dbpraktikum","postgres","1234");

        //ReviewLogik.calculateRatingTest();



        try {
            //db.addKategorien(categories);
            db.addRezension(reviewList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
