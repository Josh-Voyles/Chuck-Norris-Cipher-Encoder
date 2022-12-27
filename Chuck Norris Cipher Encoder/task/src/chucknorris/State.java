package chucknorris;

public enum State {
    MENU, ENCODE, DECODE, EXIT;

    // changes user input "encode" to state ENCODE and so on...
    public static State getState(String state) {
        for (State value : State.values()) {
            if (value.toString().equals(state.toUpperCase())) return value;

        }
        return MENU;
    }
}
