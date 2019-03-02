/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codingalgorithm;

import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author hadin
 */
public class SumProductAlgorithm {

    private final double PI = Math.PI;
    private double sigma;
    private final double E = Math.E;
    private double[] fj0;
    private double[] fj1;
//    private double[][] Qij0;
//    private double[][] Qij1;
//    private double[][] Rij0;
//    private double[][] Rij1;
    private int syndrom = 1;
    private int round = 1;
    //private final double constant12PISigma;
    private final double[] rij;
    private ParityObject[][] nodes;
    private ParityObject[][] temp;
    private int cols;
    private int rows;

    public SumProductAlgorithm(double sigma, double[] rij, int[] m, int columns, int rows, int[][] matrix) {
        //this.sigma = sigma;
        this.sigma = sigma;
        fj0 = new double[columns];
        fj1 = new double[columns];
        //constant12PISigma = 1/(Math.sqrt(2*PI)*sigma);
        this.rij = rij;
        this.rows = rows;
        this.cols = columns;
        nodes = new ParityObject[rows][columns];
        temp = new ParityObject[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                nodes[i][j] = new ParityObject(i, j);
                temp[i][j] = new ParityObject(i, j);
            }
        }

        calculatingFJx(matrix);
    }

    private void calculatingFJx(int[][] matrix) {
        while (syndrom != 0) {
            if (round == 1) {
                for (int i = 0; i < fj1.length; i++) {
                    fj1[i] = (1.0 / (1.0 + (Math.pow(E, (-2.0 * 1.0 * rij[i] / Math.pow(sigma, 2))))));
                    //fj1[i] = round(fj1[i], 4);
                    fj0[i] = 1.0 - fj1[i];
                    //fj0[i] = round(fj0[i], 4);
                }
                //Q at first is Fj
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        nodes[i][j].setQ0(fj0[j] * matrix[i][j]); //we  don't need the Q of 0 numbers.
                        nodes[i][j].setQ1(fj1[j] * matrix[i][j]); //we  don't need the Q of 0 numbers.
                    }
                }
                //R:
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        double deltaR = 1;
                        for (int k = 0; k < cols; k++) {
                            if (nodes[i][k].getQ0() != 0 && nodes[i][k].getQ1() != 0 || k != j) {
                                deltaR *= (nodes[i][k].getQ0() - nodes[i][k].getQ1());
                            }
                        }
                        nodes[i][j].setR0(0.5 * (1 + deltaR));
                        nodes[i][j].setR1(0.5 * (1 - deltaR));
                    }
                }
                if(calculateSyndrome(matrix)){
                    System.out.println("Syndrome is full zero and number of iterations is " + round);
                    syndrom = 0;
                    return;
                }else{
                    round++;
                }
            }
            else{
                //next iterations
                for(int i = 0; i < rows; i++){
                    for (int j = 0; j < cols; j++){
                        temp[i][j].setQ0(calculateAlphaJ()[i]*fj0[j]*getR0Multiplication(i));
                        temp[i][j].setQ1(calculateAlphaJ()[i]*fj1[j]*getR1Multiplication(i));
                    }
                }
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        double deltaR = 1;
                        for (int k = 0; k < cols; k++) {
                            if (temp[i][k].getQ0() != 0 && temp[i][k].getQ1() != 0 || k != j) {
                                deltaR *= (temp[i][k].getQ0() - temp[i][k].getQ1());
                            }
                        }
                        temp[i][j].setR0(0.5 * (1 + deltaR));
                        temp[i][j].setR1(0.5 * (1 - deltaR));
                    }
                }
                for(int i = 0; i< rows; i++){
                    for(int j = 0; j< cols;j++){
                        nodes[i][j].setQ0(temp[i][j].getQ0());
                        nodes[i][j].setQ1(temp[i][j].getQ1());
                        nodes[i][j].setR0(temp[i][j].getR0());
                        nodes[i][j].setR1(temp[i][j].getR1());
                    }
                }
                if(calculateSyndrome(matrix)){
                    System.out.println("Syndrome is full zero and number of iterations is " + round);
                    syndrom = 0;
                    return;
                }else{
                    round++;
                }
            }
            System.out.println("iteration number " + round);
            if(round > 4000){
                System.err.println("It is look like an infinite loop, operation stopped...");
                break;
            }
        }

    }

    private int[] calculateDJhat() {
        double[] Qj0 = new double[cols];
        double[] Qj1 = new double[cols];
        double[] alpha = calculateAlphaJ();
        int[] djHat = new int[cols];
        for (int i = 0; i < cols; i++) {
            Qj0[i] = alpha[i] * fj0[i] * getR0Multiplication(i);
            Qj1[i] = alpha[i] * fj1[i] * getR1Multiplication(i);
            //putting 0 or 1 in djHat:
            djHat[i] = getMaximumQj(Qj0[i], Qj1[i]);
            System.out.print(djHat[i] + " ");
        }
        System.out.println();
        return djHat;
    }
    
    private boolean calculateSyndrome(int[][] matrix){
        int[] djHatMatrix = calculateDJhat();
        int[] answer = new int[rows];
        //initialize the answer with full zero matrix
        for(int i=0; i < answer.length; i++){
            answer[i] = 0;
        }
        //calculate multiply
        for(int i = 0; i < rows; i++){
            for(int j = 0; j< cols; j++){
                answer[i] += (matrix[i][j] * djHatMatrix[j]);
                if(answer[i] % 2 == 0){
                    answer[i] = 0;
                }
            }
        }
        
        //check if the answer is a full zero matrix:
        for(int i = 0; i < answer.length; i++){
            if(answer[i] != 0){
                System.err.println("Syndrome is not full zero matrix, so next iteration required!");
                return false; //syndrome is not a full zero matrix
            }
        }
        return true; //syndrome is a full zero matrix
    }

    private double[] calculateAlphaJ() {
        double[] a = new double[cols];
//        double x = 1; //calculate R0 multiplication
//        double y = 1; //calculate R1 multiplication
        for (int i = 0; i < cols; i++) {
//            for (int k = 0; k < rows; k++) {
//                if (nodes[k][i].getR0() != 0 && nodes[k][i].getR1() != 0) {
//                    x *= nodes[k][i].getR0();
//                    y *= nodes[k][i].getR1();
//                }
//            }
            a[i] = 1.0 / (fj0[i] * getR0Multiplication(i) + fj1[i] * getR1Multiplication(i));
//            x = y = 1;
        }
        return a;
    }
    
//    private double[][] calculateAlphaIJ(){
//        double[][] alpha = new double[rows][cols];
//        for(int i = 0; i < rows; i++){
//            for(int j = 0; j < cols; j++){
//                alpha[i][j] = 1.0 / (fj0[i] * getR0Multiplication(i));
//            }
//        }
//        return alpha;
//    }

    private double getR0Multiplication(int i) {
        double x = 1;

        for (int k = 0; k < rows; k++) {
            if (nodes[k][i].getR0() != 0) {
                x *= nodes[k][i].getR0();
            }
        }
        return x;
    }

    private double getR1Multiplication(int i) {
        double y = 1;
        for (int k = 0; k < rows; k++) {
            if (nodes[k][i].getR1() != 0) {
                y *= nodes[k][i].getR1();
            }
        }
        return y;
    }
    
    private int getMaximumQj(double Qj0, double Qj1){
        if(Qj0 > Qj1){
            return 0;
        }
        else{
            return 1;
        }
    }

}
