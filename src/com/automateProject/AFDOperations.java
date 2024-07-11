package com.automateProject;
import java.util.*;


public class AFDOperations {
    
    // Fonction pour calculer l'union de deux AFD
    public static AFD union(AFD afd1, AFD afd2) {
        // Création d'un nouvel AFD avec l'union des alphabets
        Set<String> alphabetUnion = new HashSet<>(afd1.getAlphabet());
        alphabetUnion.addAll(afd2.getAlphabet());

        // Création d'un nouvel AFD avec l'union des états
        Set<String> statesUnion = new HashSet<>(afd1.getStates());
        statesUnion.addAll(afd2.getStates());

        // Création d'un nouvel AFD avec l'union des transitions
        Map<String, Map<String, String>> transitionsUnion = new HashMap<>(afd1.getTransitions());
        for (String state : afd2.getStates()) {
            Map<String, String> transitions = afd2.getTransitions().get(state);
            transitionsUnion.put(state, transitions);
        }

        // Création d'un nouvel AFD avec l'union des états initiaux et finaux
        String initialStateUnion = afd1.getInitialState() + "_" + afd2.getInitialState();
        Set<String> finalStatesUnion = new HashSet<>();
        finalStatesUnion.addAll(afd1.getFinalStates());
        finalStatesUnion.addAll(afd2.getFinalStates());

        return new AFD(statesUnion, alphabetUnion, initialStateUnion, finalStatesUnion, transitionsUnion);
    }

    // Fonction pour calculer l'intersection de deux AFD
    public static AFD intersection(AFD afd1, AFD afd2) {
        // Calcul de l'union des alphabets
        Set<String> alphabetIntersection = new HashSet<>(afd1.getAlphabet());
        alphabetIntersection.retainAll(afd2.getAlphabet());

        // Calcul de l'union des états
        Set<String> statesIntersection = new HashSet<>(afd1.getStates());
        statesIntersection.retainAll(afd2.getStates());

        // Calcul de l'union des transitions
        Map<String, Map<String, String>> transitionsIntersection = new HashMap<>();
        for (String state : statesIntersection) {
            Map<String, String> transitions1 = afd1.getTransitions().get(state);
            Map<String, String> transitions2 = afd2.getTransitions().get(state);
            Map<String, String> transitions = new HashMap<>();
            for (String symbol : alphabetIntersection) {
                String nextState1 = transitions1.get(symbol);
                String nextState2 = transitions2.get(symbol);
                if (nextState1 != null && nextState2 != null) {
                    String nextStateIntersection = nextState1 + "_" + nextState2;
                    transitions.put(symbol, nextStateIntersection);
                }
            }
            transitionsIntersection.put(state, transitions);
        }

        // Calcul de l'union des états initiaux et finaux
        String initialStateIntersection = afd1.getInitialState() + "_" + afd2.getInitialState();
        Set<String> finalStatesIntersection = new HashSet<>(afd1.getFinalStates());
        finalStatesIntersection.retainAll(afd2.getFinalStates());

        return new AFD(statesIntersection, alphabetIntersection, initialStateIntersection, finalStatesIntersection, transitionsIntersection);
    }

    // Fonction pour calculer la différence de deux AFD
    public static AFD difference(AFD afd1, AFD afd2) {
        // Calcul de l'union des alphabets
        Set<String> alphabetDifference = new HashSet<>(afd1.getAlphabet());
        alphabetDifference.addAll(afd2.getAlphabet());

        // Calcul de l'union des états
        Set<String> statesDifference = new HashSet<>(afd1.getStates());
        statesDifference.addAll(afd2.getStates());

        // Calcul de l'union des transitions
        Map<String, Map<String, String>> transitionsDifference = new HashMap<>();
        for (String state : statesDifference) {
            Map<String, String> transitions1 = afd1.getTransitions().get(state);
            Map<String, String> transitions2 = afd2.getTransitions().get(state);
            Map<String, String> transitions = new HashMap<>();
            for (String symbol : alphabetDifference) {
                String nextState1 = transitions1 != null ? transitions1.get(symbol) : null;
                String nextState2 = transitions2 != null ? transitions2.get(symbol) : null;
                if (nextState1 != null && (nextState2 == null || !nextState2.equals(state))) {
                    transitions.put(symbol, nextState1);
                }
            }
            transitionsDifference.put(state, transitions);
        }

        // Calcul de l'union des états initiaux et finaux
        String initialStateDifference = afd1.getInitialState();
        Set<String> finalStatesDifference = new HashSet<>(afd1.getFinalStates());
        finalStatesDifference.removeAll(afd2.getFinalStates());

        return new AFD(statesDifference, alphabetDifference, initialStateDifference, finalStatesDifference, transitionsDifference);
    }

    // Fonction pour calculer le complémentaire d'un AFD
    public static AFD complement(AFD afd) {
        // Calcul de l'ensemble des états non finaux
        Set<String> nonFinalStates = new HashSet<>(afd.getStates());
        nonFinalStates.removeAll(afd.getFinalStates());

        // Création d'un nouvel AFD avec les mêmes composants que l'AFD original
        // mais avec les états finaux et non finaux inversés
        return new AFD(afd.getStates(), afd.getAlphabet(), afd.getInitialState(), nonFinalStates, afd.getTransitions());
    }

	
}

