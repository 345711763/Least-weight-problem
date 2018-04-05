
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

//
public class Smawk
{
    public Smawk ()
    {

    }


    public void printMatrix(Matrix matrix){
        for(int i=0;i<matrix.length;i++){
           
            for(int j=0;j<matrix.width;j++)
            {
                System.out.printf("%13.2f", matrix.matrix[i][j]);
            }
            System.out.println();
            }
        
    }
    public Matrix reduceMatrix(Matrix matrix){
        Stack s = new Stack();//创建一个堆栈
        int numberOfColumnsCanBeDeleted= matrix.width-matrix.length;
        int numberOfColumnsDeleted=0;
        //从第0列到最后一列，一列一列判断是否要删除
        for(int j=0;j<matrix.width;j++){
            if(numberOfColumnsCanBeDeleted>0){
            //If s is empty, push the Colunm c onto the stack, and mark its first entry i.e., the entry in row 1, as its head
            if(s.isEmpty()){
                Column c =new Column();
                c.index=j;
                c.RowIndexOfHead=0;
                s.push(c);
            }else{
                while(s.isEmpty()==false)
                {
                    Column c= new Column();//c是栈顶元素
                    c=(Column)s.peek();
                    int i=c.RowIndexOfHead;
                    int l=c.index;
                    // Compare M[i,l] and M[i,j]
                    //If M[i,j] ≤ M[i,l], then pop C l off S. C l has now been eliminated. (The loop
                    //        continues, as C j attacks the next column on the stack.)
                    if(matrix.matrix[i][j]<=matrix.matrix[i][l])
                    {
                        if(s.size()!=1){
                        s.pop();
                        matrix.deletedColumnsNumber.add(l);
                        System.out.println("Column "+l+" is deleted");
                        numberOfColumnsCanBeDeleted--;
                        numberOfColumnsDeleted++;
                        continue;
                        }
                        if(s.size()==1){
                            s.pop();
                            matrix.deletedColumnsNumber.add(l);
                            System.out.println("Column "+l+" is deleted");
                            numberOfColumnsCanBeDeleted--;
                            numberOfColumnsDeleted++;
                            Column c1= new Column();
                            c1.index=j;
                            c1.RowIndexOfHead=i;
                            s.push(c1);
                            break;
                        }
                    }
                    // If M[i,j] > M[i,l] and i = n, then C j is defeated and eliminated. Exit the loop.
                    if(matrix.matrix[i][j]>matrix.matrix[i][l] && i==matrix.length-1)
                    {
                        matrix.deletedColumnsNumber.add(j);
                        System.out.println("Column "+j+" is deleted");
                        numberOfColumnsCanBeDeleted--;
                        numberOfColumnsDeleted++;
                        break;
                    }
                    // If M[i,j] > M[i,l] and i < n, then C j is defeated, but still survives. Push C j onto
                    //S, and mark M[i + 1,j] as its head. Exit the loop.
                    if(matrix.matrix[i][j]>matrix.matrix[i][l] && i<matrix.length-1)
                    {
                        Column c2= new Column();
                        c2.index=j;
                        c2.RowIndexOfHead=i+1;
                        s.push(c2);
                        break;
                        
                    }
                }
            }
        }
        }
        
        double[][] modifiedMatrix= new double[matrix.length][matrix.width-numberOfColumnsDeleted];
        for(int j=0;j<s.size();j++){
            for(int i=0;i<matrix.length;i++)
            {
                Column c = new Column();
                c=(Column)s.get(j);
                modifiedMatrix[i][j]= matrix.matrix[i][c.index];
            }
        }
        for(int j=s.size();j<matrix.width-numberOfColumnsDeleted;j++){
            for(int i=0;i<matrix.length;i++)
            {             
                modifiedMatrix[i][j]= matrix.matrix[i][j+numberOfColumnsDeleted];
            }
        }
        matrix.matrix=modifiedMatrix;
        matrix.width=s.size();
        return matrix;
    }

//返回一个矩阵中每行中最小的数的列坐标
public ArrayList<Position> MinCompute(Matrix A){
    ArrayList<Position> positionOfMinima= new ArrayList<Position>();
    printMatrix(A);
    System.out.println("----------------------------------------------------------------------------------------------------- ");
    System.out.println("Reduce matrix , delete columns:");
    Matrix B;
    if(A.length<A.width)
    {    B= reduceMatrix(A);
    }else{
         B= A;
    }
    System.out.println("-----------------------------------------------------------------------------------------------------");
    System.out.println("Matrix after reducing");
    System.out.println();
    printMatrix(B);
    System.out.println("-----------------------------------------------------------------------------------------------------");
    Position position = new Position();
    
 // 若如果B只有一行一列则直接输出最小值的位置
     if(B.length==1) 
     {
         position.n=0;
         position.m=0;
         Collections.sort(B.deletedColumnsNumber);
         for(int i=0;i<B.deletedColumnsNumber.size();i++)
         {
             if(B.deletedColumnsNumber.get(i)==i){
                 position.m++;
             }
         }
      
         positionOfMinima.add(position);
         return positionOfMinima;
     }
  //如果B有多行多列，则删除奇数行
     System.out.println("delete odd rows and then get a new matrix:");
     System.out.println();
     if(B.length%2==0){
         Matrix C = new Matrix(new double[B.length/2][B.length],B.length/2,B.length);
         //将从B中删除奇数行后得到的新矩阵保存在C里
         for(int i=0;i<B.length/2;i++)
             for(int j=0;j<B.length;j++)
             {
                 C.matrix[i][j]= B.matrix[i*2+1][j];
             }
         //迭代调用MaxCompute()方法
         positionOfMinima=MinCompute(C);
         for(int i=0;i<positionOfMinima.size();i++)
         {
             positionOfMinima.get(i).n=(positionOfMinima.get(i).n)*2+1;
         }
         //排序 sort positionsOfMaxima 让行数小的position出现在前面
         Collections.sort(positionOfMinima,new Comparator<Position>(){
             public int compare(Position p1, Position p2){
                 return Integer.valueOf(p1.n).compareTo(p2.n);
             }
         });
         //求B中奇数行的最小数的位置
         int count=0;
         for(int i=0;i<B.length;i=i+2){
             Position p= new Position();
               if(i==0){
                   double minima = B.matrix[i][0];
                   for(int j=0;j<=positionOfMinima.get(0).m;j++){
                       if(B.matrix[i][j]<minima)
                       {
                           minima=B.matrix[i][j];
                           p.n=i;
                           p.m=j;
                       }
                   }
                   count++;
               }else{
                   double minima=B.matrix[i][positionOfMinima.get(count-1).m];
                   p.n=i;
                   p.m=positionOfMinima.get(count-1).m;
                   for(int j=positionOfMinima.get(count-1).m;j<=positionOfMinima.get(count).m;j++){
                       if(B.matrix[i][j]<minima)
                       {
                           minima=B.matrix[i][j];
                           p.n=i;
                           p.m=j;
                       }
                   }
                   count++;
               }
               positionOfMinima.add(p);
         }
     }else{
        
         Matrix C = new Matrix(new double[B.length/2][B.length],B.length/2,B.length);
         for(int i=0;i<B.length/2;i++)
             for(int j=0;j<B.length;j++){
                 C.matrix[i][j]=B.matrix[i*2+1][j];
             }
        positionOfMinima=MinCompute(C);
         for(int i=0;i<positionOfMinima.size();i++)
         {
             positionOfMinima.get(i).n=(positionOfMinima.get(i).n)*2+1;
         }
         //排序 sort positionsOfMaxima 让行数小的position出现在前面
         Collections.sort(positionOfMinima,new Comparator<Position>(){
             public int compare(Position p1, Position p2){
                 return Integer.valueOf(p1.n).compareTo(p2.n);
             }
         });
       //求B中奇数行的最大数的位置
         int size= positionOfMinima.size();
         int count=0;
         for(int i=0;i<B.length;i=i+2){
             Position p= new Position();
               if(i==0){               
                   double minima=B.matrix[i][0];
                   p.n=i;
                   p.m=0;
                   for(int j=0;j<=positionOfMinima.get(0).m;j++){
                       if(B.matrix[i][j]<minima)
                       {
                           minima=B.matrix[i][j];
                           p.n=i;
                           p.m=j;
                       }
                   }
                   count++;
               }else if(i==B.length-1){
                   double minima=B.matrix[i][positionOfMinima.get(size-1).m];
                   p.n=i;
                   p.m=positionOfMinima.get(size-1).m;
                for(int j=positionOfMinima.get(size-1).m;j<B.width;j++){
                    if(B.matrix[i][j]<minima)
                    {
                        minima=B.matrix[i][j];
                        p.n=i;
                        p.m=j;
                    }
                }
               }else
               {
                   double minima=B.matrix[i][positionOfMinima.get(count-1).m];
                   p.n=i;
                   p.m=positionOfMinima.get(count-1).m;
                   for(int j=positionOfMinima.get(count-1).m;j<=positionOfMinima.get(count).m;j++){
                       if(B.matrix[i][j]<minima)
                       {
                           minima=B.matrix[i][j];
                           p.n=i;
                           p.m=j;
                       }
                   }
                   count++;
               }
               positionOfMinima.add(p);
         }

     }
   //排序 sort positionsOfMaxima 让行数小的position出现在前面
     Collections.sort(positionOfMinima,new Comparator<Position>(){
         public int compare(Position p1, Position p2){
             return Integer.valueOf(p1.n).compareTo(p2.n);
         }
     });
  
     for(int i=0;i<positionOfMinima.size();i++)
     {
         for(int j=0;j<B.deletedColumnsNumber.size();j++){
             if(B.deletedColumnsNumber.get(j)<=positionOfMinima.get(i).m)
                 positionOfMinima.get(i).m++;
         }
     }
     
     return positionOfMinima;
     
}

