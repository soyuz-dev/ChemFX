package chem.chemfx;

import javafx.scene.shape.Line;

public class Bond {
    public final AtomNode atom1;
    public final AtomNode atom2;
    public final Line line;


    public Bond(AtomNode atom1, AtomNode atom2, Line line) {
        this.atom1 = atom1;
        this.atom2 = atom2;
        this.line = line;
        this.atom1.getAtom().bond(this.atom2.getAtom(), 1);
    }

    public boolean connects(AtomNode atom) {
        return atom1 == atom || atom2 == atom;
    }
}
