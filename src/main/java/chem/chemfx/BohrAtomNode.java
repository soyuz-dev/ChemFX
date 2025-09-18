package chem.chemfx;

import chem.chemfx.atoms.Atom;
import chem.chemfx.atoms.BohrAtom;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class BohrAtomNode extends AtomNode {

    public BohrAtomNode(double x, double y, javafx.scene.layout.Pane container, BondManager bondManager) {
        this(x, y, container, bondManager, 6);
    }

    public BohrAtomNode(double x, double y, javafx.scene.layout.Pane container, BondManager bondManager, int atomicNumber) {
        super(x, y, container, bondManager, new BohrAtom(atomicNumber));
    }

    @Override
    protected void styleText(Text text) {
        switch(getAtom().getAtomicNumber()){
            case 1,2 -> text.setStroke(Color.rgb(80, 80, 80));
            case 3,11,19,37,55,87 -> text.setStroke(Color.rgb(140, 75, 255));
            case 4,12,20,38,56,88 -> text.setStroke(Color.rgb(0, 120, 20));
            case 5,13,31,49,81 -> text.setStroke(Color.rgb(112, 79, 0));
            case 14,32,50,82 -> text.setStroke(Color.rgb(30,30,70));
            case 7 -> text.setStroke(Color.BLUE);
            case 8 -> text.setStroke(Color.RED);
            case 9,17,35,53,85 -> text.setStroke(Color.rgb(110,150,0));
            case 10, 18, 36, 54, 86 -> text.setStroke(Color.rgb(0,150,150));
            default -> text.setStroke(Color.BLACK);
        }
    }

    @Override
    protected void styleSelected() {
        getCircle().setFill(Color.rgb(100, 255, 150));
        getCircle().setStroke(Color.rgb(60, 200, 80));
        if (getAtom().getAtomicNumber() == 6 && !getAtom().getBondedTo().isEmpty()) {
            getText().setStroke(null);
            getText().setFill(null);
            getCircle().setFill(Color.rgb(100, 255, 150, 0.5));
            getCircle().setStroke(null);
        }
        getCircle().setStrokeWidth(2);
        System.out.println("SELECTED:" + this);
    }

    @Override
    protected void styleUnselected() {
        getCircle().setFill(Color.rgb(240, 240, 240, 1));
        styleText(getText());
        if (getAtom().getAtomicNumber() == 6 && !getAtom().getBondedTo().isEmpty()) {
            getText().setStroke(null);
            getText().setFill(null);
            getCircle().setFill(Color.rgb(0,0,0,0));
        }
        getCircle().setStroke(null);
        getCircle().setStrokeWidth(0);
        System.out.println("DESELECTED"+ this);
    }
}
