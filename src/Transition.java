public class Transition {
    public State from;

    public State to;

    public char symbol;

    public Transition(State from, State to, char symbol) {
        this.from = from;
        this.to = to;
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return "from " + from.index + " to " + to.index + " by reading " + symbol + "\n";
    }
}
