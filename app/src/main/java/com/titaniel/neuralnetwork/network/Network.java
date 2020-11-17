package com.titaniel.neuralnetwork.network;

public class Network {

    private final int[] NETWORK_LAYER_SIZES;
    private final int INPUT_SIZE;
    private final int OUTPUT_SIZE;
    private final int NETWORK_SIZE;

    private double[][] output; //layer, neuron
    private double[][][] weights; //layer, neuron, docking weights
    private double[][] bias; //layer, neuron

    private double[][] errorSignal; //layer, neuron
    private double[][] outputDerivative; //layer, neuron

    public Network(int... networkLayerSizes) {
        this.NETWORK_LAYER_SIZES = networkLayerSizes;
        this.INPUT_SIZE = NETWORK_LAYER_SIZES[0];
        this.OUTPUT_SIZE = NETWORK_LAYER_SIZES[NETWORK_LAYER_SIZES.length-1];
        this.NETWORK_SIZE = NETWORK_LAYER_SIZES.length;

        output = new double[NETWORK_SIZE][];
        errorSignal = new double[NETWORK_SIZE][];
        outputDerivative = new double[NETWORK_SIZE][];
        weights = new double[NETWORK_SIZE][][];
        bias = new double[NETWORK_SIZE][];

        for(int i = 0; i < NETWORK_SIZE; i++) {
            output[i] = new double[NETWORK_LAYER_SIZES[i]];
            errorSignal[i] = new double[NETWORK_LAYER_SIZES[i]];
            outputDerivative[i] = new double[NETWORK_LAYER_SIZES[i]];
            bias[i] = NetworkUtils.createRandomArray(NETWORK_LAYER_SIZES[i], 0.3, 0.7);

            if(i > 0) {
                weights[i] =
                        NetworkUtils.createRandomArray(NETWORK_LAYER_SIZES[i], NETWORK_LAYER_SIZES[i - 1], -0.5, 0.5);
            }
        }
    }

    public double[] calc(double... input) {
        if(input.length != INPUT_SIZE) return null;
        output[0] = input;
        for(int layer = 1; layer < NETWORK_SIZE; layer++) {
            for(int neuron = 0; neuron < NETWORK_LAYER_SIZES[layer]; neuron++) {
                double sum = 0;
                for(int prevNeuron = 0; prevNeuron < NETWORK_LAYER_SIZES[layer - 1]; prevNeuron++) {
                    sum += output[layer-1][prevNeuron] * weights[layer][neuron][prevNeuron];
                }
                sum += bias[layer][neuron];
                output[layer][neuron] = activationFunction(sum);
                outputDerivative[layer][neuron] = output[layer][neuron] * (1 - output[layer][neuron]);
            }
        }
        return output[NETWORK_SIZE-1];
    }

    public void train(double[] input, double[] target, double eta) {
        if(input.length != INPUT_SIZE || target.length != OUTPUT_SIZE) return;
        calc(input);
        backPropError(target);
        updateWeights(eta);
    }
    public void backPropError(double[] target) {
        //output layer
        for(int neuron = 0; neuron < NETWORK_LAYER_SIZES[NETWORK_SIZE-1]; neuron++) {
            errorSignal[NETWORK_SIZE-1][neuron] = (output[NETWORK_SIZE-1][neuron] - target[neuron])
                    * outputDerivative[NETWORK_SIZE-1][neuron];
        }
        //hidden layers without input layer
        for(int layer = NETWORK_SIZE-2; layer > 0; layer--) {
            for(int neuron = 0; neuron < NETWORK_LAYER_SIZES[layer]; neuron++) {
                double sum = 0;
                for(int nextNeuron = 0; nextNeuron < NETWORK_LAYER_SIZES[layer+1]; nextNeuron++) {
                    sum += weights[layer + 1][nextNeuron][neuron] * errorSignal[layer + 1][nextNeuron];
                }
                errorSignal[layer][neuron] = sum * outputDerivative[layer][neuron];
            }
        }
    }

    public void updateWeights(double eta) {
        for(int layer = 1; layer < NETWORK_SIZE; layer++) {
            for(int neuron = 0; neuron < NETWORK_LAYER_SIZES[layer]; neuron++) {
                double delta = - eta * errorSignal[layer][neuron];
                bias[layer][neuron] += delta;
                for(int prevNeuron = 0; prevNeuron < NETWORK_LAYER_SIZES[layer - 1]; prevNeuron++) {
                    weights[layer][neuron][prevNeuron] += delta * output[layer-1][prevNeuron];
                }
            }
        }
    }

    private double activationFunction(double x) {
        return hypTan(x);
    }

    private double sigmoid(double x) {
        return 1d / (1d + Math.exp(-x));
    }

    private double hypTan(double x) {
        return 2d / (1d + Math.exp(-2*x)) -1;
    }
}
