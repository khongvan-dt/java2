/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.util.ArrayList;
import java.util.List;
import webController.HomeController;

/**
 *
 * @author Administrator
 */
public class productCart {

    private static productCart instance;
    private List<HomeController.Product> selectedProducts = new ArrayList<>();

    private productCart() {
    }

    public static productCart getInstance() {
        if (instance == null) {
            instance = new productCart();
        }
        return instance;
    }

    public List<HomeController.Product> getSelectedProducts() {
        return selectedProducts;
    }

    public void setSelectedProducts(List<HomeController.Product> selectedProducts) {
        this.selectedProducts = selectedProducts;
    }
}
