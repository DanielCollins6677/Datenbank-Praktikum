package Reader;

import DataClasses.Category;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CategoryReader {

    public static List<Category> getCategories(File file) {
        List<String> input = SimpleReader.readFile(file);
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
                    //does it have a parent DataClasses.Category?
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

}
