package adminController;

import javafx.stage.Stage;

public class callLink {
	Stage loginStage = new Stage();
	linkA linka = new linkA();

	public void getFormAddCategory() {
		// Tạo một Stage mới cho form AddCategory
		linka.addCategory(loginStage);
	}

	public void getFormAddProduct() {
		linka.addProduct(loginStage);
	}

	public void getFormfromAddSupplier() {
		linka.fromSupplier(loginStage);
	}

	public void getProductName() {
		linka.getMoreProductName(loginStage);
	}

	public void getHome() {
		linka.openHomePage(loginStage);
	}
        
	public void getLogin() {
		linka.openHomePage(loginStage);
	}
        public void getCreateAccount() {
		linka.redirectToLoginForm();
	}

}
