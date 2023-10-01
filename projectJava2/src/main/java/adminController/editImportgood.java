/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adminController;

/**
 *
 * @author Administrator
 */
import adminController.addSupplierController.Supplier;
import adminController.importGoodsController.Import;
import db.connect;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.Main;

public class editImportgood {

    private Map<String, Integer> supplierIdMap = new HashMap<>();
    private Map<String, Integer> productNameIdMap = new HashMap<>();

    @FXML
    private ComboBox<String> SupplierId;

    @FXML
    private ComboBox<String> fieldViewProductName;

    @FXML
    private TextField importQuantity;

    @FXML
    private TextField exchangeNumber;

    @FXML
    private TextField fieldViewProductPrice;

    @FXML
    private TextField ImportPrice;

    @FXML
    private Button saveButton;
    private Import selectedImport;
<<<<<<< HEAD
=======

    public void initData(Import selectedImport) {
        this.selectedImport = selectedImport;
        // Sử dụng thông tin từ selectedImport để điền vào các trường trong giao diện "Edit Import Goods"
        loadData();
        importQuantity.setText(String.valueOf(selectedImport.getQuantity())); // Đặt giá trị cho TextField importQuantity
        exchangeNumber.setText(String.valueOf(selectedImport.getExchanged())); // Đặt giá trị cho TextField exchangeNumber
        fieldViewProductPrice.setText(String.valueOf(selectedImport.getProductImportPrice())); // Đặt giá trị cho TextField fieldViewProductPrice
        ImportPrice.setText(String.valueOf(selectedImport.getPrice())); // Đặt giá trị cho TextField ImportPrice
    }

