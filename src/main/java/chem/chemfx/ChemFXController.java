package chem.chemfx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ChemFXController implements Initializable {

    @FXML private AnchorPane molStage;
    @FXML private ToggleButton carbon;
    @FXML private ToggleButton bond;

    private final DraggableMaker draggableMaker = new DraggableMaker();
    private BondManager bondManager;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bondManager = new BondManager(molStage);

        // Create atoms
        molStage.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            if (carbon.isSelected()) {
                AtomNode atom = new AtomNode(event.getX(), event.getY(), molStage, bondManager);
                draggableMaker.makeDraggable(atom.getCircle());
            }
        });

        // Bond mode
        bond.selectedProperty().addListener((obs, oldVal, newVal) -> bondManager.setBondMode(newVal));

        // 🔥 Global delete key handler
        molStage.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DELETE) {
                AtomNode.deleteSelectedAtoms();
            }
        });

        // Make sure molStage is focusable
        molStage.setFocusTraversable(true);
    }

}
