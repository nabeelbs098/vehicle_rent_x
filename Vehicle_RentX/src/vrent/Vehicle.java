package vrent;

public class Vehicle {
    private int id;
    private String name;
    private String type;
    private boolean available;
    private String customerName;

    public Vehicle(int id, String name, String type, boolean available, String customerName) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.available = available;
        this.customerName = customerName;
    }

    public Vehicle(String name, String type, boolean available, String customerName) {
        this(0, name, type, available, customerName);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    
}

