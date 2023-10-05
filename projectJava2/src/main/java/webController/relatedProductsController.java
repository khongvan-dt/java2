package webController;

import db.connect;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.Alert;
import main.Main;
import models.UserSession;
import models.productCart;
import static webController.HomeController.selectedProducts;

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
    int userId = UserSession.getInstance().getUserId();

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
        categoryName(categoryId);
    }

    public void displayRelatedProducts(int categoryId) {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = connect.getConnection();

            String query = "SELECT importGoods.productName,product.img,product.categoryId,product.productId,"
                    + " product.Description, importgoods.productImportPrice, importgoods.price ,category.categoryName"
                    + " FROM product "
                    + " INNER JOIN category ON product.categoryId = category.categoryId"
                    + " INNER JOIN importgoods ON importgoods.import_id = product.importProductNameId"
                    + " WHERE product.categoryId =?";

            statement = connection.prepareStatement(query);
            statement.setInt(1, categoryId);

            resultSet = statement.executeQuery();

            int productSpacing = 1; // Khoảng cách giữa các sản phẩm
            int productWidth = 190; // Chiều rộng của mỗi sản phẩm
            int productHeight = 205; // Chiều cao của mỗi sản phẩm
            int startX = 21; // Vị trí ban đầu theo trục X
            int startY = 4; // Vị trí ban đầu theo trục Y

            int productIndex = 0; // Chỉ số của sản phẩm

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
                productImageView.setFitWidth(130);
                productImageView.setFitHeight(120);
                productImageView.setPreserveRatio(false);
                // Sử dụng đường dẫn tuyệt đối đến tệp hình ảnh
                File imageFile = new File(imagePath);
                String absoluteImagePath = imageFile.toURI().toString();
                Image image = new Image(absoluteImagePath);
                System.out.println("đường dẫn ảnh ____________" + absoluteImagePath);
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
                DecimalFormat decimalFormat = new DecimalFormat("#,##0");
                String formattedTotalPrice = decimalFormat.format(productPrice);
                Label productPriceLabel = new Label("Price: " + formattedTotalPrice);
                productPriceLabel.setPrefWidth(140);
                productPriceLabel.setPrefHeight(25);
                productPriceLabel.setAlignment(Pos.CENTER);
// nút mua

                Button buyButton = new Button("Buy");
                buyButton.setPrefWidth(140);
                buyButton.setPrefHeight(25);
                buyButton.setAlignment(Pos.CENTER);
//nút mô tả
                Button describeButton = new Button("Describe");
                describeButton.setPrefWidth(140);
                describeButton.setPrefHeight(25);
                describeButton.setAlignment(Pos.CENTER);

// Đặt CSS cho nút "Describe"  
                describeButton.setStyle("-fx-background-radius: 20; -fx-background-color: #FCE4EC;");

// Đặt CSS cho nút "Buy"
                buyButton.setStyle("-fx-background-radius: 20; -fx-background-color: #FCE4EC;");

// Lưu giá trị product.productId vào nút "Describe"
                describeButton.setUserData(resultSet.getInt("productId"));
// Lưu giá trị product.productId vào nút "Buy"
                buyButton.setUserData(resultSet.getInt("productId"));

// Thêm sự kiện cho nút "Buy" để xử lý khi được nhấp
                buyButton.setOnAction(event -> {
                    int productId = (int) buyButton.getUserData();
                    if (userId == 0) {
                        try {
                            // Nếu userId không tồn tại
                            // Chuyển hướng đến trang đăng nhập
                            redirectToLogin(); // Định nghĩa phương thức redirectToLogin() để thực hiện việc này
                        } catch (IOException ex) {
                            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        // Ngược lại, thêm sản phẩm vào giỏ hàng
                        HomeController.Product selectedProduct = new HomeController.Product(userId, productId, productName, productPrice, imagePath);
                        selectedProducts.add(selectedProduct);
                        showAlert("Product added to cart successfully!");
                        productCart.getInstance().setSelectedProducts(selectedProducts);
                    }
                });

// Thêm sự kiện cho nút "Describe" để xử lý khi được nhấp
                describeButton.setOnAction(event -> {
                    int productId = (int) describeButton.getUserData();

                    // Sử dụng lại kết nối cơ sở dữ liệu đã thiết lập trước đó
                    Connection connection2 = null;     // Truy vấn SQL với INNER JOIN
                    PreparedStatement preparedStatement = null;
                    ResultSet resultSet4 = null;

                    try {
                        connection2 = connect.getConnection();

                        String query2 = "SELECT product.productId, product.idSupplier, product.categoryId, product.importProductNameId, product.description, product.img, importgoods.productName"
                                + " FROM product"
                                + " INNER JOIN importgoods ON product.importProductNameId = importgoods.import_id"
                                + " WHERE product.productId = ?";

                        preparedStatement = connection2.prepareStatement(query2);
                        preparedStatement.setInt(1, productId);

                        resultSet4 = preparedStatement.executeQuery();

                        if (resultSet4.next()) {
                            String description = resultSet4.getString("description");
                            String productName4 = resultSet4.getString("productName");

                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Product Information");
                            alert.setHeaderText(null); // Xóa tiêu đề phụ (header)
                            alert.setContentText("Product Name: " + productName4 + "\nDescription: " + description);

// Chỉnh CSS trực tiếp bằng phong cách inline
                            alert.getDialogPane().setStyle(
                                    "-fx-alignment: center; "
                                    + // Căn giữa hộp thoại
                                    "-fx-text-alignment: center;" // Căn giữa nội dung văn bản
                            );

                            alert.showAndWait();

                        } else {
                            // Sản phẩm không tồn tại
                            showAlert("Product not found.");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        // Xử lý lỗi kết nối hoặc truy vấn
                    } finally {
                        // Đóng tài nguyên liên quan
                        try {
                            if (resultSet4 != null) {
                                resultSet4.close();
                            }
                            if (preparedStatement != null) {
                                preparedStatement.close();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });

// Đưa Button và các thành phần khác vào VBox
                productBox.getChildren().addAll(productImageView, productNameLabel, productPriceLabel, buyButton, describeButton);

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

    public void categoryName(int categoryId) {

        try (Connection connection = connect.getConnection();) {

            String query = "SELECT  product.categoryId,category.categoryName"
                    + " FROM product "
                    + " INNER JOIN category ON product.categoryId = category.categoryId"
                    + " WHERE product.categoryId = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, categoryId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String categoryName = resultSet.getString("categoryName");
                // Đặt giá trị categoryName cho nút Button
                categoryId1.setText(categoryName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void redirectToLogin() throws IOException {
        Main.setRoot("/admin/login.fxml");
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void getFromHome() throws IOException {
        Main.setRoot("/web/home.fxml");
    }

    public void redirectToCart() throws IOException {
        int userId = UserSession.getInstance().getUserId();

        if (userId != 0) {
            // Nếu userId khác 0 (hoặc giá trị mặc định khác), chuyển đến trang giỏ hàng
            Main.setRoot("/web/cart.fxml");
        } else {
            // Nếu userId bằng 0 (hoặc giá trị mặc định), gọi hàm redirectToLogin()
            redirectToLogin();
        }
    }

}
