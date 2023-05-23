package DataClasses;

import java.util.ArrayList;
import java.util.List;

public class Category {

    String parentCategory;
    String name;
    List<String> items;

    public Category() {
        items = new ArrayList<>();
    }

    public Category(String parentCategory, String name, List<String> items) {
        this.parentCategory = parentCategory;
        this.name = name;
        this.items = items;
    }

    public String getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(String parentCategory) {
        this.parentCategory = parentCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("DataClasses.Category{parentCategory='");
        sb.append(parentCategory).append("\'name='").append(name).append("\', items=").append(items).append('}');

        return sb.toString();
    }
}
