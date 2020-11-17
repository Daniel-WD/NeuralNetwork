package com.titaniel.neuralnetwork.reinforcement;

import com.titaniel.neuralnetwork.tic_tac_toe.TicTacToe;
import com.titaniel.neuralnetwork.tic_tac_toe.TicTacToeJava;

import java.util.Random;

public class RandAgent {

    private TicTacToeJava mField;

    private int mSelfSymbol;

    private Random random = new Random();

    public RandAgent(TicTacToeJava mField, int mSelfSymbol) {
        this.mField = mField;
        this.mSelfSymbol = mSelfSymbol;
    }

    public void next() {
        int x, y;
        do {
            x = random.nextInt(3);
            y = random.nextInt(3);
        } while(mField.getStateAtPos(x, y) != TicTacToe.STATE_EMPTY);
        mField.setStateAtPos(x, y, mSelfSymbol);
    }
}
