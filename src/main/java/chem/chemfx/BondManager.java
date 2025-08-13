package chem.chemfx;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.*;

public class BondManager {

    private boolean bondMode = false;
    private AtomNode firstSelected = null;
    private final Pane container;
    private final List<Bond> bonds = new ArrayList<>();

    public BondManager(Pane container) {
        this.container = container;
    }

    public void setBondMode(boolean mode) {
        this.bondMode = mode;
        if (!mode && firstSelected != null) {
            firstSelected.toggleSelection();
            firstSelected = null;
        }
    }

    public boolean isBondMode() {
        return bondMode;
    }

    public void selectAtom(AtomNode atom) {
        if (!bondMode) return;

        if (firstSelected == null) {
            firstSelected = atom;
            atom.toggleSelection();
        } else if (firstSelected != atom) {
            atom.toggleSelection();         // select second
            firstSelected.toggleSelection(); // deselect first

            drawBond(firstSelected, atom);  // bond
            firstSelected = null;
        } else {
            // clicked same atom again to cancel
            atom.toggleSelection();
            firstSelected = null;
        }
    }

    private void drawBond(AtomNode a1, AtomNode a2) {
        Line bondLine = new Line();

        // Bind line ends to the center of each StackPane in container coordinates
        bondLine.startXProperty().bind(a1.getAtomGroup().layoutXProperty().add(a1.getAtomGroup().widthProperty().divide(2)));
        bondLine.startYProperty().bind(a1.getAtomGroup().layoutYProperty().add(a1.getAtomGroup().heightProperty().divide(2)));

        bondLine.endXProperty().bind(a2.getAtomGroup().layoutXProperty().add(a2.getAtomGroup().widthProperty().divide(2)));
        bondLine.endYProperty().bind(a2.getAtomGroup().layoutYProperty().add(a2.getAtomGroup().heightProperty().divide(2)));

        bondLine.setStroke(Color.BLACK);
        bondLine.setStrokeWidth(2);

        container.getChildren().add(0, bondLine); // draw behind atoms
        bonds.add(new Bond(a1, a2, bondLine));
    }


    public void deleteBondsConnectedTo(AtomNode atom) {
        Iterator<Bond> iterator = bonds.iterator();
        while (iterator.hasNext()) {
            Bond bond = iterator.next();
            if (bond.connects(atom)) {
                container.getChildren().remove(bond.line);
                iterator.remove();
            }
        }
    }
}
