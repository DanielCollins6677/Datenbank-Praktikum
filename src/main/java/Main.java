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
            //categories = CategoryReader.readCategories(new File(categoriesPath));


            /*for(Category category : categories){
                *//*if(!(category.getName() == null) && category.getName().equals("TV-Produktionen unter 15 EUR")
                    || !(category.getParentCategory() == null) && category.getParentCategory().equals("TV-Produktionen unter 15 EUR"))*//*
                System.out.println(category);
            }*/

            //Lese die Filialen aus
            Filiale f1 = XMLReader.readFilialeXML(shopAndItemsPath);
            Filiale f2 = XMLReader.readFilialeXML(leipzigTransformed);


            /*System.out.printf("name: %s, straße: %s, plz: %s\n",f1.getName(),f1.getStraße(),f1.getPlz());
            for (Produkt i: f1.getProduktPreis().keySet()){
                System.out.println(i);
            }*/

            //Lese die Reviews aus
            reviewList = ReviewReader.readReviews(new File(reviews));

            ReviewLogik.setProductRatings(f1,reviewList);
            ReviewLogik.setProductRatings(f2,reviewList);


            /*for (Review review : reviewList) {
                System.out.println(review);
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }

        Database db = new Database("localhost",5432,"dbpraktikum","postgres","1234");

        //ReviewLogik.calculateRatingTest();



        /*try {
            db.addKategorien(categories);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/

    }
}
