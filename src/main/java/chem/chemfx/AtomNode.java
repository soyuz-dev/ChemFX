package chem.chemfx;

import chem.chemfx.atoms.Atom;
import chem.chemfx.atoms.BohrAtom;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomNode {

    /* ------------------ Static Fields ------------------ */
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(0);
    private static final Set<AtomNode> ALL_ATOMS = new HashSet<>();

    /* ------------------ Instance Fields ------------------ */
    private final int id;
    private final Circle circle;
    private final Text text;
    private final StackPane atomGroup;
    private final Pane container;
    private final BondManager bondManager;
    private final Atom atom;

    private boolean selected = false;

    /* ------------------ Constructors ------------------ */
    public AtomNode(double x, double y, Pane container, BondManager bondManager) {
        this(x, y, container, bondManager, 6);
    }

    public AtomNode(double x, double y, Pane container, BondManager bondManager, int atomicNumber) {
        this.id = ID_GENERATOR.getAndIncrement();
        this.container = container;
        this.bondManager = bondManager;
        this.atom = new BohrAtom(atomicNumber);

        this.circle = createCircle();
        this.text = createText();
        this.atomGroup = createAtomGroup(x, y);

        registerEventHandlers();
        styleUnselected();

        ALL_ATOMS.add(this);
        container.getChildren().add(atomGroup);
    }

    /* ------------------ UI Creation ------------------ */
    private Circle createCircle() {
        Circle c = new Circle(15, Color.rgb(240,240,240));
        c.setUserData(this);
        return c;
    }

    private Text createText() {
        Text t = new Text(atom.getElementSymbol());
        t.setStroke(Color.BLACK);
        return t;
    }

    private StackPane createAtomGroup(double x, double y) {
        StackPane stack = new StackPane(circle, text);
        stack.setLayoutX(x);
        stack.setLayoutY(y);
        stack.setPickOnBounds(true); // clicks anywhere in the stack’s bounds
        return stack;
    }

    /* ------------------ Event Handling ------------------ */
    private void registerEventHandlers() {
        atomGroup.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (!event.isShiftDown()) {
                deselectAllExcept(this);
                container.requestFocus();
            }
            bondManager.selectAtom(this);
            toggleSelection();
            event.consume(); // consume the click; doesn't affect press/drag
        });
    }

    private static void deselectAllExcept(AtomNode except) {
        for (AtomNode atom : ALL_ATOMS) {
            if (atom != except && atom.isSelected()) {
                atom.setSelected(false);
            }
        }
    }

    /* ------------------ Selection Handling ------------------ */
    public void setSelected(boolean selected) {
        if (this.selected != selected) {
            this.selected = selected;
            updateSelectionStyle();
        }
    }

    public void toggleSelection() {
        setSelected(!selected);
    }

    public boolean isSelected() {
        return selected;
    }

    private void updateSelectionStyle() {
        if (selected) styleSelected();
        else styleUnselected();
    }

    private void styleSelected() {
        circle.setFill(Color.rgb(100,255,150));
        circle.setStroke(Color.rgb(60,200,80));
        circle.setStrokeWidth(2);
    }

    private void styleUnselected() {
        circle.setFill(Color.rgb(240, 240,240));
        circle.setStroke(null);
        circle.setStrokeWidth(0);
    }

    /* ------------------ Deletion ------------------ */
    public static void deleteSelectedAtoms() {
        Set<AtomNode> toDelete = new HashSet<>();
        for (AtomNode atom : ALL_ATOMS) {
            if (atom.isSelected()) {
                toDelete.add(atom);
            }
        }
        toDelete.forEach(AtomNode::delete);
    }

    public void delete() {
        if (bondManager != null) {
            bondManager.deleteBondsConnectedTo(this);
            for (Pair<Atom, Integer> p : atom.getBondedTo()) {
                atom.unbond(p.getKey());
            }
        }
        container.getChildren().remove(atomGroup);
        ALL_ATOMS.remove(this);
    }

    /* ------------------ Getters ------------------ */
    public int getId() { return id; }
    public Text getText() { return text; }
    public StackPane getAtomGroup() { return atomGroup; }
    public Circle getCircle() { return circle; }
    public Atom getAtom() { return atom; }
}
