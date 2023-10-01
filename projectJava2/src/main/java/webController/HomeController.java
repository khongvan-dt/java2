package webController;

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
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javax.servlet.ServletContext;
import main.Main;
import models.Product;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javax.servlet.http.HttpSession;

public class HomeController implements Initializable {

    @FXML
    private AnchorPane bgV;
    @FXML
    private Button categoryId;
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private AnchorPane bgV1;
    @FXML
    private Button categoryId1;
    @FXML
    private ScrollPane scrollPane1;

    @FXML
    private AnchorPane bgV2;
    @FXML
    private Button categoryId2;
    @FXML
    private ScrollPane scrollPane2;

    public static int categoryIDetail;
    public static int categoryIDetail1;
    public static int categoryIDetail2;

    private List<Product> selectedProducts = new ArrayList<>();

    // Constructor để truyền vào đối tượng HttpServletRequest
    public class Product {

        public int productId;
        public String productName;
        public String imagePath;
        public float productPrice;

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
        System.out.println("abc");
        displayNewestProducts();
        displayProducts2();
        displayProducts3();

    }

    private void displayNewestProducts() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = connect.getConnection();

            String query = "SELECT ProductsName.ProductName,product.productId , product.img, importgoods.price, product.categoryId, category.categoryName "
                    + "FROM product "
                    + "INNER JOIN ProductsName ON product.ProductNameId = ProductsName.ProductNameId "
                    + "INNER JOIN importgoods ON importgoods.ProductNameId = product.ProductNameId "
                    + "INNER JOIN supplier ON product.supplier_id = supplier.supplierId "
                    + "INNER JOIN category ON product.categoryId = category.categoryId "
                    + "WHERE product.categoryId = 9";

            statement = connection.prepareStatement(query);

            resultSet = statement.executeQuery();

            int productSpacing = 1; // Khoảng cách giữa các sản phẩm
            int productWidth = 190; // Chiều rộng của mỗi sản phẩm
            int productHeight = 205; // Chiều cao của mỗi sản phẩm
            int startX = 21; // Vị trí ban đầu theo trục X
            int startY = 4; // Vị trí ban đầu theo trục Y

            int productIndex = 0; // Chỉ số của sản phẩm
            if (resultSet.next()) {
                categoryIDetail = resultSet.getInt("categoryId");
                String categoryName = resultSet.getString("categoryName");
                System.out.println("categoryIDetail" + categoryIDetail);
                categoryId.setText(categoryName);

            }

