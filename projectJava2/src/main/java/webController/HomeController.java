package webController;

import db.connect;
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
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import models.Product;

public class HomeController implements Initializable {

    @FXML
    private AnchorPane bgV;

    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("abc");
        displayNewestProducts();
    }

    private void displayNewestProducts() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = connect.getConnection();

            String query = "SELECT ProductsName.ProductName, product.img, importgoods.price "
                    + "FROM product "
                    + "INNER JOIN ProductsName ON product.ProductNameId = ProductsName.ProductNameId "
                    + "INNER JOIN importgoods ON importgoods.ProductNameId = product.ProductNameId "
                    + "INNER JOIN supplier ON product.supplier_id = supplier.supplierId "
                    + "LIMIT 4 OFFSET 0";
            statement = connection.prepareStatement(query);

            resultSet = statement.executeQuery();

            int productSpacing = 5; // Khoảng cách giữa các sản phẩm
            int productWidth = 190; // Chiều rộng của mỗi sản phẩm
            int productHeight = 205; // Chiều cao của mỗi sản phẩm
            int startX = 21; // Vị trí ban đầu theo trục X
            int startY = 4; // Vị trí ban đầu theo trục Y

            int productIndex = 0; // Chỉ số của sản phẩm

            while (resultSet.next()) {
                String productName = resultSet.getString("ProductName");
                String imagePath = resultSet.getString("img");
                Float productPrice = resultSet.getFloat("price");

                // Tạo Pane mới cho sản phẩm
                Pane productPane = new Pane();
                productPane.setPrefWidth(productWidth);
                productPane.setPrefHeight(productHeight);

                // Tính toán vị trí của sản phẩm dựa trên chỉ số và khoảng cách
                int productX = startX + (productWidth + productSpacing) * productIndex;
                int productY = startY;

                // Đặt vị trí cho Pane sản phẩm
                productPane.setLayoutX(productX);
                productPane.setLayoutY(productY);

                // Tạo ImageView cho sản phẩm
                ImageView productImageView = new ImageView();
                productImageView.setFitWidth(140);
                productImageView.setFitHeight(125);
                productImageView.setLayoutX(25);
                productImageView.setLayoutY(13);
                productImageView.setPreserveRatio(true);

// Tạo Label cho tên sản phẩm
                Label productNameLabel = new Label(productName);
                productNameLabel.setPrefWidth(176);
                productNameLabel.setPrefHeight(25);
                productNameLabel.setAlignment(Pos.CENTER);
                productNameLabel.setLayoutX(8);
                productNameLabel.setLayoutY(138); // Đặt layoutY ở đây để có khoảng cách 2px

// Tạo Label cho giá sản phẩm
                Label productPriceLabel = new Label("Price: " + productPrice);
                productPriceLabel.setPrefWidth(138);
                productPriceLabel.setPrefHeight(25);
                productPriceLabel.setAlignment(Pos.CENTER);
                productPriceLabel.setLayoutX(27);
                productPriceLabel.setLayoutY(165); // Đặt layoutY ở đây để có khoảng cách 2px

                // Hiển thị ảnh sản phẩm trong ImageView
                Image image = new Image("file:///C:/java2/projectJava2/" + imagePath);
                productImageView.setImage(image);
                // Đưa ImageView và các Label vào Pane sản phẩm
                productPane.getChildren().addAll(productImageView, productNameLabel, productPriceLabel);

                // Thêm Pane sản phẩm vào AnchorPane "bgV"
                bgV.getChildren().add(productPane);

                productIndex++; // Tăng chỉ số sản phẩm
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
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
    }

}
