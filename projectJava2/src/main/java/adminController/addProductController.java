package adminController;

import adminController.addProductController.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import db.connect;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.Main;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TableCell;
import webController.HomeController;

public class addProductController extends Application {

    public HomeController.Product Product;

    public class Product {

        public SimpleStringProperty productName;
        public SimpleStringProperty img;
        public SimpleStringProperty supplierName;
        public SimpleStringProperty description;
        public SimpleStringProperty importPrice;
        public SimpleStringProperty price;
        public SimpleStringProperty imgPath;
        public SimpleStringProperty id;
        public SimpleStringProperty category;

        public Product(String productName, String img, String supplierName, String description, String importPrice, String price) {
            this.productName = new SimpleStringProperty(productName);
            this.img = new SimpleStringProperty(img);
            this.supplierName = new SimpleStringProperty(supplierName);
            this.description = new SimpleStringProperty(description);
            this.importPrice = new SimpleStringProperty(importPrice);
            this.price = new SimpleStringProperty(price);
            this.imgPath = new SimpleStringProperty(img);

        }

        public String getImgPath() {
            return imgPath.get();
        }

        public String getProductName() {
            return productName.get();
        }

        public String getImg() {
            return img.get();
        }

        public String getSupplierName() {
            return supplierName.get();
        }

        public String getDescription() {
            return description.get();
        }

        public String getImportPrice() {
            return importPrice.get();
        }

        public String getPrice() {
            return price.get();
        }

        public String getCategory() {

            return category.get();
        }

        public String getId() {
            return id.get();
        }
    }

    public Map<String, Integer> categoryNameidMap = new HashMap<>();
    public Map<String, Integer> productNameIdMap = new HashMap<>();
    public Map<String, Integer> supplierNamesMap = new HashMap<>();

    @FXML
    public ComboBox<String> fieldViewProductCategoryId;

    @FXML
    public ComboBox<String> fieldViewProductName;

    @FXML
    public ComboBox<String> supplierId;

    @FXML
    public TextArea fieldViewProductDescriptions;

    public File selectedImageFile;

    public ObservableList<Product> productList = FXCollections.observableArrayList();

    @FXML
    public TableView<Product> produtTable;

    @FXML
    public TableColumn<Product, String> productNameColumn;

    @FXML
    public TableColumn<Product, String> imgColumn;

    @FXML
    public TableColumn<Product, String> supplierNameColumn;

    @FXML
    public TableColumn<Product, String> descriptionColumn;

    @FXML
    public TableColumn<Product, String> importPriceColumn;

    @FXML
    public TableColumn<Product, String> priceColumn;

    @FXML
    public void initialize() {
        tableProduct();
        dataCombox();
    }

