package chem.chemfx;

import chem.chemfx.atoms.CovalentBondException;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.control.Alert;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BondManager {

    private final Pane container;
    private final List<Bond> bonds = new ArrayList<>();
    private int bondMode = 0;
    private AtomNode selected = null;
    private ToggleButton singleBond;
    private ToggleButton doubleBond;
    private ToggleButton tripleBond;

    public BondManager(Pane container, ToggleButton singleBond, ToggleButton doubleBond, ToggleButton tripleBond) {
        this.container = container;
        this.singleBond = singleBond;
        this.doubleBond = doubleBond;
        this.tripleBond = tripleBond;
    }

    public int getBondMode() {
        return bondMode;
    }

    public void setBondMode(int mode) {
        this.bondMode = mode;
        if (mode == 0 && selected != null) {
            selected.toggleSelection();
            selected = null;
        }
    }

    public boolean isBonding() {
        return bondMode != 0;
    }

    public boolean selectBondingAtom(AtomNode atom) {
        if (selected == null) {
            selected = atom;
            atom.setSelected(true);
        } else if (!isBonding() && selected != atom) {
            selected = atom;
            atom.setSelected(true);
        } else if (isBonding() && selected != atom) {
            makeNewBond(selected, atom, bondMode); // bond

            atom.setSelected(false);
            selected.setSelected(false);

            selected = null;
            deselectAllToggles();

        } else {
            selected.setSelected(false);
            selected = null;
        }
        return false;
    }

    /**
     * Creates a new bond or parallel bond line between two atoms.
     * - If no existing bond, creates a main line
     * - If bond exists, creates a parallel line offset by 'l' pixels
     * - Lines are dynamically bound to atom positions for drag updates
     *
     * @param a1    First AtomNode
     * @param a2    Second AtomNode
     * @param order Bond order (1-3)
     */
    private void makeNewBond(AtomNode a1, AtomNode a2, int order) {

        try {
            Bond existing;
            if (Bond.existsFor(a1, a2)){
                existing = Bond.getBond(a1,a2);
                container.getChildren().removeAll(existing.lines);
                existing.disconnect();
                existing = null;
            }

            if (order >= 1) {
                Line bondLine = new Line();
                bondLine.setStroke(Color.BLACK);
                bondLine.setStrokeWidth(2);

                bindLineToAtoms(bondLine, a1, a2, 0);
                Bond newBond = new Bond(a1, a2, bondLine);
                bonds.add(newBond);
                container.getChildren().addFirst(bondLine);
                a1.updateSelectionStyle();
                a2.updateSelectionStyle();
            }
            if (order >= 2){
                Line bondLine = new Line();
                bondLine.setStroke(Color.BLACK);
                bondLine.setStrokeWidth(2);
                bindLineToAtoms(bondLine, a1, a2, 5);
                container.getChildren().addFirst(bondLine);
                existing = Bond.getBond(a1,a2);
                existing.bond(bondLine);

                a1.updateSelectionStyle();
                a2.updateSelectionStyle();
            }
            if (order == 3){
                Line bondLine = new Line();
                bondLine.setStroke(Color.BLACK);
                bondLine.setStrokeWidth(2);
                bindLineToAtoms(bondLine, a1, a2, 10);

                container.getChildren().addFirst(bondLine);
                existing = Bond.getBond(a1,a2);
                existing.bond(bondLine);

                a1.updateSelectionStyle();
                a2.updateSelectionStyle();
            }
        } catch (CovalentBondException e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.show();
        }

        // Update atom selection visuals and reset bond mode
        a1.updateSelectionStyle();
        a2.updateSelectionStyle();
        deselectAllToggles();
        bondMode = 0;
    }

    /**
     * Binds a JavaFX Line to two AtomNodes, optionally creating a parallel offset.
     *
     * @param line   Line to bind
     * @param a1     First AtomNode
     * @param a2     Second AtomNode
     * @param offset Distance in pixels to offset the line perpendicular to the original bond
     */
    private void bindLineToAtoms(Line line, AtomNode a1, AtomNode a2, double offset) {
        // Helper bindings for atom centers
        DoubleBinding x1 = centerXBinding(a1);
        DoubleBinding y1 = centerYBinding(a1);
        DoubleBinding x2 = centerXBinding(a2);
        DoubleBinding y2 = centerYBinding(a2);

        if (offset == 0) {
            // No offset: bind directly to atom centers
            line.startXProperty().bind(x1);
            line.startYProperty().bind(y1);
            line.endXProperty().bind(x2);
            line.endYProperty().bind(y2);
        } else {
            // Offset: calculate perpendicular vector for parallel line
            DoubleBinding dx = Bindings.createDoubleBinding(() -> x2.get() - x1.get(), x1, x2);
            DoubleBinding dy = Bindings.createDoubleBinding(() -> y2.get() - y1.get(), y1, y2);
            DoubleBinding length = Bindings.createDoubleBinding(() -> Math.hypot(dx.get(), dy.get()), dx, dy);

            DoubleBinding offsetX = Bindings.createDoubleBinding(() -> -dy.get() / length.get() * offset, dx, dy, length);
            DoubleBinding offsetY = Bindings.createDoubleBinding(() -> dx.get() / length.get() * offset, dx, dy, length);

            line.startXProperty().bind(Bindings.add(x1, offsetX));
            line.startYProperty().bind(Bindings.add(y1, offsetY));
            line.endXProperty().bind(Bindings.add(x2, offsetX));
            line.endYProperty().bind(Bindings.add(y2, offsetY));
        }
    }

    /**
     * Helper binding: returns X coordinate of atom center
     */
    private DoubleBinding centerXBinding(AtomNode atom) {
        return Bindings.createDoubleBinding(
                () -> atom.getAtomGroup().getLayoutX() + atom.getAtomGroup().getWidth() / 2,
                atom.getAtomGroup().layoutXProperty(),
                atom.getAtomGroup().widthProperty()
        );
    }

    /**
     * Helper binding: returns Y coordinate of atom center
     */
    private DoubleBinding centerYBinding(AtomNode atom) {
        return Bindings.createDoubleBinding(
                () -> atom.getAtomGroup().getLayoutY() + atom.getAtomGroup().getHeight() / 2,
                atom.getAtomGroup().layoutYProperty(),
                atom.getAtomGroup().heightProperty()
        );
    }




    public void deleteBondsConnectedTo(AtomNode atom) {
        Iterator<Bond> iterator = bonds.iterator();
        while (iterator.hasNext()) {
            Bond bond = iterator.next();
            if (bond.connects(atom)) {
                for (Line l : bond.lines) container.getChildren().remove(l);
                bond.disconnect();
                iterator.remove();
            }
        }
    }

    public void deselectAllToggles(){
        singleBond.setSelected(false);
        doubleBond.setSelected(false);
        tripleBond.setSelected(false);
    }
}
