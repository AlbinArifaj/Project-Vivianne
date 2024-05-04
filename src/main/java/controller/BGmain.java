package controller;

import app.Navigator;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import repository.CompanyRepository;

public abstract class BGmain {
    public void handleDashboard(ActionEvent event){
//        System.out.println("Hello");
        Navigator.navigate(event,Navigator.HOME_PAGE);
    }

    public void handleCreateLine(ActionEvent event){
        Navigator.navigate(event,Navigator.ADD_BUS_LINE);
    }

    public void handleReport(ActionEvent event){
        Navigator.navigate(event,Navigator.SEARCH_COMPANY_PAGE);

    }

    public void handleRegister(ActionEvent event){
        Navigator.navigate(event,Navigator.REGISTER_PAGE);
    }

    public void handleLogOut(ActionEvent event){
        Navigator.navigate(event,Navigator.LOGIN_PAGE);
    }
    protected void showError(String header, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
    protected void showConfirmation(String header, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
