public class NFAOperations {
    public static NFA createFromSymbol(char c) {
        NFA nfa = new NFA();
        State startState = new State(0, true, false);
        State acceptState = new State(1, false, true);
        startState.addTransition(new Transition(startState, acceptState, c));
        nfa.addState(startState);
        nfa.addState(acceptState);
        return nfa;
    }

    public static NFA createFromEpsilon() {
        NFA nfa = new NFA();
        nfa.addState(new State(0, true, true));
        return nfa;
    }

    public static NFA createEmpty() {
        NFA nfa = new NFA();
        nfa.addState(new State(0, true, false));
        return nfa;
    }

    public static NFA concatenate(NFA nfa1, NFA nfa2) {
        if (nfa1.states.size() == 1) {
            return nfa2;
        }

        if (nfa2.states.size() == 1) {
            return nfa1;
        }

        for (State acceptState: nfa1.acceptStates) {
            acceptState.isAccept = nfa2.startState.isAccept;

            for (Transition startStateTransition: nfa2.startState.transitions) {
                acceptState.addTransition(new Transition(acceptState, startStateTransition.to, startStateTransition.symbol));
            }
        }

        NFA nfa = new NFA();
        for (State state: nfa1.states) {
            nfa.addState(state);
        }
        for (State state: nfa2.states) {
            if (state.isStart) {
                continue;
            }

            state.index += nfa1.states.size() - 1;
            nfa.addState(state);
        }

        return nfa;
    }

    public static NFA union(NFA nfa1, NFA nfa2) {
        State startState = new State(0, true, nfa1.startState.isAccept || nfa2.startState.isAccept);

        for (Transition transition: nfa1.startState.transitions) {
            startState.addTransition(new Transition(startState, transition.to, transition.symbol));
        }
        for (Transition transition: nfa2.startState.transitions) {
            startState.addTransition(new Transition(startState, transition.to, transition.symbol));
        }

        NFA nfa = new NFA();
        nfa.addState(startState);
        for (State state: nfa1.states) {
            if (state.isStart) {
                continue;
            }

            nfa.addState(state);
        }
        for (State state: nfa2.states) {
            if (state.isStart) {
                continue;
            }

            state.index += nfa1.states.size() - 1;
            nfa.addState(state);
        }

        return nfa;
    }

    public static void star(NFA nfa) {
        if (nfa.justStarred || nfa.states.size() == 1) {
            return;
        }

        for (State acceptState: nfa.acceptStates) {
            if (acceptState == nfa.startState) {
                continue;
            }

            for (Transition startStateTransition: nfa.startState.transitions) {
                acceptState.addTransition(new Transition(acceptState, startStateTransition.to, startStateTransition.symbol));
            }
        }

        State startState = new State(0, true, true);
        for (Transition startStateTransition: nfa.startState.transitions) {
            startState.addTransition(new Transition(startState, startStateTransition.to, startStateTransition.symbol));
        }

        nfa.states.remove(nfa.startState);
        nfa.acceptStates.remove(nfa.startState);

        nfa.addState(startState);

        nfa.justStarred = true;
    }

    private static String getBracketed(String regex, int characterIndex) {
        StringBuilder bracketed = new StringBuilder();

        int bracketDifference = 1;

        while (true) {
            char character = regex.charAt(characterIndex);
            if (character == ')') {
                bracketDifference--;
                if (bracketDifference == 0) {
                    return bracketed.toString();
                }
            } else if (character == '(') {
                bracketDifference++;
            }
            bracketed.append(regex.charAt(characterIndex++));
        }
    }

    private static String getRegexToUnion(String regex, int characterIndex) {
        StringBuilder regexToUnion = new StringBuilder();

        int bracketDifference = 0;

        while (characterIndex < regex.length()) {
            char character = regex.charAt(characterIndex);
            if (character == ')') {
                bracketDifference--;
            } else if (character == '(') {
                bracketDifference++;
            } else if (character == '|' && bracketDifference == 0) {
                break;
            }
            regexToUnion.append(regex.charAt(characterIndex++));
        }

        return regexToUnion.toString();
    }

    private static int starUntilPossible(NFA nfa, String regex, int characterIndex) {
        while (characterIndex + 1 < regex.length() && regex.charAt(characterIndex + 1) == '*') {
            star(nfa);
            characterIndex++;
        }
        return characterIndex;
    }

    public static NFA createFromRegex(String regex) {
        NFA nfa = createEmpty();

        for (int characterIndex = 0; characterIndex < regex.length(); characterIndex++) {
            char character = regex.charAt(characterIndex);

            NFA currentNFA;

            if (Character.isLetterOrDigit(character)) {
                currentNFA = createFromSymbol(character);

                characterIndex = starUntilPossible(currentNFA, regex, characterIndex);

                nfa = concatenate(nfa, currentNFA);
            } else if (character == '(') {
                String bracketed = getBracketed(regex, characterIndex + 1);
                if (bracketed.length() == 0) {
                    currentNFA = createFromEpsilon();
                } else {
                    currentNFA = createFromRegex(bracketed);
                }
                characterIndex += bracketed.length() + 1;

                characterIndex = starUntilPossible(currentNFA, regex, characterIndex);

                nfa = concatenate(nfa, currentNFA);
            } else if (character == '|') {
                String regexToUnion = getRegexToUnion(regex, characterIndex + 1);
                currentNFA = createFromRegex(regexToUnion);
                characterIndex += regexToUnion.length();
                nfa = union(nfa, currentNFA);
            }
        }

        return nfa;
    }
}
