package chem.chemfx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ChemFXController implements Initializable {

    @FXML private AnchorPane molStage;
    @FXML private ToggleButton carbon;
    @FXML private ToggleButton singleBond;
    @FXML private ToggleButton doubleBond;
    @FXML private ToggleButton tripleBond;

    private ToggleGroup toggleGroup = new ToggleGroup();


    private ToggleButton selected;

    private final DraggableMaker draggableMaker = new DraggableMaker();
    private BondManager bondManager;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bondManager = new BondManager(molStage);

        singleBond.selectedProperty().addListener((obs, oldVal, newVal) -> {
            bondManager.setBondMode(newVal);
        });

        toggleGroup.getToggles().addAll(carbon, singleBond, doubleBond, tripleBond);


        // Create atoms
        molStage.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            if (carbon.isSelected()) {
                AtomNode atom = new AtomNode(event.getX(), event.getY(), molStage, bondManager);
                draggableMaker.makeDraggable(atom.getCircle());
            }
        });



        // Bond mode
        singleBond.selectedProperty().addListener((obs, oldVal, newVal) -> bondManager.setBondMode(newVal));

        

        molStage.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DELETE) {
                AtomNode.deleteSelectedAtoms();
            }
        });

        molStage.setFocusTraversable(true);
    }
}
