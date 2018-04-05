import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;



public class Lws
{
  
    public int n;
    public ArrayList<Integer> lws=new ArrayList<Integer>();//lowest weight sequence
    public int[] lw;
    public WeightFunction wf= new WeightFunction();
    public int[] MinimaPositions;//链表中下标为i的元素是下标为i的列的最小值的坐标
    public Lws(int n)
    {
        this.n=n;
    }
    private int ComputeMaximumOfWeightFunction(){
        int maximum= wf.compute(0,1);
        for(int i=0;i<n;i++){
            for(int j=i+1;j<=n;j++){
             if(wf.compute(i, j)>maximum)
                 maximum=wf.compute(i, j);
            }                
        }
        return maximum;
    }
    public void computeLWS(){
        MinimaPositions=new int[n];
        int W= 1+n*ComputeMaximumOfWeightFunction();//计算出W
        //初始化一个n*n的空矩阵
        double[][] m = new double[n][n];
        Matrix matrix =new Matrix(m,n,n);
        //将矩阵对角线以下部分全部赋值为W
        for(int i=1;i<n;i++)
            for(int j=0;j<i;j++)
                matrix.matrix[i][j]=W;
        //初始化F[O]=c=r= 0.
        int[] F= new int[n+1];
        int[] H= new int[n+1];
        F[0]=0;
        int c=0,r=0;
        while(c<n)
        {
       
            //Step#1
            int p= Math.min((2*c-r), n-1);
            int j_0=p+1;
            //Step#2
            //行列互换子矩阵G[r,c,c,p] 生成一个新矩阵 reversedMatrix
            double[][] m1=new double[p-c+1][c-r+1];
            Matrix reversedMatrix= new Matrix(m1,p-c+1,c-r+1);
            Matrix copyOfReversedMatrix =new Matrix(m1,p-c+1,c-r+1);
            for(int i=0;i<reversedMatrix.length;i++)
            {
                for(int j=0;j<reversedMatrix.width;j++)
                {
                    //若i+1<=j aij=W 否则aij=g(r+j,c+i)=f(r+j)+w(r+j,c+i)
                    //if(i+1<=j)
                    //{
                    //    reversedMatrix.matrix[i][j]=W;
                  //  }else
                    //{
                        reversedMatrix.matrix[i][j]=F[r+j]+wf.compute(r+j, c+i+1);
                   // }
                }
            }
            for(int i=0;i<copyOfReversedMatrix.length;i++)
            {
                for(int j=0;j<copyOfReversedMatrix.width;j++)
                {
                    //若i+1<=j aij=W 否则aij=g(r+j,c+i)=f(r+j)+w(r+j,c+i)
                    //if(i+1<=j)
                    //{
                    //    reversedMatrix.matrix[i][j]=W;
                  //  }else
                    //{
                    copyOfReversedMatrix.matrix[i][j]=F[r+j]+wf.compute(r+j, c+i+1);
                   // }
                }
            }
            Smawk s=new Smawk();
            ArrayList<Position> positionsOfMinima=s.MinCompute(copyOfReversedMatrix);
            int count=0;
            for(int j=c;j<=p;j++){
                MinimaPositions[j]=positionsOfMinima.get(count).m;
                F[j+1]=(int)reversedMatrix.matrix[positionsOfMinima.get(count).n][positionsOfMinima.get(count).m];
                count++;
            }
            
            //Step 3
            //先判断有没有进行step3的必要
            if(p>=c+1)
            {
                double[][] m2= new double[p-c][p-c];
             Matrix reversedMatrix2= new Matrix(m2,p-c,p-c);
             Matrix copyOfreversedMatrix2= new Matrix(m2,p-c,p-c);
             for(int i=0;i<copyOfreversedMatrix2.length;i++)
             {
                 for(int j=0;j<copyOfreversedMatrix2.width;j++)
                 {
                     //若i+1<=j aij=W 否则aij=g(j+1+c,i+2+c)=f(j+1+c)+w(j+1+c,i+2+c)
                     if(j>i)
                    {
                         copyOfreversedMatrix2.matrix[i][j]=W;
                     }else
                     {
                         copyOfreversedMatrix2.matrix[i][j]=F[j+1+c]+wf.compute(j+1+c, i+2+c);
                     }
                 }
             }
             for(int i=0;i<reversedMatrix2.length;i++)
             {
                 for(int j=0;j<reversedMatrix2.width;j++)
                 {
                     //若i+1<=j aij=W 否则aij=g(j+1+c,i+2+c)=f(j+1+c)+w(j+1+c,i+2+c)
                     if(j>i)
                    {
                         reversedMatrix2.matrix[i][j]=W;
                     }else
                     {
                         reversedMatrix2.matrix[i][j]=F[j+1+c]+wf.compute(j+1+c, i+2+c);
                     }
                 }
             }
             positionsOfMinima=s.MinCompute(copyOfreversedMatrix2);
             int count2=0;
             for(int j=c+1;j<=p;j++){
                 H[j+1]=(int)reversedMatrix2.matrix[positionsOfMinima.get(count2).n][positionsOfMinima.get(count2).m];
                 count2++;
             }
             
            
            //step 4
            
            for(int j=c+1;j<=p;j++){
                if(H[j+1]<F[j+1])
                {
                    j_0=j;
                    MinimaPositions[j]=positionsOfMinima.get(count2-1).m+c+1;                    
                    break;
                }
            }
            }
            //step5
            if(j_0==p+1)
            {
                c=p+1;
            }else{
                F[j_0+1]=H[j_0+1];
                r=c+1;
                c=j_0+1;
            }
        }
        lw=F;
    }
    private void computeSubSequence(int a){
        int row=MinimaPositions[a-1];
        int column=a-1;
        lws.add(row);
        if(row!=0){
            computeSubSequence(row);
        }
    }
    public void computeSequence(){
       lws.add(n);
       computeSubSequence(n);
       Collections.sort(lws);
    }
    public static void main(String arg[]){
        Lws lws =new Lws(1);
        long t1=System.currentTimeMillis();
        lws.computeLWS();
        lws.computeSequence();
        System.out.print("lowest weight sequence :");
        for(int i=0;i<lws.lws.size();i++){
            
        
        System.out.print(lws.lws.get(i)+",");
        }
        System.out.println();
        System.out.println("lowest weight : "+lws.lw[lws.n]);
        long t2=System.currentTimeMillis();
        System.out.println("Run time : "+(t2-t1)+"ms");
    }
}
