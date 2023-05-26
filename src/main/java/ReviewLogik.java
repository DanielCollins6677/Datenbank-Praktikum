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

        return ((double) tempRatingSum) / length;
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

}