    public void dataCombox() {
        try (Connection connection = connect.getConnection()) {
            // Tạo một truy vấn SQL để lấy dữ liệu danh mục sản phẩm từ cơ sở dữ liệu
            String selectCategory = "SELECT categoryId,categoryName FROM category ";
            // Tạo một PreparedStatement để thực thi truy vấn SQL
            PreparedStatement preparedStatement = connection.prepareStatement(selectCategory);
            // Thực thi truy vấn và lấy kết quả
            ResultSet resultSet = preparedStatement.executeQuery();
            ObservableList<String> category = FXCollections.observableArrayList();
            while (resultSet.next()) {
                int categoryId = resultSet.getInt("categoryId");
                String categoryName = resultSet.getString("categoryName");
                category.add(categoryName);
                categoryNameidMap.put(categoryName, categoryId);
            }
            fieldViewProductCategoryId.setItems(category);

            String selectProduct = "SELECT importGoods.ProductNameId, ProductsName.ProductName FROM importGoods "
                    + "INNER JOIN ProductsName ON importGoods.ProductNameId = ProductsName.ProductNameId "
                    + "GROUP BY importGoods.supplier_id, importGoods.ProductNameId";
            PreparedStatement preparedStatement2 = connection.prepareStatement(selectProduct);
            ResultSet resultSet2 = preparedStatement2.executeQuery();
            ObservableList<String> productNames = FXCollections.observableArrayList();
            while (resultSet2.next()) {
                int productNameId = resultSet2.getInt("importGoods.ProductNameId");
                String productsName = resultSet2.getString("ProductsName.ProductName");
                productNames.add(productsName);
                productNameIdMap.put(productsName, productNameId);
            }
            fieldViewProductName.setItems(productNames);

            String selectSupplier = "SELECT importGoods.supplier_id, supplier.supplierName FROM importGoods "
                    + "INNER JOIN supplier ON importGoods.supplier_id = supplier.supplierId "
                    + "GROUP BY importGoods.supplier_id";
            PreparedStatement preparedStatement3 = connection.prepareStatement(selectSupplier);
            ResultSet resultSet3 = preparedStatement3.executeQuery();
            ObservableList<String> suppliers = FXCollections.observableArrayList();
            while (resultSet3.next()) {
                int supplierId = resultSet3.getInt("importGoods.supplier_id");
                String supplierNames = resultSet3.getString("supplier.supplierName");
                suppliers.add(supplierNames);
                supplierNamesMap.put(supplierNames, supplierId);
            }
            supplierId.setItems(suppliers);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void tableProduct() {
        try (Connection connection = connect.getConnection()) {
            String query = "SELECT ProductsName.ProductName, product.img, supplier.supplierName,"
                    + " product.Description, importgoods.productImportPrice, importgoods.price "
                    + "FROM product "
                    + "INNER JOIN ProductsName ON product.ProductNameId = ProductsName.ProductNameId "
                    + "INNER JOIN importgoods ON importgoods.ProductNameId = product.ProductNameId "
                    + "INNER JOIN supplier ON product.supplier_id = supplier.supplierId";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String productName = resultSet.getString("ProductName");
                String img = resultSet.getString("img");
                String supplierName = resultSet.getString("supplierName");
                String description = resultSet.getString("Description");
                String importPrice = resultSet.getString("productImportPrice");
                String price = resultSet.getString("price");

                Product product = new Product(productName, img, supplierName, description, importPrice, price);
                productList.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Set the items of the TableView to the productList
        produtTable.setItems(productList);
                                System.out.println("==========" + produtTable);

        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        supplierNameColumn.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        importPriceColumn.setCellValueFactory(new PropertyValueFactory<>("importPrice"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        imgColumn.setCellFactory(column -> {
            return new TableCell<Product, String>() {
                private final ImageView imageView = new ImageView();

                @Override
                protected void updateItem(String imagePath, boolean empty) {
                    super.updateItem(imagePath, empty);

                    if (empty || imagePath == null) {
                        setGraphic(null);
                    } else {
                        try {
                            // Print debug information
                            System.out.println("imagePath: " + imagePath);

                            // Load the image based on the provided imagePath
                            String relativePath = "C:\\java2\\projectJava2\\" + imagePath;
                            System.out.println("relativePath: " + relativePath);

                            Image image = new Image(new FileInputStream(relativePath));
                            imageView.setImage(image);
                            imageView.setFitWidth(100);
                            imageView.setPreserveRatio(true);
                            setGraphic(imageView);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            setGraphic(null);
                        }
                    }
                }
            };
        }
        );
    }

    @FXML
    public void eventImg(ActionEvent event) {
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Image File Chooser Example");

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.web", "*.png", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(primaryStage);//: Đoạn mã này hiển thị hộp thoại chọn tệp ảnh và chờ cho người dùng chọn một tệp. 
        if (selectedFile != null) {
            selectedImageFile = selectedFile;
        }
        primaryStage.close();
        System.out.println("Đường dẫn ảnh đã chọn: " + selectedImageFile);
    }

// check
    public boolean isCombinationExistsInImportGoods(int supplierId, int productNameId) {
        String selectSQL = "SELECT * FROM importGoods WHERE supplier_id = ? AND ProductNameId = ?";

        try (Connection connection = connect.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, supplierId);
            preparedStatement.setInt(2, productNameId);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next(); // If a row is found, the combination exists
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Error occurred, treat as non-existence
        }
    }
//insert 

    public String saveImageToUploads(File imageFile) {
        try {
            // Define the source and destination paths
            String sourcePath = imageFile.getAbsolutePath();
            String destinationPath = "src/uploads/" + imageFile.getName();

            // Copy the image file to the destination directory
            Files.copy(new FileInputStream(sourcePath), new File(destinationPath).toPath(), StandardCopyOption.REPLACE_EXISTING);

            return destinationPath;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @FXML
    public void insertProduct() throws IOException {

        String selectedCategory = fieldViewProductCategoryId.getValue();
        String selectedProductName = fieldViewProductName.getValue();
        String description = fieldViewProductDescriptions.getText().trim();
        String selectedSupplierid = supplierId.getValue();
        String imagePathInUploads = saveImageToUploads(selectedImageFile);

        if (description.isEmpty()) {
            showAlert("Please enter complete product description.");
            return;
        }
        if (description.length() > 1000) {
            showAlert("Product descriptions cannot be longer than 1000 characters.");
            return;
        }

        // Check if an image was selected
        if (selectedImageFile == null) {
            showAlert("Please select product photo.");
            return;
        }
        if (imagePathInUploads == null) {
            showAlert("Failed to save product photo.");
            return;
        }

        // Get the category and product IDs from the maps
        Integer categoryId = categoryNameidMap.get(selectedCategory);
        Integer productNameId = productNameIdMap.get(selectedProductName);
        Integer supplierNameId = supplierNamesMap.get(selectedSupplierid);

//        System.out.println(productNameId + "productNameId");
        if (categoryId == null || productNameId == null) {
            showAlert("Invalid category or product name.");
            return;
        }

        // Check if the combination of supplier_id and ProductNameId exists in importGoods
        if (!isCombinationExistsInImportGoods(supplierNameId, productNameId)) {
            showAlert("Supplier and Product combination does not exist in importGoods.");
            return;
        }

        String insertSQL = "INSERT INTO product (categoryId, ProductNameId, img, Description, supplier_id) VALUES (?,?,?,?,?)";

        try (Connection connection = connect.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            // Set parameters for the SQL statement
            preparedStatement.setInt(1, categoryId);
            preparedStatement.setInt(2, productNameId);
            preparedStatement.setString(3, imagePathInUploads);
            preparedStatement.setString(4, description);
            preparedStatement.setInt(5, supplierNameId);

            // Execute the SQL statement
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Add product successfully!");
                showSuccessAlert("Add product successfully!");

                fieldViewProductDescriptions.clear();

                selectedImageFile = null;

                getFromAddProduct();
            } else {
                showAlert("Failed to add product.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
// edit

    @FXML
    public void showEdit(ActionEvent event) throws IOException {
        // Get the selected product from the TableView
        Product selected = produtTable.getSelectionModel().getSelectedItem();

        if (selected != null) {
            // Load the Edit Product view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/editProduct.fxml"));
            Parent root = loader.load();

            // Get the controller for the Edit Product view
            editProductController editController = loader.getController();

            // Pass the selected product to the Edit Product controller
            editController.setSelectedProduct(selected);

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            showAlert("Please select the product you want to edit!");
        }
    }

    // Hiển thị thông báo thành công
    public void showSuccessAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Thành công");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Hiển thị thông báo lỗi
    public void showAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // các hàm gọi giao diện
    public void getFromAddcategory() throws IOException {
        Main.setRoot("/admin/addCategory.fxml");

    }

    public void getFromAddProduct() throws IOException {
        Main.setRoot("/admin/addProduct.fxml");

    }

    public void getFromfromAddSupplier() throws IOException {
        Main.setRoot("/admin/addSupplier.fxml");

    }

    public void getFromfromMoreProductName() throws IOException {
        Main.setRoot("/admin/addProductName.fxml");

    }

    public void getFromImportGoods() throws IOException {
        Main.setRoot("/admin/importGoods.fxml");
    }

    public void getFromProductDelivery() throws IOException {
        Main.setRoot("/admin/productDelivery.fxml");
    }

    public void getFromInventory() throws IOException {
        Main.setRoot("/admin/inventory.fxml");
    }

    public void getOder() throws IOException {
        Main.setRoot("/admin/oder.fxml");
    }

    public void getAccount() throws IOException {
        Main.setRoot("/admin/account.fxml");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void handleLogout(ActionEvent event) throws IOException {
        // Tạo một thể hiện của lớp logOut và thiết lập tham chiếu đến loginController
        loginController logoutHandler = new loginController();
        logoutHandler.handleLogout();
    }

}
