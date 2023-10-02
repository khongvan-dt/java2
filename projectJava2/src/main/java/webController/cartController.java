package webController;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import javafx.fxml.FXML;
import models.Product;
import webController.HomeController;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import main.Main;
import models.productCart;

public class cartController implements Initializable {

    List<HomeController.Product> selectedProducts = productCart.getInstance().getSelectedProducts();

    public void setSelectedProducts(List<HomeController.Product> selectedProducts) {
        this.selectedProducts = selectedProducts;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Check if there are selected products
        if (!selectedProducts.isEmpty()) {
            // Loop through selectedProducts list and display product information
            for (HomeController.Product product : selectedProducts) {
                System.out.println("-----------cart------------ ");
                System.out.println("Số sản phẩm đã chọn: " + selectedProducts.size());
                System.out.println("userID: " + product.getUserId());
                System.out.println("Product ID: " + product.getProductId());
                System.out.println("Product Name: " + product.getProductName());
                System.out.println("Product Price: " + product.getProductPrice());
            }
        } else {
            System.out.println("No products selected.");
        }
    }

    public void getHome() throws IOException {
        Main.setRoot("/web/home.fxml");
    }

}
