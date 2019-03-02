/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codingalgorithm;

/**
 *
 * @author hadin
 */
public class ParityObject {
    private double R0;
    private double R1;
    private double Q0;
    private double Q1;
    private int rowId;
    private int colId;
    public ParityObject(int row, int col){
        rowId = row;
        colId = col;
    }

    public void setR0(double R0) {
        this.R0 = R0;
    }

    public void setR1(double R1) {
        this.R1 = R1;
    }

    public void setQ0(double Q0) {
        this.Q0 = Q0;
    }

    public void setQ1(double Q1) {
        this.Q1 = Q1;
    }

    public double getR0() {
        return R0;
    }

    public double getR1() {
        return R1;
    }

    public double getQ0() {
        return Q0;
    }

    public double getQ1() {
        return Q1;
    }

    public int getRowId() {
        return rowId;
    }

    public int getColId() {
        return colId;
    }
    
}
