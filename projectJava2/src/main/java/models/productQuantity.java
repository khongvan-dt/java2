/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import webController.HomeController;

/**
 *
 * @author Administrator
 */
public class productQuantity {

    private HomeController.Product product;
    private int quantity;

    public productQuantity(HomeController.Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public HomeController.Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
