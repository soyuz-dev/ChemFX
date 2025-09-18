package chem.chemfx.atoms;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MoleculeBuilder {
    private static final Map<String, Integer> alkanePrefixes = Map.ofEntries(
            Map.entry("meth", 1),
            Map.entry("eth", 2),
            Map.entry("prop", 3),
            Map.entry("but", 4),
            Map.entry("pent", 5),
            Map.entry("hex", 6),
            Map.entry("hept", 7),
            Map.entry("oct", 8),
            Map.entry("non", 9),
            Map.entry("dec", 10)
    );





    public static List<BohrAtom> buildIUPACChain(String name){
        List<BohrAtom> atoms = new ArrayList<>();
        int carbonCount = -1;



        return atoms;
    }
}
