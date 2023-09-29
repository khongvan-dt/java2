package webController;

<<<<<<< HEAD
=======
import db.connect;
import java.awt.Color;
>>>>>>> main
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
import javafx.fxml.Initializable;
<<<<<<< HEAD
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
=======
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
>>>>>>> main
import models.Product;

public class HomeController implements Initializable {

    @FXML
<<<<<<< HEAD
    private ImageView imageView;
    @FXML
    private Pane newestProducts;
=======
    private AnchorPane bgV;
    @FXML
    private Button categoryId1;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private AnchorPane bgV1;
    @FXML
    private Button categoryId2;
    @FXML
    private ScrollPane scrollPane1;
>>>>>>> main

    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("abc");
        displayNewestProducts();
<<<<<<< HEAD
        // Kết nối cơ sở dữ liệu
//        Connection connection = null;
//        PreparedStatement statement = null;
//        ResultSet resultSet = null;
//
//        try {
//            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/project2", "thaoit", "It1234!");
//
//            // Truy vấn cơ sở dữ liệu để lấy thông tin tất cả các sản phẩm
//            String query = "SELECT img FROM product";
//            statement = connection.prepareStatement(query);
//
//            resultSet = statement.executeQuery();
//
//            while (resultSet.next()) {
//                String img = resultSet.getString("img");
//
//                // Hiển thị thông tin sản phẩm (chỉ hiện ảnh)
//                displayProduct(img);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            // Đóng kết nối và giải phóng tài nguyên
//            try {
//                if (resultSet != null) {
//                    resultSet.close();
//                }
//                if (statement != null) {
//                    statement.close();
//                }
//                if (connection != null) {
//                    connection.close();
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }

    }

    private void displayNewestProducts() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Product> lstProduct = new ArrayList<>();
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/project2", "thaoit", "It1234!");

            // Truy vấn cơ sở dữ liệu để lấy thông tin tất cả các sản phẩm
            String query = "SELECT product.*,productsname.ProductName FROM product inner join productsname limit 4 offset 0";
=======
        displayProducts2();

    }

   
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;

    private void displayNewestProducts() {

        try {
            connection = connect.getConnection();

            String query = "SELECT ProductsName.ProductName, product.img, importgoods.price, product.categoryId, category.categoryName "
                    + "FROM product "
                    + "INNER JOIN ProductsName ON product.ProductNameId = ProductsName.ProductNameId "
                    + "INNER JOIN importgoods ON importgoods.ProductNameId = product.ProductNameId "
                    + "INNER JOIN supplier ON product.supplier_id = supplier.supplierId "
                    + "INNER JOIN category ON product.categoryId = category.categoryId "
                    + "WHERE product.categoryId = 10";

>>>>>>> main
            statement = connection.prepareStatement(query);

            resultSet = statement.executeQuery();

<<<<<<< HEAD
            while (resultSet.next()) {
                Integer id = resultSet.getInt("productId");
                String productName = resultSet.getString("ProductName");
                Float price = Float.parseFloat("0");
                String imagePath = resultSet.getString("img");
                Product prd = new Product(id, productName, imagePath, price);
                lstProduct.add(prd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đóng kết nối và giải phóng tài nguyên
=======
            int productSpacing = 1; // Khoảng cách giữa các sản phẩm
            int productWidth = 190; // Chiều rộng của mỗi sản phẩm
            int productHeight = 205; // Chiều cao của mỗi sản phẩm
            int startX = 21; // Vị trí ban đầu theo trục X
            int startY = 4; // Vị trí ban đầu theo trục Y

            int productIndex = 0; // Chỉ số của sản phẩm
            if (resultSet.next()) {
                String categoryName = resultSet.getString("categoryName");

                // Đặt giá trị categoryName cho nút Button
                categoryId1.setText(categoryName);
            }

            while (resultSet.next()) {
                String productName = resultSet.getString("ProductName");
                String imagePath = resultSet.getString("img");
                Float productPrice = resultSet.getFloat("price");

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

                // Đưa ImageView và các Label vào VBox
                productBox.getChildren().addAll(productImageView, productNameLabel, productPriceLabel);

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

        try {
            connection = connect.getConnection();

            String query = "SELECT ProductsName.ProductName, product.img, importgoods.price, product.categoryId, category.categoryName "
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
                String categoryName = resultSet.getString("categoryName");
                // Đặt giá trị categoryName cho nút Button
                categoryId2.setText(categoryName);
            }

            while (resultSet.next()) {
                String productName = resultSet.getString("ProductName");
                String imagePath = resultSet.getString("img");
                Float productPrice = resultSet.getFloat("price");

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

                // Đưa ImageView và các Label vào VBox
                productBox.getChildren().addAll(productImageView, productNameLabel, productPriceLabel);

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
>>>>>>> main
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
<<<<<<< HEAD
        Float startX = Float.parseFloat("68");
        Float startY = Float.parseFloat("809.0");
        // iterate product to views
        for (Product prd : lstProduct) {
            Pane pane = new Pane();
            pane.setPrefWidth(143);
            pane.setLayoutX(startX);
            // Tạo ImageView và Label
            ImageView imageView = new ImageView();
            // Tạo đối tượng hình ảnh từ đường dẫn
            String imageUrl = "file:///D:/JAVAPJ2/java2/projectJava2/" + prd.getImagePath();
            try {
                Image image = new Image(imageUrl);
                imageView.setImage(image);
                imageView.setFitWidth(200); // Đặt chiều rộng của ImageView (tuỳ chọn)
                imageView.setPreserveRatio(true); // Giữ tỷ lệ khung hình (tuỳ chọn)
                Label nameLabel = new Label(prd.getProductName());
                // tinh width height anh
                nameLabel.setPrefWidth(143);
                nameLabel.setPrefHeight(50);
                // dua anh va label vao pane
                pane.getChildren().addAll(nameLabel, imageView);
                // dua pane vao pane fx:idnewestproduct
                newestProducts.getChildren().add(pane);
                startX += 143;
            } catch (Exception e) {
                 System.out.println(e.getMessage());
            }

        }
    }

    private void displayProduct(String img) {
        // Đường dẫn gốc đến thư mục chứa ảnh trong ứng dụng của bạn
        String baseImagePath = "D:/JAVAPJ2/java2/projectJava2/";

        // Nối đường dẫn cơ sở và đường dẫn từ cơ sở dữ liệu
        String fullImagePath = baseImagePath + img;

        // In đường dẫn ảnh để kiểm tra
        System.out.printf("Đường dẫn ảnh: %s%n" + fullImagePath);

        // Hiển thị ảnh sản phẩm trong ImageView
        Image image = new Image("file:///" + fullImagePath); // Sử dụng "file:///" để chỉ ra đường dẫn cục bộ
        imageView.setImage(image);
=======
>>>>>>> main
    }
}
