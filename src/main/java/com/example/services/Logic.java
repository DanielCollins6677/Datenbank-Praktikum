package com.example.services;

import com.example.datentypen.*;
import com.example.entities.*;
import com.example.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Watchable;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Service
public class Logic implements Requirements {

    @Autowired
    ProduktRep produktRep;

    @Autowired
    BuchRep buchRep;

    @Autowired
    BuchAutorRep buchAutorRep;

    @Autowired
    BuchVerlagRep buchVerlagRep;

    @Autowired
    CDRep cdRep;

    @Autowired
    CDKünstlerRep cdKünstlerRep;

    @Autowired
    CDLabelRep cdLabelRep;

    @Autowired
    CDWerkeRep cdWerkeRep;

    @Autowired
    DVDRep dvdRep;

    @Autowired
    DVDBeteiligtRep dvdBeteiligtRep;

    @Autowired
    DVDFormatRep dvdFormatRep;

    @Autowired
    KategorieRep kategorieRep;

    @Autowired
    KategorieOrdnungRep kategorieOrdnungRep;

    @Autowired
    ProduktKategorieRep produktKategorieRep;

    @Autowired
    ProduktSimilarRep produktSimilarRep;

    @Autowired
    FilialeAngebotRep filialeAngebotRep;

    @Autowired
    RezensionRep rezensionRep;

    @Autowired
    KundeRep kundeRep;

    @Override
    public void init() {

    }

    @Override
    public void finish() {

    }

    @Override
    public ProduktData getProdukt(String prodnr) {
        Optional<Produkt> produkt = produktRep.findById(prodnr);
        if(produkt.isEmpty()) return null;
        return produktToData(produkt.get());
    }

    private List<ProduktData> produktListToProduktDataList(List<Produkt> produktList){
        List<ProduktData> res = new ArrayList<>();
        for (Produkt i : produktList) res.add(produktToData(i));
        return res;
    }

    private ProduktData produktToData(Produkt produkt){
        Optional<Buch> buchOptional = buchRep.findById(produkt.getProdnr());
        if(buchOptional.isPresent()){
            return buchToData(produkt,buchOptional.get());
        }
        Optional<CD> cdOptional = cdRep.findById(produkt.getProdnr());
        if(cdOptional.isPresent()){
            return cdToData(produkt,cdOptional.get());
        }
        Optional<DVD> dvdOptional = dvdRep.findById(produkt.getProdnr());
        if(dvdOptional.isPresent()){
            return dvdToData(produkt,dvdOptional.get());
        }

        return null;
    }

    private ProduktData buchToData(Produkt produkt, Buch buch) {
        List<BuchAutor> buchAutoren = buchAutorRep.findAllByProdnr(buch.getProdnr());
        List<String> autoren  = new ArrayList<>();
        for(BuchAutor i : buchAutoren){
            autoren.add(i.getAutor());
        }
        List<BuchVerlag> buchVerlage = buchVerlagRep.findAllByProdnr(buch.getProdnr());
        List<String> verlage = new ArrayList<>();
        for(BuchVerlag i : buchVerlage){
            verlage.add(i.getVerlag());
        }

        return new BuchData(produkt.getProdnr(), produkt.getTitel(), produkt.getRating(), produkt.getRang(), produkt.getBild(),
                buch.getSeitenzahl(), buch.getErscheinungsjahr(), buch.getIsbn(), autoren, verlage);
    }

    private ProduktData cdToData(Produkt produkt, CD cd) {
        List<CDKünstler> cdKünstlers = cdKünstlerRep.findAllByProdnr(cd.getProdnr());
        List<String> künster = new ArrayList<>();
        for(CDKünstler i : cdKünstlers){
            künster.add(i.getName());
        }
        List<CDLabel> cdLabels = cdLabelRep.findAllByProdnr(cd.getProdnr());
        List<String> label = new ArrayList<>();
        for(CDKünstler i : cdKünstlers){
            künster.add(i.getName());
        }
        List<CDWerke> cdWerke = cdWerkeRep.findAllByProdnr(cd.getProdnr());
        List<String> werke = new ArrayList<>();
        for(CDKünstler i : cdKünstlers){
            künster.add(i.getName());
        }



        return new CDData(produkt.getProdnr(), produkt.getTitel(), produkt.getRating(), produkt.getRang(), produkt.getBild(),
                cd.getErscheinungsjahr(),künster,label,werke);
    }

