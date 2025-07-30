package chem.chemfx;

import javafx.scene.Node;

public class DraggableMaker {

    private double offsetX;
    private double offsetY;

    public void makeDraggable(Node node) {
        node.setOnMousePressed(mouseEvent -> {
            offsetX = mouseEvent.getSceneX() - node.getLayoutX();
            offsetY = mouseEvent.getSceneY() - node.getLayoutY();
        });

        node.setOnMouseDragged(mouseEvent -> {
            node.setLayoutX(mouseEvent.getSceneX() - offsetX);
            node.setLayoutY(mouseEvent.getSceneY() - offsetY);
        });
    }
}
