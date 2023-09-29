<<<<<<< HEAD
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author thao
 */
public class Product {
    Integer id;
    Integer supplierId;
    Integer categoryId;
    String productName;
    String imagePath;
    String description;
    Float price;
    
    
    public Product(){
        
    }
    
    public Product(Integer id, String productName, String imagePath, Float price){
=======
package models;

public class Product {
    private Integer id;
    private String productName;
    private String imagePath;
    private Float price;

    public Product() {
    }

    public Product(Integer id, String productName, String imagePath, Float price) {
>>>>>>> main
        this.id = id;
        this.productName = productName;
        this.imagePath = imagePath;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

<<<<<<< HEAD
    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

=======
>>>>>>> main
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

<<<<<<< HEAD
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

=======
>>>>>>> main
    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
<<<<<<< HEAD
    
=======
>>>>>>> main
}
