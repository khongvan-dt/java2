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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import main.Main;

/**
 *
 * @author Administrator
 */
public class listAccounts {

    @FXML
    private TableView<Account> accountTable;

    @FXML
    private TableColumn<Account, String> accountNameColumn;

    @FXML
    private TableColumn<Account, String> roleColumn;

    @FXML
    private ComboBox<String> choice;
    @FXML
//    private TableColumn<Account, Integer> id; // Change the type to Integer
    private int selectedUserId = -1;

    public class Account {

        private String username;
        private String role;
        private int userId;

        public Account(String username, String role, int userId) {
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

        public int getUserId() {
            return userId;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }

    @FXML
    public void updateRole(int userId, String newRole) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Are you sure you want to update the role?");

        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try (Connection connection = connect.getConnection()) {
                    String query = "UPDATE users SET role = ? WHERE userId = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, newRole);
                    preparedStatement.setInt(2, userId);
                    int rowsUpdated = preparedStatement.executeUpdate();
                    if (rowsUpdated > 0) {
                        showSuccessAlert("Role updated successfully!");
                        getAccount();
                    } else {
                        showAlert("Role update failed!");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (IOException ex) {
                    Logger.getLogger(listAccounts.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    @FXML
    private void updateRoleHandler(ActionEvent event) throws IOException {
        // Get the selected role from the ComboBox or any other control
        String selectedRole = choice.getValue(); // Assuming "choice" is your ComboBox

        if (selectedUserId != -1) {
            // Call the updateRole method with the selected user and role
            updateRole(selectedUserId, selectedRole);

        }
    }

    @FXML
    private void deleteAccount(ActionEvent event) {
        if (selectedUserId != -1) {
            // Kiểm tra xem tài khoản có dữ liệu tham chiếu trong bảng "orders" không
            if (isAccountReferencedInOrders(selectedUserId)) {
                showAlert("The account is in use on the order and cannot be deleted.");
            } else {
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Confirmation");
                confirmationAlert.setHeaderText(null);
                confirmationAlert.setContentText("Are you sure you want to delete this account?");

                confirmationAlert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        try (Connection connection = connect.getConnection()) {
                            String query = "DELETE FROM users WHERE userId = ?";
                            PreparedStatement preparedStatement = connection.prepareStatement(query);
                            preparedStatement.setInt(1, selectedUserId);

                            int rowsDeleted = preparedStatement.executeUpdate();
                            if (rowsDeleted > 0) {
                                showSuccessAlert("Account deleted successfully!");
                                // Refresh the table or update your data source
                                // For example, you can call initialize() to reload data
                                initialize();
                            } else {
                                showAlert("Account deletion failed!");
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } else {
            showAlert("Please select an account to delete.");
        }
    }

    public void initialize() {
        int idCounter = 1; // Initialize the id counter
        try (Connection connection = connect.getConnection()) {
            ObservableList<Account> accounts = FXCollections.observableArrayList();
            String query = "SELECT * FROM users";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String accountName = resultSet.getString("username");
                String role = resultSet.getString("role");
                int userId = resultSet.getInt("userId");

                accounts.add(new Account(accountName, role, userId));
//                idCounter++; // Increment idCounter

            }
//            id.setCellValueFactory(new PropertyValueFactory<>("id"));
            accountNameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
            roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
            roleColumn.setCellFactory(ComboBoxTableCell.forTableColumn("User", "Admin"));

            roleColumn.setOnEditCommit(event -> {
                Account account = event.getRowValue();
                account.setRole(event.getNewValue());
                updateRole(account.getUserId(), event.getNewValue());
            });

            accountTable.setItems(accounts);

            ObservableList<String> items = FXCollections.observableArrayList("user", "admin");
            choice.setItems(items);

            accountTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    selectedUserId = newSelection.getUserId();
                }
            });

            choice.setOnAction(event -> {
                String selectedRole = choice.getValue();
                if (selectedUserId != -1) {
                    updateRole(selectedUserId, selectedRole);
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Phương thức kiểm tra xem tài khoản có dữ liệu tham chiếu trong bảng "orders" hay không
    private boolean isAccountReferencedInOrders(int userId) {
        try (Connection connection = connect.getConnection()) {
            String query = "SELECT COUNT(*) FROM orders WHERE userId = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int orderCount = resultSet.getInt(1);
                return orderCount > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void getFromAddcategory() throws IOException {
        Main.setRoot("/admin/addCategory.fxml");
    }

    public void getFromAddProduct() throws IOException {
        Main.setRoot("/admin/addProduct.fxml");
    }

    public void getFromAddSupplier() throws IOException {
        Main.setRoot("/admin/addSupplier.fxml");
    }

    public void getFromMoreProductName() throws IOException {
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

    public void getOrder() throws IOException {
        Main.setRoot("/admin/order.fxml");
    }

    public void getAccount() throws IOException {
        Main.setRoot("/admin/account.fxml");
    }

    public void handleLogout(ActionEvent event) throws IOException {
        loginController logoutHandler = new loginController();
        logoutHandler.handleLogout();
    }

}
