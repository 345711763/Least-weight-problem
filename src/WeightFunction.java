
public class WeightFunction
{
    public int compute(int i,int j){
        int weight=(int) Math.pow(j-i-5, 2);//�Զ����w(i,j)����,w(i,j)��concave ��
        return weight;
    }
}
