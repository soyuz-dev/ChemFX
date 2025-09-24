package chem.chemfx;

import chem.chemfx.atoms.Atom;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Represents a chemical bond between two {@link AtomNode}s.
 * <p>
 * A {@code Bond} manages:
 * <ul>
 *     <li>References to the two participating atoms ({@link #atom1} and {@link #atom2}).</li>
 *     <li>The visual representation of the bond as one or more JavaFX {@link Line}s.</li>
 *     <li>The order of the bond (single, double, triple, etc.).</li>
 * </ul>
 * Each bond is registered in the global static {@link #bonds} list for tracking.
 */
public class Bond {

    /**
     * Global list of all bonds currently present.
     */
    public static final List<Bond> bonds = new ArrayList<>();
    /**
     * The first atom in the bond.
     */
    public final AtomNode atom1;
    /**
     * The second atom in the bond.
     */
    public final AtomNode atom2;
    /**
     * The graphical lines representing the bond.
     */
    public final List<Line> lines;
    /**
     * Bond order (1 = single, 2 = double, 3 = triple).
     */
    private int order;

    /**
     * Creates a new bond between two atoms with a single visual line.
     *
     * @param atom1 the first atom
     * @param atom2 the second atom
     * @param line  the JavaFX line representing the bond
     * @param order the bond order (e.g. 1 = single, 2 = double, etc.)
     */
    public Bond(AtomNode atom1, AtomNode atom2, Line line, int order) {
        this.atom1 = atom1;
        this.atom2 = atom2;
        this.lines = new ArrayList<>();
        this.lines.add(line);
        this.order = order;
        this.atom1.getAtom().bond(this.atom2.getAtom(), order);
        bonds.add(this);

        System.out.println("DEBUG: NEW BOND " + this + " with Order " + order + " instantiated.");
    }

    /**
     * Creates a new bond between two atoms with multiple visual lines.
     *
     * @param atom1 the first atom
     * @param atom2 the second atom
     * @param lines the JavaFX lines representing the bond
     * @param order the bond order
     */
    public Bond(AtomNode atom1, AtomNode atom2, List<Line> lines, int order) {
        this.atom1 = atom1;
        this.atom2 = atom2;
        this.lines = lines;
        this.order = order;
        this.atom1.getAtom().bond(this.atom2.getAtom(), order);
        bonds.add(this);

        System.out.println("DEBUG: NEW BOND " + this + " with Order " + order + " instantiated.");
    }

    /**
     * Creates a new single bond (order = 1) between two atoms.
     *
     * @param atom1 the first atom
     * @param atom2 the second atom
     * @param line  the JavaFX line representing the bond
     */
    public Bond(AtomNode atom1, AtomNode atom2, Line line) {
        this(atom1, atom2, line, 1);
    }

    /**
     * Checks whether a bond already exists between two atoms.
     *
     * @param atom1 the first atom
     * @param atom2 the second atom
     * @return {@code true} if such a bond exists, {@code false} otherwise
     */
    public static boolean existsFor(AtomNode atom1, AtomNode atom2) {
        for (Bond bond : bonds) {
            if (bond.connects(atom1) && bond.connects(atom2)) return true;
        }
        return false;
    }

    /**
     * Retrieves an existing bond between two atoms.
     *
     * @param atom1 the first atom
     * @param atom2 the second atom
     * @return the existing bond
     * @throws NoSuchElementException if no bond exists between the given atoms
     */
    public static Bond getBond(AtomNode atom1, AtomNode atom2) {
        for (Bond bond : bonds) {
            if (bond.connects(atom1) && bond.connects(atom2)) return bond;
        }
        throw new NoSuchElementException();
    }

    /**
     * Checks whether this bond connects to a given atom.
     *
     * @param atom the atom to test
     * @return {@code true} if the atom is part of this bond, {@code false} otherwise
     */
    public boolean connects(AtomNode atom) {
        return atom1 == atom || atom2 == atom;
    }

    /**
     * Increases the bond order and adds additional graphical lines.
     *
     * @param order the increment in bond order (e.g. +1 to go from single to double bond)
     * @param lines the additional lines to visually represent the bond
     */
    public void bond(int order, List<Line> lines) {
        this.atom1.getAtom().bond(this.atom2.getAtom(), order);
        this.order += order;
        this.lines.addAll(lines);
        System.out.println("DEBUG: BOND " + this + " increased order to " + this.order + ".");
    }

    /**
     * Increases the bond order by 1 and adds a single line.
     *
     * @param line the line to add to the visual representation
     */
    public void bond(Line line) {
        ArrayList<Line> lines = new ArrayList<>();
        lines.add(line);
        bond(1, lines);
    }

    /**
     * Disconnects the bond, removing references and visual representation.
     * <ul>
     *     <li>Calls {@link Atom#unbond(Atom)} on both atoms.</li>
     *     <li>Resets bond order to 0.</li>
     *     <li>Removes this bond from the global {@link #bonds} list.</li>
     *     <li>Clears all graphical lines.</li>
     * </ul>
     */
    public void disconnect() {
        this.atom1.getAtom().unbond(this.atom2.getAtom());
        this.atom1.updateSelectionStyle();
        this.atom2.updateSelectionStyle();
        this.order = 0;
        int numLines = lines.size();
        bonds.remove(this);
        for (int i = 0; i < numLines; i++) {
            lines.remove(i);
        }
        System.out.println("DEBUG: BOND " + this + " with Order " + order + " deleted.");
    }

    /**
     * Gets the current bond order.
     *
     * @return the bond order
     */
    public int getOrder() {
        return order;
    }
}
