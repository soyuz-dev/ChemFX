package chem.chemfx;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public class DraggableMaker {

    private static final class Delta { double x, y; }

    public void makeDraggable(Node node) {
        Delta delta = new Delta();

        node.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            // Record offset between mouse (scene) and node’s layout position
            delta.x = e.getSceneX() - node.getLayoutX();
            delta.y = e.getSceneY() - node.getLayoutY();
        });

        node.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            // Move the whole StackPane; works fine in AnchorPane
            node.relocate(e.getSceneX() - delta.x, e.getSceneY() - delta.y);
        });
    }
}
