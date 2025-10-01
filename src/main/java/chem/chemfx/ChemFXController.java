package chem.chemfx;

import chem.chemfx.atoms.BohrAtom;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChemFXController implements Initializable {

    private final ToggleGroup toggleGroup = new ToggleGroup();
    private final DraggableMaker draggableMaker = new DraggableMaker();
    @FXML
    private AnchorPane molPane;
    @FXML
    private ToggleButton carbon;
    @FXML
    private ToggleButton oxygen;
    @FXML
    private ToggleButton nitrogen;
    @FXML
    private ToggleButton singleBond;
    @FXML
    private ToggleButton doubleBond;
    @FXML
    private ToggleButton tripleBond;
    @FXML
    private Button btn_rxn_view;
    @FXML
    private Label about;
    @FXML
    private ToggleButton custom;

    private int customAtomicNumber;

    private BondManager bondManager;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bondManager = new BondManager(molPane, singleBond, doubleBond, tripleBond);


        toggleGroup.getToggles().addAll(carbon, oxygen, nitrogen, singleBond, doubleBond, tripleBond, custom);


        // Create atoms
        molPane.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {

            if (carbon.isSelected()) {
                BohrAtomNode atom = new BohrAtomNode(event.getX(), event.getY(), molPane, bondManager);
                draggableMaker.makeDraggable(atom.getAtomGroup());
            } else if (oxygen.isSelected()) {
                BohrAtomNode atom = new BohrAtomNode(event.getX(), event.getY(), molPane, bondManager, 8);
                draggableMaker.makeDraggable(atom.getAtomGroup());
            } else if (nitrogen.isSelected()) {
                BohrAtomNode atom = new BohrAtomNode(event.getX(), event.getY(), molPane, bondManager, 7);
                draggableMaker.makeDraggable(atom.getAtomGroup());
            } else if (custom.isSelected() && customAtomicNumber > 0) {
                BohrAtomNode atom = new BohrAtomNode(event.getX(), event.getY(), molPane, bondManager, customAtomicNumber);
                draggableMaker.makeDraggable(atom.getAtomGroup());
            }

        });


        // Bond mode
        singleBond.selectedProperty().addListener((obs, oldVal, newVal) -> {
            bondManager.setBondMode(newVal ? 1 : bondManager.getBondMode());
        });


        doubleBond.selectedProperty().addListener((obs, oldVal, newVal) -> {
            bondManager.setBondMode(newVal ? 2 : bondManager.getBondMode());
        });


        tripleBond.selectedProperty().addListener((obs, oldVal, newVal) -> {
            bondManager.setBondMode(newVal ? 3 : bondManager.getBondMode());
        });

        molPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DELETE) {
                BohrAtomNode.deleteSelectedAtoms();
            }
        });

        custom.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) { // button was selected
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Other Element");
                dialog.setHeaderText("Enter the atomic number of the element:");
                dialog.setContentText("Atomic Number:");
                dialog.showAndWait().ifPresent(input -> {
                    try {
                        int num = Integer.parseInt(input);
                        if (num < 1 || num > 118) { // simple validation
                            throw new NumberFormatException();
                        }
                        customAtomicNumber = num;
                        this.custom.setText(BohrAtom.elementSymbols[num-1]);
                    } catch (NumberFormatException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid atomic number. Must be 1-118.");
                        alert.showAndWait();
                        custom.setSelected(false);
                    }
                });
            }
        });


        btn_rxn_view.setOnAction(event -> {
            try {
                // Load FXML
                FXMLLoader loader = new FXMLLoader(ChemFXMain.class.getResource("chemfx-reaction-view.fxml"));
                BorderPane reactionRoot = loader.load();

                // Optional: create a new stage
                Stage reactionStage = new Stage();
                reactionStage.setTitle("Reaction View");
                reactionStage.setScene(new Scene(reactionRoot));
                reactionStage.show();

            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to open Reaction View:\n" + e.getMessage());
                alert.show();
            }
        });


        about.setOnMouseClicked(event -> {
            try {
                // Load FXML
                FXMLLoader loader = new FXMLLoader(ChemFXMain.class.getResource("chemfx-about.fxml"));
                VBox reactionRoot = loader.load();
                // Optional: create a new stage
                Stage aboutStage = new Stage();
                aboutStage.setTitle("About Programmer");
                aboutStage.setScene(new Scene(reactionRoot));
                aboutStage.initModality(Modality.APPLICATION_MODAL); // Optional: blocks main window
                aboutStage.show();

            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to open About View:\n" + e.getMessage());
                alert.show();
            }
        });


        molPane.setFocusTraversable(true);
    }
}
