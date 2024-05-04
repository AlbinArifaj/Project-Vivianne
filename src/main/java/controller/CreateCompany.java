package controller;

import ENUMS.AreaCode;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import repository.CompanyRepository;

public class CreateCompany extends BGmain {

    @FXML
    private TextField txtCompanyName;

    @FXML
    private TextArea txtDescription;

    @FXML
    private MenuButton mbtnAreaCode;


    @FXML
    private void handleAreaCodeMenuItemClicked(ActionEvent ae){
        MenuItem item = (MenuItem) ae.getSource();
        mbtnAreaCode.setText(item.getText());
    }

    @FXML
    private void handleDiscard(ActionEvent event) {
        txtCompanyName.setText("");
        txtDescription.setText("");
        mbtnAreaCode.setText("PRISTINA");
    }

    private void handleDiscard() {
        txtCompanyName.setText("");
        txtDescription.setText("");
        mbtnAreaCode.setText("PRISTINA");
    }

    @FXML
    private void handleCreate(ActionEvent event) {
        if (createCompany())
            showConfirmation("Company Alert" ,"The process was a success" );
        handleDiscard();
    }

    private boolean createCompany() {
        String companyName = txtCompanyName.getText();
        String description = txtDescription.getText();
        String areaCode = mbtnAreaCode.getText();
        try {
            if (companyName == null || companyName.trim().isEmpty()) {
                showError("Validation Error", "Company name is required.");
                return false;
            }

            if (description == null || description.trim().isEmpty()) {
                showError("Validation Error", "Description is required.");
                return false;
            }
            CompanyRepository.createCompany(companyName, AreaCode.valueOf(areaCode),description);

        }
        catch (RuntimeException e){
            showError("Create Company Error", e.getMessage());
        }
        return true;
    }
}
