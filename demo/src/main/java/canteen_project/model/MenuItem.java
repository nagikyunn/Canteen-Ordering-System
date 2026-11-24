package canteen_project.model;

public class MenuItem {
    private int itemId;
    private String name;
    private String category;
    private double price;
    private boolean available;

    public MenuItem(int itemId, String name, String category, double price, boolean available) {
        this.itemId = itemId;
        this.name = name;
        this.category = category;
        this.price = price;
        this.available = available;
    }

    public int getItemId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}