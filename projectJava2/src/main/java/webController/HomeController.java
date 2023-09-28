package webController;

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
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import models.Product;

public class HomeController implements Initializable {

    @FXML
    private ImageView imageView;
    @FXML
    private Pane newestProducts;

    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("abc");
        displayNewestProducts();
     

    }

    private void displayNewestProducts() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Product> lstProduct = new ArrayList<>();
        // get db
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/project2", "root", "");

            // Truy vấn cơ sở dữ liệu để lấy thông tin tất cả các sản phẩm
            String query = "SELECT product.*,productsname.ProductName FROM product inner join productsname limit 4 offset 0";
            statement = connection.prepareStatement(query);

            resultSet = statement.executeQuery();

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
            String imageUrl = "file:///C:\\java2\\projectJava2\\" + prd.getImagePath();
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
                // dua pane vao pane fx:id=newestproduct
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
    }
}
