package chem.chemfx;

import javafx.scene.shape.Line;

import java.util.*;

public class Bond {
    public final AtomNode atom1;
    public final AtomNode atom2;
    public final List<Line> lines;
    private int order;
    public static final List<Bond> bonds = new ArrayList<>();


    public Bond(AtomNode atom1, AtomNode atom2, Line line, int order){
        this.atom1 = atom1;
        this.atom2 = atom2;
        this.lines = new ArrayList<>();
        this.lines.add(line);
        this.atom1.getAtom().bond(this.atom2.getAtom(), order);
        bonds.add(this);
    }


    public Bond(AtomNode atom1, AtomNode atom2, List<Line> lines, int order){
        this.atom1 = atom1;
        this.atom2 = atom2;
        this.lines = lines;
        this.atom1.getAtom().bond(this.atom2.getAtom(), order);
        bonds.add(this);
    }

    public Bond(AtomNode atom1, AtomNode atom2, Line line) {
        this(atom1, atom2, line, 1);
    }

    public boolean connects(AtomNode atom) {
        return atom1 == atom || atom2 == atom;
    }

    public void bond(int order){
        this.atom1.getAtom().bond(this.atom2.getAtom(), order);
        this.order += order;
    }

    public void disconnect(){
        this.atom1.getAtom().unbond(this.atom2.getAtom());
        this.atom1.updateSelectionStyle();
        this.atom2.updateSelectionStyle();
        this.order = 0;
        int numLines = lines.size();
        bonds.remove(this);
        for (int i = 0; i<numLines;i++){
            lines.remove(i);
        }
    }

    public static boolean existsFor(AtomNode atom1, AtomNode atom2){
        for (Bond bond: bonds){
            if(bond.connects(atom1) && bond.connects(atom2)) return true;
        }
        return false;
    }

    public static Bond getBond(AtomNode atom1, AtomNode atom2){
        for (Bond bond: bonds){
            if(bond.connects(atom1) && bond.connects(atom2)) return bond;
        }
        throw new NoSuchElementException();
    }

}
