package webController;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.sql.*;

public class ProductDetailsController {

    @FXML
    private ImageView productImageView;
    @FXML
    private Label descriptionLabel;

    private int productId;

    public void setProductId(int productId) {
        this.productId = productId;
        loadProductDetails();
    }
    private String ProductName;

    public void setProductName(String productId) {
        this.ProductName = ProductName;
        loadProductDetails();
    }

    private void loadProductDetails() {
        // Kết nối đến cơ sở dữ liệu
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/project2", "thaoit", "It1234!")) {
            // Tạo câu lệnh SQL SELECT để lấy thông tin sản phẩm từ bảng product
            String sql = "SELECT ProductName FROM product WHERE ProductName = ?";

            // Chuẩn bị câu lệnh SQL
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, productId);

                // Thực thi câu lệnh SELECT
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {

                        String description = resultSet.getString("ProductName");

                        // Hiển thị thông tin sản phẩm lên giao diện
                        descriptionLabel.setText(description);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
