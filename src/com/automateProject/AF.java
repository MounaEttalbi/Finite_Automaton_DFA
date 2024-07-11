package com.automateProject;

import java.util.Set;
import java.util.Map;

public abstract class AF {
	
	    protected Set<String> etats;
	    protected Set<String> alphabet;
	    protected String etatInit;
	    protected Set<String> etatfinal;
	    protected Map<String, Map<String, String>> transitions;

	    public AF (Set<String> etats, Set<String> alphabet, String etatInit, Set<String> etatfinal, Map<String, Map<String, String>> transitions) {
	        this.etats = etats;
	        this.alphabet = alphabet;
	        this.etatInit = etatInit;
	        this.etatfinal = etatfinal;
	        this.transitions = transitions;
	    }

	    public abstract boolean accept(String input);

		public Set<String> getEtats() {
			return etats;
		}

		public void setEtats(Set<String> etats) {
			this.etats = etats;
		}

		public Set<String> getAlphabet() {
			return alphabet;
		}

		public void setAlphabet(Set<String> alphabet) {
			this.alphabet = alphabet;
		}

		public String getEtatInit() {
			return etatInit;
		}

		public void setEtatInit(String etatInit) {
			this.etatInit = etatInit;
		}

		public Set<String> getEtatfinal() {
			return etatfinal;
		}

		public void setEtatfinal(Set<String> etatfinal) {
			this.etatfinal = etatfinal;
		}

		public Map<String, Map<String, String>> getTransitions() {
			return transitions;
		}

		public void setTransitions(Map<String, Map<String, String>> transitions) {
			this.transitions = transitions;
		}
	    
	   
	}


