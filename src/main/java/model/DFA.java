package model;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

public abstract class DFA {
    Collection<String> states;
    String initialState;
    Collection<Character> inAlphabet;
    Collection<Character> outAlphabet;
    Map<String, Map<Character, String>> transitionMatrix;

    public DFA(Collection<String> states, String initialState, Collection<Character> inAlphabet,
            Collection<Character> outAlphabet, Map<String, Map<Character, String>> transitionMatrix) {
        this.states = states;
        this.initialState = initialState;
        this.inAlphabet = inAlphabet;
        this.outAlphabet = outAlphabet;
        this.transitionMatrix = transitionMatrix;
    }

    public Collection<String> getStates() {
        return states;
    }

    public String getInitialState() {
        return initialState;
    }

    public Collection<Character> getInAlphabet() {
        return inAlphabet;
    }

    public Collection<Character> getOutAlphabet() {
        return outAlphabet;
    }

    public Map<String, Map<Character, String>> getTransitionMatrix() {
        return transitionMatrix;
    }

    // Needs to be overwritten in the implementations. Parameter "outputMap" might not be used.
    public void addState(String state, Map<Character, String> transitionMap, Map<Character, Character> outputMap) {
        states.add(state);
        transitionMatrix.put(state, transitionMap);
    }

    public void deleteState(String state) {
        if (states.contains(state)) {
            states.remove(state);
            transitionMatrix.remove(state);
            transitionMatrix.values().forEach(transitionMap -> {
                while (transitionMap.values().contains(state)) {
                    transitionMap.values().remove(state);
                }
            });
        }
    }

    public void setInitialState(String initialState) {
        if (states.contains(initialState)) {
            this.initialState = initialState;
        }
    }

    public void addToInAlphabet(char input) {
        inAlphabet.add(input);
    }

    // Needs to be overwritten in the MealyDFA implementation.
    public void removeFromInAlphabet(char input) {
        if (inAlphabet.contains(input)) {
            inAlphabet.remove(input);
            transitionMatrix.values().forEach(transitionMap -> {
                transitionMap.remove(input);
            });
        }
    }

    public void addToOutAlphabet(char output) {
        outAlphabet.add(output);
    }

    // Needs to be overwritten in the implementations.
    public abstract void removeFromOutAlphabet(char output);

    public void setTransFunction(String inState, Character input, String outState) {
        if (states.contains(inState) && inAlphabet.contains(input) && states.contains(outState)) {
            transitionMatrix.get(inState).put(input, outState);
        }
    }

    // Needs to be overwritten in the implementations. Parameter "input" might not be used.
    public abstract void setOutFunction(String state, Character input, Character output);

    public abstract void minimizeFDA();

    private Collection<String> getUnreachableStates() {
        Collection<String> reachableStates = getReachableStates();
        Collection<String> unreachableStates = new LinkedList<>();

        for (String state : states) {
            if (!reachableStates.contains(state)) {
                unreachableStates.add(state);
            }
        }

        return unreachableStates;
    }

    private Collection<String> getReachableStates() {
        LinkedList<String> reachableStates = new LinkedList<>();
        LinkedList<String> queue = new LinkedList<>();

        reachableStates.add(initialState);
        queue.addFirst(initialState);

        while (!queue.isEmpty()) {
            String state = queue.removeLast();
            for (String neighborState : transitionMatrix.get(state).values()) {
                if (!reachableStates.contains(neighborState)) {
                    reachableStates.add(neighborState);
                    queue.addFirst(neighborState);
                }
            }
        }

        return reachableStates;
    }

    private void getEquivalentStates(Collection<Collection<String>> partition) {
        //TODO.
    }
}
