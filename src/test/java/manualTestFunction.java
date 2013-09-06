import ass1.MathUtil;

/**
 * Created with IntelliJ IDEA.
 * User: Andrew2012
 * Date: 28/08/13
 * Time: 4:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class manualTestFunction {
    public static void main(String[] args){
        double[][] matrix = MathUtil.identityMatrix(3);

        printMatrix(matrix);
    }

    public static void printMatrix(double[][] matrix){
        for(int i = 0; i < 3; i++){
            System.out.print("{");
            for(int j = 0; j < 3; j++){
                System.out.print(" " + matrix[i][j] + " ");
            }
            System.out.println("}");
        }
    }
}
