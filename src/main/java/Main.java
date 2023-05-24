import DataClasses.*;
import Reader.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.sql.SQLException;
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


        try {
            //lese die Kategorien aus
            List<Category> categories = CategoryReader.getCategories(new File(categoriesPath));


            for(Category category : categories){
                System.out.println(category);
            }

            //Lese die Filialen aus
            Filiale f1 = XMLReader.readFilialeXML(shopAndItemsPath);
            //Filiale f2 = XMLReader.readFilialeXML(leipzigTransformed);


           /* System.out.printf("name: %s, straße: %s, plz: %s\n",f1.getName(),f1.getStraße(),f1.getPlz());
            for (Produkt i: f1.getProduktPreis().keySet()){
                if(i instanceof DVD){
                    System.out.println(i);
                    //System.out.println(f2.getProduktPreis().get(i));
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

        /*Database db = new Database("localhost",5432,"dbpraktikum","postgres","1234");

        Buch test = new Buch();
        test.setProdNr("1");
        test.setTitel("test");
        test.setRating(4.45);
        test.setVerkaufsRank(1);
        test.setBild("");*/

        /*try {
            //db.addProdukt(test);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }*/
    }
}
