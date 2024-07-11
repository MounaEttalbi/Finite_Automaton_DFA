package com.automateProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class InterfaceUser extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTextField statesField, alphabetField, initialStateField, finalStatesField;
    private JTextArea transitionsArea;
    private JButton createButton, displayButton, calculateButton, langageButton, regexButton, productButton; 
    private JButton unionButton, differenceButton, intersectionButton, complementButton; // Bouton pour le complément

    private AFD afd1, afd2; // Instances des 2 AFD

    public InterfaceUser() {
        setTitle("AFD Interface");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(13, 2)); // Ajuster la grille pour les nouveaux boutons
        
        // Changer la couleur de fond du panneau principal
        mainPanel.setBackground(new Color(230, 192, 233)); 

        mainPanel.add(new JLabel("States:"));
        statesField = new JTextField();
        mainPanel.add(statesField);

        mainPanel.add(new JLabel("Alphabet:"));
        alphabetField = new JTextField();
        mainPanel.add(alphabetField);

        mainPanel.add(new JLabel("Initial State:"));
        initialStateField = new JTextField();
        mainPanel.add(initialStateField);

        mainPanel.add(new JLabel("Final States:"));
        finalStatesField = new JTextField();
        mainPanel.add(finalStatesField);

        mainPanel.add(new JLabel("Transitions:"));
        transitionsArea = new JTextArea();
        mainPanel.add(new JScrollPane(transitionsArea));

        createButton = new JButton("Create AFD");
        mainPanel.add(createButton);
        createButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createAFD();
            }
        });

        displayButton = new JButton("Display Info");
        mainPanel.add(displayButton);
        displayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayInfo();
            }
        });

        calculateButton = new JButton("Calculate");
        mainPanel.add(calculateButton);
        calculateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calculate();
            }
        });
        
        langageButton = new JButton("Language Type");
        mainPanel.add(langageButton);
        langageButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayLanguageType();
            }
        });

        regexButton = new JButton("Calculate Regular Expression");
        mainPanel.add(regexButton);
        regexButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calculateRegularExpression();
            }
        });

       /* productButton = new JButton("Product of AFDs"); 
        mainPanel.add(productButton);
        productButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calculateProduct();
            }
        });*/

        // Ajouter les nouveaux boutons pour l'union, la différence, l'intersection et le complément
        unionButton = new JButton("Union of AFDs");
        mainPanel.add(unionButton);
        unionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calculateUnion();
            }
        });

        differenceButton = new JButton("Difference of AFDs");
        mainPanel.add(differenceButton);
        differenceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calculateDifference();
            }
        });

        intersectionButton = new JButton("Intersection of AFDs");
        mainPanel.add(intersectionButton);
        intersectionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calculateIntersection();
            }
        });

        complementButton = new JButton("Complement of AFD");
        mainPanel.add(complementButton);
        complementButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calculateComplement();
            }
        });

        add(mainPanel);
    }

    private void createAFD() {
        // Récupérer les valeurs des champs de texte
        Set<String> etats = new HashSet<>(Arrays.asList(statesField.getText().split(",")));
        Set<String> alphabet = new HashSet<>(Arrays.asList(alphabetField.getText().split(",")));
        String etatInit = initialStateField.getText();
        Set<String> etatfinal = new HashSet<>(Arrays.asList(finalStatesField.getText().split(",")));

        // Créer une structure pour les transitions
        Map<String, Map<String, String>> transitions = new HashMap<>();
        String[] transitionsLines = transitionsArea.getText().split("\n");
        for (String line : transitionsLines) {
            String[] parts = line.split("=");
            String[] fromTo = parts[0].trim().split(",");
            String fromState = fromTo[0].trim();
            String symbol = fromTo[1].trim();
            String toState = parts[1].trim();
            if (!transitions.containsKey(fromState)) {
                transitions.put(fromState, new HashMap<>());
            }
            transitions.get(fromState).put(symbol, toState);
        }

        // Créer l'instance d'AFD
        if (afd1 == null) {
            afd1 = new AFD(etats, alphabet, etatInit, etatfinal, transitions);
            System.out.println("Premier AFD créé");
        } else {
            afd2 = new AFD(etats, alphabet, etatInit, etatfinal, transitions);
            System.out.println("Deuxième AFD créé");
        }
    }

    private void displayInfo() {
        // Afficher les informations de l'AFD dans une boîte de dialogue
        if (afd1 != null && afd2 != null) {
            JOptionPane.showMessageDialog(this, afd1.toString() + "\n" + afd2.toString(), "Informations sur les AFDs", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez d'abord créer deux AFDs", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void calculate() {
        // Obtenir le mot à partir d'une boîte de dialogue
        String mot = JOptionPane.showInputDialog(this, "Entrez le mot à calculer:");
        if (mot != null) {
            // Calculer les étapes sur le mot donné
            if (afd1 != null && afd2 != null) {
                System.out.println("Calcul pour le premier AFD : ");
                afd1.calculer(mot);
                System.out.println("Calcul pour le deuxième AFD : ");
                afd2.calculer(mot);
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez d'abord créer deux AFDs", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void displayLanguageType() {
        // Afficher le type de langage (vide, fini ou infini) dans une boîte de dialogue
        String languageType;
        if (afd1 != null && afd2 != null) {
            languageType = "Type de langage pour le premier AFD : ";
            if (afd1.isLanguageEmpty()) {
                languageType += "Langage vide\n";
            } else if (afd1.isLanguageFinite()) {
                languageType += "Langage fini\n";
            } else {
                languageType += "Langage infini\n";
            }
            languageType += "Type de langage pour le deuxième AFD : ";
            if (afd2.isLanguageEmpty()) {
                languageType += "Langage vide";
            } else if (afd2.isLanguageFinite()) {
                languageType += "Langage fini";
            } else {
                languageType += "Langage infini";
            }
            JOptionPane.showMessageDialog(this, languageType, "Types de Langage", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez d'abord créer deux AFDs", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void calculateRegularExpression() {
        // Vérifier si les AFDs ont été créés
        if (afd1 != null && afd2 != null) {
            // Appeler la méthode de calcul de l'expression régulière de chaque AFD
            String regex1 = afd1.calculateRegularExpression();
            String regex2 = afd2.calculateRegularExpression();
            // Afficher les résultats dans une boîte de dialogue
            JOptionPane.showMessageDialog(this, "Expression régulière pour le premier AFD : " + regex1 + "\nExpression régulière pour le deuxième AFD : " + regex2, "Expressions régulières", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez d'abord créer deux AFDs", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void calculateProduct() {
        // Vérifier si les AFDs ont été créés
        if (afd1 != null && afd2 != null) {
            // Calculer le produit des deux AFDs
            AFD product = AFD.product(afd1, afd2);
            // Afficher le résultat dans une boîte de dialogue
            JOptionPane.showMessageDialog(this, "Produit des deux AFDs :\n" + product.toString(), "Produit des AFDs", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez d'abord créer deux AFDs", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void calculateUnion() {
        // Vérifier si les AFDs ont été créés
        if (afd1 != null && afd2 != null) {
            // Calculer l'union des deux AFDs
            AFD unionAFD = AFDOperations.union(afd1, afd2);
            // Afficher le résultat dans une boîte de dialogue
            JOptionPane.showMessageDialog(this, "Union des deux AFDs :\n" + unionAFD.toString(), "Union des AFDs", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez d'abord créer deux AFDs", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void calculateDifference() {
        // Vérifier si les AFDs ont été créés
        if (afd1 != null && afd2 != null) {
            // Calculer la différence des deux AFDs
            AFD differenceAFD = AFDOperations.difference(afd1, afd2);
            // Afficher le résultat dans une boîte de dialogue
            JOptionPane.showMessageDialog(this, "Différence des deux AFDs :\n" + differenceAFD.toString(), "Différence des AFDs", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez d'abord créer deux AFDs", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void calculateIntersection() {
        // Vérifier si les AFDs ont été créés
        if (afd1 != null && afd2 != null) {
            // Calculer l'intersection des deux AFDs
            AFD intersectionAFD = AFDOperations.intersection(afd1, afd2);
            // Afficher le résultat dans une boîte de dialogue
            JOptionPane.showMessageDialog(this, "Intersection des deux AFDs :\n" + intersectionAFD.toString(), "Intersection des AFDs", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez d'abord créer deux AFDs", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void calculateComplement() {
        // Vérifier si l'AFD a été créé
        if (afd1 != null) {
            // Calculer le complément de l'AFD
            AFD complementAFD = AFDOperations.complement(afd1);
            // Afficher le résultat dans une boîte de dialogue
            JOptionPane.showMessageDialog(this, "Complément de l'AFD :\n" + complementAFD.toString(), "Complément de l'AFD", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez d'abord créer un AFD", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
