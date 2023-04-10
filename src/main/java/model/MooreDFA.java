package model;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MooreDFA extends DFA {
    Map<String, Character> outFunctions;

    public MooreDFA(Collection<String> states, String initialState, Collection<Character> inAlphabet,
            Collection<Character> outAlphabet, Map<String, Map<Character, String>> transFunctions,
            Map<String, Character> outFunctions) {
        super(states, initialState, inAlphabet, outAlphabet, transFunctions);
        this.outFunctions = outFunctions;
    }

    @Override
    public void removeFromOutAlphabet(char output) {
        if (outAlphabet.contains(output)) {
            outAlphabet.remove(output);
            while (outFunctions.values().contains(output)) {
                outFunctions.values().remove(output);
            }
        }
    }

    public Character getOutFunction(String state) {
        return outFunctions.get(state);
    }

    @Override
    public void setOutFunction(String state, Character input, Character output) {
        if (states.contains(state) && outAlphabet.contains(output)) {
            outFunctions.put(state, output);
        }
    }
    
    @Override
    protected List<List<String>> getZeroEquivalentPartitions() {
        List<List<String>> groups = new LinkedList<>();

        for (String i : states) {
            if (groups.size() == 0 || !AuxMethods.isContainedInMatrix(i, groups)) {
                Character outputI = outFunctions.get(i);

                List<String> newGroup = new LinkedList<>();
                newGroup.add(i);

                for (String j : states) {
                    Character outputJ = outFunctions.get(j);

                    if (i != j && outputI.equals(outputJ)) {
                        newGroup.add(j);
                    }
                }
                
                groups.add(newGroup);
            }
        }

        return groups;
    }
}
