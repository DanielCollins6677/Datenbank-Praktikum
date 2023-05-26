package Reader;

import DataClasses.Category;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CategoryReader {

    public static List<Category> readCategories(File file) {
        List<Category> res = new ArrayList<>();
        try {
            NodeList nodeList = XMLReader.nodeListFromFile(file,"categories");
            Node categoriesNode = nodeList.item(0);

            NodeList categoriesSubNodeList = categoriesNode.getChildNodes();

            for (int i = 0; i < categoriesSubNodeList.getLength(); i++) {
                Node node = categoriesSubNodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String name = getCategoryName(element);
                    //System.out.println(name);

                    res.addAll(recursiveCategory(null,name,element.getChildNodes()));
                }
            }


        } catch (Exception e){
            System.err.println("Kategorien konnten nicht eingelesen werden");
            e.printStackTrace();
        }

        return res;
    }

    private static String getCategoryName(Element element) {
        String temp = element.getTextContent();
        int indexLineBreak = temp.length();
        if(temp.contains("\n")) {
            indexLineBreak = temp.indexOf("\n");
        }
        return temp.substring(0,indexLineBreak);
    }

    private static List<Category> recursiveCategory(String parent, String name,NodeList categoryNodes){
        List<Category> res = new ArrayList<>();
        List<String> items = new ArrayList<>();

        for (int i = 0; i < categoryNodes.getLength(); i++) {
            Node node = categoryNodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                switch (element.getTagName()){
                    case "category":
                        String child =  getCategoryName(element);
                        res.addAll(recursiveCategory(name,child,element.getChildNodes()));
                        break;
                    case "item":
                        items.add(getCategoryName(element));
                }
            }
        }

        res.add(new Category(parent, name, items));
        return res;
    }

}
