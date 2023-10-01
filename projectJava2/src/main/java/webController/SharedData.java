/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package webController;

import adminController.loginController;
import java.util.List;
import webController.HomeController.Product;

/**
 *
 * @author Administrator
 */
public class SharedData {

    private static int categoryId;
    private static List<Product> selectedProducts; // Thêm danh sách sản phẩm đã chọn

    public static int getCategoryId() {
        return categoryId;
    }

    public static void setCategoryId(int categoryId) {
        SharedData.categoryId = categoryId;
    }

    public static List<Product> getSelectedProducts() {
        return selectedProducts;
    }

    public static void setSelectedProducts(List<Product> selectedProducts) {
        SharedData.selectedProducts = selectedProducts;
    }

    static loginController getLoginController() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
