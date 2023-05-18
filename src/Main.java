import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.*;

public class Main {

    //Items die eine similar-relation zueinander haben. Id -> Id
    public static Map<String, String> similars = new HashMap<>();
    //Items die nicht in die Datenbank aufgenommen werden und warum. Id -> Grund
    public static Map<String, String> abgelehnt =  new HashMap<>();

    public static void ablehnen(String id, String grund){
        if(abgelehnt.get(id) == null){
            abgelehnt.put(id,grund);
        } else {
            String grundNeu = abgelehnt.get(id) + "und " + grund;
        }
    }

    //Globale Listen aller Filialen
    public static List<Filiale> filialen = new ArrayList<>();

    public static List<String> readFile(File file) {
        List<String> res = new ArrayList<>();
        try (
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);
        ) {
            res.add(br.readLine());
            String temp;
            while (true) {
                temp = br.readLine();
                if (temp == null) {
                    break;
                }
                res.add(temp);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static List<Category> getCategories(List<String> input) {
        List<Category> res = new ArrayList<>();
        Category category = new Category();
        Stack<String> parentCategory = new Stack<>();
        parentCategory.push("");
        //int counter = 0;
        for (String i : input) {
            //counter++;
            while (!i.isBlank()) {
                //System.out.println(i);
                if (i.contains("<category>")) {
                    //does it have a parent Category?
                    if (!parentCategory.peek().equals("")) {
                        category.setParentCategory(parentCategory.peek());
                    }
                    //using a StringBuilder to remove the <category> from the String
                    StringBuilder sb = new StringBuilder(i);
                    sb.delete(0, 10);
                    //replace all &amp; with &
                    while (sb.indexOf("&amp;") != -1) {
                        sb.replace(sb.indexOf("&amp;"), sb.indexOf("&amp;") + 5, "&");
                    }
                    //set the name
                    int endOfName = sb.indexOf("<");
                    String name = "";
                    if (endOfName == -1) {
                        name = sb.toString();
                        sb.delete(0, sb.length());
                    } else {
                        name = sb.substring(0, endOfName);
                        sb.delete(0, endOfName);

                        //System.out.println(category);
                    }
                    category.setName(name);
                    parentCategory.push(name);
                    i = sb.toString();


                    //System.out.println(i);
                    //System.out.println(sb.indexOf("<"));
                } else if (i.contains("<item>")) {
                    StringBuilder sb = new StringBuilder(i);
                    sb.delete(0, 6);
                    int indexEndItem = sb.indexOf("</item>");
                    sb.delete(indexEndItem, indexEndItem + 7);
                    int endOfName = sb.indexOf("<");
                    String name = "";
                    if (endOfName == -1) {
                        name = sb.toString();
                        sb.delete(0, sb.length());
                    } else {
                        name = sb.substring(0, endOfName);
                        sb.delete(0, endOfName);
                    }

                    category.getItems().add(name);
                    i = sb.toString();
                    //System.out.println(category.getItems());
                    //System.out.println(i);
                } else if (i.contains("</category>")) {
                    //System.out.println(counter);
                    StringBuilder sb = new StringBuilder(i);
                    int indexCategory = sb.indexOf("</category>");
                    sb.delete(indexCategory, indexCategory + 11);
                    i = sb.toString();
                    res.add(category);
                    String nextCategoryName = parentCategory.pop();
                    category = new Category(parentCategory.peek(), nextCategoryName, new ArrayList<>());

                    //System.out.println(parentCategory.pop() + " " + counter);

                } else {
                    break;
                }
            }
        }
        return res;
    }

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {

        String basePath = new File("").getAbsolutePath();

        String dataPath = basePath + "\\src\\dbpraktikum-mediastore-master\\data";

        String categoriesPath = dataPath + "\\categories.xml";
        String shopAndItemsPath = dataPath + "\\dresden.xml";
        String leipzigTransformed = dataPath + "\\leipzig_transformed.xml";
        String reviews = dataPath + "\\reviews.csv";

        List<String> categoriesString = readFile(new File(categoriesPath));
        //List<String> shopAndItemsString = readFile(new File(shopAndItemsPath));
        //List<String> leipzigTransformedString = readFile(new File(leipzigTransformed));
        //List<String> reviewsString = readFile(new File(reviews));

        List<Category> categories = getCategories(categoriesString);



        try {
            readFilialeXML(shopAndItemsPath);
            readFilialeXML(leipzigTransformed);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println(abgelehnt);
    }

    private static Filiale readFilialeXML(String filePath) throws ParserConfigurationException, IOException, SAXException {


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
                                    ablehnen(buch.getProdNr(),"Buch hat keine Authoren angegeben");
                                } else if (buch.getVerlag() == null){
                                    ablehnen(buch.getProdNr(),"Buch hat keinen Verlag angegeben");
                                } else if (buch.getSeitenZahl() == -1){
                                    ablehnen(buch.getProdNr(),"Buch hat ungültigen Wert für Seitenanzahl");
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
                                    ablehnen(cd.getProdNr(),"0 Lieder in der CD");
                                } else if(cd.getKünstler().size() == 0){
                                    ablehnen(cd.getProdNr(),"0 Künstler für die CD");
                                } else if(cd.getErscheinungsdatum() == null){
                                    ablehnen(cd.getProdNr(),"CD hat Fehler beim Erscheinungsdatum");
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
                                    ablehnen(dvd.getProdNr(),"DVD hat kein Format angegeben");
                                } else if(dvd.getLaufzeit() == -1){
                                    ablehnen(dvd.getProdNr(),"DVD hat ungültige Laufzeit angegeben");
                                } else if(dvd.getRegionCode() == -1){
                                    ablehnen(dvd.getProdNr(),"DVD hat ungültigen Regions code angegeben");
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
        List<String> ids = new ArrayList<>();

        for(Produkt produkt : bücher){
            if(!ids.contains(produkt.getProdNr())){
                ids.add(produkt.getProdNr());
            } else {
                ablehnen(produkt.getProdNr(),"Produkt verwendet vergebene ID");
            }
        }
        for(Produkt produkt : cds){
            if(!ids.contains(produkt.getProdNr())){
                ids.add(produkt.getProdNr());
            }else {
                ablehnen(produkt.getProdNr(),"Produkt verwendet vergebene ID");
            }
        }
        for(Produkt produkt : dvds){
            if(!ids.contains(produkt.getProdNr())){
                ids.add(produkt.getProdNr());
            }else {
                ablehnen(produkt.getProdNr(),"Produkt verwendet vergebene ID");
            }
        }


        //Überprüfe, ob abgelehnte Produkte im Ergebnis sind
        List<Produkt> doppelteID = new ArrayList<>();
        for(Produkt produkt : bücher){
            if(abgelehnt.containsKey(produkt.getProdNr())){
                doppelteID.add(produkt);
            }
        }
        for(Produkt produkt : cds){
            if(abgelehnt.containsKey(produkt.getProdNr())){
                doppelteID.add(produkt);
            }
        }
        for(Produkt produkt : dvds){
            if(abgelehnt.containsKey(produkt.getProdNr())){
                doppelteID.add(produkt);
            }
        }

        bücher.removeAll(doppelteID);
        cds.removeAll(doppelteID);
        dvds.removeAll(doppelteID);



        //Entferne flasche Produkte aus der produktPreis relation

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
            salesRank = Long.parseLong(item.getAttribute("salesrank"));
        }catch (Exception e){
            salesRank = -1;
        }
        NodeList itemDetails = item.getChildNodes();

        String classification = item.getAttribute("pgroup");

        if(classification.equals("Book") ){
            return getBuchXML(itemDetails,laden,id,salesRank);
        } else if(classification.equals("DVD")){
            return getDVDXML(itemDetails,laden,id,salesRank);
        }else if(classification.equals("Music")){
            return getCDXML(itemDetails,laden,id,salesRank);
        } else {
            //System.out.println(classification + " abgelehnt");
            ablehnen(id,"Falsche Produktgruppen Klassifizierung, \'" + classification + "\' ist keine gültige Produktgruppe");
            throw new IllegalArgumentException();
        }



    }

    private static Buch getBuchXML(NodeList bookDetails, Filiale laden, String id, long salesRank) {
        Buch result =  new Buch();
        result.setProdNr(id);
        result.setVerkaufsRank(salesRank);


        for (int i = 0; i < bookDetails.getLength(); i++) {
            Node detail = bookDetails.item(i);
            if (detail != null && detail.getNodeType() == Node.ELEMENT_NODE) {
                Element detailElement = (Element) detail;
                switch(detailElement.getTagName()){
                    case "title":
                        readTitle(result,detailElement);
                        break;
                    case "price":
                        readPriceXML(laden,result,detailElement);
                        break;

                    case "details":
                        result.setBild(detailElement.getAttribute("img"));
                        break;

                    case "similars":
                        NodeList similarsSubNodes = detailElement.getChildNodes();
                        readSimilarsXML(similarsSubNodes,id);
                        break;

                    case "authors":

                        List<String> authors = new ArrayList<>();

                        NodeList authorSubNodes = detailElement.getChildNodes();
                        for (int j = 0; j < authorSubNodes.getLength(); j++) {
                            Node authorNode = authorSubNodes.item(j);
                            if (authorNode != null && authorNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element authorElement = (Element) authorNode;
                                authors.add(authorElement.getTextContent());
                            }
                        }

                        result.setAuthors(authors);

                        break;

                    case "publishers":

                        String publisher = "";

                        NodeList publisherSublist = detailElement.getChildNodes();
                        for (int j = 0; j < publisherSublist.getLength(); j++) {
                            Node publisherNode = publisherSublist.item(j);
                            if (publisherNode != null && publisherNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element publisherElement = (Element) publisherNode;
                                if (!publisher.equals("")) {
                                    publisher = "mehrere Publisher";
                                    break;
                                }
                                publisher = publisherElement.getTextContent();
                            }
                        }

                        result.setVerlag(publisher);

                        break;

                    case "bookspec":
                        NodeList bookspecSublist = detailElement.getChildNodes();
                        readBookspecXML(bookspecSublist,result);
                        break;

                    default:
                        //System.out.println(detailElement.getTagName());
                }
            }
        }

        return result;
    }

    private static void readBookspecXML(NodeList dvdspecSublist,Buch result) {

        for (int i = 0; i < dvdspecSublist.getLength(); i++) {
            Node boocspecNode = dvdspecSublist.item(i);
            if (boocspecNode != null && boocspecNode.getNodeType() == Node.ELEMENT_NODE) {
                Element bookspecElement = (Element) boocspecNode;

                switch (bookspecElement.getTagName()) {

                    case "isbn":

                        try {
                            result.setIsbn(Long.parseLong(bookspecElement.getAttribute("val")));
                        } catch (Exception e) {
                            result.setIsbn(-1);
                        }
                        break;

                    case "pages":

                        try {
                            result.setSeitenZahl(Integer.parseInt(bookspecElement.getTextContent()));
                        } catch (Exception e) {
                            result.setSeitenZahl(-1);
                        }
                        break;

                    case "publication":


                        result.setErscheinungsJahr(bookspecElement.getAttribute("date"));

                }
            }
        }

    }

    private static CD getCDXML(NodeList cdDetails, Filiale laden, String id, long salesRank ) {
        CD result = new CD();
        result.setProdNr(id);
        result.setVerkaufsRank(salesRank);

        for (int i = 0; i < cdDetails.getLength(); i++) {
            Node detail = cdDetails.item(i);
            if (detail != null && detail.getNodeType() == Node.ELEMENT_NODE) {
                Element detailElement = (Element) detail;
                switch (detailElement.getTagName()) {
                    case "title":
                        readTitle(result, detailElement);
                        break;
                    case "price":
                        readPriceXML(laden, result, detailElement);
                        break;

                    case "details":
                        result.setBild(detailElement.getAttribute("img"));
                        break;

                    case "similars":
                        NodeList similarsSubNodes = detailElement.getChildNodes();
                        readSimilarsXML(similarsSubNodes, id);
                        break;

                    case "artists":
                        NodeList artistsSubNodes = detailElement.getChildNodes();
                        readArtistsXML(artistsSubNodes,result);

                    case "labels":
                        NodeList labelsSubNodes = detailElement.getChildNodes();
                        readLabelsXML(labelsSubNodes,result);
                        break;

                    case "tracks":
                        NodeList tracksSubnodes = detailElement.getChildNodes();
                        readTracksXML(tracksSubnodes,result);
                        break;

                    case "musicspec":
                        NodeList musicspecsSubnode = detailElement.getChildNodes();
                        readMusicspecs(musicspecsSubnode,result);
                        break;

                    default:
                        //System.out.println(detailElement.getTagName());
                }
            }
        }

        /*if(result.getKünstler().size() == 0){
            result.getKünstler().add("Kein Künstler");
        }*/
        return result;
    }

    private static void readLabelsXML(NodeList labelsSubNodes, CD result) {
        for (int i = 0; i < labelsSubNodes.getLength(); i++) {
            Node detail = labelsSubNodes.item(i);
            if (detail != null && detail.getNodeType() == Node.ELEMENT_NODE) {
                Element detailElement = (Element) detail;
                if(result.getLabel() == null) {
                    result.setLabel(detailElement.getTextContent());
                }
            }
        }
    }

    private static void readMusicspecs(NodeList musicspecsSubnode, CD result) {
        for (int i = 0; i < musicspecsSubnode.getLength(); i++) {
            Node musicspecsNode = musicspecsSubnode.item(i);
            if (musicspecsNode != null && musicspecsNode.getNodeType() == Node.ELEMENT_NODE) {
                Element musicspecElement = (Element) musicspecsNode;

                switch (musicspecElement.getTagName()) {
                    case "releasedate":
                        try {
                            String datum = musicspecElement.getTextContent();
                            String[] datumElemente = datum.split("-");
                            int[] datumInts = new int[3];
                            for(int j = 0;j < 3; j++){
                                datumInts[j] = Integer.parseInt(datumElemente[j]);
                            }
                            result.setErscheinungsdatum(new Date(datumInts[0],datumInts[1],datumInts[2]));

                            //System.out.println(result.getErscheinungsdatum());
                        } catch (Exception e){
                            result.setErscheinungsdatum(null);
                        }
                        break;

                    default:

                }
            }
        }
    }

    private static void readTracksXML(NodeList tracksSubNodes, CD result) {
        for (int j = 0; j < tracksSubNodes.getLength(); j++) {
            Node trackNode = tracksSubNodes.item(j);
            if (trackNode != null && trackNode.getNodeType() == Node.ELEMENT_NODE) {
                Element tracksElement = (Element) trackNode;

                switch (tracksElement.getTagName()) {

                    case "title":
                        String track = tracksElement.getTextContent();
                        result.getTracks().add(track);
                        break;

                    default:

                }
            }
        }
    }

    private static void readArtistsXML(NodeList artistsSubNodes, CD result) {
        for (int j = 0; j < artistsSubNodes.getLength(); j++) {
            Node creatorsNode = artistsSubNodes.item(j);
            if (creatorsNode != null && creatorsNode.getNodeType() == Node.ELEMENT_NODE) {
                Element creatorsElement = (Element) creatorsNode;

                switch (creatorsElement.getTagName()) {

                    case "artist":
                        String name = creatorsElement.getTextContent();
                        result.getKünstler().add(name);
                        //System.out.println(result.getKünstler().size() + " Künstler");
                        break;

                    default:

                }
            }
        }
    }

    private static DVD getDVDXML(NodeList dvdDetails, Filiale laden, String id, long salesRank) {
        DVD result = new DVD();
        result.setProdNr(id);
        result.setVerkaufsRank(salesRank);

        for (int i = 0; i < dvdDetails.getLength(); i++) {
            Node detail = dvdDetails.item(i);
            if (detail != null && detail.getNodeType() == Node.ELEMENT_NODE) {
                Element detailElement = (Element) detail;
                switch (detailElement.getTagName()) {
                    case "title":
                        readTitle(result, detailElement);
                        break;

                    case "price":
                        readPriceXML(laden, result, detailElement);
                        break;

                    case "details":
                        result.setBild(detailElement.getAttribute("img"));
                        break;

                    case "similars":
                        NodeList similarsSublist = detailElement.getChildNodes();
                        readSimilarsXML(similarsSublist,id);
                        break;

                    case "creators":
                        NodeList creatorsSublist = detailElement.getChildNodes();
                        readCreatorsXML(creatorsSublist, result);
                        break;

                    case "dvdspec":
                        NodeList dvdspecSublist = detailElement.getChildNodes();
                        readDvdspecXML(dvdspecSublist,result);
                        break;

                    default:

                }
            }
        }
        return result;
    }

    private static void readCreatorsXML(NodeList creatorsSubNodes, DVD result) {
        for (int j = 0; j < creatorsSubNodes.getLength(); j++) {
            Node creatorsNode = creatorsSubNodes.item(j);
            if (creatorsNode != null && creatorsNode.getNodeType() == Node.ELEMENT_NODE) {
                Element creatorsElement = (Element) creatorsNode;

                switch (creatorsElement.getTagName()) {

                    case "actor": {
                        String name = creatorsElement.getTextContent();
                        result.getDvdBeteiligte().add(new DVDBeteiligt(name, DVDBeteiligtenTitel.Actor));
                        break;
                    }

                    case "creator": {
                        String name = creatorsElement.getTextContent();
                        result.getDvdBeteiligte().add(new DVDBeteiligt(name, DVDBeteiligtenTitel.Creator));
                        break;
                    }

                    case "director": {
                        String name = creatorsElement.getTextContent();
                        result.getDvdBeteiligte().add(new DVDBeteiligt(name, DVDBeteiligtenTitel.Director));
                        break;
                    }
                    default:

                }
            }
        }
    }

    private static void readDvdspecXML(NodeList dvdspecSublist, DVD result) {
        for (int i = 0; i < dvdspecSublist.getLength(); i++) {
            Node dvdspecNode = dvdspecSublist.item(i);
            if (dvdspecNode != null && dvdspecNode.getNodeType() == Node.ELEMENT_NODE) {
                Element dvdspecElement = (Element) dvdspecNode;

                switch (dvdspecElement.getTagName()) {
                    case "format":
                        result.setFormat(dvdspecElement.getTextContent());
                        break;

                    case "regioncode":
                        try {
                            result.setRegionCode(Integer.parseInt(dvdspecElement.getTextContent()));
                        } catch (Exception e){
                            result.setRegionCode(-1);
                        }
                        break;

                    case "runningtime":
                        try {
                            result.setLaufzeit(Integer.parseInt(dvdspecElement.getTextContent()));
                        } catch (Exception e){
                            result.setLaufzeit(-1);
                        }
                        break;

                    default:

                }
            }
        }
    }

    private static void readPriceXML(Filiale laden, Produkt result, Element detailElement) {
        if(laden.getProduktPreis().get(result) == null){
            laden.getProduktPreis().put(result,new ArrayList<>());
        }
        try {
            laden.getProduktPreis().get(result).add(readPriceZustandXML(detailElement));
        } catch (IllegalArgumentException e){
            ablehnen(result.getProdNr(), "Ungültige Währung angegeben");
        }
    }

    private static PreisZustand readPriceZustandXML(Element itemElement) throws IllegalArgumentException{
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
            result.setPreis(result.getPreis() * multiplier);
        }
        return result;
    }

    private static void readTitle(Produkt result, Element detailElement) {
        result.setTitel(detailElement.getTextContent());
    }

    private static void readSimilarsXML(NodeList similarsSubNodes, String id) {
        for (int j = 0; j < similarsSubNodes.getLength(); j++) {
            Node similarsNode = similarsSubNodes.item(j);
            if (similarsNode != null && similarsNode.getNodeType() == Node.ELEMENT_NODE) {
                Element similarsElement = (Element) similarsNode;
                similars.put(id,similarsElement.getAttribute("asin"));
            }
        }
    }
}
