package Reader;

import DataClasses.Review;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static Reader.SimpleReader.readFile;

public class ReviewReader {
    public static List<Review> readReviews(File file) {
        List<String> reviewsString = readFile(file);
        List<Review> result = new ArrayList<>();

        for(int i = 1; i < reviewsString.size();i++){
            String reviewZeile = reviewsString.get(i);
            //System.out.println(reviewZeile);

            //Wir teilen den String am \" und bekommen so immer 14 Teile
            //In diesen 14 Teilen sind die Informationen in 2,4,6,8,10,14
            //Die Informationsreihenfolge ist:
            //"product","rating","helpful","reviewdate","user","summary","content"
            String[] reviewTeile = reviewZeile.split("\"");
            String prodID = reviewTeile[1];
            short rating = Short.parseShort(reviewTeile[3]);
            int helpful = Integer.parseInt(reviewTeile[5]);

            LocalDate date = null;
            try {
                String datum = reviewTeile[7];
                date = LocalDate.parse(datum);

            } catch (Exception e){
            }

            String userName = reviewTeile[9];
            String summary = reviewTeile[11];
            String content = reviewTeile[13];

            Review review = new Review(prodID,rating,helpful,date,userName,summary,content);

            result.add(review);
        }

        return result;
    }
}
