/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adminController;

import adminController.importGoodsController.Import;
import db.connect;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Main;

/**
 *
 * @author Administrator
 */
public class editImportController {

    @FXML
    private ComboBox<String> SupplierId;

    @FXML
    private ComboBox<String> ProductNameId;

    @FXML
    private TextField importQuantity;

    @FXML
    private TextField exchangeNumber;

    @FXML
    private TextField total_quantity_received;

    @FXML
    private Button saveButton;
    private Map<String, Integer> productNameIdMap = new HashMap<>();
    private Map<String, Integer> supplierIdMap = new HashMap<>();

    private Import editProductName;
    private Import editSupplierName;
    private Import editQuantity;
    private Import editExchanged;
    private Import editTotalReceived;

    public void initData(Import selectedImport) {
        editProductName = selectedImport;
        ProductNameId.setValue(selectedImport.getProductId());

        editSupplierName = selectedImport;
        SupplierId.setValue(selectedImport.getSupplierId());

        editQuantity = selectedImport;
        importQuantity.setText(selectedImport.getQuantity());

        editExchanged = selectedImport;
        exchangeNumber.setText(selectedImport.getExchanged());

        editTotalReceived = selectedImport;
        total_quantity_received.setText(selectedImport.getTotalReceived());
    }

    @FXML
    private void initialize() {
        try (Connection connection = connect.getConnection()) {
            // Lấy danh sách nhà cung cấp từ cơ sở dữ liệu và đổ vào ComboBox SupplierId
            String selectSupplier = "SELECT supplierId, supplierName FROM supplier";
            PreparedStatement preparedStatement = connection.prepareStatement(selectSupplier);
            ResultSet resultSet = preparedStatement.executeQuery();
            ObservableList<String> suppliers = FXCollections.observableArrayList();

            while (resultSet.next()) {
                String supplierName = resultSet.getString("supplierName");
                int supplierId = resultSet.getInt("supplierId");
                suppliers.add(supplierName);
                supplierIdMap.put(supplierName, supplierId); // Populate the supplierIdMap
            }

            SupplierId.setItems(suppliers);

            // Lấy danh sách tên sản phẩm từ cơ sở dữ liệu và đổ vào ComboBox ProductNameId
            String selectProduct = "SELECT ProductNameId, ProductName FROM ProductsName";
            PreparedStatement preparedStatement2 = connection.prepareStatement(selectProduct);
            ResultSet resultSet2 = preparedStatement2.executeQuery();
            ObservableList<String> productNames = FXCollections.observableArrayList();

            while(resultSet2.next()) {
                String productsName = resultSet2.getString("ProductName");
                int productId = resultSet2.getInt("ProductNameId");
                productNames.add(productsName);
                productNameIdMap.put(productsName, productId); // Populate the productNameIdMap
            }

            ProductNameId.setItems(productNames);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void saveEdit(ActionEvent event) throws IOException {
        String updatedProductName = ProductNameId.getValue();
        String updateSupplierName = SupplierId.getValue();
        String Quantity = importQuantity.getText();
        String exchange = exchangeNumber.getText();
        String totalQuantity = total_quantity_received.getText();

        if (editProductName != null && editSupplierName != null && editQuantity != null && editExchanged != null && editTotalReceived != null) {
            int updatedProductNameId = productNameIdMap.get(updatedProductName); 
            int updateSupplierId = supplierIdMap.get(updateSupplierName); 

            editProductName.setProductId(updatedProductName);
            editSupplierName.setSupplierId(updateSupplierName);
            editQuantity.setQuantity(Quantity);
            editExchanged.setExchanged(exchange);
            editTotalReceived.setTotalReceived(totalQuantity);

            String updateSQL = "UPDATE importGoods SET ProductNameId=?, supplier_id=?, quantity_imported=?, quantity_returned=?, total_quantity_received=? WHERE import_id=?";
            try (Connection connection = connect.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
                preparedStatement.setInt(1, updatedProductNameId); 
                preparedStatement.setInt(2, updateSupplierId); 
                preparedStatement.setString(3, Quantity);
                preparedStatement.setString(4, exchange);
                preparedStatement.setString(5, totalQuantity);
                preparedStatement.setInt(6, editProductName.getImportId());

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    showSuccessAlert("Update successful!");

                    Stage stage = (Stage) SupplierId.getScene().getWindow();
                    stage.close();

                    // Reload the import goods table
                    Main.setRoot("/admin/importGoods.fxml");
                } else {
                    showAlert("Update failed!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
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

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
