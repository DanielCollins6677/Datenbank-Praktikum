package com.example.frontend;
import com.example.datentypen.AngebotList;
import com.example.datentypen.Category;
import com.example.datentypen.ProduktData;
import com.example.entities.FilialeAngebot;
import com.example.entities.Kunde;
import com.example.entities.Rezension;
import com.example.repositories.FilialeAngebotRep;
import com.example.services.Logic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@org.springframework.web.bind.annotation.RestController
@CrossOrigin(origins = "http://localhost:3000")
public class RestController {

    @Autowired
    Logic logic;

    @Autowired
    FilialeAngebotRep filialeAngebotRep;

    @RequestMapping(value = "/getProduct", method = RequestMethod.GET)
    public ProduktData getProdukt(@RequestParam(value = "prodnr") String prodnr) {
        return logic.getProdukt(prodnr);
    }

    @RequestMapping(value = "/getProducts", method = RequestMethod.GET)
    public List<ProduktData> getProdukte(@RequestParam (value = "pattern") String pattern){
        return logic.getProdukts(pattern);
    }

    @RequestMapping(value = "/getCategoryTree", method = RequestMethod.GET)
    public Category getCategoryTree(){
        return logic.getCategoryTree(true);
    }

    @RequestMapping(value = "/getProductsByCategoryPath", method = RequestMethod.GET)
    public List<ProduktData> getProductsByCategoryPath(@RequestParam (value = "path") String path){
        return logic.getProductsByCategoryPath(path);
    }

    @RequestMapping(value = "/getTopProducts", method = RequestMethod.GET)
    public List<ProduktData> getTopProducts(@RequestParam (value = "limit") int limit){
        return logic.getTopProducts(limit);
    }

    @RequestMapping(value = "/getSimilarCheaperProducts", method = RequestMethod.GET)
    public List<ProduktData> getSimilarCheaperProduct(@RequestParam (value = "prodnr") String prodnr){
        return logic.getSimilarCheaperProduct(prodnr);
    }

    @RequestMapping(value = "/addNewReview", method = RequestMethod.GET)
    public String addNewReview(@RequestParam(value = "kname") String kname,
                                      @RequestParam(value = "prodnr") String prodnr,
                                      @RequestParam(value = "rating") int rating,
                                      @RequestParam(value = "kommentar") String kommentar){
        return logic.addNewReview(kname, prodnr, rating, kommentar);
    }

    @RequestMapping(value ="/getReviews", method = RequestMethod.GET)
    public List<Rezension> getReviews(@RequestParam(value = "prodnr") String prodnr){
        return logic.getReviews(prodnr);
    }

    @RequestMapping(value = "/getTrolls", method = RequestMethod.GET)
    public List<Kunde> getTrolls(@RequestParam(value = "limit") double limit){
        return logic.getTrolls(limit);
    }

    @RequestMapping(value = "/getOffers", method = RequestMethod.GET)
    public AngebotList getOffers(@RequestParam (value = "prodnr") String prodnr){
        return logic.getOffers(prodnr);
    }

}