   public static void main(String args[]){
       double[][] test = new double[9][18];
        test[0][0]=25;
        test[0][1]=21;
        test[0][2]=13;
        test[0][3]=10;
        test[0][4]=20;
        test[0][5]=13;
        test[0][6]=19;
        test[0][7]=35;
        test[0][8]=37;
        test[0][9]=41;
        test[0][10]=58;
        test[0][11]=66;
        test[0][12]=82;
        test[0][13]=99;
        test[0][14]=124;
        test[0][15]=133;
        test[0][16]=156;
        test[0][17]=178;
        
        test[1][0]=42;
        test[1][1]=35;
        test[1][2]=26;
        test[1][3]=20;
        test[1][4]=29;
        test[1][5]=21;
        test[1][6]=25;
        test[1][7]=37;
        test[1][8]=36; 
        test[1][9]=39;
        test[1][10]=56;
        test[1][11]=64;
        test[1][12]=76;
        test[1][13]=91;
        test[1][14]=116;
        test[1][15]=125;
        test[1][16]=146;
        test[1][17]=164;
    
        test[2][0]=57;
        test[2][1]=48;
        test[2][2]=35;
        test[2][3]=28;
        test[2][4]=33;
        test[2][5]=24;
        test[2][6]=28;
        test[2][7]=40;
        test[2][8]=37;
        test[2][9]=37;
        test[2][10]=54;
        test[2][11]=61;
        test[2][12]=72;
        test[2][13]=83;
        test[2][14]=107;
        test[2][15]=113;
        test[2][16]=131;
        test[2][17]=146;
        
        test[3][0]=78;
        test[3][1]=65;
        test[3][2]=51;
        test[3][3]=42;
        test[3][4]=44;
        test[3][5]=35;
        test[3][6]=38;
        test[3][7]=48;
        test[3][8]=42;
        test[3][9]=42;
        test[3][10]=55;
        test[3][11]=61;
        test[3][12]=70;
        test[3][13]=80;
        test[3][14]=100;
        test[3][15]=106;
        test[3][16]=120;
        test[3][17]=135;
        
        test[4][0]=90;
        test[4][1]=76;
        test[4][2]=58;
        test[4][3]=48;
        test[4][4]=49;
        test[4][5]=39;
        test[4][6]=42;
        test[4][7]=48;
        test[4][8]=39;
        test[4][9]=35;
        test[4][10]=47;
        test[4][11]=51;
        test[4][12]=56;
        test[4][13]=63;
        test[4][14]=80;
        test[4][15]=86;
        test[4][16]=97;
        test[4][17]=110;
        
        test[5][0]=103;
        test[5][1]=85;
        test[5][2]=67;
        test[5][3]=56;
        test[5][4]=55;
        test[5][5]=44;
        test[5][6]=44;
        test[5][7]=49;
        test[5][8]=39;
        test[5][9]=33;
        test[5][10]=41;
        test[5][11]=44;
        test[5][12]=49;
        test[5][13]=56;
        test[5][14]=71;
        test[5][15]=75;
        test[5][16]=84;
        test[5][17]=96;
        
        test[6][0]=123;
        test[6][1]=105;
        test[6][2]=86;
        test[6][3]=75;
        test[6][4]=73;
        test[6][5]=59;
        test[6][6]=57;
        test[6][7]=62;
        test[6][8]=51;
        test[6][9]=44;
        test[6][10]=50;
        test[6][11]=52;
        test[6][12]=55;
        test[6][13]=59;
        test[6][14]=72;
        test[6][15]=74;
        test[6][16]=80;
        test[6][17]=92;
        
        test[7][0]=142;
        test[7][1]=123;
        test[7][2]=100;
        test[7][3]=86;
        test[7][4]=82;
        test[7][5]=65;
        test[7][6]=61;
        test[7][7]=62;
        test[7][8]=50;
        test[7][9]=43;
        test[7][10]=47;
        test[7][11]=45;
        test[7][12]=46;
        test[7][13]=46;
        test[7][14]=58;
        test[7][15]=59;
        test[7][16]=65;
        test[7][17]=73;
        
        test[8][0]=151;
        test[8][1]=130;
        test[8][2]=104;
        test[8][3]=88;
        test[8][4]=80;
        test[8][5]=59;
        test[8][6]=52;
        test[8][7]=49;
        test[8][8]=37;
        test[8][9]=29;
        test[8][10]=29;
        test[8][11]=24;
        test[8][12]=23;
        test[8][13]=20;
        test[8][14]=28;
        test[8][15]=25;
        test[8][16]=31;
        test[8][17]=39;
        
        Smawk s = new Smawk();
        Matrix matrix= new Matrix(test,9,18);
        Matrix matrix2= new Matrix(test,9,18);
  
       
        //s.printMatrix( s.reduceMatrix(matrix));
        ArrayList<Position> positionsOfMinima=s.MinCompute(matrix);
        for(int i=0;i<positionsOfMinima.size();i++){
            System.out.println("["+positionsOfMinima.get(i).n+"]["+positionsOfMinima.get(i).m+"]");
        }
       /* for(int i=0;i<positionsOfMaxima.size();i++){
            if(positionsOfMaxima.get(i).m<matrix.length)
                System.out.println("The farthest neighbor of P"+i+" is :P"+positionsOfMaxima.get(i).m+"  the distance is "+matrix2.matrix[positionsOfMaxima.get(i).n][positionsOfMaxima.get(i).m]);
            if(positionsOfMaxima.get(i).m>=matrix.length)
                System.out.println("The farthest neighbor of P"+i+" is :P"+(positionsOfMaxima.get(i).m-matrix.length)+"  the distance is "+matrix2.matrix[positionsOfMaxima.get(i).n][positionsOfMaxima.get(i).m]);
        }*/                                                                                                                                                                                                                                                                    
       
    }
}
