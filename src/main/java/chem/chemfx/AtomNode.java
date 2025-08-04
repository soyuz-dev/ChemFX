package chem.chemfx;

import chem.chemfx.atoms.Atom;
import chem.chemfx.atoms.BohrAtom;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomNode {

    private static final AtomicInteger idGenerator = new AtomicInteger(0);
    private static final Set<AtomNode> allAtoms = new HashSet<>();

    private final int id;
    private final Circle circle;
    private boolean selected = false;
    private final Pane container;
    private final BondManager bondManager;
    private final Atom atom;


    public AtomNode(double x, double y, Pane container, BondManager bondManager) {
        this(x,y,container,bondManager, 6);
    }


    public AtomNode(double x, double y, Pane container, BondManager bondManager, int atomicNumber) {
        this.bondManager = bondManager;
        this.id = idGenerator.getAndIncrement();
        this.container = container;

        this.atom = new BohrAtom(atomicNumber);

        circle = new Circle(x, y, 10, Color.DARKGRAY);
        circle.setUserData(this);

        styleUnselected();

        circle.setOnMouseClicked(event -> {
            if (bondManager != null) {
                bondManager.selectAtom(this);
            } else {
                if (!event.isShiftDown()) {
                    // Deselect all other atoms
                    for (AtomNode atom : allAtoms) {
                        if (atom != this && atom.isSelected()) {
                            atom.setSelected(false);
                        }
                    }
                }

                // Toggle selection of this one
                toggleSelection();

                container.requestFocus(); // Ensure DELETE key still works
            }
            event.consume();
        });


        allAtoms.add(this);
        container.getChildren().add(circle);
    }

    private void setSelected(boolean selected) {
        this.selected = selected;
        if (selected) styleSelected();
        else styleUnselected();
    }

    public static void deleteSelectedAtoms() {
        Set<AtomNode> toDelete = new HashSet<>();
        for (AtomNode atom : allAtoms) {
            if (atom.isSelected()) {
                toDelete.add(atom);
            }
        }
        for (AtomNode atom : toDelete) {
            atom.delete();
        }
    }

    public void delete() {
        if (bondManager != null) {
            bondManager.deleteBondsConnectedTo(this);
        }
        container.getChildren().remove(circle);
        allAtoms.remove(this);
    }


    public void toggleSelection() {
        selected = !selected;
        if (selected) styleSelected();
        else styleUnselected();
    }

    public boolean isSelected() {
        return selected;
    }

    public int getId() {
        return id;
    }

    public Circle getCircle() {
        return circle;
    }

    private void styleSelected() {
        circle.setStroke(Color.YELLOW);
        circle.setStrokeWidth(2);
    }

    private void styleUnselected() {
        circle.setStroke(null);
        circle.setStrokeWidth(0);
    }

    public Atom getAtom() { return this.atom; }
}

