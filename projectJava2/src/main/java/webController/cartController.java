package webController;

import adminController.loginController;
import db.connect;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.HashMap;
import javafx.fxml.FXML;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import main.Main;
import models.productCart;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import models.UserSession;

public class cartController implements Initializable {

    @FXML
    private ScrollPane big;

    @FXML
    private Label sumPrice;

    @FXML
    private VBox small;

    @FXML
    private TextField note;

    @FXML
    private TextField address;
    @FXML
    private TextField phone;

    @FXML
    private TextField customerName;
    @FXML
    private Label noProductsLabel;

    private int quantity;

    public class oder {

        public int oderID;

        public int getOderid() {
            return oderID;
        }

        // Định nghĩa phương thức để xóa danh sách sản phẩm đã chọn
        public void clearSelectedProducts() {
            selectedProducts.clear();
        }
    }

    List<HomeController.Product> selectedProducts = productCart.getInstance().getSelectedProducts();

    public void setSelectedProducts(List<HomeController.Product> selectedProducts) {
        this.selectedProducts = selectedProducts;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Map<Integer, Integer> productQuantityMap = new HashMap<>();

// Trong phương thức initialize:
        if (selectedProducts.isEmpty()) {
            noProductsLabel.setText("No products.");
        }
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
                Label quantityLabel = new Label("Quantity: " + product.getQuantity());

                HBox quantityControls = new HBox(10, decreaseButton, quantityLabel, increaseButton);
                quantityControls.setLayoutX(650);
                quantityControls.setLayoutY(58);

// Increase quantity
                increaseButton.setOnAction(event -> {
                    int updatedQuantity = product.getQuantity() + 1;
                    product.setQuantity(updatedQuantity);
                    quantityLabel.setText("Quantity: " + updatedQuantity);
                });

// Decrease quantity (minimum 1)
                decreaseButton.setOnAction(event -> {
                    int updatedQuantity = product.getQuantity() - 1;
                    if (updatedQuantity >= 1) {
                        product.setQuantity(updatedQuantity);
                        quantityLabel.setText("Quantity: " + updatedQuantity);
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

    public void oderInssert() throws IOException {
        String CustomerName = customerName.getText().trim();
        String Note = note.getText().trim();
        String Address = address.getText().trim();
        String phoneNumber = phone.getText().trim();
        int userId = UserSession.getInstance().getUserId();
        LocalDate currentDate = getCurrentDate();
        String vietnamPhonePattern = "^(\\+?84|0)([3|5|7|8|9]\\d{8})$";
        Pattern pattern = Pattern.compile(vietnamPhonePattern);
        Matcher matcher = pattern.matcher(phoneNumber);

        if (!matcher.matches()) {
            showAlert("Invalid phone number");
            return;
        }

        if (CustomerName.isEmpty() || Address.isEmpty()) {
            showAlert("Please fill in all fields.");
            return;
        }

        int orderId = insertOrderAndRetrieveId(CustomerName, Address, Note, phoneNumber, userId, currentDate);

        if (orderId > 0) {
            boolean orderDetailsInserted = true; // Flag to track order details insertion

            // Iterate through selected products and insert them into "orderDetails" table
            for (HomeController.Product product : selectedProducts) {
                int productId = product.getProductId();
                int productQuantity = product.getQuantity();

                // Insert the product into "orderDetails" table
                boolean productInserted = insertProductToOrderDetails(orderId, productId, productQuantity, product.getProductPrice());

                if (!productInserted) {
                    orderDetailsInserted = false; // Set the flag to false if insertion fails
                    break; // Exit the loop if there's an insertion failure
                }
            }

            if (orderDetailsInserted) {
                System.out.println("Success order details!");
                showSuccessAlert("Success order details!");

                // Clear input fields after successful insertion
                note.clear();
                customerName.clear();
                phone.clear();
                address.clear();
                clearSelectedProducts();

                getSuccessCart();
            } else {
                showAlert("Not Success order details.");
            }
        } else {
            showAlert("Not Success order.");
        }
    }

// Insert an order into the "orders" table and return the inserted order_id
    private int insertOrderAndRetrieveId(String customerName, String address, String note, String phoneNumber, int userId, LocalDate currentDate) {
        String insertSQL = "INSERT INTO orders (customerName, address, orderNotes, phoneNumber, userId, orderDate) VALUES (?, ?, ?, ?, ?, ?)";
        int orderId = -1;

        try (Connection connection = connect.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, customerName);
            preparedStatement.setString(2, address); // Change to lowercase 'address'
            preparedStatement.setString(3, note);    // Change to lowercase 'note'
            preparedStatement.setString(4, phoneNumber);
            preparedStatement.setInt(5, userId);
            preparedStatement.setDate(6, java.sql.Date.valueOf(currentDate));

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    orderId = generatedKeys.getInt(1); // Retrieve the inserted order_id
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderId;
    }

// Insert a product into the "orderDetails" table
    private boolean insertProductToOrderDetails(int orderId, int productId, int quantity, float productPrice) {
        String insertSQL2 = "INSERT INTO orderdetails (orderId, product_id, quantity, price, totalPrice) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = connect.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(insertSQL2)) {
            preparedStatement.setInt(1, orderId);
            preparedStatement.setInt(2, productId);
            preparedStatement.setInt(3, quantity);
            preparedStatement.setFloat(4, productPrice);
            preparedStatement.setFloat(5, quantity * productPrice);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected <= 0) {
                return false; // Return false if insertion fails
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false in case of an exception
        }

        return true; // Return true if insertion is successful
    }

    public int getInsertedOrderId() {
        int orderId = -1;
        try (Connection connection = connect.getConnection(); Statement statement = connection.createStatement()) {
            String query = "SELECT MAX(order_id) FROM orders"; // Sử dụng tên cột "order_id"
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                orderId = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderId;
    }

    public LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    private void clearSelectedProducts() {
        productCart.getInstance().clearSelectedProducts();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void getHome() throws IOException {
        Main.setRoot("/web/home.fxml");
    }

    private void getSuccessCart() throws IOException {
        Main.setRoot("/web/dathangthanhcong.fxml");
    }
     public void handleLogout(ActionEvent event) throws IOException {
        // Tạo một thể hiện của lớp logOut và thiết lập tham chiếu đến loginController
        loginController logoutHandler = new loginController();
        logoutHandler.handleLogout();
    }

}