    private ProduktData dvdToData(Produkt produkt,DVD dvd) {
        List<DVDBeteiligt> dvdBeteiligte = dvdBeteiligtRep.findAllByProdnr(dvd.getProdnr());
        List<Beteiligt> beteiligte = new ArrayList<>();
        for(DVDBeteiligt i : dvdBeteiligte){
            beteiligte.add(new Beteiligt(i.getName(),i.getTitel()));
        }
        List<DVDFormat> dvdFormate = dvdFormatRep.findAllByProdnr(dvd.getProdnr());
        List<String> formate = new ArrayList<>();
        for(DVDFormat i: dvdFormate){
            formate.add(i.getFormat());
        }
        return new DVDData(produkt.getProdnr(), produkt.getTitel(), produkt.getRating(), produkt.getRang(), produkt.getBild(),
                dvd.getLaufzeit(),dvd.getRegioncode(),beteiligte,formate);
    }

    @Override
    public List<ProduktData> getProdukts(String pattern) {
        List<Produkt> produkts = new ArrayList<>();

        if(pattern == null || pattern.isEmpty()) {
            produkts = produktRep.findAll();
        } else {
            produkts = produktRep.findAllByProdnrLike(pattern);
        }

       return produktListToProduktDataList(produkts);
    }

    @Override
    public Category getCategoryTree(boolean extern) {
        Map<String,Category> bearbeiteteKategorien = new HashMap<>();
        Category wurzel = new Category("Wurzel");
        List<Category> wurzelKategorien = new ArrayList<>();

        List<Kategorie> alleKategorien = kategorieRep.findAll();
        List<KategorieOrdnung> alleKategorieOrdnungen = kategorieOrdnungRep.findAll();

        //Entfernt alle Unterkategorien
        for(KategorieOrdnung i : alleKategorieOrdnungen){
            alleKategorien.remove(i.getUnterkategorie());
        }

        for(Kategorie i : alleKategorien){
            wurzelKategorien.add(new Category(i.getName()));
        }

        wurzel.setUnterkategorien(wurzelKategorien);

        alleKategorien.clear();
        alleKategorieOrdnungen.clear();

        Stack<Category> arbeitsStack = new Stack<>();
        for(Category i : wurzel.getUnterkategorien()) {
            findeUnterkategorien(i, bearbeiteteKategorien,arbeitsStack, extern);
        }
        return wurzel;
    }

    private void findeUnterkategorien(Category kategorie, Map<String,Category> bearbeiteteKategorien,Stack<Category> arbeitsStack, boolean extern){

        List<KategorieOrdnung> kategorieOrdnungList = kategorieOrdnungRep.findAllByOberkategorie(kategorie.getName());
        List<Category> unterkategorien = new ArrayList<>();
        for(KategorieOrdnung i : kategorieOrdnungList){
            if(bearbeiteteKategorien.containsKey(i.getUnterkategorie())){
                unterkategorien.add(bearbeiteteKategorien.get(i.getUnterkategorie()));
            } else {
                // Falls der Tree ausgegeben werden soll, brauchen wir einen endlichen Baum.
                // In diesem Fall fügen wir bei wiederholung EndNodes ohne Unterkategorien hinzu
                // Falls der Tree intern verwendet wird können wir die Kategorien mit Unterkategorien wiederverwenden

                Category neueUnterkategorieFullData = new Category(i.getUnterkategorie());
                unterkategorien.add(neueUnterkategorieFullData);
                arbeitsStack.push(neueUnterkategorieFullData);

                if(extern){
                    Category neueUnterkategorieEndNode = new Category(i.getUnterkategorie());
                    bearbeiteteKategorien.put(i.getUnterkategorie(),neueUnterkategorieEndNode);
                } else {
                    bearbeiteteKategorien.put(i.getUnterkategorie(),neueUnterkategorieFullData);
                }
            }
        }

        kategorie.setUnterkategorien(unterkategorien);

        if(!arbeitsStack.isEmpty()){
            findeUnterkategorien(arbeitsStack.pop(),bearbeiteteKategorien,arbeitsStack, extern);
        }
    }

