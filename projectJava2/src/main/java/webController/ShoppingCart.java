///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package webController;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ShoppingCart {
//
//    private static ShoppingCart instance;
//    private List<HomeController.Product> selectedProducts;
//
//    private ShoppingCart() {
//        selectedProducts = new ArrayList<>();
//    }
//
//    public static ShoppingCart getInstance() {
//        if (instance == null) {
//            instance = new ShoppingCart();
//        }
//        return instance;
//    }
//
//    public List<HomeController.Product> getSelectedProducts() {
//        return selectedProducts;
//    }
//
//    public void addProduct(HomeController.Product product) {
//        selectedProducts.add(product);
//    }
//
//    public void clearCart() {
//        selectedProducts.clear();
//    }
//}
