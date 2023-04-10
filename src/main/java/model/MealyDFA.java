package model;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
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
    public void deleteState(String state) {
        if (states.contains(state)) {
            outputMatrix.remove(state);
        }
        super.deleteState(state);
    }
    
    @Override
    public void removeFromInAlphabet(char input) {
        if (inAlphabet.contains(input)) {
            outputMatrix.values().forEach(outputMap -> {
                outputMap.remove(input);
            });
        }
        super.removeFromInAlphabet(input);
    }

    @Override
    public void removeFromOutAlphabet(char output) {
        if (outAlphabet.contains(output)) {
            outAlphabet.remove(output);
            for (String state : states) {
                for (Character character : outputMatrix.get(state).keySet()) {
                    if (outputMatrix.get(state).get(character) == output) {
                        transitionMatrix.get(state).remove(character);
                        outputMatrix.get(state).remove(character);
                    }
                }
            }
        }
    }

    public Character getOutFunction(String state, Character input) {
        return outputMatrix.get(state).get(input);
    }

    @Override
    public void setOutFunction(String state, Character input, Character output) {
        if (states.contains(state) && inAlphabet.contains(input) && outAlphabet.contains(output)) {
            outputMatrix.get(state).put(input, output);
        }
    }

    @Override
    protected List<List<String>> getZeroEquivalentPartitions() {
        List<List<String>> groups = new LinkedList<>();

        for (String i : states) {
            if (groups.size() == 0 || !AuxMethods.isContainedInMatrix(i, groups)) {
                Map<Character, Character> outputMapI = outputMatrix.get(i);

                List<String> newGroup = new LinkedList<>();
                newGroup.add(i);

                for (String j : states) {
                    Map<Character, Character> outputMapJ = outputMatrix.get(j);

                    if (i != j && outputMapI.equals(outputMapJ)) {
                        newGroup.add(j);
                    }
                }
                
                groups.add(newGroup);
            }
        }

        return groups;
    }

    public Map<String, Map<Character, Character>> getOutputMatrix() {
        return outputMatrix;
    }
}
