import DataClasses.Ablehner;
import DataClasses.Category;
import DataClasses.Filiale;
import DataClasses.Produkt;

import java.util.ArrayList;
import java.util.List;

public class KategorieLogik {

    public static void prüfeAufKategorie(Filiale filiale, List<Category> categories){
        List<Produkt> abgelehnteProdukte = new ArrayList<>();
        for (Produkt produkt : filiale.getProduktPreis().keySet()){
            String prodnr = produkt.getProdNr();
            boolean produktHatKategorie = false;

            for(Category category : categories){
                if(category.getItems().contains(prodnr)){
                    produktHatKategorie = true;
                    break;
                }
            }

            if(!produktHatKategorie){
                Ablehner.ablehnen(prodnr,"Produkt hat keine zugehörige Kategorie");
                abgelehnteProdukte.add(produkt);
            }
        }
        for(Produkt abgelehntesProdukt : abgelehnteProdukte){
            filiale.getProduktPreis().remove(abgelehntesProdukt);
        }
    }
}
