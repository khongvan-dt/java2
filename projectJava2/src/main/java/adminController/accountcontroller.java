/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adminController;

import db.connect;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;  // Add this import
import main.Main;

/**
 *
 * @author Administrator
 */
public class accountcontroller {
    //in dữ liệu ra bảng 

    @FXML
    private TableView<Account> AccountTable;
    
    @FXML
    private TableColumn<Account, String> AccountNameColumn;
    @FXML
    private TableColumn<Account, Integer> id;
    
    @FXML
    private TableColumn<Account, String> RoleColumn;
    @FXML
    private ComboBox<String> choice; // Đảm bảo bạn chỉ định kiểu dữ liệu cho ComboBox, trong trường hợp này là String

    public class Account {
        
        private String username;
        private String role;
        private int userId;
        
        public Account(String username, String role, Integer userId) {
            this.username = username;
            this.role = role;
            this.userId = userId;
        }
        
        public String getUsername() {
            return username;
        }
        
        public String getRole() {
            return role;
        }
        
        public String getUserId() {
            return role;
        }
        
        public void setRole(String role) {
            this.role = role;
        }
        
    }
    
    public void initialize() {
        try (Connection connection = connect.getConnection()) {
            ObservableList<Account> accounts = FXCollections.observableArrayList();
            int i = 0;
            String query = "SELECT * FROM users";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                String accountName = resultSet.getString("username");
                String role = resultSet.getString("role");
                Integer userId = resultSet.getInt("userId ");
                
                accounts.add(new Account(accountName, role, userId));
            }
            
            AccountNameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
//            id.setCellFactory(++i);
            // Set cell factory for roleColumn to display a ComboBox
            RoleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
            RoleColumn.setCellFactory(ComboBoxTableCell.forTableColumn("User", "Admin"));

            // Handle ComboBox selection changes
            RoleColumn.setOnEditCommit(event -> {
                Account account = event.getRowValue();
                account.setRole(event.getNewValue());
            });
            
            AccountTable.setItems(accounts);

            // Populate the ComboBox with values
            ObservableList<String> items = FXCollections.observableArrayList("User", "Admin");
            choice.setItems(items);
            choice.setValue("choice");
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
    
    @FXML
    private void formHome() throws IOException {
        Main.setRoot("/web/home.fxml");
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