    @Override
    public List<ProduktData> getProductsByCategoryPath(String path) {
        System.out.println("Suche nach Produkt entlang Pfad: " + path);
        String [] pfadElemente = path.split("/");
        Category wurzel = getCategoryTree(false);
        Category searchCategory = wurzel;
        for(String i : pfadElemente) {
            System.out.println("Suche bei: " + i);
            Category tempCategory = null;
            if(searchCategory == null) return null;

            for (Category unterkategorie : searchCategory.getUnterkategorien()){
                if(unterkategorie.getName().equals(i)) {
                    tempCategory = unterkategorie;
                    break;
                }
            }
            searchCategory = tempCategory;
            System.out.println(searchCategory.getName());
            System.out.println(searchCategory.getUnterkategorien());
        }

        List<ProduktKategorie> pfadProdukte = produktKategorieRep.findAllByKategorieName(searchCategory.getName());
        List<Produkt> produkte = new ArrayList<>();
        for(ProduktKategorie i : pfadProdukte) produkte.add(produktRep.findById(i.getProdnr()).get());
        System.out.println(produkte);
        return produktListToProduktDataList(produkte);
    }

    @Override
    public List<ProduktData> getTopProducts(int limit) {
        List<Produkt> topProdukte = produktRep.findByOrderByRatingDesc();
        return produktListToProduktDataList(topProdukte.subList(0,limit));
    }

    @Override
    public List<ProduktData> getSimilarCheaperProduct(String prodnr) {

        List<FilialeAngebot> produktAngebote = filialeAngebotRep.findAllByProdnr(prodnr);
        List<Double> preise = new ArrayList<>();
        produktAngebote.stream().map((p) -> preise.add(p.getPreis()));

        double vergleichPreis = 0;
        for (Double i : preise){
            if(i > vergleichPreis) vergleichPreis = i;
        }


        List<ProduktSimilar> similarProdukte1 = produktSimilarRep.findAllByProdnr1(prodnr);
        List<ProduktSimilar> similarProdukte2 = produktSimilarRep.findAllByProdnr2(prodnr);

        List<Produkt> similarCheaperProdukte = new ArrayList<>();

        for(ProduktSimilar i : similarProdukte1){
            List<FilialeAngebot> tempAngebote = filialeAngebotRep.findAllByProdnr(i.getProdnr2());
            for(FilialeAngebot j : tempAngebote){
                if (j.getPreis() != -1 && j.getPreis() < vergleichPreis) similarCheaperProdukte.add(produktRep.findById(j.getProdnr()).get());
            }

        }
        for(ProduktSimilar i : similarProdukte2){
            List<FilialeAngebot> tempAngebote = filialeAngebotRep.findAllByProdnr(i.getProdnr1());
            for(FilialeAngebot j : tempAngebote){
                if (j.getPreis() < vergleichPreis) similarCheaperProdukte.add(produktRep.findById(j.getProdnr()).get());
            }
        }

        return produktListToProduktDataList(similarCheaperProdukte);
    }

    @Override
    public void addNewReview(String kname, String prodnr, int rating, String kommentar) {
        rezensionRep.save(new Rezension(kname, prodnr, rating, 0, Date.valueOf(LocalDate.now()), kommentar));
    }

    @Override
    public List<Kunde> getTrolls(double grenze) {
        List<Rezension> rezensionen = rezensionRep.findAll();
        Map<String, Integer> kundeMaxBewertung = new HashMap<>();
        List<Kunde> res = new ArrayList<>();


        for (Rezension i : rezensionen){
            if(!kundeMaxBewertung.containsKey(i.getKundenName())){
                kundeMaxBewertung.put(i.getKundenName(),i.getRating());
            }
            else if(kundeMaxBewertung.containsKey(i.getKundenName()) && kundeMaxBewertung.get(i.getKundenName()) < i.getRating()){
                kundeMaxBewertung.put(i.getKundenName(),i.getRating());
            }
        }

        for(String kunde : kundeMaxBewertung.keySet()){
            if(kundeMaxBewertung.get(kunde) <= grenze){
                res.add(new Kunde(kunde));
            }
        }

        return res;
    }

    @Override
    public List<FilialeAngebot> getOffers(String prodnr) {
        return filialeAngebotRep.findAllByProdnr(prodnr);
    }
}
