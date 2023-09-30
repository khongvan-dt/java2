package webController;

import java.net.URL;
import javafx.fxml.FXML;
import models.Product;

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
        List<HomeController.Product> selectedProducts = ShoppingCart.getInstance().getSelectedProducts();

        if (selectedProducts != null) {
            System.out.println("Số sản phẩm đã chọn: " + selectedProducts.size());

            if (!selectedProducts.isEmpty()) {
                for (HomeController.Product product : selectedProducts) {
                    String productName = product.getProductName();
                    float productPrice = product.getProductPrice();
                    int userId = product.getUserId();
                    System.out.println("id: " + userId);
                    System.out.println("Tên sản phẩm: " + productName);
                    System.out.println("Giá sản phẩm: " + productPrice);
                    // Thêm các thông tin khác nếu cần
                }
            } else {
                System.out.println("Không có sản phẩm nào được chọn");
            }
        } else {
            System.out.println("Không có sản phẩm nào được chọn");
        }
    }

}
