import ass1.GameEngine;

/**
 * Created with IntelliJ IDEA.
 * User: Andrew
 * Date: 8/09/13
 * Time: 5:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class testComparePointAndLine {
    public static void main(String[] args){
        double condition = GameEngine.comparePointAndLine(new double[]{4,3}, new double[]{5,6,-5,-1});

        System.out.println("condition = " + condition);
    }
}
