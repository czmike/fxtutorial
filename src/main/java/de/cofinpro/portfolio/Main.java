package de.cofinpro.portfolio;

import de.cofinpro.portfolio.model.Wertpapier;
import de.cofinpro.portfolio.model.WertpapierListWrapper;
import de.cofinpro.portfolio.view.RootLayoutController;
import de.cofinpro.portfolio.view.WertpapierEditDialogController;
import de.cofinpro.portfolio.view.WertpapierOverviewController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.prefs.Preferences;

public class Main extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    private ObservableList<Wertpapier> wertpapierData = FXCollections.observableArrayList();

    public Main() {
        // Add some sample data
        wertpapierData.add(new Wertpapier("Siemens", "DE0007236101", 88.67,"SIEM", LocalDateTime.now()));
        wertpapierData.add(new Wertpapier("BASF", "DE000BASF111", 71.91, "BASF", LocalDateTime.now()));
    }

    /**
     * Returns the data as an observable list of Wertpapier.
     * @return
     */
    public ObservableList<Wertpapier> getWertpapierData() {
        return wertpapierData;
    }
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("PortfolioApp");

        initRootLayout();

        showPortfolioOverview();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class
                    .getResource("view/rootlayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            // Give the controller access to the main app.
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Try to load last opened person file.
        File file = getWertpapierFilePath();
        if (file != null) {
            loadWertpapierDataFromFile(file);
        }
    }

    /**
     * Shows the person overview inside the root layout.
     */
    public void showPortfolioOverview() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/portfolio.fxml"));
            AnchorPane wertpapierOverview = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(wertpapierOverview);
            // Give the controller access to the main app.
            WertpapierOverviewController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean showWertpapierEditDialog(Wertpapier wertpapier) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/wertpapierEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Wertpapier");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            WertpapierEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setWertpapier(wertpapier);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns the person file preference, i.e. the file that was last opened.
     * The preference is read from the OS specific registry. If no such
     * preference can be found, null is returned.
     *
     * @return
     */
    public File getWertpapierFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        String filePath = prefs.get("filePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    /**
     * Sets the file path of the currently loaded file. The path is persisted in
     * the OS specific registry.
     *
     * @param file the file or null to remove the path
     */
    public void setWertpapierFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        if (file != null) {
            prefs.put("filePath", file.getPath());

            // Update the stage title.
            primaryStage.setTitle("PortfolioApp - " + file.getName());
        } else {
            prefs.remove("filePath");

            // Update the stage title.
            primaryStage.setTitle("PortfolioApp");
        }
    }

    /**
     * Loads wertpapier data from the specified file. The current person data will
     * be replaced.
     *
     * @param file
     */
    public void loadWertpapierDataFromFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(WertpapierListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            // Reading XML from the file and unmarshalling.
            WertpapierListWrapper wrapper = (WertpapierListWrapper) um.unmarshal(file);

            wertpapierData.clear();
            wertpapierData.addAll(wrapper.getWertpapiere());

            // Save the file path to the registry.
            setWertpapierFilePath(file);

        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load data");
            alert.setContentText("Could not load data from file:\n" + file.getPath());

            alert.showAndWait();
        }
    }


    /**
     * Saves the current person data to the specified file.
     *
     * @param file
     */
    public void saveWertpapierDataToFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(WertpapierListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Wrapping our person data.
            WertpapierListWrapper wrapper = new WertpapierListWrapper();
            wrapper.setWertpapiere(wertpapierData);

            // Marshalling and saving XML to the file.
            m.marshal(wrapper, file);

            // Save the file path to the registry.
            setWertpapierFilePath(file);
        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not save data");
            alert.setContentText("Could not save data to file:\n" + file.getPath());

            alert.showAndWait();
        }
    }

    public void getLatestPreise()
    {
        for (Wertpapier wertpapier: wertpapierData)
        {
            getLatestPreis(wertpapier);
        }
    }
    private void getLatestPreis(Wertpapier wertpapier)
    {
        ObjectMapper mapper = new ObjectMapper();

        try {

            // convert user object to json string, and save to a file
            //mapper.writeValue(new File("c:\\user.json"), user);

            // display to console
            System.out.println(mapper.writeValueAsString(wertpapier));

        } catch (JsonGenerationException e) {

            e.printStackTrace();

        } catch (JsonMappingException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }
    /**
     * Returns the main stage.
     *
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}