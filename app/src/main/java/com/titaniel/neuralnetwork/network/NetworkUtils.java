package com.titaniel.neuralnetwork.network;

import java.util.ArrayList;

public class NetworkUtils {

    public static double[] createArray(int size, double value) {
        double[] out = new double[size];
        for(int i = 0; i < size; i++) {
            out[i] = value;
        }
        return out;
    }

    public static double[] createRandomArray(int size, double minVal, double maxVal) {
        double[] out = new double[size];
        for(int i = 0; i < size; i++) {
            out[i] = randomValue(minVal, maxVal);
        }
        return out;
    }

    public static double[][] createRandomArray(int sizeX, int sizeY, double minVal, double maxVal) {
        double[][] out = new double[sizeX][sizeY];
        for(int i = 0; i < sizeX; i++) {
            out[i] = createRandomArray(sizeY, minVal, maxVal);
        }
        return out;
    }

    public static double randomValue(double minVal, double maxVal) {
        return Math.random()*(maxVal-minVal) + minVal;
    }

    public static Integer[] randomValues(int minVal, int maxVal, int amount) {
        minVal--;
        if(amount > (maxVal-minVal)) {
            return null;
        }

        ArrayList<Integer> values = new ArrayList<>();
        int n;
        for(int i = 0; i < amount; i++) {
            do {
                n = (int)(Math.random() * (maxVal-minVal+1) + minVal);
            } while(values.contains(n));
            values.add(n);
        }
        return values.toArray(new Integer[amount]);
    }

    public static int indexOfMaxValue(double[] values) {
        int index = 0;
        for(int i = 0; i < values.length; i++) {
            if(values[i] > values[index]) index = i;
        }
        return index;
    }

}
