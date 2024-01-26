import java.util.*;

public class NFA {
    public String input;

    public List<State> states;

    public State startState;

    public List<State> acceptStates;

    public boolean justStarred = false;

    public NFA(){
        states = new ArrayList<>();
        acceptStates = new ArrayList<>();
    }

    public NFA(String input) {
        this.input = input;
        states = new ArrayList<>();
        acceptStates = new ArrayList<>();
    }

    public void addState(State state) {
        states.add(state);
        if (state.isStart) {
            startState = state;
        }
        if (state.isAccept) {
            acceptStates.add(state);
        }
    }

    private void transition(State state, List<Character> output, int step) {
        if (step >= 0 && state.isAccept) {
            output.set(step, 'Y');
        }

        for (Transition transition: state.transitions) {
            if (step + 1 < output.size() && transition.symbol == input.charAt(step + 1)) {
                transition(transition.to, output, step + 1);
            }
        }
    }

    public String simulate() {
        List<Character> output = new ArrayList<>();

        int outputSize = input.length();
        while (outputSize-- > 0) {
            output.add('N');
        }

        transition(startState, output, -1);

        StringBuilder outputString = new StringBuilder();
        for (char c: output) {
            outputString.append(c);
        }

        return outputString.toString();
    }

    public String toOutputForm() {
        StringBuilder outputForm = new StringBuilder();

        int transition_amount = 0;
        for (State state: states) {
            transition_amount += state.getTransitionAmount();
        }

        outputForm.append(states.size()).append(" ").append(acceptStates.size()).append(" ").append(transition_amount).append("\n");

        for (int i = 0; i < acceptStates.size() - 1; i++) {
            outputForm.append(acceptStates.get(i).index).append(" ");
        }
        if (acceptStates.size() > 0) {
            outputForm.append(acceptStates.get(acceptStates.size() - 1).index).append("\n");
        }

        Collections.sort(states);

        for (State state: states) {
            if (state.getTransitionAmount() == 0) {
                outputForm.append(0).append("\n");
                continue;
            }
            outputForm.append(state.getTransitionAmount()).append(" ");
            for (int i = 0; i < state.getTransitionAmount() - 1; i++) {
                outputForm.append(state.transitions.get(i).symbol).append(" ");
                outputForm.append(state.transitions.get(i).to.index).append(" ");
            }
            outputForm.append(state.transitions.get(state.getTransitionAmount() - 1).symbol).append(" ");
            outputForm.append(state.transitions.get(state.getTransitionAmount() - 1).to.index).append("\n");
        }

        return outputForm.toString();
    }

    @Override
    public String toString() {
        StringBuilder NFAInfo = new StringBuilder();

        NFAInfo.append("state amount: ").append(states.size()).append("\n");

        NFAInfo.append("states:\n");
        for (State state: states) {
            NFAInfo.append(state.toString());
        }

        return NFAInfo.toString();
    }
}
