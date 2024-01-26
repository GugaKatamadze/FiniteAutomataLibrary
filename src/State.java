import java.util.ArrayList;
import java.util.List;

public class State implements Comparable<State> {
    public int index;

    public boolean isStart;

    public boolean isAccept;

    public List<Transition> transitions;

    public State(int index, boolean isStart, boolean isAccept) {
        this.index = index;
        this.isStart = isStart;
        this.isAccept = isAccept;
        transitions = new ArrayList<>();
    }

    public void addTransition(Transition transition) {
        transitions.add(transition);
    }

    public int getTransitionAmount() {
        return transitions.size();
    }

    @Override
    public int compareTo(State state) {
        return Integer.compare(index, state.index);
    }

    @Override
    public String toString() {
        StringBuilder stateInfo = new StringBuilder();

        stateInfo.append("state index: ").append(index).append("\n");

        stateInfo.append("is start state?: ").append(isStart).append("\n");

        stateInfo.append("is accept state?: ").append(isAccept).append("\n");

        if (getTransitionAmount() == 0) {
            stateInfo.append("no transitions\n");
        } else {
            stateInfo.append("transition amount: ").append(getTransitionAmount()).append("\n");
            stateInfo.append("transitions:\n");
            for (Transition transition: transitions) {
                stateInfo.append(transition.toString());
            }
        }

        return stateInfo.toString();
    }
}
