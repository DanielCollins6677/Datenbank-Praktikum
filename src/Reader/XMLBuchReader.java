package Reader;

import DataClasses.Ablehner;
import DataClasses.Buch;
import DataClasses.Filiale;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static Reader.XMLReader.*;

public class XMLBuchReader {
    protected static Buch getBuchXML(NodeList bookDetails, Filiale laden, String id, long salesRank) {
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

                                String author = "";

                                if(authorElement.hasAttribute("name") && !authorElement.getAttribute("name").equals("")){
                                    author = authorElement.getAttribute("name");
                                } else {
                                    author = authorElement.getTextContent();
                                }

                                if(!author.equals("")){
                                    authors.add(author);
                                } else {
                                    Ablehner.ablehnen(id,"Autoren Name ist Leer");
                                }
                            }
                        }

                        result.setAuthors(authors);

                        break;

                    case "publishers":

                        List<String> publishers = new ArrayList<>();

                        NodeList publisherSublist = detailElement.getChildNodes();
                        for (int j = 0; j < publisherSublist.getLength(); j++) {
                            Node publisherNode = publisherSublist.item(j);
                            if (publisherNode != null && publisherNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element publisherElement = (Element) publisherNode;
                                if(publisherElement.hasAttribute("name") && !publisherElement.getAttribute("name").equals("") ){
                                    publishers.add(publisherElement.getAttribute("name"));
                                } else if(!publisherElement.getTextContent().equals("")){
                                    publishers.add(publisherElement.getTextContent());
                                }
                            }
                        }

                        result.setVerlag(publishers);

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
                            long isbn = Long.parseLong(bookspecElement.getAttribute("val"));
                            result.setIsbn(String.valueOf(isbn));
                        } catch (Exception e) {
                            String errorISBN = bookspecElement.getAttribute("val");
                            //Manche ISBN enden mit X in diesem Fall ist es trotzdem eine gültige ISBN
                            if(errorISBN.endsWith("X")){

                                //Wir entfernen das X und versuchen es erneut in ein long umzuwandeln, fall erfolgreich ist es eine gültige ISBN
                                try{
                                    StringBuilder sb = new StringBuilder(errorISBN);
                                    sb.delete(sb.length() - 1, sb.length());
                                    Long.valueOf(sb.toString());
                                    result.setIsbn(errorISBN);
                                } catch (Exception keineZahl){
                                    result.setIsbn("-1");
                                }
                            } else {
                                result.setIsbn("-1");
                            }
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

                        try {
                            String datum = bookspecElement.getAttribute("date");
                            result.setErscheinungsJahr(LocalDate.parse(datum));

                        } catch (Exception e){
                            result.setErscheinungsJahr(null);
                        }

                        //System.out.println(result.getErscheinungsJahr() + " DataClasses.Buch Erscheinungsjahr");
                        break;

                    default:
                        //System.out.println(bookspecElement.getTagName());

                }
            }
        }

    }
}
