package Reader;

import DataClasses.*;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import static Reader.XMLReader.readSimilarsXML;
import static Reader.XMLReader.readTitle;

public class XMLDVDReader {
    protected static DVD getDVDXML(NodeList dvdDetails, Filiale laden, String id, long salesRank) {
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
                        XMLReader.readPriceXML(laden, result, detailElement);
                        break;

                    case "details":
                        result.setBild(detailElement.getAttribute("img"));
                        break;

                    case "similars":
                        NodeList similarsSublist = detailElement.getChildNodes();
                        readSimilarsXML(similarsSublist,id);
                        break;

                    case "creators":
                    case "actors":
                    case "directors":
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

                    case "actor":
                        String nameActor = "";
                        if(creatorsElement.hasAttribute("name") && !creatorsElement.getAttribute("name").equals("")) {
                            nameActor = creatorsElement.getAttribute("name");
                        } else if(!creatorsElement.getTextContent().equals("")){
                            nameActor = creatorsElement.getTextContent();
                        }

                        if(!nameActor.equals("")){
                            result.getDvdBeteiligte().add(new DVDBeteiligt(nameActor, DVDBeteiligtenTitel.Actor));
                        }
                        break;


                    case "creator":
                        String nameCreator = "";
                        if(creatorsElement.hasAttribute("name") && !creatorsElement.getAttribute("name").equals("")) {
                            nameCreator = creatorsElement.getAttribute("name");
                        } else if(!creatorsElement.getTextContent().equals("")){
                            nameCreator = creatorsElement.getTextContent();
                        }

                        if(!nameCreator.equals("")){
                            result.getDvdBeteiligte().add(new DVDBeteiligt(nameCreator, DVDBeteiligtenTitel.Creator));
                        }
                        break;


                    case "director":
                        String nameDirector = "";
                        if(creatorsElement.hasAttribute("name") && !creatorsElement.getAttribute("name").equals("")) {
                            nameDirector = creatorsElement.getAttribute("name");
                        } else if(!creatorsElement.getTextContent().equals("")){
                            nameDirector = creatorsElement.getTextContent();
                        }
                        if(!nameDirector.equals("")){
                            result.getDvdBeteiligte().add(new DVDBeteiligt(nameDirector, DVDBeteiligtenTitel.Director));
                        }

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
                        String formatTextContext = dvdspecElement.getTextContent();
                        if(formatTextContext.contains(",")){
                            String[] alleFormate = formatTextContext.split(",");
                            for(String format : alleFormate){
                                if(!format.equals("")){
                                    result.getFormat().add(format.trim());
                                }
                            }
                        }
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
}
