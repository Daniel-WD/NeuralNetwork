package com.titaniel.neuralnetwork;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.titaniel.neuralnetwork.network.Network;
import com.titaniel.neuralnetwork.reinforcement.Agent;
import com.titaniel.neuralnetwork.reinforcement.RandAgent;
import com.titaniel.neuralnetwork.tic_tac_toe.TicTacToe;

import java.util.Arrays;

public class NetworkActivity extends AppCompatActivity {

    private TicTacToe mField;
    private Agent mCircleAgent;
    private RandAgent mCrossAgent;

    private Button mBtnClear;
    private Button mBtnNext;
    private TextView mTvOne, mTvTwo, mTvThree;

    private int mOne = 0, mTwo = 0, mThree = 0;

    private boolean flag = true;

    private Handler mHandler = new Handler();
    private Runnable mLoop = new Runnable() {
        @Override
        public void run() {
            if(flag) {
                mCircleAgent.next();
            } else {
                mCrossAgent.next();
            }
            flag = !flag;
            check();
            mHandler.postDelayed(this, 0);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);

        mField = findViewById(R.id.ticTacToe);
        mBtnClear = findViewById(R.id.btnClear);
        mBtnNext = findViewById(R.id.btnNext);
        mTvOne = findViewById(R.id.tvOne);
        mTvTwo = findViewById(R.id.tvTwo);
        mTvThree = findViewById(R.id.tvThree);

        //mCircleAgent = new Agent(mField, TicTacToe.STATE_CIRCLE, TicTacToe.STATE_CROSS);
        //mCrossAgent = new RandAgent(mField, TicTacToe.STATE_CROSS);

        mBtnClear.setOnClickListener(v -> {
            mField.clear();
        });
        mBtnNext.setOnClickListener(v -> {
            mHandler.post(mLoop);
        });
    }

    private void check() {
        int winner = mField.checkWin();
        if(winner == TicTacToe.STATE_CIRCLE) {
            mField.clear();
            mOne++;
        } else if(winner == TicTacToe.STATE_CROSS) {
            mField.clear();
            mThree++;
        }

        if(mField.isFull()) {
            mField.clear();
            mTwo++;
        }

        updateTexts();
    }

    private void updateTexts() {
        mTvOne.setText(String.valueOf(mOne));
        mTvTwo.setText(String.valueOf(mTwo));
        mTvThree.setText(String.valueOf(mThree));
    }

}
