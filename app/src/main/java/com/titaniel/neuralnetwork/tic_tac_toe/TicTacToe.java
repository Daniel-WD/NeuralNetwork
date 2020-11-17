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

public class TicTacToe extends View {

    public static final int STATE_EMPTY = 0;
    public static final int STATE_CROSS = -1;
    public static final int STATE_CIRCLE = 1;


    private Drawable mIcCross, mIcCircle;

    private Paint mDivPaint;
    private float mWidth, mHeight;
    private int mBlock;

    private int[][] mStates;
    private int nextState = STATE_CROSS;

    public TicTacToe(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mDivPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDivPaint.setStrokeWidth(5);
        mDivPaint.setColor(Color.BLUE);
        mDivPaint.setStyle(Paint.Style.STROKE);

        mIcCross = context.getDrawable(R.drawable.ic_cross);
        mIcCircle = context.getDrawable(R.drawable.ic_circle);

        mIcCross.setColorFilter(Color.MAGENTA, PorterDuff.Mode.SRC_ATOP);
        mIcCircle.setColorFilter(Color.CYAN, PorterDuff.Mode.SRC_ATOP);

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

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;

        mBlock = (int)(mWidth/3f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //horizontal lines
        for(int i = 1; i < 3; i++) {
            canvas.drawLine(0, mBlock*i, mWidth, mBlock*i, mDivPaint);
        }
        //vertical lines
        for(int i = 1; i < 3; i++) {
            canvas.drawLine(mBlock*i, 0, mBlock*i, mHeight, mDivPaint);
        }

        //states... crosses and circles
        int x, y;
        for(int i = 0; i < mStates.length; i++) {
            for(int j = 0; j < mStates[i].length; j++) {
                x = i*mBlock;
                y = j*mBlock;
                switch(mStates[i][j]) {
                    case STATE_CIRCLE:
                        mIcCircle.setBounds(x, y, x + mBlock, y + mBlock);
                        mIcCircle.draw(canvas);
                        break;
                    case STATE_CROSS:
                        mIcCross.setBounds(x, y, x + mBlock, y + mBlock);
                        mIcCross.draw(canvas);
                        break;
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int x = (int)event.getX();
                int y = (int)event.getY();
                int i = x/mBlock;
                int j = y/mBlock;
                if(mStates[i][j] != STATE_EMPTY) return false;

                mStates[i][j] = STATE_CROSS;
//                mStates[i][j] = nextState;
//                switchNextState();

                int winner = checkWin();
                if(winner != STATE_EMPTY) {
                    String text = "";
                    switch(winner) {
                        case STATE_CROSS:
                            text = "Crosses win";
                            break;
                        case STATE_CIRCLE:
                            text = "Circles win";
                            break;
                    }
                    Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                }

                invalidate();
                return false;
        }
        return super.onTouchEvent(event);
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
        invalidate();
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
        invalidate();
    }
}
