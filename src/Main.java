import DataClasses.*;
import Reader.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.*;

public class Main {

    //Globale Listen aller Filialen
    public static List<Filiale> filialen = new ArrayList<>();

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {

        String basePath = new File("").getAbsolutePath();

        String dataPath = basePath + "\\src\\dbpraktikum-mediastore-master\\data";

        String categoriesPath = dataPath + "\\categories.xml";
        String shopAndItemsPath = dataPath + "\\dresden.xml";
        String leipzigTransformed = dataPath + "\\leipzig_transformed.xml";
        String reviews = dataPath + "\\reviews.csv";


        //lese die Kategorien aus
        List<Category> categories = CategoryReader.getCategories(new File(categoriesPath));


        //Lese die Filialen aus
        try {
            Filiale f1 = XMLReader.readFilialeXML(shopAndItemsPath);
            Filiale f2 = XMLReader.readFilialeXML(leipzigTransformed);


            /*System.out.printf("name: %s, straße: %s, plz: %s\n",f1.getName(),f1.getStraße(),f1.getPlz());
            for (Produkt i: f1.getProduktPreis().keySet()){
                if(i instanceof DVD){
                    System.out.println(i);
                    //System.out.println(f2.getProduktPreis().get(i));
                }
            }*/


        } catch (Exception e) {
            e.printStackTrace();
        }

        //Lese die Reviews aus
        List<String> reviewsString = SimpleReader.readFile(new File(reviews));
        List<Review> reviewList = ReviewReader.readReviews(reviewsString);
        /*for (DataClasses.Review review : reviewList) {
            System.out.println(review);
        }*/

        /*for(String key : abgelehnt.keySet()){
            System.out.println(key + " wurde abgelehnt wegen:");
            System.out.println(abgelehnt.get(key) + "\n");
        }*/


        //System.out.println(abgelehnt);
    }
}
