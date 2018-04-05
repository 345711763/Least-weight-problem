

import java.util.ArrayList;

public class Matrix
{
    public double[][] matrix;
    public int length,width;   
    public Matrix(double[][] matrix,int length,int width){
        this.matrix=matrix;
        this.length=length;
        this.width=width;
        deletedColumnsNumber = new ArrayList<Integer>();
        
    }
    public ArrayList<Integer> deletedColumnsNumber;
}
