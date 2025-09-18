package chem.chemfx;

import chem.chemfx.atoms.CovalentBondException;
import javafx.scene.control.Alert;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.*;

public class BondManager {

    private int bondMode = 0;
    private AtomNode selected = null;
    private final Pane container;
    private final List<Bond> bonds = new ArrayList<>();
    private ToggleButton bondToggle;

    public BondManager(Pane container, ToggleButton bondToggle) {
        this.container = container;
        this.bondToggle = bondToggle;
    }

    public void setBondMode(int mode) {
        this.bondMode = mode;
        if (mode==0 && selected != null) {
            selected.toggleSelection();
            selected = null;
        }
    }

    public int getBondMode() {
        return bondMode;
    }

    public boolean isBonding() {return bondMode!=0;}

    public boolean selectBondingAtom(AtomNode atom) {
        if(selected == null) {
            selected = atom;
            atom.setSelected(true);
        } else if(!isBonding() && selected != atom){
            selected = atom;
            atom.setSelected(true);
        } else if(isBonding() && selected != atom){
            makeNewBond(selected, atom, bondMode); // bond

            atom.setSelected(false);
            selected.setSelected(false);

            selected = null;
            bondToggle.setSelected(false);
        } else {
            selected.setSelected(false);
            selected = null;
        }
        return false;
    }

    private void makeNewBond(AtomNode a1, AtomNode a2, int order) {
        Line bondLine = new Line();

        // Bind line ends to the center of each StackPane in container coordinates
        bondLine.startXProperty().bind(a1.getAtomGroup().layoutXProperty().add(a1.getAtomGroup().widthProperty().divide(2)));
        bondLine.startYProperty().bind(a1.getAtomGroup().layoutYProperty().add(a1.getAtomGroup().heightProperty().divide(2)));

        bondLine.endXProperty().bind(a2.getAtomGroup().layoutXProperty().add(a2.getAtomGroup().widthProperty().divide(2)));
        bondLine.endYProperty().bind(a2.getAtomGroup().layoutYProperty().add(a2.getAtomGroup().heightProperty().divide(2)));

        bondLine.setStroke(Color.BLACK);
        bondLine.setStrokeWidth(2);

        try{
            if(!Bond.existsFor(a1,a2)) {
                bonds.add(new Bond(a1, a2, bondLine));
                container.getChildren().add(0, bondLine); // draw behind atoms
                a1.updateSelectionStyle();
                a2.updateSelectionStyle();
                bondToggle.setSelected(false);
                bondMode = 0;
            } else {

            }
        } catch (CovalentBondException e){
            Alert a = new Alert(Alert.AlertType.ERROR, "One of the atoms have already bonded to full capacity.");
            a.show();
        }
    }


    public void deleteBondsConnectedTo(AtomNode atom) {
        Iterator<Bond> iterator = bonds.iterator();
        while (iterator.hasNext()) {
            Bond bond = iterator.next();
            if (bond.connects(atom)) {
                for (Line l: bond.lines) container.getChildren().remove(l);
                bond.disconnect();
                iterator.remove();
            }
        }
    }
}
