package adminController;

import com.mysql.cj.conf.IntegerProperty;
import com.mysql.cj.conf.StringProperty;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.util.StringConverter;
import db.connect;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
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

public class addProductController extends Application {

    public class Product {

        private SimpleStringProperty productName;
        private SimpleStringProperty img;
        private SimpleStringProperty supplierName;
        private SimpleStringProperty description;
        private SimpleStringProperty importPrice;
        private SimpleStringProperty price;
        private SimpleStringProperty imgPath;
        private SimpleStringProperty categoryName;
        private SimpleIntegerProperty productId;

        public Product(String categoryName, String productName, String img, String supplierName, String description, String importPrice, String price, Integer productId) {
            this.productName = new SimpleStringProperty(productName);
            this.img = new SimpleStringProperty(img);
            this.supplierName = new SimpleStringProperty(supplierName);
            this.description = new SimpleStringProperty(description);
            this.importPrice = new SimpleStringProperty(importPrice);
            this.price = new SimpleStringProperty(price);
            this.imgPath = new SimpleStringProperty(img);
            this.categoryName = new SimpleStringProperty(categoryName);
            this.productId = new SimpleIntegerProperty(productId);
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

        public String getCategoryName() {
            return categoryName.get();
        }

        public Integer getProductId() {
            return productId.get();
        }

    }

    private Map<String, Integer> categoryNameidMap = new HashMap<>();
    private Map<String, Integer> productNameIdMap = new HashMap<>();
    private Map<String, Integer> supplierNamesMap = new HashMap<>();

    @FXML
    private ComboBox<String> fieldViewProductCategoryId;

    @FXML
    private ComboBox<String> fieldViewProductName;

    @FXML
    private ComboBox<String> supplierId;

    @FXML
    private TextArea fieldViewProductDescriptions;

    private File selectedImageFile;

    private ObservableList<Product> productList = FXCollections.observableArrayList();
    @FXML
    private TableView<Product> importTable;
    @FXML
    private TableColumn<Product, String> productNameColumn;
    @FXML
    private TableColumn<Product, String> imgColumn;
    @FXML
    private TableColumn<Product, String> supplierNameColumn;
    @FXML
    private TableColumn<Product, String> CategoryColum;
    @FXML
    private TableColumn<Product, String> descriptionColumn;
    @FXML
    private TableColumn<Product, String> importPriceColumn;
    @FXML
    private TableColumn<Product, String> priceColumn;

