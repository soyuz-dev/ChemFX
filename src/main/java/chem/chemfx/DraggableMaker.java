package chem.chemfx;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 * Utility class to make JavaFX {@link Node}s draggable within the bounds of their parent {@link Pane}.
 * <p>
 * Nodes made draggable using this class can be clicked and dragged by the mouse.
 * The movement is automatically constrained within the parent's dimensions,
 * leaving a configurable margin to prevent overlap with the pane edges.
 */
public class DraggableMaker {

    /** Margin (in pixels) to keep between the draggable node and the edges of its parent. */
    private static final double margin = 10;

    /**
     * Helper structure to store the offset (delta) between the mouse click position
     * and the node's layout coordinates during a drag.
     */
    private static final class Delta {
        double x, y;
    }

    /**
     * Makes the given {@link Node} draggable within its parent {@link Pane}.
     * <p>
     * Usage:
     * <pre>
     *     DraggableMaker maker = new DraggableMaker();
     *     maker.makeDraggable(myNode);
     * </pre>
     *
     * @param node the node to make draggable
     * @throws IllegalStateException if the node has not yet been added to a {@link Pane}
     */
    public void makeDraggable(Node node) {
        Delta delta = new Delta();

        Pane parent = (Pane) node.getParent();
        if (parent == null) {
            throw new IllegalStateException("Node must be added to a Pane before making it draggable.");
        }

        // Record mouse offset on press
        node.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            delta.x = e.getSceneX() - node.getLayoutX();
            delta.y = e.getSceneY() - node.getLayoutY();
        });

        // Update position on drag, constrained to parent bounds
        node.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            clampAndRelocate(node, parent, e.getSceneX() - delta.x, e.getSceneY() - delta.y);
        });

        // Re-clamp position if the parent resizes
        parent.widthProperty().addListener((obs, oldVal, newVal) -> {
            clampAndRelocate(node, parent, node.getLayoutX(), node.getLayoutY());
        });

        parent.heightProperty().addListener((obs, oldVal, newVal) -> {
            clampAndRelocate(node, parent, node.getLayoutX(), node.getLayoutY());
        });
    }

    /**
     * Relocates the node to the specified (x,y) position, ensuring it stays
     * within the bounds of its parent {@link Pane}, respecting the {@link #margin}.
     *
     * @param node   the node being moved
     * @param parent the parent pane that constrains the movement
     * @param x      proposed X coordinate (before clamping)
     * @param y      proposed Y coordinate (before clamping)
     */
    private void clampAndRelocate(Node node, Pane parent, double x, double y) {
        // Get current node size based on bounds in parent
        double nodeWidth = node.getBoundsInParent().getWidth();
        double nodeHeight = node.getBoundsInParent().getHeight();

        // Calculate maximum allowed positions considering margin
        double maxX = parent.getWidth() - nodeWidth - margin;
        double maxY = parent.getHeight() - nodeHeight - margin;

        // Clamp X and Y within margin bounds
        double newX = Math.max(margin, Math.min(x, maxX));
        double newY = Math.max(margin, Math.min(y, maxY));

        // Relocate the node within the allowed area
        node.relocate(newX, newY);
    }
}