    private void loadData() {
        try (Connection connection = connect.getConnection()) {
>>>>>>> mainBranch

    public void initData(Import selectedImport) {
        this.selectedImport = selectedImport;
        // Sử dụng thông tin từ selectedImport để điền vào các trường trong giao diện "Edit Import Goods"
        loadSuppliers();
        loadProductNames();
        importQuantity.setText(String.valueOf(selectedImport.getQuantity())); // Đặt giá trị cho TextField importQuantity
        exchangeNumber.setText(String.valueOf(selectedImport.getExchanged())); // Đặt giá trị cho TextField exchangeNumber
        fieldViewProductPrice.setText(String.valueOf(selectedImport.getProductImportPrice())); // Đặt giá trị cho TextField fieldViewProductPrice
        ImportPrice.setText(String.valueOf(selectedImport.getPrice())); // Đặt giá trị cho TextField ImportPrice
    }

    private void loadSuppliers() {
        try (Connection connection = connect.getConnection()) {
            String selectSupplier = "SELECT supplierId, supplierName FROM supplier";
            PreparedStatement preparedStatement = connection.prepareStatement(selectSupplier);
            ResultSet resultSet = preparedStatement.executeQuery();
            ObservableList<String> suppliers = FXCollections.observableArrayList();

            while (resultSet.next()) {
                String supplierName = resultSet.getString("supplierName");
                suppliers.add(supplierName);
            }

            SupplierId.setItems(suppliers);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadProductNames() {
        try (Connection connection = connect.getConnection()) {
            String selectProduct = "SELECT ProductName FROM ProductsName";
            PreparedStatement preparedStatement2 = connection.prepareStatement(selectProduct);
            ResultSet resultSet2 = preparedStatement2.executeQuery();
            ObservableList<String> productNames = FXCollections.observableArrayList();

            while (resultSet2.next()) {
                String productsName = resultSet2.getString("ProductName");
                productNames.add(productsName);
            }

            fieldViewProductName.setItems(productNames);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
<<<<<<< HEAD
private void updateImport() {
    try (Connection connection = connect.getConnection()) {
        // Lấy thông tin đã chỉnh sửa từ các trường trong giao diện
        String selectedSupplierName = SupplierId.getValue();
        String selectedProductName = fieldViewProductName.getValue();
        String quantity = importQuantity.getText().trim();
        String exchange = exchangeNumber.getText().trim();
        String productPrice = fieldViewProductPrice.getText().trim();
        String importPrice = ImportPrice.getText().trim();

        // Kiểm tra xem tên nhà cung cấp và tên sản phẩm có tồn tại trong Map không
        Integer selectedSupplierId = supplierIdMap.get(selectedSupplierName);
        Integer selectedProductId = productNameIdMap.get(selectedProductName);

        if (selectedSupplierId != null && selectedProductId != null) {
            // Thực hiện cập nhật thông tin hóa đơn nhập hàng vào cơ sở dữ liệu
            String updateSQL = "UPDATE importgoods SET supplier_id=?, ProductNameId=?, quantity_imported=?, quantity_returned=?, price=?, productImportPrice=? WHERE import_id=?";

            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);
            preparedStatement.setInt(1, selectedSupplierId);
            preparedStatement.setInt(2, selectedProductId);
            preparedStatement.setInt(3, Integer.parseInt(quantity));
            preparedStatement.setInt(4, Integer.parseInt(exchange));
            preparedStatement.setFloat(5, Float.parseFloat(productPrice));
            preparedStatement.setFloat(6, Float.parseFloat(importPrice));
            preparedStatement.setInt(7, selectedImport.getImportId()); // Đây là ID của hóa đơn nhập hàng cần cập nhật

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                showSuccessAlert("Hóa đơn nhập hàng đã được cập nhật thành công!");
                // Đóng giao diện "Edit Import Goods"
                Stage stage = (Stage) saveButton.getScene().getWindow();
                stage.close();
            } else {
                showAlert("Cập nhật hóa đơn nhập hàng thất bại.");
            }
        } else {
            showAlert("Tên nhà cung cấp hoặc tên sản phẩm không hợp lệ.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        showAlert("Lỗi SQL khi cập nhật hóa đơn nhập hàng: " + e.getMessage());
    } catch (NumberFormatException e) {
        e.printStackTrace();
        showAlert("Lỗi: Định dạng số không hợp lệ.");
    }
}
=======
    private void updateImport() throws IOException {
        try (Connection connection = connect.getConnection()) {
            // Lấy thông tin đã chỉnh sửa từ các trường trong giao diện
            String selectedSupplierName = SupplierId.getValue();
            String selectedProductName = fieldViewProductName.getValue();
            String quantityText = importQuantity.getText().trim();
            String exchangeText = exchangeNumber.getText().trim();
            String productPriceText = fieldViewProductPrice.getText().trim();
            String importPriceText = ImportPrice.getText().trim();

            // Chuyển đổi dữ liệu chuỗi thành số nguyên hoặc số thực
            int quantity = Integer.parseInt(quantityText);
            int exchange = Integer.parseInt(exchangeText);
            float productPrice = Float.parseFloat(productPriceText);
            float importPrice = Float.parseFloat(importPriceText);

            // Tính toán số lượng tổng và tổng phí nhập hàng
            int totalQuantity = quantity - exchange;
            float totalImportFee = totalQuantity * importPrice;

            // Kiểm tra xem tên nhà cung cấp và tên sản phẩm có tồn tại trong Map không
            Integer selectedSupplierId = supplierIdMap.get(selectedSupplierName);
            Integer selectedProductId = productNameIdMap.get(selectedProductName);

            if (selectedSupplierId != null && selectedProductId != null) {
                // Thực hiện cập nhật thông tin hóa đơn nhập hàng vào cơ sở dữ liệu
                String updateSQL = "UPDATE importgoods SET supplier_id=?, ProductNameId=?, quantity_imported=?, quantity_returned=?, "
                        + "price=?, productImportPrice=?, total_quantity_received=?, totalImportFee=? WHERE import_id=?";

                PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);
                preparedStatement.setInt(1, selectedSupplierId);
                preparedStatement.setInt(2, selectedProductId);
                preparedStatement.setInt(3, quantity);
                preparedStatement.setInt(4, exchange);
                preparedStatement.setFloat(5, productPrice);
                preparedStatement.setFloat(6, importPrice);
                preparedStatement.setInt(7, totalQuantity);
                preparedStatement.setFloat(8, totalImportFee);
                preparedStatement.setInt(9, selectedImport.getImportId());

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    showSuccessAlert("Hóa đơn nhập hàng đã được cập nhật thành công!");
                    // Đóng giao diện "Edit Import Goods"
                    Stage stage = (Stage) saveButton.getScene().getWindow();
                    stage.close();
                    getFromImportGoods();
                    // Gọi phương thức để nạp lại trang cũ ở đây
                } else {
                    showAlert("Cập nhật hóa đơn nhập hàng thất bại.");
                }
            } else {
                showAlert("Tên nhà cung cấp hoặc tên sản phẩm không hợp lệ.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Lỗi SQL khi cập nhật hóa đơn nhập hàng: " + e.getMessage());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            showAlert("Lỗi: Định dạng số không hợp lệ.");
        }
    }

    public void getFromImportGoods() throws IOException {
        Main.setRoot("/admin/importGoods.fxml");
    }
>>>>>>> mainBranch

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
