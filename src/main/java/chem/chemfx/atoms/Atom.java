package chem.chemfx.atoms;



import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

public interface Atom {
    static String deepToString(int[][] orbitals) {
        StringBuilder result = new StringBuilder();
        for (int[] orbital : orbitals) {
            result.append(Arrays.toString(orbital)).append("\n");
        }
        return result.toString();
    }

    static void addToJSON(String fileName, Atom atom){
        String json = "{";

        json += "}";
    }

    void fill(int electronNumber);
    void ionise(int electronLoss);

    void bond(Atom other, int bondOrder) throws CovalentBondException;

    void bond(Atom other, int bondOrder, boolean needToRecur) throws CovalentBondException;

    int[] getValenceShell();
    int[] getMaxValenceShell();
    void addElectronsTo(int[] shell, int[] maxCapacity, int electronsToAdd);
    int getMaxCapacityValence();

    int getNeutronNumber();
    int getAtomicNumber();

    ArrayList<Pair<Atom, Integer>> getBondedTo();

    Atom alphaDecay();
    void betaDecay(boolean isPositive);

    String toString();
}
