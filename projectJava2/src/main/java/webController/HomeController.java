package webController;

import db.connect;
import java.io.File;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import main.Main;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import models.productCart;
import models.UserSession;

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

    @FXML
    private Label Cart;
    @FXML
    private Label Address;
    @FXML
    private Label listOder;

    public static int categoryIDetail;
    public static int categoryIDetail1;
    public static int categoryIDetail2;

    public static List<Product> selectedProducts = new ArrayList<>();
    int userId = UserSession.getInstance().getUserId();

    // Constructor để truyền vào đối tượng HttpServletRequest
    public static class Product {

        public int userId;
        public int productId;
        public String productName;
        public String imagePath;
        public float productPrice;
        public int quantity;
        public float totalPrice;

        public float getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(float totalPrice) {
            this.totalPrice = totalPrice;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public Product(int userId, int productId, String productName, float productPrice, String imagePath) {
            this.userId = userId;
            this.productId = productId;
            this.productName = productName;
            this.productPrice = productPrice;
            this.imagePath = imagePath;
        }

        public int getProductId() {
            return productId;
        }

        public int getUserId() {
            return userId;
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
    public cartController cartControllerInstance;

    public void setCartController(cartController cartControllerInstance) {
        this.cartControllerInstance = cartControllerInstance;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("abc");
        displayNewestProducts();
        displayProducts2();
        displayProducts3();
        categoryName();
        Cart.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    redirectToCart();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        Address.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    FromAddress();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void displayNewestProducts() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = connect.getConnection();
            String query = "SELECT importGoods.productName,product.img,product.categoryId,product.productId,"
                    + " product.Description, importgoods.price ,category.categoryName"
                    + " FROM product "
                    + " INNER JOIN category ON product.categoryId = category.categoryId"
                    + " INNER JOIN importgoods ON importgoods.import_id = product.importProductNameId"
                    + " WHERE product.categoryId =1";

            statement = connection.prepareStatement(query);

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
                        Product selectedProduct = new Product(userId, productId, productName, productPrice, imagePath);
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

                        String query2 = "SELECT product.productId, product.idSupplier, product.categoryId, importgoods.price, product.description, product.img, importgoods.productName"
                                + " FROM product"
                                + " INNER JOIN importgoods ON product.importProductNameId = importgoods.import_id"
                                + " WHERE product.productId = ?";

                        preparedStatement = connection2.prepareStatement(query2);
                        preparedStatement.setInt(1, productId);

                        resultSet4 = preparedStatement.executeQuery();

                        if (resultSet4.next()) {
                            String description = resultSet4.getString("description");
                            String productName4 = resultSet4.getString("productName");
                            Float productPrice4 = resultSet4.getFloat("price");
                            String imagePath4 = resultSet4.getString("img");

                            // Format productPrice4 with commas
                            DecimalFormat decimalFormat2 = new DecimalFormat("#,##0");
                            String formattedProductPrice2 = decimalFormat2.format(productPrice4);

                            showAlertWithProductInfo(productName4, formattedProductPrice2, description, imagePath4);
                        } else {
                            // Product not found
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
                }
                );

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
        }
    }

    private void displayProducts2() {
        Connection connection = null;
        PreparedStatement statement2 = null;
        ResultSet resultSet2 = null;

        try {
            connection = connect.getConnection();

            String query3 = "SELECT importGoods.productName, product.img, product.categoryId,product.productId, product.Description, importgoods.productImportPrice, importgoods.price ,category.categoryName"
                    + " FROM product "
                    + " INNER JOIN category ON product.categoryId = category.categoryId"
                    + " INNER JOIN importgoods ON importgoods.import_id = product.importProductNameId"
                    + " WHERE product.categoryId = 2";

            statement2 = connection.prepareStatement(query3);

            resultSet2 = statement2.executeQuery();

            int productSpacing = 1; // Khoảng cách giữa các sản phẩm
            int productWidth = 190; // Chiều rộng của mỗi sản phẩm
            int productHeight = 205; // Chiều cao của mỗi sản phẩm
            int startX = 21; // Vị trí ban đầu theo trục X
            int startY = 4; // Vị trí ban đầu theo trục Y

            int productIndex = 0; // Chỉ số của sản phẩm

            while (resultSet2.next()) {
                String productName = resultSet2.getString("ProductName");
                String imagePath = resultSet2.getString("img");
                Float productPrice = resultSet2.getFloat("price");
//                System.out.println("------------------------------");
//                System.out.println("Product ID: " + productName);
//                System.out.println("Product Name: " + imagePath);
//                System.out.println("Product Price: " + productPrice);
//                System.out.println("------------------------------");
                // Tạo VBox để chứa các thành phần
                VBox productBox = new VBox();
                productBox.setSpacing(2); // Đặt khoảng cách giữa các thành phần là 2px
                productBox.setAlignment(Pos.CENTER);

                // Tạo ImageView cho sản phẩm
                ImageView productImageView = new ImageView();
                productImageView.setFitWidth(130);
                productImageView.setFitHeight(120); // Đặt chiều cao cố định
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

                Button buyButton = new Button("Buy");
                buyButton.setPrefWidth(140);
                buyButton.setPrefHeight(25);
                buyButton.setAlignment(Pos.CENTER);
                buyButton.setStyle("-fx-background-radius: 20; -fx-background-color: #FCE4EC;");

                Button describeButton = new Button("Describe");
                describeButton.setPrefWidth(140);
                describeButton.setPrefHeight(25);
                describeButton.setAlignment(Pos.CENTER);
                describeButton.setStyle("-fx-background-radius: 20; -fx-background-color: #FCE4EC;");

// Lưu giá trị product.productId vào nút "Buy"
                buyButton.setUserData(resultSet2.getInt("productId"));

// Lưu giá trị product.productId vào nút "Describe"
                describeButton.setUserData(resultSet2.getInt("productId"));

// Thêm sự kiện cho nút "Buy" để xử lý khi được nhấp
                buyButton.setOnAction(event -> {

                    int productId = (int) buyButton.getUserData();
                    if (userId == 0) {
                        try {
                            // Nếu userId không tồn tại
                            // Chuyển hướng đến trang đăng nhập
                            redirectToLogin(); // Định nghĩa phương thức redirectToLogin() để thực hiện việc này

                        } catch (IOException ex) {
                            Logger.getLogger(HomeController.class
                                    .getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        // Ngược lại, thêm sản phẩm vào giỏ hàng
                        Product selectedProduct = new Product(userId, productId, productName, productPrice, imagePath);
                        selectedProducts.add(selectedProduct);
                        showAlert("Product added to cart successfully!");
                        productCart.getInstance().setSelectedProducts(selectedProducts);
                    }

                });
// Thêm sự kiện cho nút "Describe" để xử lý khi được nhấp
                describeButton.setOnAction(event -> {
                    int productId = (int) describeButton.getUserData();

                    // Sử dụng lại kết nối cơ sở dữ liệu đã thiết lập trước đó
                    Connection connection3 = null;     // Truy vấn SQL với INNER JOIN
                    PreparedStatement preparedStatement = null;
                    ResultSet resultSet4 = null;

                    try {
                        connection3 = connect.getConnection();

                        String query2 = "SELECT product.productId, product.idSupplier, product.categoryId, importgoods.price, product.description, product.img, importgoods.productName"
                                + " FROM product"
                                + " INNER JOIN importgoods ON product.importProductNameId = importgoods.import_id"
                                + " WHERE product.productId = ?";

                        preparedStatement = connection3.prepareStatement(query2);
                        preparedStatement.setInt(1, productId);

                        resultSet4 = preparedStatement.executeQuery();

                        if (resultSet4.next()) {
                            String description = resultSet4.getString("description");
                            String productName4 = resultSet4.getString("productName");
                            Float productPrice4 = resultSet4.getFloat("price");
                            String imagePath4 = resultSet4.getString("img");

                            // Format productPrice4 with commas
                            DecimalFormat decimalFormat2 = new DecimalFormat("#,##0");
                            String formattedProductPrice2 = decimalFormat2.format(productPrice4);

                            showAlertWithProductInfo(productName4, formattedProductPrice2, description, imagePath4);
                        } else {
                            // Product not found
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

            String query4 = "SELECT importGoods.productName, product.img, product.categoryId,product.productId, product.Description, importgoods.productImportPrice, importgoods.price ,category.categoryName"
                    + " FROM product "
                    + " INNER JOIN category ON product.categoryId = category.categoryId"
                    + " INNER JOIN importgoods ON importgoods.import_id = product.importProductNameId"
                    + " WHERE product.categoryId = 3";

            statement3 = connection.prepareStatement(query4);

            resultSet3 = statement3.executeQuery();

            int productSpacing = 1; // Khoảng cách giữa các sản phẩm
            int productWidth = 190; // Chiều rộng của mỗi sản phẩm
            int productHeight = 205; // Chiều cao của mỗi sản phẩm
            int startX = 21; // Vị trí ban đầu theo trục X
            int startY = 4; // Vị trí ban đầu theo trục Y

            int productIndex = 0; // Chỉ số của sản phẩm

            while (resultSet3.next()) {
                String productName = resultSet3.getString("ProductName");
                String imagePath = resultSet3.getString("img");
                Float productPrice = resultSet3.getFloat("price");

                // Tạo VBox để chứa các thành phần
                VBox productBox = new VBox();
                productBox.setSpacing(2); // Đặt khoảng cách giữa các thành phần là 2px
                productBox.setAlignment(Pos.CENTER);

                ImageView productImageView = new ImageView();
                productImageView.setFitWidth(130);
                productImageView.setFitHeight(120); // Đặt chiều cao cố định
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
                Button buyButton = new Button("Buy");
                buyButton.setPrefWidth(140);
                buyButton.setPrefHeight(25);
                buyButton.setAlignment(Pos.CENTER);
                buyButton.setStyle("-fx-background-radius: 20; -fx-background-color: #FCE4EC;");

                Button describeButton = new Button("Describe");
                describeButton.setPrefWidth(140);
                describeButton.setPrefHeight(25);
                describeButton.setAlignment(Pos.CENTER);
                describeButton.setStyle("-fx-background-radius: 20; -fx-background-color: #FCE4EC;");

                describeButton.setUserData(resultSet3.getInt("productId"));
                // Lưu giá trị product.productId vào nút "Buy"
                buyButton.setUserData(resultSet3.getInt("productId"));

// Thêm sự kiện cho nút "Buy" để xử lý khi được nhấp
                buyButton.setOnAction(event -> {
                    int productId = (int) buyButton.getUserData();
                    if (userId == 0) {
                        try {
                            // Nếu userId không tồn tại
                            // Chuyển hướng đến trang đăng nhập
                            redirectToLogin(); // Định nghĩa phương thức redirectToLogin() để thực hiện việc này

                        } catch (IOException ex) {
                            Logger.getLogger(HomeController.class
                                    .getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        // Ngược lại, thêm sản phẩm vào giỏ hàng
                        Product selectedProduct = new Product(userId, productId, productName, productPrice, imagePath);
                        selectedProducts.add(selectedProduct);
                        showAlert("Product added to cart successfully!");
                        productCart.getInstance().setSelectedProducts(selectedProducts);
                    }

                });
// Thêm sự kiện cho nút "Describe" để xử lý khi được nhấp
                describeButton.setOnAction(event -> {
                    int productId = (int) describeButton.getUserData();

                    // Sử dụng lại kết nối cơ sở dữ liệu đã thiết lập trước đó
                    Connection connection4 = null;     // Truy vấn SQL với INNER JOIN
                    PreparedStatement preparedStatement = null;
                    ResultSet resultSet4 = null;

                    try {
                        connection4 = connect.getConnection();

                        String query2 = "SELECT product.productId, product.idSupplier, product.categoryId, importgoods.price, product.description, product.img, importgoods.productName"
                                + " FROM product"
                                + " INNER JOIN importgoods ON product.importProductNameId = importgoods.import_id"
                                + " WHERE product.productId = ?";

                        preparedStatement = connection4.prepareStatement(query2);
                        preparedStatement.setInt(1, productId);

                        resultSet4 = preparedStatement.executeQuery();

                        if (resultSet4.next()) {
                            String description = resultSet4.getString("description");
                            String productName4 = resultSet4.getString("productName");
                            Float productPrice4 = resultSet4.getFloat("price");
                            String imagePath4 = resultSet4.getString("img");

                            // Format productPrice4 with commas
                            DecimalFormat decimalFormat2 = new DecimalFormat("#,##0");
                            String formattedProductPrice2 = decimalFormat2.format(productPrice4);

                            showAlertWithProductInfo(productName4, formattedProductPrice2, description, imagePath4);
                        } else {
                            // Product not found
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

//    public void displaySelectedProducts() {
//        for (Product product : selectedProducts) {
//            System.out.println("Product ID: " + product.getProductId());
//            System.out.println("Product Name: " + product.getProductName());
//            System.out.println("Product Price: " + product.getProductPrice());
//            System.out.println("Image Path: " + product.getImagePath());
//            System.out.println("userID: " + product.getUserId());
//
//            System.out.println("------------------------------");
//        }
//    }
    public void categoryName() {

        try (Connection connection = connect.getConnection();) {

            String query2 = "SELECT  product.categoryId,category.categoryName"
                    + " FROM product "
                    + " INNER JOIN category ON product.categoryId = category.categoryId"
                    + " WHERE product.categoryId = 2";
            PreparedStatement statement2 = connection.prepareStatement(query2);

            ResultSet resultSet2 = statement2.executeQuery();
            if (resultSet2.next()) {
                categoryIDetail1 = resultSet2.getInt("categoryId");
                String categoryName = resultSet2.getString("categoryName");
                // Đặt giá trị categoryName cho nút Button
                categoryId1.setText(categoryName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (Connection connection = connect.getConnection();) {

            String query = "SELECT  product.categoryId,category.categoryName"
                    + " FROM product "
                    + " INNER JOIN category ON product.categoryId = category.categoryId"
                    + " WHERE product.categoryId = 1";
            PreparedStatement statement = connection.prepareStatement(query);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                categoryIDetail = resultSet.getInt("categoryId");
                String categoryName = resultSet.getString("categoryName");
                categoryId.setText(categoryName);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (Connection connection = connect.getConnection();) {

            String query3 = "SELECT  product.categoryId,category.categoryName"
                    + " FROM product "
                    + " INNER JOIN category ON product.categoryId = category.categoryId"
                    + " WHERE product.categoryId = 3";
            PreparedStatement statement3 = connection.prepareStatement(query3);

            ResultSet resultSet3 = statement3.executeQuery();
            if (resultSet3.next()) {
                categoryIDetail2 = resultSet3.getInt("categoryId");
                String categoryName = resultSet3.getString("categoryName");
                // Đặt giá trị categoryName cho nút Button
                categoryId2.setText(categoryName);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlertWithProductInfo(String productName, String formattedProductPrice, String description, String imagePath) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Product Information");
        alert.setHeaderText(null); // Remove the header

        // Create an ImageView for the product and set its properties
        ImageView productImageView = new ImageView();
        productImageView.setFitWidth(130);
        productImageView.setFitHeight(120);
        productImageView.setPreserveRatio(false);

        // Use the absolute image path
        File imageFile = new File(imagePath);
        String absoluteImagePath = imageFile.toURI().toString();
        Image image = new Image(absoluteImagePath);
        productImageView.setImage(image);

        // Create a VBox to contain ImageView and product information
        VBox vbox = new VBox(10); // 10 is the spacing between ImageView and text
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(productImageView, new Text("Product Name: " + productName), new Text("Price: $" + formattedProductPrice), new Text("Description: " + description));

        // Set the content of the dialog
        alert.getDialogPane().setContent(vbox);

        // Add an OK button
        alert.getButtonTypes().setAll(ButtonType.OK);

        // Apply CSS styles directly using inline styles
        alert.getDialogPane().setStyle(
                "-fx-alignment: center; "
                + "-fx-text-alignment: center;"
        );

        // ShowAndWait and wait for the OK button to be clicked
        alert.showAndWait();
    }

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
        int userId = UserSession.getInstance().getUserId();

        if (userId != 0) {
            // Nếu userId khác 0 (hoặc giá trị mặc định khác), chuyển đến trang giỏ hàng
            Main.setRoot("/web/cart.fxml");
        } else {
            // Nếu userId bằng 0 (hoặc giá trị mặc định), gọi hàm redirectToLogin()
            redirectToLogin();
        }
    }

    public void redirectToLogin() throws IOException {
        Main.setRoot("/admin/login.fxml");
    }

    public void FromAddress() throws IOException {
        Main.setRoot("/web/contact.fxml");
    }

    public void getHome() throws IOException {
        Main.setRoot("/web/home.fxml");
    }

}
