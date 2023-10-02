package webController;

import java.net.URL;
import javafx.fxml.FXML;
import models.Product;
import webController.HomeController;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

public class cartController implements Initializable {

    private List<HomeController.Product> selectedProducts;

    public void setSelectedProducts(List<HomeController.Product> selectedProducts) {
        System.out.println("Initialize của cartController được gọi.");

        this.selectedProducts = selectedProducts;
        System.out.println("Số sản phẩm đã chọn: " + selectedProducts.size());

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if (selectedProducts != null) {
            System.out.println("Số sản phẩm đã chọn: " + selectedProducts.size());

            if (!selectedProducts.isEmpty()) {
                List<HomeController.Product> selectedProducts = HomeController.selectedProducts;

                // Now you can work with the selectedProducts list
                for (HomeController.Product product : selectedProducts) {
                    System.out.println("Product ID: " + product.getProductId());
                    System.out.println("Product Name: " + product.getProductName());
                    System.out.println("Product Price: " + product.getProductPrice());
                    // Add your code here to use the product data as needed
                }
            } else {
                System.out.println("Không có sản phẩm nào được chọn");
            }
        } else {
            System.out.println("Không có sản phẩm nào được chọn");
        }
    }

}
