package webController;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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
import static scala.collection.immutable.Nil.product;
import webController.HomeController;

public class cartController implements Initializable {

    @FXML
    private ScrollPane big;

    @FXML
    private VBox small;
    File imageFile = new File("C:/java2/projectJava2/src/uploads/image.jpg");

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
                // Tạo Pane để hiển thị thông tin sản phẩm

            }
            for (HomeController.Product product : selectedProducts) {

                int productId = product.getProductId();
//                int quantity = productQuantityMap.get(productId);
                Integer quantity = productQuantityMap.get(productId);
                if (quantity == null) {
                    // If the quantity is not in the map, set a default value (e.g., 1)
                    quantity = 1;
                }
                // Tạo Pane để hiển thị thông tin sản phẩm
                Pane productPane = new Pane();
                productPane.setPrefHeight(140);
                productPane.setPrefWidth(834);

                // Create an ImageView for the product image
                ImageView productImage = new ImageView();
                productImage.setFitHeight(123);
                productImage.setFitWidth(148);
                productImage.setLayoutX(14);
                productImage.setLayoutY(9);

                // Load the product image from the file path
                File imageFile = new File("C:/java2/projectJava2/" + product.getImagePath());
                if (imageFile.exists()) {
                    try {
                        URL imageURL = imageFile.toURI().toURL();
                        Image image = new Image(imageURL.toString());
                        productImage.setImage(image);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }

                // Create Labels for the product name and price
                Label productNameLabel = new Label(product.getProductName());
                productNameLabel.setLayoutX(174);
                productNameLabel.setLayoutY(58);
                productNameLabel.setPrefHeight(25);
                productNameLabel.setPrefWidth(326);

                Label productPriceLabel = new Label("Price: " + product.getProductPrice());
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

                // Add the image and labels to the productPane
                productPane.getChildren().addAll(productImage, productNameLabel, productPriceLabel, quantityControls);

                // Add the productPane to the VBox
                small.getChildren().add(productPane);

            }
        } else {
            System.out.println("No products selected.");
        }

    }

    public void getHome() throws IOException {
        Main.setRoot("/web/home.fxml");
    }

}
