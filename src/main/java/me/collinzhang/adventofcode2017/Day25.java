package me.collinzhang.adventofcode2017;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day25 {

    public static void main(String[] args) {
        System.out.println(day25P1());
    }

    private static int day25P1() {
        Map<String, State> stateMap = buildFSMInput();

        int cursor = 0;
        String stateName = "A";
        Map<Integer, Integer> tape = new HashMap<>();

        for (int i = 0; i < 12481997; i++) {
            State state = stateMap.get(stateName);
            int value = tape.getOrDefault(cursor, 0);

            Action action = state.actions.get(value);
            tape.put(cursor, action.writeValue);
            cursor += action.cursorOffset;
            stateName = action.nextState;
        }

        return (int) tape.values().stream().filter(i -> i == 1).count();
    }

    private static Map<String, State> buildFSMInput() {
        List<State> states = new ArrayList<>();

        // @f:off
        states.add(newStateBuilder()
            .withName("A")
            .onValue(0)
                .withWriteValue(1)
                .withCursorOffset(1)
                .withNextState("B")
                .and()
            .onValue(1)
                .withWriteValue(0)
                .withCursorOffset(-1)
                .withNextState("C")
                .and()
            .build());

        states.add(newStateBuilder()
            .withName("B")
            .onValue(0)
                .withWriteValue(1)
                .withCursorOffset(-1)
                .withNextState("A")
                .and()
            .onValue(1)
                .withWriteValue(1)
                .withCursorOffset(1)
                .withNextState("D")
                .and()
            .build());

        states.add(newStateBuilder()
            .withName("C")
            .onValue(0)
                .withWriteValue(0)
                .withCursorOffset(-1)
                .withNextState("B")
                .and()
            .onValue(1)
                .withWriteValue(0)
                .withCursorOffset(-1)
                .withNextState("E")
                .and()
            .build());

        states.add(newStateBuilder()
            .withName("D")
            .onValue(0)
                .withWriteValue(1)
                .withCursorOffset(1)
                .withNextState("A")
                .and()
            .onValue(1)
                .withWriteValue(0)
                .withCursorOffset(1)
                .withNextState("B")
                .and()
            .build());

        states.add(newStateBuilder()
            .withName("E")
            .onValue(0)
                .withWriteValue(1)
                .withCursorOffset(-1)
                .withNextState("F")
                .and()
            .onValue(1)
                .withWriteValue(1)
                .withCursorOffset(-1)
                .withNextState("C")
                .and()
            .build());

        states.add(newStateBuilder()
            .withName("F")
            .onValue(0)
                .withWriteValue(1)
                .withCursorOffset(1)
                .withNextState("D")
                .and()
            .onValue(1)
                .withWriteValue(1)
                .withCursorOffset(1)
                .withNextState("A")
                .and()
            .build());
        // @f:on

        return states.stream().collect(Collectors.toMap(s -> s.name, s -> s));
    }

    static class Action {

        int writeValue;
        int cursorOffset;
        String nextState;
    }

    static class State {

        String name;
        Map<Integer, Action> actions = new HashMap<>();
    }

    private static StateBuilder newStateBuilder() {
        return new StateBuilder();
    }

    static class ActionBuilder {

        private Action action = new Action();
        private StateBuilder stateBuilder;

        ActionBuilder(StateBuilder stateBuilder) {
            this.stateBuilder = stateBuilder;
        }

        ActionBuilder withWriteValue(int val) {
            action.writeValue = val;
            return this;
        }

        ActionBuilder withCursorOffset(int offset) {
            action.cursorOffset = offset;
            return this;
        }

        ActionBuilder withNextState(String state) {
            action.nextState = state;
            return this;
        }

        Action build() {
            return action;
        }

        StateBuilder and() {
            return stateBuilder;
        }
    }

    static class StateBuilder {

        private State state = new State();
        private Map<Integer, ActionBuilder> actionBuilderMap = new HashMap<>();

        StateBuilder withName(String name) {
            state.name = name;
            return this;
        }

        ActionBuilder onValue(Integer val) {
            ActionBuilder actionBuilder = new ActionBuilder(this);
            actionBuilderMap.put(val, actionBuilder);
            return actionBuilder;
        }

        State build() {
            state.actions = actionBuilderMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().build()));
            return state;
        }
    }
}