    @FXML
    private void initialize() {
        // Tạo một HashMap để lưu dữ liệu danh mục sản phẩm

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

            String selectProduct = "SELECT * FROM importGoods ";
            PreparedStatement preparedStatement2 = connection.prepareStatement(selectProduct);
            ResultSet resultSet2 = preparedStatement2.executeQuery();
            ObservableList<String> productNames = FXCollections.observableArrayList();
            while (resultSet2.next()) {
                int productNameId = resultSet2.getInt("importGoods.import_id");
                String productsName = resultSet2.getString("importGoods.productName");
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

// Inside your initialize() method
        try (Connection connection = connect.getConnection()) {
            String query = "SELECT importGoods.productName, product.img, supplier.supplierName, product.Description, importgoods.productImportPrice, product.productId, importgoods.price, category.categoryName "
                    + "FROM product "
                    + "INNER JOIN category ON product.categoryId = category.categoryId "
                    + "INNER JOIN importgoods ON importgoods.import_id = product.importProductNameId "
                    + "INNER JOIN supplier ON product.idSupplier = supplier.supplierId "
                    + "ORDER BY product.productId DESC";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String categoryName = resultSet.getString("categoryName");
                String productName = resultSet.getString("ProductName");
                String img = resultSet.getString("img");
                String supplierName = resultSet.getString("supplierName");
                String description = resultSet.getString("Description");
                String importPrice = resultSet.getString("productImportPrice");
                String price = resultSet.getString("price");
                Integer productId2 = resultSet.getInt("productId");

                Product product = new Product(categoryName, productName, img, supplierName, description, importPrice, price, productId2);
                productList.add(product);
            }

            // Set the items of the TableView to the productList
            importTable.setItems(productList);
            CategoryColum.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
            productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
            supplierNameColumn.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
            descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            importPriceColumn.setCellValueFactory(new PropertyValueFactory<>("importPrice"));
            priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
            imgColumn.setCellValueFactory(new PropertyValueFactory<>("img"));

        } catch (SQLException e) {
            e.printStackTrace();
        }

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

    private String saveImageToUploads(File imageFile) {
        String uploadsDirectoryPath = "src/uploads/";

        File uploadsDirectory = new File(uploadsDirectoryPath);

        if (!uploadsDirectory.exists()) {
            if (uploadsDirectory.mkdirs()) {
                System.out.println("Created uploads directory");
            } else {
                System.out.println("Failed to create uploads directory");
                return null;
            }
        }

        String fileName = imageFile.getName();
        String imagePathInUploads = uploadsDirectoryPath + File.separator + fileName;
        File destFile = new File(imagePathInUploads);

        try {
            Files.copy(imageFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return imagePathInUploads;
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Return null in case of an error
        }
    }

// check
    private boolean isCombinationExistsInImportGoods(int supplierId) {
        String selectSQL = "SELECT * FROM importGoods WHERE supplier_id = ?";

        try (Connection connection = connect.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, supplierId);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next(); // If a row is found, the combination exists
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Error occurred, treat as non-existence
        }
    }
//insert 

    @FXML
    public void insertProduct() throws IOException {

        String selectedCategory = fieldViewProductCategoryId.getValue();
        String selectedProductName = fieldViewProductName.getValue();
        String description = fieldViewProductDescriptions.getText().trim();
        String selectedSupplierid = supplierId.getValue();

        // Kiểm tra xem tệp ảnh đã được chọn chưa
        if (selectedImageFile == null) {
            showAlert("Please select product photo.");
            return;
        }
        String uploadsDirectoryPath = "src/uploads/";

        // Lấy tên tệp ảnh từ đường dẫn đầy đủ
        String imageName = selectedImageFile.getName();
        // Kết hợp tên tệp ảnh với đường dẫn uploadsDirectoryPath
        String imagePathInUploads = uploadsDirectoryPath + imageName;

        // Kiểm tra xem mô tả có trống không hoặc quá dài không
        if (description.isEmpty()) {
            showAlert("Please enter complete product description.");
            return;
        }
        if (description.length() > 2000) {
            showAlert("Product descriptions cannot be longer than 2000 characters.");
            return;
        }

        // Get the category and product IDs from the maps
        Integer categoryId = categoryNameidMap.get(selectedCategory);
        Integer productNameId = productNameIdMap.get(selectedProductName);
        Integer supplierNameId = supplierNamesMap.get(selectedSupplierid);

        if (categoryId == null || productNameId == null) {
            showAlert("Invalid category or product name.");
            return;
        }

        // Kiểm tra xem sự kết hợp của supplier_id và ProductNameId có tồn tại trong importGoods không
        if (!isCombinationExistsInImportGoods(supplierNameId)) {
            showAlert("Supplier combination does not exist in importGoods.");
            return;
        }

        String insertSQL = "INSERT INTO product (categoryId, importProductNameId, img, Description, idSupplier) VALUES (?,?,?,?,?)";

        try (Connection connection = connect.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            // Thiết lập tham số cho câu lệnh SQL
            preparedStatement.setInt(1, categoryId);
            preparedStatement.setInt(2, productNameId);
            preparedStatement.setString(3, imagePathInUploads);
            preparedStatement.setString(4, description);
            preparedStatement.setInt(5, supplierNameId);

            // Thực hiện câu lệnh SQL
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Add product successfully!");
                showSuccessAlert("Add product successfully!");

                fieldViewProductDescriptions.clear();

                // Sao chép tệp ảnh vào thư mục uploads
                String uploadedImagePath = saveImageToUploads(selectedImageFile);

                if (uploadedImagePath != null) {
                    System.out.println("Image copied to: " + uploadedImagePath);
                    selectedImageFile = null;
                    getFromAddProduct();
                } else {
                    showAlert("Failed to copy image to uploads directory.");
                }
            } else {
                showAlert("Failed to add product.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteCategory(ActionEvent event) throws IOException {
        // Get the selected category from the TableView
        Product selectedCategory = importTable.getSelectionModel().getSelectedItem();

        if (selectedCategory != null) {
            // Show a confirmation dialog to confirm the deletion
            Alert confirmation = new Alert(AlertType.CONFIRMATION);
            confirmation.setTitle("Confirm Delete");
            confirmation.setHeaderText("Are you sure you want to delete this category?");
            confirmation.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

            ButtonType result = confirmation.showAndWait().orElse(ButtonType.NO);

            if (result == ButtonType.YES) {
                if (deleteCategoryFromDatabase(selectedCategory.getProductId())) {
                    // Remove the deleted category from the TableView
                    importTable.getItems().remove(selectedCategory);
                    showSuccessAlert("Category deleted successfully!");
                } else {
                    showAlert("Failed to delete category.");
                }
            }
        } else {
            showAlert("Please select the category you want to delete!");
        }
    }

    private boolean deleteCategoryFromDatabase(int productId) {
        // Implement the logic to delete the category from the database
        String deleteSQL = "DELETE FROM product WHERE productId  = ?";
        //PreparedStatement dùng để truy vấn đến csdl bằng 1 câu lệnh sql

        try (Connection connection = connect.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {

            preparedStatement.setInt(1, productId);
            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Hiển thị thông báo thành công
    private void showSuccessAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Thành công");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Hiển thị thông báo lỗi
    private void showAlert(String message) {
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
