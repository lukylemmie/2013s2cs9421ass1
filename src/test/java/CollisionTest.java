/**
 * Created with IntelliJ IDEA.
 * User: Andrew
 * Date: 8/09/13
 * Time: 8:19 PM
 * To change this template use File | Settings | File Templates.
 */

import ass1.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * User: Pierzchalski
 * Date: 04/09/13
 * Package: ass1.tests
 * Project: COMP3421_Assignment1
 */
public class CollisionTest {
    private static final double[] SQUARE_VERTICES = new double[]
            {
                    0, 0,
                    1, 0,
                    1, 1,
                    0, 1};

    private static final double[] TRIANGLE_VERTICES = new double[]
            {
                    0, 0,
                    1, 0,
                    0.5, Math.sqrt(3) / 2.0};

    private static final double[] RED = new double[]{1f, 0f, 0f, 1f};

    @Test
    public void collideSingleShape() {
        Camera camera = new Camera();
        GameEngine gameEngine = new GameEngine(camera);

        PolygonalGameObject square = new PolygonalGameObject(GameObject.ROOT, SQUARE_VERTICES, RED, RED);

        System.out.println("Testing Single Shape Collision");

        expectCollisionWith(gameEngine, .5, .5, square, "square001");
        expectCollisionWith(gameEngine, 0, 0, square, "square002");
        expectCollisionWith(gameEngine, 0, .5, square, "square003");
        expectCollisionWith(gameEngine, .5, 0, square, "square004");

        expectMissWith(gameEngine, -1, .5, square, "square005");

        square.translate(1, 1);

        expectMissWith(gameEngine, -1, 1.5, square, "square006");

        expectCollisionWith(gameEngine, 1, 1, square, "square007");
        expectCollisionWith(gameEngine, 1.5, 1.5, square, "square008");
        expectCollisionWith(gameEngine, 1.5, 2, square, "square009");

        expectMissWith(gameEngine, 2.5, 1.5, square, "square010");

        square.rotate(45);
        square.scale(1d / Math.sqrt(2));

        expectCollisionWith(gameEngine, 1, 1, square, "square011");
        expectCollisionWith(gameEngine, 1, 1.5, square, "square012");
        expectCollisionWith(gameEngine, 1.25, 1.25, square, "square013");
        expectCollisionWith(gameEngine, 1.25, 1.75, square, "square014");
        expectCollisionWith(gameEngine, 0.75, 1.75, square, "square015");
        expectCollisionWith(gameEngine, 0.75, 1.25, square, "square016");

        expectMissWith(gameEngine, 0, 1.2, square, "square017");
        expectMissWith(gameEngine, 2, 1.2, square, "square018");
    }

    @Test
    public void parentTransformations() {
        Camera camera = new Camera();
        GameEngine gameEngine = new GameEngine(camera);

        GameObject pivot = new GameObject(GameObject.ROOT);
        PolygonalGameObject square = new PolygonalGameObject(pivot, SQUARE_VERTICES, RED, RED);
        pivot.translate(1, 1);
        square.translate(0, 2);

        System.out.println("Testing Parent Transformation Collision");

        expectCollisionWith(gameEngine, 1, 3, square, "square019");
        expectCollisionWith(gameEngine, 2, 4, square, "square020");
        expectCollisionWith(gameEngine, 1.5, 3.5, square, "square021");
        expectCollisionWith(gameEngine, 1.5, 3, square, "square022");
        expectCollisionWith(gameEngine, 2, 3.5, square, "square023");

        expectMissWith(gameEngine, 2.5, 3.5, square, "square024");

        pivot.rotate(90);
        pivot.scale(2);

        expectCollisionWith(gameEngine, -5, 2, square, "square025");
        expectCollisionWith(gameEngine, -4, 2, square, "square026");
        expectCollisionWith(gameEngine, -4, 3, square, "square027");

        square.translate(-0.5, 0);
        square.rotate(-45);
        square.scale(1d / Math.sqrt(2));

        Assert.assertEquals(square.getGlobalPosition()[0], -3, 1e-10);
        Assert.assertEquals(square.getGlobalPosition()[1], 0, 1e-10);
        Assert.assertEquals(square.getGlobalRotation(), 45, 1e-10);
        Assert.assertEquals(square.getGlobalScale(), Math.sqrt(2), 1e-10);

        //System.out.println(coordinateList2String(square.getGlobalPoints()));

        expectCollisionWith(gameEngine, -3, 0.5, square, "square028");
        expectCollisionWith(gameEngine, -3, 2, square, "square029");
        expectCollisionWith(gameEngine, -2, 1, square, "square030");
        expectCollisionWith(gameEngine, -3.99, 1, square, "square031");
        expectCollisionWith(gameEngine, -4, 1, square, "square032");
        expectCollisionWith(gameEngine, -2.5, 0.5, square, "square033");
        expectCollisionWith(gameEngine, -3.5, 0.5, square, "square034");
        expectCollisionWith(gameEngine, -3, 0, square, "square035");
        expectMissWith(gameEngine, -3, -0.01, square, "square036");
    }

