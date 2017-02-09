package io.intrepid.contest.models;

public class Category {
    private String name;
    private String description;

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public boolean equals(Object obj) {
        try {
            Category otherCategory = (Category) obj;
            return this.name == otherCategory.name &&
                    this.description == otherCategory.description;
        }catch (ClassCastException  exception){
            return false;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
}
