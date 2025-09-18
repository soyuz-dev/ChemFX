package chem.chemfx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ChemFXController implements Initializable {

    @FXML private AnchorPane molPane;
    @FXML private ToggleButton carbon;
    @FXML private ToggleButton oxygen;
    @FXML private ToggleButton nitrogen;
    @FXML private ToggleButton singleBond;
    @FXML private ToggleButton doubleBond;
    @FXML private ToggleButton tripleBond;

    private boolean bonding;

    private final ToggleGroup toggleGroup = new ToggleGroup();


    private final DraggableMaker draggableMaker = new DraggableMaker();
    private BondManager bondManager;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bondManager = new BondManager(molPane, singleBond);

        singleBond.selectedProperty().addListener((obs, oldVal, newVal) -> {
            bondManager.setBondMode(newVal ? 1 : bondManager.getBondMode());
            bonding = bondManager.isBonding();
        });

        toggleGroup.getToggles().addAll(carbon, oxygen, nitrogen, singleBond, doubleBond, tripleBond);


        // Create atoms
        molPane.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {

            if (carbon.isSelected()) {
                BohrAtomNode atom = new BohrAtomNode(event.getX(), event.getY(), molPane, bondManager);
                draggableMaker.makeDraggable(atom.getAtomGroup());
            } else if (oxygen.isSelected()) {
                BohrAtomNode atom = new BohrAtomNode(event.getX(), event.getY(), molPane, bondManager, 8);
                draggableMaker.makeDraggable(atom.getAtomGroup());
            } else if (nitrogen.isSelected()) {
                BohrAtomNode atom = new BohrAtomNode(event.getX(), event.getY(), molPane, bondManager, 7);
                draggableMaker.makeDraggable(atom.getAtomGroup());
            }

        });



        // Bond mode
        singleBond.selectedProperty().addListener((obs, oldVal, newVal) -> {bondManager.setBondMode(newVal?1: bondManager.getBondMode());});

        

        molPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DELETE) {
                BohrAtomNode.deleteSelectedAtoms();
            }
        });

        molPane.setFocusTraversable(true);
    }
}
