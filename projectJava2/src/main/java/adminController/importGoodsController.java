package adminController;

import db.connect;
import java.io.IOException;
import static java.lang.Math.E;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Callback;
import main.Main;
import scala.Product;

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
        String Quantity = importQuantity.getText();
        String exchange = exchangeNumber.getText();
        String totalQuantity = total_quantity_received.getText();

        String selectedSupplierName = SupplierId.getValue();
        String selectedProductName = fieldViewProductName.getValue();

        if (Quantity.isEmpty() || exchange.isEmpty() || totalQuantity.isEmpty() || selectedSupplierName == null || selectedProductName == null) {
            showAlert("Please fill in all fields and select a supplier and product.");
            return;
        }

        int supplierId = supplierIdMap.get(selectedSupplierName);
        int productNameId = productNameIdMap.get(selectedProductName);

        java.util.Date currentDate = new java.util.Date();
        Date dateNew = new Date(currentDate.getTime());

        String insertSQL = "INSERT INTO importgoods (ProductNameId, supplier_id, import_date, quantity_imported, quantity_returned, total_quantity_received) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = connect.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            preparedStatement.setInt(1, productNameId);
            preparedStatement.setInt(2, supplierId);
            preparedStatement.setDate(3, dateNew);
            preparedStatement.setString(4, Quantity);
            preparedStatement.setString(5, exchange);
            preparedStatement.setString(6, totalQuantity);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Add successfully!");
                showSuccessAlert("Added successfully!");
                importQuantity.clear();
                exchangeNumber.clear();
                total_quantity_received.clear();
                SupplierId.getSelectionModel().clearSelection();
                fieldViewProductName.getSelectionModel().clearSelection();
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
        private String quantity;
        private String exchanged;
        private String totalReceived;

        public Import(int importId, String productName, String supplierName, Date importDate, String quantity, String exchanged, String totalReceived) {
            this.importId = importId;
            this.productId = productName;
            this.supplierId = supplierName;
            this.importDate = importDate;
            this.quantity = quantity;
            this.exchanged = exchanged;
            this.totalReceived = totalReceived;
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

        public String getQuantity() {
            return quantity;
        }

        public String getExchanged() {
            return exchanged;
        }

        public String getTotalReceived() {
            return totalReceived;
        }

        // Phương thức set cho importId
        public void setImportId(int importId) {
            this.importId = importId;
        }

        // Phương thức set cho productId
        public void setProductId(String productId) {
            this.productId = productId;
        }

        // Phương thức set cho supplierId
        public void setSupplierId(String supplierId) {
            this.supplierId = supplierId;
        }

        // Phương thức set cho importDate
        public void setImportDate(Date importDate) {
            this.importDate = importDate;
        }

        // Phương thức set cho quantity
        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        // Phương thức set cho exchanged
        public void setExchanged(String exchanged) {
            this.exchanged = exchanged;
        }

        // Phương thức set cho totalReceived
        public void setTotalReceived(String totalReceived) {
            this.totalReceived = totalReceived;
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

    private List<Import> fetchDataFromDatabase() {
        List<Import> importDataList = new ArrayList<>();

        try (Connection connection = connect.getConnection()) {
            String query = "SELECT importGoods.import_id, ProductsName.ProductName, "
                    + "supplier.supplierName, importGoods.import_date, importGoods.quantity_imported, "
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
                        resultSet.getString("importGoods.quantity_imported"),
                        resultSet.getString("importGoods.quantity_returned"),
                        resultSet.getString("importGoods.total_quantity_received")
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

        ObservableList<Import> imports = FXCollections.observableArrayList(fetchDataFromDatabase());
        importTable.getItems().setAll(imports);
    }

//edit 
    @FXML
    private void showEdit(ActionEvent event) throws IOException {
        // Lấy hàng đã chọn từ TableView
        Import selectImportTable = importTable.getSelectionModel().getSelectedItem();

        if (selectImportTable != null) {
            // Tải cảnh "Sửa nhập hàng" và chuyển dữ liệu hàng đã chọn
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/editImport.fxml"));
            Parent root = loader.load();

            // để trỏ đến controller của editImportController
            editImportController editImport = loader.getController();

            // Truyền dữ liệu hàng đã chọn cho controller của editImportController
            editImport.initData(selectImportTable);

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            // Add a listener to refresh the table when the edit dialog is closed
            stage.setOnHidden(e -> {
                importTable.getItems().clear();
                printData(); // Reload the table data
            });
        } else {
            showAlert("Please select the information you want to edit!");
        }
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
        Main.setRoot("/admin/importGoods.fxml");
    }

    public void getFromInventory() throws IOException {
        Main.setRoot("/admin/inventory.fxml");
    }

    public void handleLogout(ActionEvent event) throws IOException {
        // Tạo một thể hiện của lớp logOut và thiết lập tham chiếu đến loginController
        loginController logoutHandler = new loginController();
        logoutHandler.handleLogout();
    }

    // insert thành công sẽ hiện
    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
