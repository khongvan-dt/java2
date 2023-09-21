package adminController;

import com.mysql.cj.conf.StringProperty;
import db.connect;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;
import main.Main;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.fxml.Initializable;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.net.URL;
import java.util.ResourceBundle;

public class productDeliveryController {

    public class ProductDelivery {

        private SimpleStringProperty supplierName;
        private int supplierId;
        private SimpleStringProperty productName;
        private int productNameId;

        public ProductDelivery(String supplierName, int supplierId, String productName, int productNameId) {
            this.supplierName = new SimpleStringProperty(supplierName);
            this.supplierId = supplierId;
            this.productName = new SimpleStringProperty(productName);
            this.productNameId = productNameId;
        }

        public String getSupplierName() {
            return supplierName.get();
        }

        public String getProductName() {
            return productName.get();
        }

        public int getSupplierId() {
            return supplierId;
        }

        public int getProductNameId() {
            return productNameId;
        }
    }

    @FXML
    private TableView<ProductDelivery> exportTable;

    @FXML
    private TableColumn<ProductDelivery, String> supplierNameColumn;

    @FXML
    private TableColumn<ProductDelivery, String> productNameColumn;

    public void initialize() {
        String query = "SELECT ProductsName.ProductNameId, supplier.supplierId, \n"
                + "       MAX(supplier.supplierName) as supplierName,\n"
                + "       MAX(ProductsName.ProductName) as ProductName,\n"
                + "       SUM(quantity_imported) as total_quantity_imported,\n"
                + "       SUM(quantity_returned) as total_quantity_returned,\n"
                + "       SUM(total_quantity_received) as total_total_quantity_received\n"
                + "FROM importGoods \n"
                + "INNER JOIN supplier ON importGoods.supplier_id = supplier.supplierId \n"
                + "INNER JOIN ProductsName ON importGoods.ProductNameId = ProductsName.ProductNameId \n"
                + "GROUP BY ProductsName.ProductNameId, supplier.supplierId;";

        try (Connection connection = connect.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query); ResultSet resultSet = preparedStatement.executeQuery()) {

            ObservableList<ProductDelivery> productDeliveries = FXCollections.observableArrayList();

            while (resultSet.next()) {
                int productNameId = resultSet.getInt("ProductNameId");
                int supplierId = resultSet.getInt("supplierId");
                String supplierName = resultSet.getString("supplierName");
                String productName = resultSet.getString("ProductName");

                // Tạo một đối tượng ProductDelivery và thêm vào danh sách
                productDeliveries.add(new ProductDelivery(supplierName, supplierId, productName, productNameId));
            }

            // Đặt cột dữ liệu cho exportTable
            supplierNameColumn.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
            productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));

            // Đưa danh sách vào exportTable
            exportTable.setItems(productDeliveries);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // insert
    @FXML
    private TextField quantity;

    public void insertProductdelivery() throws IOException {
        String shipQuantity = quantity.getText();

        // Check if shipQuantity is a valid integer
        if (!isNumeric(shipQuantity)) {
            showAlert("Quantity must be a numeric value.");
            return;
        }

        // Assuming you have selected a row from exportTable, you can get the selected item.
        ProductDelivery selectedProduct = exportTable.getSelectionModel().getSelectedItem();

        if (selectedProduct == null) {
            showAlert("Please select a product from the export table.");
            return;
        }

        int supplierId = selectedProduct.getSupplierId();
        int productNameId = selectedProduct.getProductNameId();

        // Get the current date
        java.util.Date currentDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(currentDate.getTime());

        String insertSQL = "INSERT INTO productdelivery (supplier_id, ProductNameId, dayShipping, shipmentQuantity) VALUES (?, ?, ?, ?)";

        try (Connection connection = connect.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setInt(1, supplierId);
            preparedStatement.setInt(2, productNameId);
            preparedStatement.setDate(3, sqlDate);
            preparedStatement.setString(4, shipQuantity);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                showSuccessAlert("Product delivery added successfully.");
            } else {
                showAlert("Failed to insert product delivery.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error occurred while inserting product delivery.");
        }
    }

// in ra bảng 
    public class ImportItem {

        private String productName;
        private String supplierName;
        private Date dayShipping;
        private int shipmentQuantity;

        public ImportItem(String productName, String supplierName, Date dayShipping, int shipmentQuantity) {
            this.productName = productName;
            this.supplierName = supplierName;
            this.dayShipping = dayShipping;
            this.shipmentQuantity = shipmentQuantity;
        }

        // Getters and setters
        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getSupplierName() {
            return supplierName;
        }

        public void setSupplierName(String supplierName) {
            this.supplierName = supplierName;
        }

        public Date getDayShipping() {
            return dayShipping;
        }

        public void setDayShipping(Date dayShipping) {
            this.dayShipping = dayShipping;
        }

        public int getShipmentQuantity() {
            return shipmentQuantity;
        }

        public void setShipmentQuantity(int shipmentQuantity) {
            this.shipmentQuantity = shipmentQuantity;
        }
    }
    @FXML
    private TableView<ImportItem> ProductDeliveryTable;

    @FXML
    private TableColumn<ImportItem, String> productName;

    @FXML
    private TableColumn<ImportItem, String> supplierName;

    @FXML
    private TableColumn<ImportItem, Date> importDate;

    @FXML
    private TableColumn<ImportItem, Integer> quantityColumn;

    public List<ImportItem> populateImportTable() {
        List<ImportItem> importItems = new ArrayList<>();

        try (Connection connection = connect.getConnection()) {
            String query = "SELECT ProductsName.ProductName, supplier.supplierName, productdelivery.dayShipping, productdelivery.shipmentQuantity "
                    + "FROM productdelivery "
                    + "INNER JOIN ProductsName ON productdelivery.ProductNameId = ProductsName.ProductNameId "
                    + "INNER JOIN supplier ON productdelivery.supplier_id = supplier.supplierId";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ImportItem importItem = new ImportItem(
                        resultSet.getString("ProductName"),
                        resultSet.getString("supplierName"),
                        resultSet.getDate("dayShipping"),
                        resultSet.getInt("shipmentQuantity")
                );
                importItems.add(importItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return importItems;
    }

    @FXML
    public void printData() {
        productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        supplierName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        importDate.setCellValueFactory(new PropertyValueFactory<>("dayShipping"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("shipmentQuantity"));

        ObservableList<ImportItem> productdelivery = FXCollections.observableArrayList(populateImportTable());
        ProductDeliveryTable.setItems(productdelivery);
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
    // Helper method to check if a string is numeric

    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
