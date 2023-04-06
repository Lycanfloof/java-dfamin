package model;

import java.util.Collection;
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

    @Override
    public void setOutFunction(String state, Character input, Character output) {
        if (states.contains(state) && outAlphabet.contains(output)) {
            outFunctions.put(state, output);
        }
    }

    @Override
    public void minimizeFDA() {
        // TODO.
    }
}
