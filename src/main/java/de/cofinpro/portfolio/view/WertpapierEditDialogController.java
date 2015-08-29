package de.cofinpro.portfolio.view;

import de.cofinpro.portfolio.Main;
import de.cofinpro.portfolio.model.Wertpapier;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;


/**
 * Created by mczadek on 28.08.2015.
 */
public class WertpapierEditDialogController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField isinField;
    @FXML
    private TextField preisField;
    @FXML
    private TextField tickerField;

    private Stage dialogStage;
    private Wertpapier wertpapier;
    private boolean okClicked = false;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    }

    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Sets the person to be edited in the dialog.
     *
     * @param wertpapier
     */
    public void setWertpapier(Wertpapier wertpapier) {
        this.wertpapier = wertpapier;

        nameField.setText(wertpapier.getName());
        isinField.setText(wertpapier.getIsin());
        preisField.setText(String.valueOf(wertpapier.getPreis()));
        tickerField.setText(wertpapier.getTicker());
    }

    /**
     * Returns true if the user clicked OK, false otherwise.
     *
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            wertpapier.setName(nameField.getText());
            wertpapier.setIsin(isinField.getText());
            wertpapier.setPreis(Double.parseDouble(preisField.getText()));
            wertpapier.setTicker(tickerField.getText());

            okClicked = true;
            dialogStage.close();
        }
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Validates the user input in the text fields.
     *
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (nameField.getText() == null || nameField.getText().length() == 0) {
            errorMessage += "No valid name!\n";
        }
        if (isinField.getText() == null || isinField.getText().length() == 0) {
            errorMessage += "No valid isin !\n";
        }
        if (preisField.getText() == null || preisField.getText().length() == 0) {
            errorMessage += "No valid price!\n";
        }
        if (tickerField.getText() == null || tickerField.getText().length() == 0) {
            errorMessage += "No valid ticker !\n";
        }
        else{
            try {
                Double.parseDouble(preisField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "No valid price!\n";
            }
        }


        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }

}
