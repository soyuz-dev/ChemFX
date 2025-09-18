package chem.chemfx;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class DraggableMaker {

    private static final double margin = 10;

    private static final class Delta { double x, y; }

    public void makeDraggable(Node node) {
        Delta delta = new Delta();

        Pane parent = (Pane) node.getParent();
        if (parent == null) {
            throw new IllegalStateException("Node must be added to a Pane before making it draggable.");
        }

        // Drag handlers
        node.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            delta.x = e.getSceneX() - node.getLayoutX();
            delta.y = e.getSceneY() - node.getLayoutY();
        });

        node.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            clampAndRelocate(node, parent, e.getSceneX() - delta.x, e.getSceneY() - delta.y);
        });

        // Parent resize listeners
        parent.widthProperty().addListener((obs, oldVal, newVal) -> {
            clampAndRelocate(node, parent, node.getLayoutX(), node.getLayoutY());
        });

        parent.heightProperty().addListener((obs, oldVal, newVal) -> {
            clampAndRelocate(node, parent, node.getLayoutX(), node.getLayoutY());
        });
    }

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
