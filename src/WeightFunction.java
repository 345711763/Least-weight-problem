
public class WeightFunction
{
    public int compute(int i,int j){
        int weight=(int) Math.pow(j-i-5, 2);//自定义的w(i,j)函数,w(i,j)是concave 的
        return weight;
    }
}