    @Test
    public void multipleShapes() {
        Camera camera = new Camera();
        GameEngine gameEngine = new GameEngine(camera);

        GameObject pivot = new GameObject(GameObject.ROOT);
        PolygonalGameObject square = new PolygonalGameObject(pivot, SQUARE_VERTICES, RED, RED);
        PolygonalGameObject triangle = new PolygonalGameObject(square, TRIANGLE_VERTICES, RED, RED);

        System.out.println("Testing Multiple Shapes Collision");

        expectCollisionWith(gameEngine, 0, 0, square, "square037");
        expectCollisionWith(gameEngine, 0, 0, square, "triangle038");
        expectCollisionWith(gameEngine, 0.5, 0, square, "square039");
        expectCollisionWith(gameEngine, 0.5, 0, square, "triangle040");
        expectCollisionWith(gameEngine, 0.5, 0.5, square, "square041");
        expectCollisionWith(gameEngine, 0.5, 0.5, square, "triangle042");
        expectCollisionWith(gameEngine, 0.25, 0.75, square, "square043");
        expectMissWith(gameEngine, 0.25, 0.75, square, "triangle044");

        triangle.rotate(-60);

        expectCollisionWith(gameEngine, 0.5, 0, square, "square045");
        expectCollisionWith(gameEngine, 0.5, 0, square, "triangle046");
        expectCollisionWith(gameEngine, 0.5, -0.5, square, "triangle047");
        expectMissWith(gameEngine, 0.5, -0.5, square, "square048");
    }

    private static void expectCollisionWith(GameEngine gameEngine,
                                            double testPointX,
                                            double testPointY,
                                            PolygonalGameObject polygonalGameObject,
                                            String object) {
        double[] testPoint = new double[2];
        System.out.println("Testing: Collision - point: (" + testPointX + ", " + testPointY + "); object: " + object);
        testPoint[0] = testPointX;
        testPoint[1] = testPointY;
        List<GameObject> collisions = gameEngine.collision(testPoint);
        Assert.assertTrue(!collisions.isEmpty());
        Assert.assertTrue(collisions.contains(polygonalGameObject));
    }

    private static void expectMissWith(GameEngine gameEngine,
                                       double testPointX,
                                       double testPointY,
                                       PolygonalGameObject polygonalGameObject,
                                       String object) {
        double[] testPoint = new double[2];
        System.out.println("Testing: No collision - point: (" + testPointX + ", " + testPointY + "); object: " + object);
        testPoint[0] = testPointX;
        testPoint[1] = testPointY;
        List<GameObject> collisions = gameEngine.collision(testPoint);
        Assert.assertTrue(!collisions.contains(polygonalGameObject));
    }
}
