package com.automateProject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class AFD extends AF {

    public AFD(Set<String> etats, Set<String> alphabet, String etatInit, Set<String> etatfinal,
               Map<String, Map<String, String>> transitions) {
        super(etats, alphabet, etatInit, etatfinal, transitions);
    }

   

	@Override
    public boolean accept(String input) {
        // TODO : Implémenter la fonction d'acceptation
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Q = {").append(String.join(", ", etats)).append("}\n");
        sb.append("Σ = {").append(String.join(", ", alphabet)).append("}\n");
        sb.append("δ : Q x Σ -> Q\n");
        for (String state : transitions.keySet()) {
            for (String symbol : transitions.get(state).keySet()) {
                sb.append("  δ(").append(state).append(", ").append(symbol).append(") = {")
                        .append(String.join(", ", transitions.get(state).get(symbol))).append("}\n");
            }
        }
        sb.append("q0 = ").append(etatInit).append("\n");
        sb.append("F = {").append(String.join(", ", etatfinal)).append("}\n");
        return sb.toString();
    }
    public void calculer(String mot) {
        String currentState = etatInit;
        System.out.println("Étapes de calcul pour le mot " + mot + ":");

        // Parcours des symboles du mot x
        for (int i = 0; i < mot.length(); i++) {
            String symbol = String.valueOf(mot.charAt(i));
            System.out.println("État actuel : " + currentState + ", Symbole lu : " + symbol);
            
            // Vérification de la transition pour le symbole actuel
            if (transitions.containsKey(currentState) && transitions.get(currentState).containsKey(symbol)) {
                currentState = transitions.get(currentState).get(symbol);
                System.out.println("Transition : δ(" + currentState + ", " + symbol + ") = " + currentState);
            } else {
                // Si aucune transition n'est disponible, le mot est rejeté
                System.out.println("Aucune transition disponible pour le symbole " + symbol);
                System.out.println("Le mot est rejeté.");
                return;
            }
        }

        // Vérification de l'état final
        if (etatfinal.contains(currentState)) {
            System.out.println("Le mot est accepté.");
        } else {
            System.out.println("Le mot est rejeté.");
        }
    }
    
    
   
    
    
    //Méthode pour tester si le langage est vide 
    public boolean isLanguageEmpty() {
        // Si l'ensemble des états finaux est vide, alors le langage est vide
        return etatfinal.isEmpty();
    }
    
    
    //Méthode pour tester si le langage est fini
    public boolean isLanguageFinite() {
        // Si tous les états finaux sont accessibles depuis l'état initial, alors le langage est fini
        Set<String> reachableFinalStates = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        queue.add(etatInit);
        while (!queue.isEmpty()) {
            String currentState = queue.poll();
            if (etatfinal.contains(currentState)) {
                reachableFinalStates.add(currentState);
            }
            Map<String, String> transitions = this.transitions.get(currentState);
            if (transitions != null) {
                for (String nextState : transitions.values()) {
                    if (!reachableFinalStates.contains(nextState)) {
                        queue.add(nextState);
                    }
                }
            }
        }
        return reachableFinalStates.equals(etatfinal);
    }

    //Méthode pour tester si le langage est infini
    public boolean isLanguageInfinite() {
        // Si le langage n'est ni vide ni fini, alors il est infini
        return !isLanguageEmpty() && !isLanguageFinite();
    }
   

    /*************************************Expression Regulier********************************************/
    public String calculateRegularExpression() {
        // Création de l'AFD inverse
        AFD inverseAFD = inverseAFD();

        // Création des équations pour chaque paire d'états (i, j)
        Map<String, Map<String, String>> equations = new HashMap<>();
        for (String state : etats) {
            Map<String, String> stateEquations = new HashMap<>();
            for (String nextState : etats) {
                if (!state.equals(nextState)) {
                    // Si i = j, l'équation est ε
                    if (state.equals(nextState)) {
                        stateEquations.put(nextState, "ε");
                    } else {
                        // Sinon, on cherche l'équation correspondant à (i, j) en utilisant l'algorithme BMC
                        String equation = BMCAlgorithm(state, nextState, inverseAFD);
                        stateEquations.put(nextState, equation);
                    }
                }
            }
            equations.put(state, stateEquations);
        }

        // Construction de l'expression régulière
        StringBuilder regexBuilder = new StringBuilder();
        for (String state : etats) {
            for (String nextState : etats) {
                if (!state.equals(nextState)) {
                    // Ajout de l'équation entre les états à l'expression régulière
                    String equation = equations.get(state).get(nextState);
                    regexBuilder.append("(").append(state).append(equation).append(nextState).append(")").append(" | ");
                }
            }
        }

        // Supprimer le dernier " | " de la chaîne de caractères
        if (regexBuilder.length() > 3) {
            regexBuilder.setLength(regexBuilder.length() - 3);
        }

        return regexBuilder.toString();
    }

    private String BMCAlgorithm(String state, String nextState, AFD inverseAFD) {
        Set<String> visited = new HashSet<>(); // Ensemble des états visités
        Map<String, String> equationMap = new HashMap<>(); // Map pour stocker les équations pour chaque état visité

        // Appel à une fonction récursive pour calculer l'équation entre les états
        String equation = computeEquation(state, nextState, inverseAFD, visited, equationMap);

        // Retour de l'équation calculée
        return equation;
    }

    // Fonction récursive pour calculer l'équation entre deux états
    private String computeEquation(String currentState, String targetState, AFD inverseAFD, Set<String> visited, Map<String, String> equationMap) {
        // Vérification si l'état courant a déjà été visité
        if (visited.contains(currentState)) {
            return equationMap.get(currentState);
        }

        // Marquage de l'état courant comme visité
        visited.add(currentState);

        // Récupération des transitions sortantes de l'état courant dans l'AFD inverse
        Map<String, String> transitions = inverseAFD.transitions.get(currentState);

        // Vérification si l'état courant est l'état cible
        if (currentState.equals(targetState)) {
            return "ε"; // Si oui, l'équation est ε
        }

        // Initialisation de l'équation avec un caractère vide
        StringBuilder equationBuilder = new StringBuilder();

        // Parcours des transitions sortantes de l'état courant
        for (String symbol : inverseAFD.alphabet) {
            String nextState = transitions.get(symbol);

            // Vérification si la transition existe et que l'état suivant n'est pas l'état cible
            if (nextState != null && !nextState.equals(targetState)) {
                // Appel récursif pour calculer l'équation entre l'état suivant et l'état cible
                String nextEquation = computeEquation(nextState, targetState, inverseAFD, visited, equationMap);

                // Construction de l'équation en ajoutant le symbole suivi de l'équation pour l'état suivant
                equationBuilder.append(symbol).append(nextEquation).append(" | ");
            }
        }

        // Suppression du dernier " | " de l'équation
        if (equationBuilder.length() > 0) {
            equationBuilder.setLength(equationBuilder.length() - 3);
        }

        // Stockage de l'équation calculée pour l'état courant
        equationMap.put(currentState, equationBuilder.toString());

        // Retour de l'équation
        return equationBuilder.toString();
    }

    private AFD inverseAFD() {
        // Construction de l'AFD inverse
        Map<String, Map<String, String>> inverseTransitions = new HashMap<>();
        for (String state : etats) {
            inverseTransitions.put(state, new HashMap<>());
        }

        // Inversion des transitions de l'AFD
        for (String state : etats) {
            Map<String, String> transitionsFromState = transitions.get(state);
            if (transitionsFromState != null) { // Vérification nulle ajoutée ici
                for (String symbol : transitionsFromState.keySet()) {
                    String nextState = transitionsFromState.get(symbol);
                    inverseTransitions.computeIfAbsent(nextState, k -> new HashMap<>()).put(symbol, state);
                }
            }
        }

        // Création de l'AFD inverse
        return new AFD(etats, alphabet, etatInit, etatfinal, inverseTransitions);
    }
    //*************************************************************************************
    
    public Set<String> getStates() {
        return etats;
    }

    public String getInitialState() {
        return etatInit;
    }

    public Set<String> getFinalStates() {
        return etatfinal;
    }
//***********************************************Produit**************************
    public static AFD product(AFD afd1, AFD afd2) {
        // Obtention des états, de l'alphabet, de l'état initial et des états finaux de chaque AFD
        Set<String> states1 = afd1.getStates();
        Set<String> states2 = afd2.getStates();
        Set<String> alphabet1 = afd1.getAlphabet();
        Set<String> alphabet2 = afd2.getAlphabet();
        String initialState1 = afd1.getInitialState();
        String initialState2 = afd2.getInitialState();
        Set<String> finalStates1 = afd1.getFinalStates();
        Set<String> finalStates2 = afd2.getFinalStates();

        // Création d'un nouvel ensemble d'états pour le produit
        Set<String> productStates = new HashSet<>();
        // Ajout de toutes les combinaisons possibles d'états des deux automates dans le nouvel ensemble
        for (String state1 : states1) {
            for (String state2 : states2) {
                productStates.add("(" + state1 + "," + state2 + ")");
            }
        }

        // Calcul de l'alphabet du produit (union des alphabets des deux automates)
        Set<String> productAlphabet = new HashSet<>(alphabet1);
        productAlphabet.addAll(alphabet2);

        // Création d'une nouvelle map pour les transitions du produit
        Map<String, Map<String, String>> productTransitions = new HashMap<>();

        // Calcul de l'état initial du produit
        String productInitialState = "(" + initialState1 + "," + initialState2 + ")";

        // Calcul des états finaux du produit
        Set<String> productFinalStates = new HashSet<>();
        // Ajout des états finaux du produit qui sont des combinaisons d'états finaux des deux automates
        for (String finalState1 : finalStates1) {
            for (String finalState2 : finalStates2) {
                productFinalStates.add("(" + finalState1 + "," + finalState2 + ")");
            }
        }

        // Parcours de tous les états du produit pour calculer les transitions
        for (String productState : productStates) {
            Map<String, String> stateTransitions = new HashMap<>();
            // Obtention des états individuels des deux automates
            String[] individualStates = productState.substring(1, productState.length() - 1).split(",");
            String state1 = individualStates[0];
            String state2 = individualStates[1];
            // Parcours de chaque symbole de l'alphabet du produit
            for (String symbol : productAlphabet) {
                // Obtention des transitions individuelles pour chaque automate
                String nextState1 = afd1.transitions.getOrDefault(state1, new HashMap<>()).get(symbol);
                String nextState2 = afd2.transitions.getOrDefault(state2, new HashMap<>()).get(symbol);
                // Si les deux automates ont une transition valide pour le symbole donné
                if (nextState1 != null && nextState2 != null) {
                    // Calcul de l'état suivant dans le produit et ajout à la map des transitions
                    String nextProductState = "(" + nextState1 + "," + nextState2 + ")";
                    stateTransitions.put(symbol, nextProductState);
                }
            }
            // Ajout des transitions calculées pour l'état actuel dans la map globale des transitions
            productTransitions.put(productState, stateTransitions);
        }

        // Création et retour de l'automate produit
        return new AFD(productStates, productAlphabet, productInitialState, productFinalStates, productTransitions);
    }

}