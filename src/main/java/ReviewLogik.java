import DataClasses.Ablehner;
import DataClasses.Filiale;
import DataClasses.Produkt;
import DataClasses.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewLogik {

    public static double calculateRating(List<Review> reviews,String prodnr){
        List<Short> productScores = new ArrayList<>();

        //Finde alle relevanten Reviews und füge sie einer Liste hinzu
        for(Review review : reviews){
            if(review.getProdID().equals(prodnr)){
                productScores.add(review.getRating());
            }
        }

        int length = productScores.size();
        if(length == 0) return 0.0;

        long tempRatingSum = 0;

        for(Short i : productScores){
            tempRatingSum += i;
        }

        double rating = ((double) tempRatingSum) / length;

        //rating auf 2. Nachkommastelle runden
        rating = ( (double) Math.round( rating*100) ) / 100;

        return rating;
    }

    public static void setProductRatings(Filiale filiale, List<Review> reviews){
        for(Produkt prod : filiale.getProduktPreis().keySet()){
            double prodRating = ReviewLogik.calculateRating(reviews, prod.getProdNr());
            if(prodRating < 0 || prodRating > 5){
                prod.setRating(-1);
            } else {
                prod.setRating(prodRating);
            }
        }
    }


    public static void calculateRatingTest(){
        List<Review> testReviews1 = new ArrayList<>();

        testReviews1.add(new Review("1", (short) 1,0,null,null,null,null));
        testReviews1.add(new Review("1", (short) 2,0,null,null,null,null));
        testReviews1.add(new Review("1", (short) 3,0,null,null,null,null));
        testReviews1.add(new Review("1", (short) 4,0,null,null,null,null));
        testReviews1.add(new Review("1", (short) 5,0,null,null,null,null));

        List<Review> testReviews2 = new ArrayList<>();

        testReviews2.add(new Review("1", (short) 1,0,null,null,null,null));
        testReviews2.add(new Review("1", (short) 2,0,null,null,null,null));
        testReviews2.add(new Review("1", (short) 3,0,null,null,null,null));
        testReviews2.add(new Review("1", (short) 4,0,null,null,null,null));
        testReviews2.add(new Review("1", (short) 4,0,null,null,null,null));
        testReviews2.add(new Review("2", (short) 1,0,null,null,null,null));
        testReviews2.add(new Review("2", (short) 2,0,null,null,null,null));
        testReviews2.add(new Review("2", (short) 3,0,null,null,null,null));
        testReviews2.add(new Review("2", (short) 4,0,null,null,null,null));
        testReviews2.add(new Review("2", (short) 5,0,null,null,null,null));

        System.out.println(ReviewLogik.calculateRating(testReviews1,"1"));
        System.out.println(ReviewLogik.calculateRating(testReviews1,"2"));
        System.out.println(ReviewLogik.calculateRating(testReviews2,"1"));
        System.out.println(ReviewLogik.calculateRating(testReviews2,"2"));
    }

    public static void removeFaultyReviews(List<Review> reviews){
        List<Review> abgelehnteReviews = new ArrayList<>();
        for (Review review : reviews){

            if(review.getHelpful() < 0) review.setHelpful(0);

            if(review.getUser() == null ){
                Ablehner.ablehnen(review.toString(),"Review hat keinen Benutzer");
                abgelehnteReviews.add(review);
                continue;
            }
            if(review.getProdID() == null){
                Ablehner.ablehnen(review.toString(),"Review hat kein Produkt");
                abgelehnteReviews.add(review);
            }
            if(review.getRating() == 0){
                Ablehner.ablehnen(review.toString(),"Review hat keine Bewertung");
                abgelehnteReviews.add(review);
            }
            if(review.getDate() == null){
                Ablehner.ablehnen(review.toString(),"Review hat kein Datum");
                abgelehnteReviews.add(review);
            }
        }

        for (Review abgelehnteReview : abgelehnteReviews){
            reviews.remove(abgelehnteReview);
        }

    }

}
