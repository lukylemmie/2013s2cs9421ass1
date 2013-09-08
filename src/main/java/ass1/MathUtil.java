package ass1;

/**
 * A collection of useful math methods
 * @author AndrewLem
 */
public class MathUtil {

    /**
     * Normalise an angle to the range (-180, 180]
     * 
     * @param angle 
     * @return
     */
    static public double normaliseAngle(double angle) {
        return ((angle + 180.0) % 360.0 + 360.0) % 360.0 - 180.0;
    }

    /**
     * Clamp a value to the given range
     * 
     * @param value
     * @param min
     * @param max
     * @return
     */

    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
    
    /**
     * Multiply two matrices
     * 
     * @param p A 3x3 matrix
     * @param q A 3x3 matrix
     * @return
     */
    public static double[][] multiply(double[][] p, double[][] q) {

        double[][] m = new double[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                m[i][j] = 0;
                for (int k = 0; k < 3; k++) {
                   m[i][j] += p[i][k] * q[k][j]; 
                }
            }
        }

        return m;
    }

    /**
     * Multiply a vector by a matrix
     * 
     * @param m A 3x3 matrix
     * @param v A 3x1 vector
     * @return
     */
    public static double[] multiply(double[][] m, double[] v) {

        double[] u = new double[3];

        for (int i = 0; i < 3; i++) {
            u[i] = 0;
            for (int j = 0; j < 3; j++) {
                u[i] += m[i][j] * v[j];
            }
        }

        return u;
    }



    // ===========================================
    // COMPLETE THE METHODS BELOW
    // ===========================================

    public static double[][] identityMatrix(int size){
        double matrix[][] = new double [size][size];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(i == j){
                    matrix[i][j] = 1;
                } else {
                    matrix[i][j] = 0;
                }
            }
        }

        return matrix;
    }

    /**
     * A 2D translation matrix for the given offset vector
     * 
     * @param v
     * @return
     */
    public static double[][] translationMatrix(double[] v) {
        double matrix[][] = identityMatrix(3);

        matrix[0][2] = v[0];
        matrix[1][2] = v[1];

        return matrix;
    }

    public static double[][] reverseTranslationMatrix(double[] v){
        v[0] = -v[0];
        v[1] = -v[1];

        return translationMatrix(v);
    }

    public static double[] positionFromMatrix(double[][] matrix){
        double p[] = new double[2];

        p[0] = matrix[0][2];
        p[1] = matrix[1][2];

        return p;
    }

    /**
     * A 2D rotation matrix for the given angle
     * 
     * @param angle
     * @return
     */
    public static double[][] rotationMatrix(double angle) {
        double matrix[][] = identityMatrix(3);

        double cosAngle = Math.cos(Math.toRadians(angle));
        double sinAngle = Math.sin(Math.toRadians(angle));

        matrix[0][0] = cosAngle;
        matrix[1][0] = sinAngle;
        matrix[0][1] = -sinAngle;
        matrix[1][1] = cosAngle;

        return matrix;
    }

    public static double[][] reverseRotationMatrix(double angle){
        return rotationMatrix(-angle);
    }

    /**
     * A 2D scale matrix that scales both axes by the same factor
     * 
     * @param scale
     * @return
     */
    public static double[][] scaleMatrix(double scale) {
        double matrix[][] = identityMatrix(3);

        matrix[0][0] = scale;
        matrix[1][1] = scale;

        return matrix;
    }

    public static double[][] reverseScaleMatrix(double scale){
        return scaleMatrix(1/scale);
    }


    // returns -1 if on left, 0 if on line, 1 if on right
    public static double comparePointAndLine(double[] point, double[] line){
        double condition = -2;

        if(line[3] > line[1])
            condition = (line[3] - line[1])*(point[0] - line[0]) - (line[2] - line[0])*(point[1] - line[1]) ;
        else
            condition = (line[1] - line[3])*(point[0] - line[2]) - (line[0] - line[2])*(point[1] - line[3]) ;

        return condition;
    }

    public static void printMatrix(double[][] matrix){
        for(int i = 0; i < matrix.length; i++){
            System.out.print("{");
            for(int j = 0; j < matrix[i].length; j++){
                System.out.print(matrix[i][j] + ",");
            }
            System.out.println("}");
        }
    }

    public static void printVector(double[] vector){
        System.out.print("{");
        for(int j = 0; j < vector.length; j++){
            System.out.print(vector[j] + ",");
        }
        System.out.println("}");
    }

    public static double cleanNumberTo10dp(double x){
        x = Math.round(x * (1e10)) / 1e10;
        return x;
    }
}
