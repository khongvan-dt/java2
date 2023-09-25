package adminController;

import com.mysql.cj.conf.IntegerProperty;
import com.mysql.cj.conf.StringProperty;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.util.StringConverter;
import db.connect;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jdk.jfr.Category;
import main.Main;
import static org.apache.commons.lang3.StringUtils.isNumeric;

public class addProductController extends Application {

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

    @FXML
    public void eventImg(ActionEvent event) {
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Image File Chooser Example");

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            selectedImageFile = selectedFile;
        }
        primaryStage.close();
        System.out.println("Đường dẫn ảnh đã chọn: " + selectedImageFile);
    }
// check

    private boolean isCombinationExistsInImportGoods(int supplierId, int productNameId) {
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

    @FXML
    public void insertProduct() {

        String selectedCategory = fieldViewProductCategoryId.getValue();
        String selectedProductName = fieldViewProductName.getValue();
        String description = fieldViewProductDescriptions.getText().trim();
        String selectedSupplierid = supplierId.getValue();

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
            preparedStatement.setString(3, selectedImageFile.getAbsolutePath());
            preparedStatement.setString(4, description);
            preparedStatement.setInt(5, supplierNameId);

            // Execute the SQL statement
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Add product successfully!");
                showSuccessAlert("Add product successfully!");

                fieldViewProductDescriptions.clear();

                selectedImageFile = null;
            } else {
                showAlert("Failed to add product.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
