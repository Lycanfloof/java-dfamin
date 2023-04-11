package model;

import java.util.Collection;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
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
        if (!states.contains(state)) {
            states.add(state);
            transitionMatrix.put(state, transitionMap);}
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
            if (state.equals(initialState)) {
                initialState = null;
            }
        }
    }

    public void deleteStateCollection(Collection<String> states) {
        states.forEach(state -> {
            deleteState(state);
        });
    }

    public void setInitialState(String initialState) {
        if (states.contains(initialState)) {
            this.initialState = initialState;
        }
    }

    public void addToInAlphabet(char input) {
        if (!inAlphabet.contains(input)) {
            inAlphabet.add(input);
        }
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
        if (!outAlphabet.contains(output)) {
            outAlphabet.add(output);
        }
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

    public void minimizeFDA() {
        if (!states.isEmpty() && initialState != null) {
            deleteStateCollection(getUnreachableStates());
            List<List<String>> initialPartition = getZeroEquivalentPartitions();
            List<List<String>> partition = getEquivalentPartitions(initialPartition);
            partition.forEach(group -> {
                group.remove(0);
                deleteStateCollection(group);
            });
        }
    }

    protected Collection<String> getUnreachableStates() {
        Collection<String> reachableStates = getReachableStates();
        Collection<String> unreachableStates = new LinkedList<>();

        for (String state : states) {
            if (!reachableStates.contains(state)) {
                unreachableStates.add(state);
            }
        }

        return unreachableStates;
    }

    protected Collection<String> getReachableStates() {
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

    protected abstract List<List<String>> getZeroEquivalentPartitions();

    protected List<List<String>> getEquivalentPartitions(List<List<String>> partition) {
        Map<String, Map<Character, String>> matrix;
        List<List<String>> oldPartition;
        List<List<String>> newPartition = partition;
        do {
            oldPartition = newPartition;
            matrix = getTransitionMatrixWithPartitions(oldPartition);
            newPartition = getKEquivalentPartition(oldPartition, matrix);
        } while (!oldPartition.equals(newPartition));
        transitionMatrix = matrix;
        return newPartition;
    }

    protected Map<String, Map<Character, String>> getTransitionMatrixWithPartitions(List<List<String>> partition) {
        Map<String, Map<Character, String>> matrixCopy = new Hashtable<>();
        for (String state : states) {
            Map<Character, String> tFunctions = transitionMatrix.get(state);
            Map<Character, String> newCopy = new Hashtable<>();

            for (Character input : inAlphabet) {
                if (tFunctions.keySet().contains(input)) {
                    String group = AuxMethods.getGroupInMatrix(tFunctions.get(input), partition);
                    newCopy.put(input, group);
                }
            }

            matrixCopy.put(state, newCopy);
        }
        return matrixCopy;
    }

    protected List<List<String>> getKEquivalentPartition(List<List<String>> partition, Map<String, Map<Character, String>> matrix) {
        List<List<String>> groups = new LinkedList<>();

        for (String i : states) {
            if (groups.size() == 0 || !AuxMethods.isContainedInMatrix(i, groups)) {
                Map<Character, String> transitionMapI = matrix.get(i);

                List<String> newGroup = new LinkedList<>();
                newGroup.add(i);

                for (String j : states) {
                    Map<Character, String> transitionMapJ = matrix.get(j);

                    String iGroup = AuxMethods.getGroupInMatrix(i, partition);
                    String jGroup = AuxMethods.getGroupInMatrix(j, partition);

                    boolean inSamePartition = iGroup.equals(jGroup);

                    if (i != j && transitionMapI.equals(transitionMapJ) && inSamePartition) {
                        newGroup.add(j);
                    }
                }
                
                groups.add(newGroup);
            }
        }

        return groups;
    }
}
