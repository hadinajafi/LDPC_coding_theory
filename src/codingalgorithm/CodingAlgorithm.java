/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codingalgorithm;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.ejml.simple.SimpleMatrix;
import java.util.Scanner;
/**
 *
 * @author hadin
 */
public class CodingAlgorithm {
    
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        Random rand = new Random();
//        SimpleMatrix matrix = SimpleMatrix.random_DDRM(12, 8, 0, 2, rand);
//        for(int i = 0; i < matrix.numRows(); i++){
//            for(int j = 0; j < matrix.numCols(); j++){
//                int value = (int) matrix.get(i, j);
//                //Math.floor(value);
//                matrix.set(i, j, value);
//                System.out.print(matrix.get(i, j) + "\t");
//            }
//            System.out.println("");
//        }
        int[][] sample = {{0,1,0,1,0,1,1,1,0,0,0,1}, {1,0,1,1,0,0,0,0,1,0,0,0}, {0,1,0,0,1,0,1,0,0,0,0,1}, {1,0,0,1,0,0,0,0,0,1,1,0}, 
            {0,0,1,0,1,1,0,0,0,1,0,0}, {1,0,1,0,0,0,1,1,0,0,1,0}, {0,1,0,0,0,1,0,1,1,1,0,0}, {0,0,0,0,1,0,0,0,1,0,1,1}};
        int rows = 8, columns = 12;
        for(int i = 0; i <8; i++){
            for(int j = 0; j<12; j++){
                System.out.print(sample[i][j] + "\t");
            }
            System.out.println();
        }
        
        Scanner input = new Scanner(System.in);
        System.out.println("Do you want to input custom matrix? (y/n)");
        if(input.next().equals("y")){
            System.out.println("Enter number of rows:");
            rows = input.nextInt();
            System.out.println("Enter number of columns: ");
            columns = input.nextInt();
            System.out.println("Now Enter entire matrix:");
            sample = new int[rows][columns];
            for(int i = 0; i < rows; i++){
                for(int j = 0; j<columns;j++){
                    sample[i][j] = input.nextInt();
                }
            }
        }
        double[] rij = {1.31, 2.65, 0.74, 2.17, 0.59, -0.83, -0.39, -1.75, 1.49, 0.408, -0.929, 1.076};
        System.out.println("Enter constant Sigma:");
        double sigma = input.nextDouble();
        System.out.println("Do you want Enter Recieved vector code? (y/n)");
        if(input.next().equals("y")){
            System.out.println("You must enter " + columns + " double numbers for recieved vector.");
            System.out.println("Enter Recieved vector now: ");
            rij = new double[columns];
            for(int i = 0; i < rij.length; i++){
                rij[i] = input.nextDouble();
            }
        }
//        int[] m = new int[sample.length];
//        for(int i = 0; i < m.length; i++){
//            m[i] = input.nextInt();
//        }
        int[] m = {1,0,0,0};
        
        SumProductAlgorithm algorithm = new SumProductAlgorithm(sigma, rij, m, columns, rows, sample);
    }
    
}
