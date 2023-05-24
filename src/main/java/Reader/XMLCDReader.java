package Reader;

import DataClasses.CD;
import DataClasses.Filiale;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.time.LocalDate;

public class XMLCDReader {
    protected static CD getCDXML(NodeList cdDetails, Filiale laden, String id, long salesRank ) {
        CD result = new CD();
        result.setProdNr(id);
        result.setVerkaufsRank(salesRank);

        for (int i = 0; i < cdDetails.getLength(); i++) {
            Node detail = cdDetails.item(i);
            if (detail != null && detail.getNodeType() == Node.ELEMENT_NODE) {
                Element detailElement = (Element) detail;
                switch (detailElement.getTagName()) {
                    case "title":
                        XMLReader.readTitle(result, detailElement);
                        break;
                    case "price":
                        XMLReader.readPriceXML(laden, result, detailElement);
                        break;

                    case "details":
                        result.setBild(detailElement.getAttribute("img"));
                        break;

                    case "similars":
                        NodeList similarsSubNodes = detailElement.getChildNodes();
                        XMLReader.readSimilarsXML(similarsSubNodes, id);
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
            result.getKünstler().add("Kein DataClasses.Künstler");
        }*/
        return result;
    }

    private static void readLabelsXML(NodeList labelsSubNodes, CD result) {
        for (int i = 0; i < labelsSubNodes.getLength(); i++) {
            Node detail = labelsSubNodes.item(i);
            if (detail != null && detail.getNodeType() == Node.ELEMENT_NODE) {
                Element detailElement = (Element) detail;
                if(detailElement.hasAttribute("name") && !detailElement.getAttribute("name").equals("")){
                    result.getLabels().add(detailElement.getAttribute("name"));
                }else if (!detailElement.getTextContent().equals("")) {
                    result.getLabels().add(detailElement.getTextContent());
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
                            result.setErscheinungsdatum(LocalDate.parse(datum));

                            //System.out.println(result.getErscheinungsdatum() + " DataClasses.CD Erscheinungsdatum");
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
                        String name = "";

                        if(creatorsElement.hasAttribute("name") && !creatorsElement.getAttribute("name").equals("")) {
                            name = creatorsElement.getAttribute("name");
                        } else if (!creatorsElement.getTextContent().equals("")){
                            name = creatorsElement.getTextContent();
                        }
                        if(!name.equals("")){
                            result.getKünstler().add(name);
                        }
                        //System.out.println(result.getKünstler().size() + " DataClasses.Künstler");
                        break;

                    default:

                }
            }
        }
    }
}
