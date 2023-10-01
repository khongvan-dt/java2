package webController;

import db.connect;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.fxml.Initializable;
import main.Main;

/**
 *
 * @author Administrator
 */
public class relatedProductsController implements Initializable {

    @FXML
    private AnchorPane bgV;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Label categoryId1;

    private int categoryId;

    public relatedProductsController() {
        // Constructor mặc định không có đối số
    }

    @FXML
    private Label categoryLabel;

    private int categoryID;

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public relatedProductsController(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        int categoryId = SharedData.getCategoryId(); // Get the category ID
        System.out.println("Category ID in relatedProductsController: " + categoryId);
        displayRelatedProducts(categoryId);
    }

    public void displayRelatedProducts(int categoryId) {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = connect.getConnection();

            String query = "SELECT ProductsName.ProductName, product.img, importgoods.price, product.categoryId, category.categoryName, category.categoryId "
                    + "FROM product "
                    + "INNER JOIN ProductsName ON product.ProductNameId = ProductsName.ProductNameId "
                    + "INNER JOIN importgoods ON importgoods.ProductNameId = product.ProductNameId "
                    + "INNER JOIN supplier ON product.supplier_id = supplier.supplierId "
                    + "INNER JOIN category ON product.categoryId = category.categoryId "
                    + "WHERE product.categoryId = ?";

            statement = connection.prepareStatement(query);
            statement.setInt(1, categoryId);

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
                categoryId1.setText(categoryName);
            }

            while (resultSet.next()) {
                String productName = resultSet.getString("ProductName");
                String imagePath = resultSet.getString("img");
                Float productPrice = resultSet.getFloat("price");
                System.out.println("--------------------------------------------------------------------------");
                System.out.println("price" + productPrice);
                System.out.println("imagePath:" + imagePath);
                System.out.println("productName:" + productName);
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

    public void getFromHome() throws IOException {
        Main.setRoot("/web/home.fxml");
    }
}
