package de.cofinpro.portfolio.view;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import de.cofinpro.portfolio.Main;
import de.cofinpro.portfolio.model.Wertpapier;
/**
 * Created by mczadek on 28.08.2015.
 */
public class WertpapierOverviewController {
    @FXML
    private TableView<Wertpapier> wertpapierTable;
    @FXML
    private TableColumn<Wertpapier, String> nameColumn;
    @FXML
    private TableColumn<Wertpapier, String> isinColumn;
    @FXML
    private TableColumn<Wertpapier,Double> preisColumn;
    @FXML
    private TableColumn<Wertpapier, String> tickerColumn;

    @FXML
    private Label nameLabel;
    @FXML
    private Label isinLabel;
    @FXML
    private Label preisLabel;
    @FXML
    private Label tickerLabel;

    // Reference to the main application.
    private Main mainApp;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public WertpapierOverviewController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialize the person table with the two columns.
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        isinColumn.setCellValueFactory(cellData -> cellData.getValue().isinProperty());
        preisColumn.setCellValueFactory(cellData -> cellData.getValue().preisProperty().asObject());
        tickerColumn.setCellValueFactory(cellData -> cellData.getValue().tickerProperty());

        // Clear person details.
        showWertpapierDetails(null);

        // Listen for selection changes and show the person details when changed.
        wertpapierTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showWertpapierDetails(newValue));

    }
    private void showWertpapierDetails(Wertpapier wertpapier) {
        if (wertpapier != null) {
            // Fill the labels with info from the person object.
            nameLabel.setText(wertpapier.getName());
            isinLabel.setText(wertpapier.getIsin());
            preisLabel.setText(String.valueOf(wertpapier.getPreis()));
            tickerLabel.setText(wertpapier.getTicker());
        } else {
            // Person is null, remove all the text.
            nameLabel.setText("");
            isinLabel.setText("");
            preisLabel.setText("");
            tickerLabel.setText("");
        }
    }

    /**
     * Called when the user clicks on the delete button.
     */
    @FXML
    private void handleDeleteWertpapier() {
        int selectedIndex = wertpapierTable.getSelectionModel().getSelectedIndex();
        if(selectedIndex > 0) {
            wertpapierTable.getItems().remove(selectedIndex);
        }
        else {
            // Nothing selected.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Person Selected");
            alert.setContentText("Please select a person in the table.");

            alert.showAndWait();
        }
    }

    /**
     * Called when the user clicks the new button. Opens a dialog to edit
     * details for a new person.
     */
    @FXML
    private void handleNewWertpapier() {
        Wertpapier tempWertpapier = new Wertpapier();
        boolean okClicked = mainApp.showWertpapierEditDialog(tempWertpapier);
        if (okClicked) {
            mainApp.getWertpapierData().add(tempWertpapier);
        }
    }

    /**
     * Called when the user clicks the edit button. Opens a dialog to edit
     * details for the selected person.
     */
    @FXML
    private void handleEditWertpapier() {
        Wertpapier selectedWertpapier = wertpapierTable.getSelectionModel().getSelectedItem();
        if (selectedWertpapier != null) {
            boolean okClicked = mainApp.showWertpapierEditDialog(selectedWertpapier);
            if (okClicked) {
                showWertpapierDetails(selectedWertpapier);
            }

        } else {
            // Nothing selected.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Wertpapier Selected");
            alert.setContentText("Please select a Wertpapier in the table.");

            alert.showAndWait();
        }
    }
    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        wertpapierTable.setItems(mainApp.getWertpapierData());
    }

}
