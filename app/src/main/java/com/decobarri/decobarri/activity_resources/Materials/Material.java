package com.decobarri.decobarri.activity_resources.Materials;

public class Material {

    //@SerializedName("_id")
    //@Expose
    private String id;

    //@SerializedName("image")
    //@Expose
    private String image;

    //@SerializedName("name")
    //@Expose
    private String name;

    //@SerializedName("description")
    //@Expose
    private String description;

    //@SerializedName("urgent")
    //@Expose
    private boolean urgent;

    //@SerializedName("quantity")
    //@Expose
    private int quantity;

    //@SerializedName("address")
    //@Expose
    private String address;

    public Material(String id, String image, String name, String description, boolean urgent, int quantity, String address) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.description = description;
        this.urgent = urgent;
        this.quantity = quantity;
        this.address = address;
    }

    public String getID() { return id; }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isUrgent(){
        return urgent;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getAddress() {
        return address;
    }
}
