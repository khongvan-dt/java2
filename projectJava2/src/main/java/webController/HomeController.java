package webController;

import adminController.loginController;
import db.connect;
import java.awt.Color;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javax.servlet.ServletContext;
import main.Main;
import models.Product;

import javax.servlet.http.HttpSession;

public class HomeController implements Initializable {

    private loginController loginController;

    @FXML
    private AnchorPane bgV1;
    @FXML
    private Button categoryId1;
    @FXML
    private ScrollPane scrollPane1;

    public int categoryIDetail;
    private List<Product> selectedProducts = new ArrayList<>();

    // Constructor để truyền vào đối tượng HttpServletRequest
    public class Product {

        public int productId;
        public String productName;
        public String imagePath;
        public float productPrice;

        private int userId;
        private int quantity;

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

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

        public float getProductPrice() {
            return productPrice;
        }

        public void setProductPrice(float productPrice) {
            this.productPrice = productPrice;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Khởi tạo đối tượng loginController
        loginController = new adminController.loginController();

        displayProducts2();
    }
//    boolean isLoggedIn = loginController.isLoggedIn();

    private void displayProducts2() {
        Connection connection = null;
        PreparedStatement statement2 = null;
        ResultSet resultSet2 = null;

        try {
            connection = connect.getConnection();

            String query2 = "SELECT product.productId , ProductsName.ProductName, product.img, importgoods.price, product.categoryId, category.categoryName, category.categoryId "
                    + "FROM product "
                    + "INNER JOIN ProductsName ON product.ProductNameId = ProductsName.ProductNameId "
                    + "INNER JOIN importgoods ON importgoods.ProductNameId = product.ProductNameId "
                    + "INNER JOIN supplier ON product.supplier_id = supplier.supplierId "
                    + "INNER JOIN category ON product.categoryId = category.categoryId "
                    + "WHERE product.categoryId =10";

            statement2 = connection.prepareStatement(query2);

            resultSet2 = statement2.executeQuery();
            System.err.println("resultSet2" + resultSet2);

            int productSpacing = 1; // Khoảng cách giữa các sản phẩm
            int productWidth = 190; // Chiều rộng của mỗi sản phẩm
            int productHeight = 205; // Chiều cao của mỗi sản phẩm
            int startX = 21; // Vị trí ban đầu theo trục X
            int startY = 4; // Vị trí ban đầu theo trục Y

            int productIndex = 0; // Chỉ số của sản phẩm
            if (resultSet2.next()) {
                categoryIDetail = resultSet2.getInt("categoryId");
                System.out.println("categoryIDetail2:  " + categoryIDetail);
                String categoryName = resultSet2.getString("categoryName");
                // Đặt giá trị categoryName cho nút Button
                categoryId1.setText(categoryName);

            }
            while (resultSet2.next()) {
                String productName = resultSet2.getString("ProductName");
                String imagePath = resultSet2.getString("img");
                Float productPrice = resultSet2.getFloat("price");

                // Tạo VBox để chứa các thành phần
                VBox productBox = new VBox();
                productBox.setSpacing(2); // Đặt khoảng cách giữa các thành phần là 2px
                productBox.setAlignment(Pos.CENTER);

                // Tạo ImageView cho sản phẩm
                ImageView productImageView = new ImageView();
                productImageView.setFitWidth(140);
                productImageView.setFitHeight(125);
                productImageView.setPreserveRatio(true);
                Image image = new Image("file:///C:\\java2\\projectJava2\\src\\uploads" + imagePath);
                productImageView.setImage(image);

//// Tạo Label cho tên sản phẩm
                Label productNameLabel = new Label(productName);
                productNameLabel.setPrefWidth(150);
                productNameLabel.setPrefHeight(25);
                productNameLabel.setAlignment(Pos.CENTER);

                // Đặt xử lý sự kiện cho Label 
                productNameLabel.setOnMouseClicked(event -> {
                    // Xử lý khi Label được nhấp chuột
                });

// Đưa Button và các thành phần khác vào VBox
                // Tạo Label cho giá sản phẩm
                Label productPriceLabel = new Label("Price: " + productPrice);
                productPriceLabel.setPrefWidth(140);
                productPriceLabel.setPrefHeight(25);
                productPriceLabel.setAlignment(Pos.CENTER);

                Button buyButton = new Button("Buy");
                buyButton.setPrefWidth(140);
                buyButton.setPrefHeight(25);
                buyButton.setAlignment(Pos.CENTER);

// Lưu giá trị product.productId vào nút "Buy"
                buyButton.setUserData(resultSet2.getInt("productId"));

// Thêm sự kiện cho nút "Buy" để xử lý khi được nhấp
                // Trong phương thức handleBuyButtonAction()
                buyButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
<<<<<<< HEAD
                        if (loginController.getLoggedInUserId()>0) {
=======
                        if (loginController.getLoggedInUserId() > 0) {
>>>>>>> mainBranch
                            // The user is logged in, proceed with adding the product to the cart
                            int userId = loginController.getLoggedInUserId();
                            int productId = (int) buyButton.getUserData();

                            // Check if the product is already in the cart, and update quantity if necessary
                            boolean productAlreadyInCart = false;
                            for (Product p : selectedProducts) {
                                if (p.getProductId() == productId) {
                                    // Product is already in the cart, update quantity
                                    p.setQuantity(p.getQuantity() + 1);
                                    productAlreadyInCart = true;
                                    break;
                                }
                            }

                            if (!productAlreadyInCart) {
                                // Product is not in the cart, add it with quantity 1
                                Product selectedProduct = new Product();
                                selectedProduct.setProductId(productId);
                                selectedProduct.setProductName(productName);
                                selectedProduct.setImagePath(imagePath);
                                selectedProduct.setProductPrice(productPrice);
                                selectedProduct.setUserId(userId);
                                selectedProduct.setQuantity(1); // Set initial quantity to 1
                                selectedProducts.add(selectedProduct);
                            }

                            // Notify the user that the product has been added to the cart
                            showSuccessAlert("Product added to cart successfully!");
                        } else {
                            // The user is not logged in, redirect to the login page
                            try {
                                redirectToLogin();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
<<<<<<< HEAD
//
//                    private boolean isemty(int loggedInUserId) {
//                        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
//                    }
//
//                    private boolean isEmty(int loggedInUserId) {
//                        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
//                    }
=======
>>>>>>> mainBranch

                });

                // Hàm chuyển đến trang đăng nhập
// Đưa Button và các thành phần khác vào VBox
                productBox.getChildren()
                        .addAll(productImageView, productNameLabel, productPriceLabel, buyButton);

                // Đặt VBox vào Pane sản phẩm
                Pane productPane = new Pane(productBox);

                productPane.setPrefWidth(productWidth);

                productPane.setPrefHeight(productHeight);

                // Tính toán vị trí của sản phẩm dựa trên chỉ số và khoảng cách
                int productX = startX + (productWidth + productSpacing) * productIndex;
                int productY = startY;

                // Đặt vị trí cho Pane sản phẩm
                productPane.setLayoutX(productX);

                productPane.setLayoutY(productY);

                // Thêm Pane sản phẩm vào AnchorPane "bgV"
                bgV1.getChildren()
                        .add(productPane);
                scrollPane1.setContent(bgV1);
                productIndex++; // Tăng chỉ số sản phẩm
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet2 != null) {
                    resultSet2.close();
                }
                if (statement2 != null) {
                    statement2.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    // các hàm gọi giao diện

    public void getFromRelatedProducts() throws IOException {
        SharedData.setCategoryId(categoryIDetail); // Set the category ID
        Main.setRoot("/web/relatedProducts.fxml"); // Navigate to relatedProducts.fxml
    }

    // Truyền danh sách sản phẩm đã chọn vào SharedData và chuyển trang
    public void redirectToCart() throws IOException {
        SharedData.setSelectedProducts(selectedProducts); // Truyền danh sách sản phẩm đã chọn
        Main.setRoot("/web/cart.fxml");
    }

    private void redirectToLogin() throws IOException {
        Main.setRoot("/admin/login.fxml");
    }

//thông báo  
    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
