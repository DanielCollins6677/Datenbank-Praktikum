package Reader;

import DataClasses.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Reader.XMLBuchReader.getBuchXML;
import static Reader.XMLCDReader.getCDXML;
import static Reader.XMLDVDReader.getDVDXML;

public class XMLReader {

    //Items die eine similar-relation zueinander haben. Id -> Id
    public static Map<String, String> similars = new HashMap<>();


    public static Filiale readFilialeXML(String filePath) throws ParserConfigurationException, IOException, SAXException {


        List<Produkt> produkte = new ArrayList<>();
        List<Buch> bücher = new ArrayList<>();
        List<CD> cds = new ArrayList<>();
        List<DVD> dvds = new ArrayList<>();

        File inputFile = new File(filePath);


        //get documentBuilder
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        //get the document
        Document doc = dBuilder.parse(inputFile);

        //normalize the xml structure
        doc.getDocumentElement().normalize();

        //get
        NodeList nodeList = doc.getElementsByTagName("shop");

        Filiale laden = new Filiale();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                laden =
                        new Filiale(
                                element.getAttribute("name"),
                                element.getAttribute("street"),
                                Integer.parseInt(element.getAttribute("zip")),
                                new HashMap<>());

                String ladenName = laden.getName();
                System.out.println("shop name: " + ladenName);

                NodeList items = element.getChildNodes();
                for (int j = 0; j < items.getLength(); j++) {
                    Node item = items.item(j);
                    if (item != null && item.getNodeType() == Node.ELEMENT_NODE) {
                        Element itemElement = (Element) item;
                        try {
                            Produkt produkt = getProduktXML(itemElement, laden);

                            produkte.add(produkt);
                            //Test booleans um zu prüfen, ob alle Produkte zugeordnet wurden
                            boolean e1 = false,e2 = false,e3 = false;
                            try {
                                Buch buch = (Buch) produkt;


                                if(buch.getAuthors().size() > 0 && buch.getVerlag() != null && buch.getSeitenZahl() != -1){
                                    bücher.add(buch);
                                } else if(buch.getAuthors().size() == 0){
                                    Ablehner.ablehnen(buch.getProdNr(),"Buch hat keine Authoren angegeben");
                                } else if (buch.getVerlag() == null){
                                    Ablehner.ablehnen(buch.getProdNr(),"Buch hat keinen Verlag angegeben");
                                } else if (buch.getSeitenZahl() == -1){
                                    Ablehner.ablehnen(buch.getProdNr(),"Buch hat ungültigen Wert für Seitenanzahl");
                                }else {
                                    throw new Exception("Fehler bei Buch Fallunterscheidung!");
                                }

                                //System.out.println(buch);
                            } catch (ClassCastException classCastException) {
                                e1 = true;
                                //System.out.println("kein Buch");
                            } catch (Exception exception){
                                exception.printStackTrace();
                            }

                            try {
                                CD cd = (CD) produkt;

                                if(cd.getTracks().size() > 0 && cd.getKünstler().size() > 0 && cd.getErscheinungsdatum() != null){
                                    cds.add(cd);
                                } else if (cd.getTracks().size() == 0){
                                    Ablehner.ablehnen(cd.getProdNr(),"0 Lieder in der CD");
                                } else if(cd.getKünstler().size() == 0){
                                    Ablehner.ablehnen(cd.getProdNr(),"0 Künstler für die CD");
                                } else if(cd.getErscheinungsdatum() == null){
                                    Ablehner.ablehnen(cd.getProdNr(),"CD hat Fehler beim Erscheinungsdatum");
                                } else{
                                    throw new Exception("Fehler bei CD Fallunterscheidung!");
                                }
                                //System.out.println(cd);

                            } catch (ClassCastException classCastException) {
                                e2 = true;
                                //System.out.println("keine CD");
                            } catch (Exception exception){
                                exception.printStackTrace();
                            }

                            try {
                                DVD dvd = (DVD) produkt;

                                if(!dvd.getFormat().equals("") && dvd.getLaufzeit() != -1 && dvd.getRegionCode() != -1){
                                    dvds.add(dvd);
                                } else if(dvd.getFormat().equals("")){
                                    Ablehner.ablehnen(dvd.getProdNr(),"DVD hat kein Format angegeben");
                                } else if(dvd.getLaufzeit() == -1){
                                    Ablehner.ablehnen(dvd.getProdNr(),"DVD hat ungültige Laufzeit angegeben");
                                } else if(dvd.getRegionCode() == -1){
                                    Ablehner.ablehnen(dvd.getProdNr(),"DVD hat ungültigen Regions code angegeben");
                                } else {
                                    throw new Exception("Fehler bei DVD Fallunterscheidung!");
                                }

                                //System.out.println(dvd);
                            } catch (ClassCastException classCastException) {
                                e3 = true;
                                //System.out.println("keine DVD");
                            }catch (Exception exception){
                                exception.printStackTrace();
                            }

                            if(e1 && e2 && e3){
                                System.out.println(produkt.getProdNr() + " Ist keins der drei Gruppen");
                            }
                        } catch (Exception e){

                        }
                    }
                }
            }
        }

        /*for(String key : abgelehnt.keySet()){
            System.out.println(abgelehnt.get(key));
        }*/


        //Überprüfe ids wiederverwendet werden
        List<String> wiederverwendeteIds = new ArrayList<>();

        for(Produkt produkt : bücher){
            if(!wiederverwendeteIds.contains(produkt.getProdNr())){
                wiederverwendeteIds.add(produkt.getProdNr());
            } else {
                Ablehner.ablehnen(produkt.getProdNr(),"Produkt verwendet vergebene ID");
            }
        }
        for(Produkt produkt : cds){
            if(!wiederverwendeteIds.contains(produkt.getProdNr())){
                wiederverwendeteIds.add(produkt.getProdNr());
            }else {
                Ablehner.ablehnen(produkt.getProdNr(),"Produkt verwendet vergebene ID");
            }
        }
        for(Produkt produkt : dvds){
            if(!wiederverwendeteIds.contains(produkt.getProdNr())){
                wiederverwendeteIds.add(produkt.getProdNr());
            }else {
                Ablehner.ablehnen(produkt.getProdNr(),"Produkt verwendet vergebene ID");
            }
        }

        //letzter Check der Integrität von den Produkten
        checkFalscheProdukte(laden);

        //Überprüfe, ob abgelehnte Produkte im Ergebnis sind
        List<Produkt> abgelehnteIds = new ArrayList<>();

        for(Produkt produkt : bücher){
            if(Ablehner.abgelehnt.containsKey(produkt.getProdNr())){
                abgelehnteIds.add(produkt);
            }
        }
        for(Produkt produkt : cds){
            if(Ablehner.abgelehnt.containsKey(produkt.getProdNr())){
                abgelehnteIds.add(produkt);
            }
        }
        for(Produkt produkt : dvds){
            if(Ablehner.abgelehnt.containsKey(produkt.getProdNr())){
                abgelehnteIds.add(produkt);
            }
        }

        bücher.removeAll(abgelehnteIds);
        cds.removeAll(abgelehnteIds);
        dvds.removeAll(abgelehnteIds);



        //Entferne falsche Produkte aus der produktPreis relation

        int produktAnz = produkte.size();

        produkte.removeAll(bücher);
        produkte.removeAll(cds);
        produkte.removeAll(dvds);


        int produktAnzNeu = produkte.size();


        for (Produkt fehlerhaftesProdukt : produkte){
            laden.getProduktPreis().remove(fehlerhaftesProdukt);
        }


        int counter = 0;
        for(Produkt produkt : laden.getProduktPreis().keySet()){
            if (laden.getProduktPreis().get(produkt).size() > 1 ){
                counter++;
            } else if (laden.getProduktPreis().get(produkt).size() == 0){
                //System.out.println(produkt);
            }
        }

        //Konsolen Ausgaben zum Prüfen der Anzahl der Produkte
        /*
        System.out.printf("%d Produkte vor Bereinigung, %d Produkte nach Bereinigung, %d Produkte gelöscht\n",produktAnz,produktAnzNeu, produktAnz - produktAnzNeu);
        System.out.println(cds.size() + dvds.size() + bücher.size() + " Anzahl von richtigen Produkten");
        System.out.println(counter + " Anzahl der Produkte mit mehreren Preisen");
        System.out.println(laden.getProduktPreis().size() - counter + " Differenz von Anzahl von sortierten Produkten mit Preisen und Anzahl der Produkte mit mehreren Preisen");
        */


        System.out.println("Laden konnte gelesen werden");

        return laden;
    }

    private static Produkt getProduktXML(Element item, Filiale laden) throws IllegalArgumentException{

        String id = item.getAttribute("asin");
        long salesRank = 0;
        //try to read salesRank, if it cant be read into an int we set it to -1
        try {
            if(!item.getAttribute("salesrank").equals("")) {
                salesRank = Long.parseLong(item.getAttribute("salesrank"));
            }
        }catch (Exception e){
            salesRank = -1;
        }


        NodeList itemDetails = item.getChildNodes();

        String classification = item.getAttribute("pgroup");

        if(classification.equals("Book") ){
            Buch buch = getBuchXML(itemDetails,laden,id,salesRank);
            if(item.hasAttribute("picture")){
                buch.setBild(item.getAttribute("picture"));
            }
            return buch;

        } else if(classification.equals("DVD")){
            DVD dvd = getDVDXML(itemDetails,laden,id,salesRank);
            if(item.hasAttribute("picture")){
                dvd.setBild(item.getAttribute("picture"));
            }
            return dvd;

        }else if(classification.equals("Music")){
            CD cd = getCDXML(itemDetails,laden,id,salesRank);
            if(item.hasAttribute("picture")){
                cd.setBild(item.getAttribute("picture"));
            }
            return cd;

        } else {
            //System.out.println(classification + " abgelehnt");
            Ablehner.ablehnen(id,"Falsche Produktgruppen Klassifizierung, \'" + classification + "\' ist keine gültige Produktgruppe");
            throw new IllegalArgumentException();
        }
    }

    protected static void readTitle(Produkt result, Element detailElement) {
        result.setTitel(detailElement.getTextContent());
    }

    protected static void readSimilarsXML(NodeList similarsSubNodes, String id) {
        for (int i = 0; i < similarsSubNodes.getLength(); i++) {
            Node similarsNode = similarsSubNodes.item(i);
            if (similarsNode != null && similarsNode.getNodeType() == Node.ELEMENT_NODE) {
                Element similarsElement = (Element) similarsNode;

                //in Leipzig-Transformed werden die ähnlichen Produkte anders angegeben
                if(similarsElement.getTagName().equals("sim_product")){

                    NodeList sim_productSubNodes = similarsElement.getChildNodes();
                    for (int j = 0; j < sim_productSubNodes.getLength(); j++) {

                        Node sim_productNode = sim_productSubNodes.item(i);
                        if (sim_productNode != null && sim_productNode.getNodeType() == Node.ELEMENT_NODE) {

                            Element sim_productElement = (Element) sim_productNode;

                            if(sim_productElement.getTagName().equals("asin")){
                                similars.put(id,sim_productElement.getTextContent());
                            }
                        }
                    }
                } else {
                    similars.put(id,similarsElement.getAttribute("asin"));
                }
            }
        }
    }

    protected static void readPriceXML(Filiale laden, Produkt result, Element detailElement) {
        if(laden.getProduktPreis().get(result) == null){
            laden.getProduktPreis().put(result,new ArrayList<>());
        }
        try {
            laden.getProduktPreis().get(result).add(readPriceZustandXML(detailElement));
        } catch (IllegalArgumentException e){
            Ablehner.ablehnen(result.getProdNr(), "Ungültige Währung angegeben");
        }
    }

    protected static PreisZustand readPriceZustandXML(Element itemElement) throws IllegalArgumentException{
        PreisZustand result = new PreisZustand();
        result.setZustand(itemElement.getAttribute("state"));
        double multiplier = 0;
        try {
            multiplier = Double.parseDouble(itemElement.getAttribute("mult"));
        }catch (Exception e){
            multiplier = -1;
        }

        String währung = itemElement.getAttribute("currency");
        if(!währung.equals("") && !währung.equals("EUR"))  {
            throw new IllegalArgumentException();

            //System.out.println(währung + " Währung");
        }

        try{
            double höhe = Double.parseDouble(itemElement.getTextContent());
            if(währung.equals("EUR")){
                result.setPreis(höhe);
            }
        } catch(Exception e){
            if(!itemElement.getTextContent().equals("")) System.out.println(itemElement.getTextContent() + " Preisfehler");
            result.setPreis(-1);
        }

        if(result.getPreis() != -1 && multiplier != -1){
            //verrechne den multiplier
            double temp = result.getPreis() * multiplier;
            //Runde auf 2. Nachkommastelle
            temp  = Math.round(temp*100.0)/100.0;
            result.setPreis(temp);
        }
        return result;
    }

    private static void checkFalscheProdukte(Filiale f2) {
        System.out.printf("name: %s, straße: %s, plz: %s\n",f2.getName(),f2.getStraße(),f2.getPlz());
        for (Produkt i: f2.getProduktPreis().keySet()){
            if(i.getProdNr() == null || i.getProdNr().equals("")){
                i.setProdNr("");
                Ablehner.ablehnen("","Keine ProduktId angegeben");
                //System.out.println(i + "\n");
            }
            if (i.getTitel() == null){
                Ablehner.ablehnen(i.getProdNr(),"Produkt hat keinen Titel");
                //System.out.println(i + "\n");
            }
            //Rating wird im nachher berechnet
            /*if(i.getRating() == -1){
                System.out.println(i + "\n");
            }*/
            if(i.getVerkaufsRank() == -1){
                Ablehner.ablehnen(i.getProdNr(),"Verkaufsrang ungültig");
                //System.out.println(i + "\n");
            }

            if(i instanceof  Buch){

                Buch buch = (Buch) i;

                if(buch.getVerlag().isEmpty()){
                    Ablehner.ablehnen(i.getProdNr(),"Buch hat keinen Verlag");
                    //System.out.println(buch + "\n");
                }
                if(buch.getAuthors().isEmpty()){
                    Ablehner.ablehnen(i.getProdNr(),"Buch hat keine Autoren");
                    //System.out.println(buch + "\n");
                }
                if(buch.getIsbn().equals("") || buch.getIsbn().equals("-1")){
                    Ablehner.ablehnen(i.getProdNr(),"Buch hat keine oder eine ungültige ISBN");
                    //System.out.println(buch + "\n");
                }
                if(buch.getSeitenZahl() == 0 || buch.getSeitenZahl() == -1){
                    Ablehner.ablehnen(i.getProdNr(),"Buch hat keine oder eine ungültige Seitenanzahl");
                    //System.out.println(buch + "\n");
                }
                if(buch.getErscheinungsJahr() == null || buch.getErscheinungsJahr().isAfter(LocalDate.now())){
                    Ablehner.ablehnen(i.getProdNr(),"Buch hat kein oder ein ungültiges Erscheinungsdatum");
                    //System.out.println(buch + "\n");
                }

            }else if(i instanceof  CD){

                CD cd = (CD) i;

                if(cd.getKünstler().isEmpty()){
                    Ablehner.ablehnen(i.getProdNr(),"CD hat keine Künstler");
                    //System.out.println(cd + "\n");
                }
                if(cd.getLabels().isEmpty()){
                    Ablehner.ablehnen(i.getProdNr(),"CD hat kein Label");
                    //System.out.println(cd + "\n");
                }
                if(cd.getTracks().isEmpty()){
                    Ablehner.ablehnen(i.getProdNr(),"CD hat keine Lieder");
                    //System.out.println(cd + "\n");
                }
                //Kein Erscheinungsdatum oder ein Datum in der Zukunft
                if(cd.getErscheinungsdatum() == null || cd.getErscheinungsdatum().isAfter(LocalDate.now())){
                    Ablehner.ablehnen(i.getProdNr(),"CD hat kein oder ein ungültiges Erscheinungsdatum");
                    //System.out.println(cd + "\n");
                }

            }else if(i instanceof  DVD){

                DVD dvd = (DVD) i;

                if(dvd.getDvdBeteiligte().isEmpty()){
                    Ablehner.ablehnen(i.getProdNr(),"DVD hat keine Beteiligten");
                    //System.out.println(dvd + "\n");
                }
                if(dvd.getLaufzeit() == 0 || dvd.getLaufzeit() < 0){
                    Ablehner.ablehnen(i.getProdNr(),"DVD hat eine ungültige Laufzeit");
                    //System.out.println(dvd + "\n");
                }
                if(dvd.getFormat() == null || dvd.getFormat().equals("")){
                    Ablehner.ablehnen(i.getProdNr(),"DVD hat ein ungültiges Format");
                    //System.out.println(dvd + "\n");
                }
                if(dvd.getRegionCode() < 0){
                    Ablehner.ablehnen(i.getProdNr(),"DVD hat ungültigen Region code");
                    //System.out.println(dvd + "\n");
                }
            }
            //System.out.println(f2.getProduktPreis().get(i));
        }
    }

}
