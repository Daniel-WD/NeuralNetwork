package com.titaniel.neuralnetwork;

import com.titaniel.neuralnetwork.reinforcement.Agent;
import com.titaniel.neuralnetwork.reinforcement.RandAgent;
import com.titaniel.neuralnetwork.tic_tac_toe.TicTacToe;
import com.titaniel.neuralnetwork.tic_tac_toe.TicTacToeJava;

public class Main {

    private static TicTacToeJava mField;
    private static Agent mCircleAgent;
    private static Agent mCrossAgent;

    private static int mOne = 0, mTwo = 0, mThree = 0;

    private static boolean flag = true;

    private static boolean running = true;

    public static void main(String[] args) {

        mField = new TicTacToeJava();

        mCircleAgent = new Agent(mField, TicTacToe.STATE_CIRCLE, TicTacToe.STATE_CROSS);
        mCrossAgent = new Agent(mField, TicTacToe.STATE_CROSS, TicTacToe.STATE_CIRCLE);

        mCircleAgent.setETA(0.1);
        mCrossAgent.setETA(0.9);

        while(running) {
            if(flag) {
                mCircleAgent.next();
            } else {
                mCrossAgent.next();
            }
            flag = !flag;
            check();
        }
    }


    private static void check() {
        int winner = mField.checkWin();
        if(winner == TicTacToe.STATE_CIRCLE) {
            mField.clear();
            mOne++;
            updateTexts();
        } else if(winner == TicTacToe.STATE_CROSS) {
            mField.clear();
            mThree++;
            updateTexts();
        }

        if(mField.isFull()) {
            mField.clear();
            mTwo++;
            updateTexts();
        }

    }

    static int i = 0;
    private static void updateTexts() {
        if(i == 5000) {
            mCircleAgent.setThreshold(1);
            mCrossAgent.setThreshold(1);
        }
        System.out.println(i++ + ": " + mOne + " --- " + mTwo + " --- " + mThree + " --- " + (mOne-mThree));
        if(i > 20000) running = false;
    }

}
