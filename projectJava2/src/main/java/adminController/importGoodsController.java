package adminController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import db.connect;
import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.Main;
import static org.apache.commons.lang3.StringUtils.isNumeric;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class importGoodsController {

    @FXML
    private ComboBox<String> SupplierId;

    @FXML
    private ComboBox<String> fieldViewProductName;

    @FXML
    private TextField importQuantity;

    @FXML
    private TextField exchangeNumber;

    @FXML
    private TextField total_quantity_received;

    private Map<String, Integer> supplierIdMap = new HashMap<>();
    private Map<String, Integer> productNameIdMap = new HashMap<>();
    @FXML
    private TextField fieldViewProductPrice;
    @FXML
    private TextField ImportPrice;

    @FXML
    private void initialize() {
        try (Connection connection = connect.getConnection()) {

            String selectSupplier = "SELECT supplierId, supplierName FROM supplier";
            PreparedStatement preparedStatement = connection.prepareStatement(selectSupplier);
            ResultSet resultSet = preparedStatement.executeQuery();
            ObservableList<String> suppliers = FXCollections.observableArrayList();

            while (resultSet.next()) {
                int supplierID = resultSet.getInt("supplierId");
                String supplierName = resultSet.getString("supplierName");
                suppliers.add(supplierName);
                supplierIdMap.put(supplierName, supplierID);
            }

            SupplierId.setItems(suppliers);

            String selectProduct = "SELECT ProductNameId, ProductName FROM ProductsName";
            PreparedStatement preparedStatement2 = connection.prepareStatement(selectProduct);
            ResultSet resultSet2 = preparedStatement2.executeQuery();
            ObservableList<String> productNames = FXCollections.observableArrayList();

            while (resultSet2.next()) {
                int productNameId = resultSet2.getInt("ProductNameId");
                String productsName = resultSet2.getString("ProductName");
                productNames.add(productsName);
                productNameIdMap.put(productsName, productNameId);
            }
            fieldViewProductName.setItems(productNames);

            printData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertImportgoods() throws IOException {
        String Quantity = importQuantity.getText().trim();
        String exchange = exchangeNumber.getText().trim();
        String totalQuantity = total_quantity_received.getText().trim();

        String selectedSupplierName = SupplierId.getValue();
        String selectedProductName = fieldViewProductName.getValue();
        String productPrice = fieldViewProductPrice.getText().trim();
        String importPrice = ImportPrice.getText().trim();

        if (Quantity.isEmpty() || exchange.isEmpty() || totalQuantity.isEmpty() || selectedSupplierName == null || selectedProductName == null) {
            showAlert("Please fill in all fields and select a supplier and product.");
            return;
        }
        if (!isNumeric(totalQuantity) || !isNumeric(Quantity) || !isNumeric(exchange)) {
            showAlert("All three values (totalQuantity, Quantity, and exchange) must be numeric.");
            return;
        }
        if (!isNumeric(productPrice) || !isNumeric(importPrice)) {
            showAlert("All three values (productPrice, importPrice) must be numeric.");
            return;
        }
        if (productPrice.isEmpty()) {
            showAlert("Please enter the full product price.");
            return;
        }
        int supplierId = supplierIdMap.get(selectedSupplierName);
        int productNameId = productNameIdMap.get(selectedProductName);

        java.util.Date currentDate = new java.util.Date();
        Date dateNew = new Date(currentDate.getTime());

        String insertSQL = "INSERT INTO importgoods "
                + "(ProductNameId, supplier_id, import_date, quantity_imported, quantity_returned, total_quantity_received,price,productImportPrice)"
                + "VALUES (?, ?, ?, ?, ?, ?,?,?)";

        try (Connection connection = connect.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            preparedStatement.setInt(1, productNameId);
            preparedStatement.setInt(2, supplierId);
            preparedStatement.setDate(3, dateNew);
            preparedStatement.setString(4, Quantity);
            preparedStatement.setString(5, exchange);
            preparedStatement.setString(6, totalQuantity);
            preparedStatement.setFloat(7, Float.parseFloat(importPrice));
            preparedStatement.setDouble(8, Float.parseFloat(productPrice));
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Add successfully!");
                showSuccessAlert("Added successfully!");
                importQuantity.clear();
                exchangeNumber.clear();
                total_quantity_received.clear();
                SupplierId.getSelectionModel().clearSelection();
                fieldViewProductName.getSelectionModel().clearSelection();
                fieldViewProductPrice.clear();
                ImportPrice.clear();
                // Sau khi thêm dữ liệu mới, cập nhật lại TableView
                importTable.getItems().clear();

                getFromImportGoods();
            } else {
                showAlert("Failed to add.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

// in ra bảng 
    public class Import {

        private int importId;
        private String productId;
        private String supplierId;
        private Date importDate;
        private int quantity;
        private int exchanged;
        private int totalReceived;
        private float price;
        private float productImportPrice;

        public Import(int importId, String productName, String supplierName, Date importDate, int quantity, int exchanged,
                int totalReceived, float price, float productImportPrice) {
            this.importId = importId;
            this.productId = productName;
            this.supplierId = supplierName;
            this.importDate = importDate;
            this.quantity = quantity;
            this.exchanged = exchanged;
            this.totalReceived = totalReceived;
            this.price = price;
            this.productImportPrice = productImportPrice;
        }

        public int getImportId() {
            return importId;
        }

        public String getProductId() {
            return productId;
        }

        public String getSupplierId() {
            return supplierId;
        }

        public Date getImportDate() {
            return importDate;
        }

        public int getQuantity() {
            return quantity;
        }

        public int getExchanged() {
            return exchanged;
        }

        public int getTotalReceived() {
            return totalReceived;
        }

        public Float getPrice() {
            return price;
        }

        public float getProductImportPrice() {
            return productImportPrice;
        }
    }
    @FXML
    private TableView<Import> importTable;

    @FXML
    private TableColumn<Import, String> productNameColumn;

    @FXML
    private TableColumn<Import, String> supplierNameColumn;

    @FXML
    private TableColumn<Import, Date> importDateColumn;

    @FXML
    private TableColumn<Import, Integer> quantityColumn;

    @FXML
    private TableColumn<Import, Integer> exchangedColumn;

    @FXML
    private TableColumn<Import, Integer> totalReceivedColumn;

    @FXML
    private TableColumn<Import, Float> price;

    @FXML
    private TableColumn<Import, Float> ImportPriceColumn;

    private List<Import> fetchDataFromDatabase() {
        List<Import> importDataList = new ArrayList<>();

        try (Connection connection = connect.getConnection()) {
            String query = "SELECT importGoods.import_id, ProductsName.ProductName, "
                    + "supplier.supplierName, importGoods.import_date, importGoods.quantity_imported, importGoods.price, importGoods.productImportPrice, "
                    + "importGoods.quantity_returned, importGoods.total_quantity_received FROM importGoods "
                    + "INNER JOIN ProductsName ON importGoods.ProductNameId = ProductsName.ProductNameId "
                    + "INNER JOIN supplier ON importGoods.supplier_id = supplier.supplierId";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Import importData = new Import(
                        resultSet.getInt("importGoods.import_id"),
                        resultSet.getString("ProductsName.ProductName"),
                        resultSet.getString("supplier.supplierName"),
                        resultSet.getDate("importGoods.import_date"),
                        resultSet.getInt("importGoods.quantity_imported"),
                        resultSet.getInt("importGoods.quantity_returned"),
                        resultSet.getInt("importGoods.total_quantity_received"),
                        resultSet.getFloat("importGoods.price"),
                        resultSet.getFloat("importGoods.productImportPrice")
                );

                importDataList.add(importData);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return importDataList;
    }

    public void printData() {
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
        supplierNameColumn.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        importDateColumn.setCellValueFactory(new PropertyValueFactory<>("importDate"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        exchangedColumn.setCellValueFactory(new PropertyValueFactory<>("exchanged"));
        totalReceivedColumn.setCellValueFactory(new PropertyValueFactory<>("totalReceived"));
        ImportPriceColumn.setCellValueFactory(new PropertyValueFactory<>("productImportPrice"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        ObservableList<Import> imports = FXCollections.observableArrayList(fetchDataFromDatabase());
        importTable.setItems(imports);
    }

    // Xóa một hóa đơn nhập hàng
    @FXML
    private void delete(ActionEvent event) throws IOException, SQLException {
        try (Connection connection = connect.getConnection()) {
            // Lấy hóa đơn nhập hàng được chọn từ TableView
            Import selectedImport = importTable.getSelectionModel().getSelectedItem();

            if (selectedImport != null) {
                // Hiển thị hộp thoại xác nhận xóa
                Alert confirmation = new Alert(AlertType.CONFIRMATION);
                confirmation.setTitle("Confirm Delete");
                confirmation.setHeaderText("Are you sure you want to delete this import?");
                confirmation.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

                ButtonType result = confirmation.showAndWait().orElse(ButtonType.NO);

                if (result == ButtonType.YES) {
                    if (deleteImportFromDatabase(selectedImport.getImportId())) {
                        // Xóa hóa đơn nhập hàng khỏi TableView
                        importTable.getItems().remove(selectedImport);
                        showSuccessAlert("Import deleted successfully!");
                    } else {
                        // Hiển thị thông báo không cho phép xóa
                        showAlert("Deletion is not allowed due to related records.");
                    }
                }
            } else {
                showAlert("Please select the import you want to delete!");
            }
        } catch (SQLException e) {
            // Xử lý lỗi SQL
            showAlert("An error occurred while deleting the import: " + e.getMessage());
        } catch (Exception e) {
            // Xử lý các lỗi khác
            showAlert("An unexpected error occurred: " + e.getMessage());
        }
    }
   

    private boolean canDeleteImport(int import_id) {
        // Check if the import_id is used in the productDelivery table
        String checkUsageSQL = "SELECT COUNT(*) FROM productDelivery WHERE ProductNameId = ? OR supplier_id = ?";

        try (Connection connection = connect.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(checkUsageSQL)) {
            preparedStatement.setInt(1, import_id);  // Set ProductNameId
            preparedStatement.setInt(2, import_id);  // Set supplier_id

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int usageCount = resultSet.getInt(1);
                if (usageCount == 0) {
                    // Không tìm thấy sử dụng trong bảng productDelivery, có thể xóa
                    return true;
                } else {
                    // Có các tham chiếu trong bảng productDelivery, không thể xóa
                    showAlert("Không thể xóa do có liên kết trong bảng productDelivery.");
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Mặc định không cho phép xóa trong trường hợp có lỗi
    }

    private boolean deleteImportFromDatabase(int import_id) {
        // Implement the logic to delete the import from the database
        String deleteSQL = "DELETE FROM importgoods WHERE import_id = ?";

        try (Connection connection = connect.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, import_id);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                // Deletion was successful
                return true;
            } else {
                // Deletion failed
                showAlert("Failed to delete the import.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Xử lý ngoại lệ và trả về false        }
        }
    }

    @FXML
    private void edit(ActionEvent event) throws IOException {

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

    public void handleLogout(ActionEvent event) throws IOException {
        // Tạo một thể hiện của lớp logOut và thiết lập tham chiếu đến loginController
        loginController logoutHandler = new loginController();
        logoutHandler.handleLogout();
    }

//    // insert thành công sẽ hiện
    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
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
}
