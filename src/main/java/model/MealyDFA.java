package model;

import java.util.Collection;
import java.util.Map;

public class MealyDFA extends DFA {
    Map<String, Map<Character, Character>> outputMatrix;

    public MealyDFA(Collection<String> states, String initialState, Collection<Character> inAlphabet,
            Collection<Character> outAlphabet, Map<String, Map<Character, String>> transFunctions,
            Map<String, Map<Character, Character>> outputMatrix) {
        super(states, initialState, inAlphabet, outAlphabet, transFunctions);
        this.outputMatrix = outputMatrix;
    }

    @Override
    public void addState(String state, Map<Character, String> transitionMap, Map<Character, Character> outputMap) {
        super.addState(state, transitionMap, outputMap);
        outputMatrix.put(state, outputMap);
    }

    @Override
    public void removeFromInAlphabet(char input) {
        super.removeFromInAlphabet(input);
        if (inAlphabet.contains(input)) {
            outputMatrix.values().forEach(outputMap -> {
                outputMap.remove(input);
            });
        }
    }

    @Override
    public void removeFromOutAlphabet(char output) {
        if (outAlphabet.contains(output)) {
            outAlphabet.remove(output);
            outputMatrix.values().forEach(outputMap -> {
                while (outputMap.values().contains(output)) {
                    outputMap.values().remove(output);
                }
            });
        }
    }

    @Override
    public void setOutFunction(String state, Character input, Character output) {
        if (states.contains(state) && inAlphabet.contains(input) && outAlphabet.contains(output)) {
            outputMatrix.get(state).put(input, output);
        }
    }

    @Override
    public void minimizeFDA() {
        // TODO.
    }
}
