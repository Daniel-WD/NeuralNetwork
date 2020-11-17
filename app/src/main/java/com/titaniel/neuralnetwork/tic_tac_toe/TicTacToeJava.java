package com.titaniel.neuralnetwork.tic_tac_toe;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.titaniel.neuralnetwork.R;

public class TicTacToeJava {

    public static final int STATE_EMPTY = 0;
    public static final int STATE_CROSS = -1;
    public static final int STATE_CIRCLE = 1;

    private int[][] mStates;
    private int nextState = STATE_CROSS;

    public TicTacToeJava() {
        init();
    }

    private void init() {
        mStates = new int[3][3];
        for(int i = 0; i < mStates.length; i++) {
            for(int j = 0; j < mStates[i].length; j++) {
                mStates[i][j] = STATE_EMPTY;
            }
        }
    }

    public boolean isFull() {
        boolean res = true;
        for(int i = 0; i < mStates.length; i++) {
            for(int j = 0; j < mStates[i].length; j++) {
                if(mStates[i][j] == STATE_EMPTY) res = false;
            }
        }
        return res;
    }

    private void switchNextState() {
        nextState = nextState == STATE_CROSS ? STATE_CIRCLE : STATE_CROSS;
    }

    public void clear() {
        init();
    }

    public int checkWin() {
        int winner = STATE_EMPTY;

        //horizontal
        nextRowCol:
        for(int i = 0; i < mStates.length; i++) {
            int firstState = mStates[i][0];
            for(int j = 1; j < mStates[i].length; j++) {
                if(mStates[i][j] == STATE_EMPTY) continue nextRowCol;
                if(mStates[i][j] != firstState) continue nextRowCol;
            }
            winner = firstState;
        }

        //vertical
        nextRowCol:
        for(int i = 0; i < mStates.length; i++) {
            int firstState = mStates[0][i];
            for(int j = 1; j < mStates[i].length; j++) {
                if(mStates[j][i] == STATE_EMPTY) continue nextRowCol;
                if(mStates[j][i] != firstState) continue nextRowCol;
            }
            winner = firstState;
        }

        //diagonal
        int firstState = mStates[0][0];
        if(firstState == mStates[1][1] && firstState == mStates[2][2]) winner = firstState;

        firstState = mStates[2][0];
        if(firstState == mStates[1][1] && firstState == mStates[0][2]) winner = firstState;

        return winner;
    }

    public double[] getState() {
        double[] res = new double[9];
        int index = 0;
        for(int i = 0; i < mStates.length; i++) {
            for(int j = 0; j < mStates[i].length; j++) {
                res[index++] = mStates[i][j];
            }
        }
        return res;
    }

    public int getStateAtPos(int x, int y) {
        return mStates[x][y];
    }

    public void setStateAtPos(int x, int y, int state) {
        mStates[x][y] = state;
    }
}
