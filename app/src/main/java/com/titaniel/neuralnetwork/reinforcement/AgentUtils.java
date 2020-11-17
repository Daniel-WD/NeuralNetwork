package com.titaniel.neuralnetwork.reinforcement;

import com.titaniel.neuralnetwork.tic_tac_toe.TicTacToe;

public class AgentUtils {

    static final int ACTION = 1;
    static final int NO_ACTION = 0;

    static double[][] findPossibleActions(double[] state) {
        double[] actions = new double[state.length];
        int actionCount = 0;
        for(int i = 0; i < state.length; i++) {
            if(state[i] == TicTacToe.STATE_EMPTY) {
                actions[i] = ACTION;
                actionCount++;
            } else {
                actions[i] = NO_ACTION;
            }
        }
        double[][] res = new double[actionCount][];
        int index = 0;
        for(int i = 0; i < actions.length; i++) {
            if(actions[i] == ACTION) {
                double[] action = new double[state.length];
                action[i] = ACTION;
                res[index++] = action;
            }
        }
        return res;
    }

}
