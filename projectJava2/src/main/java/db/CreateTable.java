package db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTable {

    public static void main(String[] args) {

        Connection connection = null;
        Statement statement = null;

        try {

            connection = connect.getConnection(); // Lấy kết nối tới cơ sở dữ liệu từ lớp Connect

            // Tạo đối tượng Statement để thực thi câu lệnh SQL
            statement = connection.createStatement();

            // Create "category" table  
            String createCategoryTableSQL = "CREATE TABLE IF NOT EXISTS category ("
                    + "categoryId INT PRIMARY KEY AUTO_INCREMENT,"
                    + "categoryName VARCHAR(100) NOT NULL)";
            statement.executeUpdate(createCategoryTableSQL);

            // Create "supplier" table
            String createSupplierTableSQL = "CREATE TABLE IF NOT EXISTS supplier ("
                    + "supplierId INT PRIMARY KEY AUTO_INCREMENT,"
                    + "supplierName VARCHAR(100) NOT NULL)";
            statement.executeUpdate(createSupplierTableSQL);

            // Create "users" table
            String createTableSQL = "CREATE TABLE IF NOT EXISTS users ("
                    + "userId INT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                    + "username VARCHAR(50) NOT NULL,"
                    + "password VARCHAR(500) NOT NULL,"
                    + "email VARCHAR(100) NOT NULL,"
                    + "role VARCHAR(50) NOT NULL)";
            statement.executeUpdate(createTableSQL);

            // Create "importGoods" table
            String createImportTableSQL = "CREATE TABLE IF NOT EXISTS importGoods ("
                    + "import_id INT PRIMARY KEY AUTO_INCREMENT,"
                    + "productName VARCHAR(500) NOT NULL,"
                    + "supplier_id INT NOT NULL,"
                    + "import_date DATE NOT NULL,"
                    + "price FLOAT NOT NULL,"
                    + "productImportPrice FLOAT NOT NULL,"
                    + "quantity_imported INT NOT NULL,"
                    + "quantity_returned INT NOT NULL,"
                    + "total_quantity_received INT NOT NULL,"
                    + "totalImportFee FLOAT NOT NULL,"
                    + "FOREIGN KEY (supplier_id) REFERENCES supplier(supplierId))";
            statement.executeUpdate(createImportTableSQL);

            // Create "productDelivery" table
            String createExportTableSQL = "CREATE TABLE IF NOT EXISTS productDelivery ("
                    + "productDeliveryID INT PRIMARY KEY AUTO_INCREMENT,"
                    + "importProductNameId INT NOT NULL,"
                    + "supplier_id INT NOT NULL,"
                    + "dayShipping DATE NOT NULL,"
                    + "shipmentQuantity INT NOT NULL,"
                    + "FOREIGN KEY (importProductNameId) REFERENCES importGoods(import_id),"
                    + "FOREIGN KEY (supplier_id) REFERENCES supplier(supplierId))";
            statement.executeUpdate(createExportTableSQL);

            // Create "inventory" table
            String createInventoryTableSQL = "CREATE TABLE IF NOT EXISTS inventory ("
                    + "inventory_id INT PRIMARY KEY AUTO_INCREMENT,"
                    + "importProductNameId INT NOT NULL,"
                    + "supplierId INT NOT NULL,"
                    + "inventoryNumber INT NOT NULL,"
                    + "date DATE NOT NULL,"
                    + "FOREIGN KEY (importProductNameId) REFERENCES importGoods(import_id),"
                    + "FOREIGN KEY (supplierId) REFERENCES supplier(supplierId))";
            statement.executeUpdate(createInventoryTableSQL);

            // Create "product" table
            String createProductTableSQL = "CREATE TABLE IF NOT EXISTS product ("
                    + "productId INT PRIMARY KEY AUTO_INCREMENT,"
                    + "idSupplier INT NOT NULL,"
                    + "categoryId INT NOT NULL,"
                    + "importProductNameId INT NOT NULL,"
                    + "description VARCHAR(2000) NOT NULL,"
                    + "img VARCHAR(200) NOT NULL,"
                    + "FOREIGN KEY (categoryId) REFERENCES category(categoryId),"
                    + "FOREIGN KEY (importProductNameId) REFERENCES importGoods(import_id),"
                    + "FOREIGN KEY (idSupplier) REFERENCES supplier(supplierId))";
            statement.executeUpdate(createProductTableSQL);

            // Create "orders" table
            String createOrderTableSQL = "CREATE TABLE IF NOT EXISTS orders ("
                    + "order_id INT PRIMARY KEY AUTO_INCREMENT,"
                    + "userId INT NOT NULL,"
                    + "customerName VARCHAR(100) NOT NULL,"
                    + "phoneNumber VARCHAR(20) NOT NULL,"
                    + "address VARCHAR(200) NOT NULL,"
                    + "orderDate DATE NOT NULL,"
                    + "orderNotes TEXT,"
                    + "FOREIGN KEY (userId) REFERENCES users(userId))";
            statement.executeUpdate(createOrderTableSQL);

            // Create "orderDetails" table
            String createOrderDetailTableSQL = "CREATE TABLE IF NOT EXISTS orderDetails ("
                    + "order_detail_id INT PRIMARY KEY AUTO_INCREMENT,"
                    + "orderId INT NOT NULL,"
                    + "product_id INT NOT NULL,"
                    + "quantity INT NOT NULL,"
                    + "price FLOAT NOT NULL,"
                    + "totalPrice FLOAT NOT NULL,"
                    + "FOREIGN KEY (orderId) REFERENCES orders(order_id),"
                    + "FOREIGN KEY (product_id) REFERENCES product(productId))";
            statement.executeUpdate(createOrderDetailTableSQL);

            // Create "feedback" table
            String createFeedbackTableSQL = "CREATE TABLE IF NOT EXISTS feedback ("
                    + "feedbackId INT PRIMARY KEY AUTO_INCREMENT,"
                    + "userId INT NOT NULL,"
                    + "order_id INT NOT NULL,"
                    + "nd_feedback TEXT NOT NULL,"
                    + "FOREIGN KEY (userId) REFERENCES users(userId),"
                    + "FOREIGN KEY (order_id) REFERENCES orders(order_id))";
            statement.executeUpdate(createFeedbackTableSQL);

            System.out.println("Tables created successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