            while (resultSet.next()) {
                String productName = resultSet.getString("ProductName");
                String imagePath = resultSet.getString("img");
                Float productPrice = resultSet.getFloat("price");
                System.out.println("webController.HomeController.displayNewestProducts()" + productPrice);
                // Tạo VBox để chứa các thành phần
                VBox productBox = new VBox();
                productBox.setSpacing(2); // Đặt khoảng cách giữa các thành phần là 2px
                productBox.setAlignment(Pos.CENTER);

                // Tạo ImageView cho sản phẩm
                ImageView productImageView = new ImageView();
                productImageView.setFitWidth(140);
                productImageView.setFitHeight(125);
                productImageView.setPreserveRatio(true);
                Image image = new Image("file:///C:/java2/projectJava2/" + imagePath);
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
// nút mua

                Button buyButton = new Button("Buy");
                buyButton.setPrefWidth(140);
                buyButton.setPrefHeight(25);
                buyButton.setAlignment(Pos.CENTER);
// Đặt CSS cho nút "Buy"
                buyButton.setStyle("-fx-background-radius: 20; -fx-background-color: #FCE4EC;");

// Lưu giá trị product.productId vào nút "Buy"
                buyButton.setUserData(resultSet.getInt("productId"));

// Thêm sự kiện cho nút "Buy" để xử lý khi được nhấp
                buyButton.setOnAction(event -> {
                    int productId = (int) buyButton.getUserData();
                    // Bây giờ bạn có thể sử dụng productId cho các hoạt động sau này, như thêm vào giỏ hàng hoặc xử lý mua hàng.
                    System.out.println("Product ID to buy: " + productId);
                });

// Đưa Button và các thành phần khác vào VBox
                productBox.getChildren().addAll(productImageView, productNameLabel, productPriceLabel, buyButton);

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
                bgV.getChildren().add(productPane);
                scrollPane.setContent(bgV);
                productIndex++; // Tăng chỉ số sản phẩm
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
                    + "WHERE product.categoryId =11";

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
                categoryIDetail1 = resultSet2.getInt("categoryId");
                System.out.println("categoryIDetail2:  " + categoryIDetail1);
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
                Image image = new Image("file:///C:/java2/projectJava2/" + imagePath);
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
                buyButton.setStyle("-fx-background-radius: 20; -fx-background-color: #FCE4EC;");

// Lưu giá trị product.productId vào nút "Buy"
                buyButton.setUserData(resultSet2.getInt("productId"));

// Thêm sự kiện cho nút "Buy" để xử lý khi được nhấp
                buyButton.setOnAction(event -> {
                    int productId = (int) buyButton.getUserData();

                    Product selectedProduct = new Product();
                    selectedProduct.setProductId(productId);
                    selectedProduct.setProductName(productName);
                    selectedProduct.setImagePath(imagePath);
                    selectedProduct.setProductPrice(productPrice);

// Thêm sản phẩm vào danh sách
                    selectedProducts.add(selectedProduct);

                    try {
                        redirectToCart();
                    } catch (IOException ex) {
                        Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });

// Đưa Button và các thành phần khác vào VBox
                productBox.getChildren().addAll(productImageView, productNameLabel, productPriceLabel, buyButton);

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
                bgV1.getChildren().add(productPane);
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

    private void displayProducts3() {
        Connection connection = null;
        PreparedStatement statement3 = null;
        ResultSet resultSet3 = null;

        try {
            connection = connect.getConnection();

            String query2 = "SELECT product.productId , ProductsName.ProductName, product.img, importgoods.price, product.categoryId, category.categoryName, category.categoryId "
                    + "FROM product "
                    + "INNER JOIN ProductsName ON product.ProductNameId = ProductsName.ProductNameId "
                    + "INNER JOIN importgoods ON importgoods.ProductNameId = product.ProductNameId "
                    + "INNER JOIN supplier ON product.supplier_id = supplier.supplierId "
                    + "INNER JOIN category ON product.categoryId = category.categoryId "
                    + "WHERE product.categoryId =9";

            statement3 = connection.prepareStatement(query2);

            resultSet3 = statement3.executeQuery();
            System.err.println("resultSet3" + resultSet3);

            int productSpacing = 1; // Khoảng cách giữa các sản phẩm
            int productWidth = 190; // Chiều rộng của mỗi sản phẩm
            int productHeight = 205; // Chiều cao của mỗi sản phẩm
            int startX = 21; // Vị trí ban đầu theo trục X
            int startY = 4; // Vị trí ban đầu theo trục Y

            int productIndex = 0; // Chỉ số của sản phẩm
            if (resultSet3.next()) {
                categoryIDetail2 = resultSet3.getInt("categoryId");
                System.out.println("categoryIDetail2:  " + categoryIDetail2);
                String categoryName = resultSet3.getString("categoryName");
                // Đặt giá trị categoryName cho nút Button
                categoryId2.setText(categoryName);

            }
            while (resultSet3.next()) {
                String productName = resultSet3.getString("ProductName");
                String imagePath = resultSet3.getString("img");
                Float productPrice = resultSet3.getFloat("price");

                // Tạo VBox để chứa các thành phần
                VBox productBox = new VBox();
                productBox.setSpacing(2); // Đặt khoảng cách giữa các thành phần là 2px
                productBox.setAlignment(Pos.CENTER);

                // Tạo ImageView cho sản phẩm
                ImageView productImageView = new ImageView();
                productImageView.setFitWidth(140);
                productImageView.setFitHeight(125);
                productImageView.setPreserveRatio(true);
                Image image = new Image("file:///C:/java2/projectJava2/" + imagePath);
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
                buyButton.setStyle("-fx-background-radius: 20; -fx-background-color: #FCE4EC;");

// Lưu giá trị product.productId vào nút "Buy"
                buyButton.setUserData(resultSet3.getInt("productId"));

// Thêm sự kiện cho nút "Buy" để xử lý khi được nhấp
                buyButton.setOnAction(event -> {
                    int productId = (int) buyButton.getUserData();
                    // Bây giờ bạn có thể sử dụng productId cho các hoạt động sau này, như thêm vào giỏ hàng hoặc xử lý mua hàng.
                    System.out.println("Product ID to buy: " + productId);
                });

// Đưa Button và các thành phần khác vào VBox
                productBox.getChildren().addAll(productImageView, productNameLabel, productPriceLabel, buyButton);

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
                bgV2.getChildren().add(productPane);
                scrollPane2.setContent(bgV2);
                productIndex++; // Tăng chỉ số sản phẩm
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet3 != null) {
                    resultSet3.close();
                }
                if (statement3 != null) {
                    statement3.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    // Phương thức để lấy đối tượng HttpSession từ ServletContext

    // các hàm gọi giao diện
    public void getFromRelatedProducts() throws IOException {
        SharedData.setCategoryId(categoryIDetail); // Set the category ID
        Main.setRoot("/web/relatedProducts.fxml"); // Navigate to relatedProducts.fxml
    }

    public void getFromRelatedProducts1() throws IOException {
        SharedData.setCategoryId(categoryIDetail1); // Set the category ID
        Main.setRoot("/web/relatedProducts.fxml"); // Navigate to relatedProducts.fxml
    }

    public void getFromRelatedProducts2() throws IOException {
        SharedData.setCategoryId(categoryIDetail2); // Set the category IDs
        Main.setRoot("/web/relatedProducts.fxml"); // Navigate to relatedProducts.fxml
    }

    public void redirectToCart() throws IOException {
        // Thay đổi categoryIDetail thành selectedProducts
        SharedData.setSelectedProducts(selectedProducts); // Truyền danh sách sản phẩm đã chọn
        Main.setRoot("/web/cart.fxml"); // Chuyển đến trang giỏ hàng
    }

    public void getAccount() throws IOException {
        Main.setRoot("/admin/login.fxml");
    }

}
