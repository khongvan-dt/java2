/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adminController;

import db.connect;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import main.Main;

/**
 *
 * @author Administrator
 */
public class inventoryController {

    // in ra bảng 
    @FXML
    private TableView<Delivery> ProductDeliveryTable;

    @FXML
    private TableColumn<Delivery, String> productName;

    @FXML
    private TableColumn<Delivery, String> supplierName;

    @FXML
    private TableColumn<Delivery, Date> importDate;

    @FXML
    private TableColumn<Delivery, Integer> quantityColumn;

    private ObservableList<Delivery> productList = FXCollections.observableArrayList();

    public class Delivery {

        private String productName;
        private String supplierName;
        private Date importDate;
        private int quantity;
        private int supplierId;
        private int productNameId;

        public Delivery(String productName, String supplierName, Date importDate, int quantity, int supplierId, int productNameId) {
            this.productName = productName;
            this.supplierName = supplierName;
            this.importDate = importDate;
            this.quantity = quantity;
            this.supplierId = supplierId;
            this.productNameId = productNameId;
        }

        // Getter và setter cho supplierId và productNameId
        public int getSupplierId() {
            return supplierId;
        }

        public void setSupplierId(int supplierId) {
            this.supplierId = supplierId;
        }

        public int getProductNameId() {
            return productNameId;
        }

        public void setProductNameId(int productNameId) {
            this.productNameId = productNameId;
        }

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

        public Date getImportDate() {
            return importDate;
        }

        public void setImportDate(Date importDate) {
            this.importDate = importDate;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }

    @FXML
    public void initialize() {
        // Gọi phương thức populateImportTable() để đổ dữ liệu vào productList
        populateImportTable();

        // Ánh xạ cột với thuộc tính của lớp Delivery
        productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        supplierName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        importDate.setCellValueFactory(new PropertyValueFactory<>("importDate"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        // Đặt dữ liệu vào TableView
        ProductDeliveryTable.setItems(productList);
    }

    // Phương thức này sẽ điền dữ liệu vào productList từ cơ sở dữ liệu
    private void populateImportTable() {
        try (Connection connection = connect.getConnection()) {
            String query = "SELECT ProductsName.ProductName,ProductsName.ProductNameId,"
                    + "supplier.supplierName,supplier.supplierId,productdelivery.dayShipping,productdelivery.shipmentQuantity "
                    + "FROM productdelivery "
                    + "INNER JOIN ProductsName ON productdelivery.ProductNameId = ProductsName.ProductNameId "
                    + "INNER JOIN supplier ON productdelivery.supplier_id = supplier.supplierId";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Delivery productsDelivery = new Delivery(
                        resultSet.getString("ProductName"),
                        resultSet.getString("supplierName"),
                        resultSet.getDate("dayShipping"),
                        resultSet.getInt("shipmentQuantity"),
                        resultSet.getInt("supplierId"),
                        resultSet.getInt("ProductNameId")
                );

                productList.add(productsDelivery);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // insert thành công sẽ hiện
    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // insert không thành công sẽ hiện
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
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

    public void handleLogout(ActionEvent event) throws IOException {
        // Tạo một thể hiện của lớp logOut và thiết lập tham chiếu đến loginController
        loginController logoutHandler = new loginController();
        logoutHandler.handleLogout();
    }
}
