package webController;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import javafx.fxml.FXML;
import models.Product;
import webController.HomeController;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import main.Main;
import models.productCart;
import models.productQuantity;
import static scala.collection.immutable.Nil.product;
import webController.HomeController;

public class cartController implements Initializable {

    @FXML
    private ScrollPane big;

    @FXML
    private Label sumPrice;

    @FXML
    private VBox small;
//    File imageFile = new File("C:/java2/projectJava2/src/uploads/image.jpg");
    private int quantity; // Khai báo biến quantity ở mức độ lớn hơn
    List<HomeController.Product> selectedProducts = productCart.getInstance().getSelectedProducts();

    public void setSelectedProducts(List<HomeController.Product> selectedProducts) {
        this.selectedProducts = selectedProducts;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Map<Integer, Integer> productQuantityMap = new HashMap<>();

        // Check if there are selected products
        if (!selectedProducts.isEmpty()) {
            // Loop through selectedProducts list and display product information
            for (HomeController.Product product : selectedProducts) {
                System.out.println("-----------cart------------ ");
                System.out.println("Số sản phẩm đã chọn: " + selectedProducts.size());
                System.out.println("userID: " + product.getUserId());
                System.out.println("Product ID: " + product.getProductId());
                System.out.println("Product Name: " + product.getProductName());
                System.out.println("Product Img: " + product.getImagePath());
                System.out.println("Product Price: " + product.getProductPrice());

            }
            Map<Integer, HomeController.Product> productMap = new HashMap<>();

            for (HomeController.Product product : selectedProducts) {
                int productId = product.getProductId();
                // Kiểm tra xem sản phẩm đã có trong Map chưa
                if (productMap.containsKey(productId)) {
                    // Nếu đã có, tăng số lượng của sản phẩm
                    HomeController.Product existingProduct = productMap.get(productId);
                    existingProduct.setQuantity(existingProduct.getQuantity() + 1);
                } else {
                    // Nếu chưa có, thêm sản phẩm vào Map với số lượng ban đầu là 1

                    product.setQuantity(1);
                    productMap.put(productId, product);
                }
            }
// Tính tổng giá trị đơn hàng
            float totalPrice = 0;
            for (HomeController.Product product : productMap.values()) {
                int quantity = product.getQuantity();
                float productPrice = product.getProductPrice();
                float productTotalPrice = quantity * productPrice;
                totalPrice += productTotalPrice;
                product.setTotalPrice(productTotalPrice);
            }

// Định dạng giá trị tổng đơn hàng với dấu , ở hàng nghìn
            DecimalFormat decimalFormat = new DecimalFormat("#,##0");
            String formattedTotalPrice = decimalFormat.format(totalPrice);

// Hiển thị tổng giá trị đơn hàng với dấu , ở hàng nghìn lên Label sumPrice
            sumPrice.setText("Total Price: " + formattedTotalPrice);

            int i = 0;
            for (HomeController.Product product : productMap.values()) {
                // Sử dụng product.getQuantity() để lấy số lượng sản phẩm
                int quantity = product.getQuantity();
                // Tạo Pane để hiển thị thông tin sản phẩm
                Pane productPane = new Pane();
                productPane.setPrefHeight(140);
                productPane.setPrefWidth(834);

                // Tạo ImageView cho sản phẩm
                ImageView productImage = new ImageView();
                productImage.setFitWidth(130);
                productImage.setFitHeight(120);
                productImage.setPreserveRatio(false);
                // Sử dụng đường dẫn tuyệt đối đến tệp hình ảnh
                File imageFile = new File(product.getImagePath());
                String absoluteImagePath = imageFile.toURI().toString();
                Image image = new Image(absoluteImagePath);
                System.out.println("đường dẫn ảnh ____________" + absoluteImagePath);
                productImage.setImage(image);

                // Create Labels for the product name and price
                Label productNameLabel = new Label(product.getProductName());
                productNameLabel.setLayoutX(174);
                productNameLabel.setLayoutY(58);
                productNameLabel.setPrefHeight(25);
                productNameLabel.setPrefWidth(326);

                float productPrice = product.getProductPrice();
                String formattedProductPrice = decimalFormat.format(productPrice);
                Label productPriceLabel = new Label("Price: " + formattedProductPrice);
                productPriceLabel.setLayoutX(535);
                productPriceLabel.setLayoutY(58);
                productPriceLabel.setPrefHeight(25);
                productPriceLabel.setPrefWidth(113);

                // Create controls to increase and decrease quantity
                Button increaseButton = new Button("+");
                Button decreaseButton = new Button("-");
                Label quantityLabel = new Label("Quantity: " + quantity);

                HBox quantityControls = new HBox(10, decreaseButton, quantityLabel, increaseButton);
                quantityControls.setLayoutX(650);
                quantityControls.setLayoutY(58);

                // Initial quantity
                int[] productQuantity = {quantity};
                quantity = productQuantity[0]; // Gán giá trị ban đầu

                // Increase quantity
                increaseButton.setOnAction(event -> {
                    productQuantity[0]++;
                    quantityLabel.setText("Quantity: " + productQuantity[0]);

                });

                // Decrease quantity (minimum 1)
                decreaseButton.setOnAction(event -> {
                    if (productQuantity[0] > 1) {
                        productQuantity[0]--;
                        quantityLabel.setText("Quantity: " + productQuantity[0]);
                    }
                });
                                System.out.println("Quantity: " + quantity); // In ra console

                // Initial quantity

// Increase quantity
                // Add the image and labels to the productPane
                productPane.getChildren().addAll(productImage, productNameLabel, productPriceLabel, quantityControls);

                // Add the productPane to the VBox
                small.getChildren().add(productPane);
                i++;
            }
        } else {
            System.out.println("No products selected.");
        }

    }

    public void getHome() throws IOException {
        Main.setRoot("/web/home.fxml");
    }

}
