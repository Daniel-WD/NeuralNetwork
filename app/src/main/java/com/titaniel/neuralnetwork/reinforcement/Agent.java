package com.titaniel.neuralnetwork.reinforcement;

import com.titaniel.neuralnetwork.network.Network;
import com.titaniel.neuralnetwork.tic_tac_toe.TicTacToeJava;

import java.util.ArrayList;
import java.util.Random;

public class Agent {

    private double mThreshold = 1;

    public void setETA(double ETA) {
        this.ETA = ETA;
    }

    private double ETA = 0.5;
    private double DISCOUNT_FACTOR = 0.7;
    private static final int MEMORY_SIZE = 500;
    private static final int BATCH_SIZE = 10;

    private TicTacToeJava mField;

    private Network mNet;

    private Random mRand = new Random();

    private MemoryEntry[] mMemory;
    private int mMemoryIndex = 0;
    private boolean mIsMemoryFull = false;

    private final int mSelfFigure, mOpponent;

    class MemoryEntry {
        double[] state, action, newState;
        double reward;

        double[] getInput() {
            double[] input = new double[18];
            System.arraycopy(state, 0, input, 0, state.length);
            System.arraycopy(action, 0, input, 9, state.length);
            return input;
        }
    }

    public Agent(TicTacToeJava mField, int selfFigure, int mOpponent) {
        this.mField = mField;
        this.mSelfFigure = selfFigure;
        this.mOpponent = mOpponent;
        mNet = new Network(18, 18, 1);
        mMemory = new MemoryEntry[MEMORY_SIZE];
    }

    public void next() {
        double r = Math.random();

        double[] state = mField.getState();

        double[][] actions = AgentUtils.findPossibleActions(state);

        double[] chosenAction;
        if(r > mThreshold) {
            //random action
            int index = mRand.nextInt(actions.length);
            chosenAction = actions[index];
        } else {
            chosenAction = actions[networkBestIndex(state, actions)];
        }

        //take action
        int index = 0;
        for(int i = 0; i < chosenAction.length; i++) {
            if(chosenAction[i] == AgentUtils.ACTION) {
                index = i;
                break;
            }
        }

        mField.setStateAtPos(index/3, index%3, mSelfFigure);

        //get reward
        double reward = 0;
        int winner = mField.checkWin();
        if(winner == mSelfFigure) {
            reward = 1;
        } else if(winner == mOpponent) {
            reward = -1;
        }

        //save entry
        MemoryEntry entry = new MemoryEntry();

        entry.state = state;
        entry.action = chosenAction;
        entry.reward = reward;
        entry.newState = mField.getState();

        mMemory[mMemoryIndex++] = entry;
        if(mMemoryIndex == MEMORY_SIZE-1) {
            mMemoryIndex = 0;
            mIsMemoryFull = true;
        }

        if(mIsMemoryFull) {
            //train network
            MemoryEntry[] batches = getBatch();
            for(int i = 0; i < batches.length; i++) {
                MemoryEntry batch = batches[i];
                if(batch == null) return;
                double[] input = batch.getInput();
                double[] target = new double[1];
                target[0] = batch.reward + DISCOUNT_FACTOR*networkBestOutput(batch.newState, AgentUtils.findPossibleActions(batch.newState));
                mNet.train(input, target, ETA);
            }
        }
    }

    private int networkBestIndex(double[] state, double[][] actions) {
        double[] input = new double[18];
        double[][] output = new double[actions.length][];

        //insert state into input
        System.arraycopy(state, 0, input, 0, state.length);

        //get all outputs
        for(int i = 0; i < actions.length; i++) {
            //insert action into input
            System.arraycopy(actions[i], 0, input, 9, state.length);

            //get output from network
            output[i] = mNet.calc(input);
        }

        //search best output index
        int bestIndex = 0;
        for(int i = 1; i < output.length; i++) {
            if(output[i][0] > output[bestIndex][0]) bestIndex = i;
        }
        return bestIndex;
    }

    private double networkBestOutput(double[] state, double[][] actions) {
        double[] input = new double[18];
        double[][] output = new double[actions.length][];

        //insert state into input
        System.arraycopy(state, 0, input, 0, state.length);

        //get all outputs
        for(int i = 0; i < actions.length; i++) {
            //insert action into input
            System.arraycopy(actions[i], 0, input, 9, state.length);

            //get output from network
            output[i] = mNet.calc(input);
        }

        //search best output index
        double out = 0;
        for(int i = 1; i < output.length; i++) {
            if(output[i][0] > out) out = output[i][0];
        }
        return out;
    }

    private MemoryEntry[] getBatch() {
        ArrayList<MemoryEntry> batchList = new ArrayList<>();
        for(int i = 0; i < BATCH_SIZE; i++) {
            MemoryEntry entry;
            do {
                entry = mMemory[mRand.nextInt(MEMORY_SIZE)];
            } while(batchList.contains(entry));
            batchList.add(entry);
        }
        return batchList.toArray(new MemoryEntry[BATCH_SIZE]);
    }

    public void setThreshold(double t) {
        mThreshold = t;
    }

}
