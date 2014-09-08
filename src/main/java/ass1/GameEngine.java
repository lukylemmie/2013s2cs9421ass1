package ass1;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import java.util.ArrayList;
import java.util.List;

/**
 * The ass1.GameEngine is the GLEventListener for our game.
 * 
 * Every object in the scene tree is updated on each display call.
 * Then the scene tree is rendered.
 *
 * You shouldn't need to modify this class.
 *
 * @author malcolmr
 */
public class GameEngine implements GLEventListener {

    private Camera myCamera;
    private long myTime;

    private static boolean DEBUG = false;

    /**
     * Construct a new game engine.
     *
     * @param camera The camera that is used in the scene.
     */
    public GameEngine(Camera camera) {
        myCamera = camera;
    }
    
    /**
     * @see javax.media.opengl.GLEventListener#init(javax.media.opengl.GLAutoDrawable)
     */
    @Override
    public void init(GLAutoDrawable drawable) {
        // initialise myTime
        myTime = System.currentTimeMillis();
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        // ignore
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width,
            int height) {
        
        // tell the camera and the mouse that the screen has reshaped
        GL2 gl = drawable.getGL().getGL2();

        myCamera.reshape(gl, x, y, width, height);
        
        // this has to happen after myCamera.reshape() to use the new projection
        Mouse.theMouse.reshape(gl);
    }


    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        // set the view matrix based on the camera position
        myCamera.setView(gl); 
        
        // update the mouse position
        Mouse.theMouse.update(gl);
        
        // update the objects
        update();

        // draw the scene tree
        GameObject.ROOT.draw(gl);        
    }

    private void update() {
        
        // compute the time since the last frame
        long time = System.currentTimeMillis();
        double dt = (time - myTime) / 1000.0;
        myTime = time;
        
        // take a copy of the ALL_OBJECTS list to avoid errors 
        // if new objects are created in the update
        List<GameObject> objects = new ArrayList<GameObject>(GameObject.ALL_OBJECTS);
        
        // update all objects
        for (GameObject g : objects) {
            g.update(dt);
        }        
    }

    public List<GameObject> collision(double[] p){
        ArrayList<GameObject> objectList = new ArrayList<GameObject>();

        if(DEBUG){
            System.out.println("Checking for collisions");
        }
        int currentObjectCount = 0;

        for(GameObject object : GameObject.ALL_OBJECTS){
            if(DEBUG){
                currentObjectCount++;
                System.out.println("Processed " + currentObjectCount + " out of " + GameObject.ALL_OBJECTS.size() + " objects");
            }

            if(object instanceof PolygonalGameObject){
                PolygonalGameObject polygon = (PolygonalGameObject) object;
                PointAndPolygon temp = new PointAndPolygon(p, polygon.getGlobalPoints());
                if(temp.isPointInPolygon()){
                    objectList.add(object);
                }
            }
        }

        return objectList;
    }

    private class PointAndPolygon{
        private double[] point;
        private double[] polygon;
        private boolean pointInPolygon = false;

        private PointAndPolygon(double[] point, double[] polygon){
            int count;

            this.point = point;
            point[0] = MathUtil.cleanNumberTo10dp(point[0]);
            point[1] = MathUtil.cleanNumberTo10dp(point[1]);

            this.polygon = polygon;

            count = countLinesOnRight();

            if(!pointInPolygon){
                if(count % 2 == 1){
                    pointInPolygon = true;
                }
            }

            if(DEBUG){
                System.out.println("point: (" + point[0] + "," + point[1] + ")");
                printPointsList(polygon);
                System.out.println("count: " + count + " & pointInPolygon: " + pointInPolygon);
            }
        }

        private boolean isPointInPolygon() {
            return pointInPolygon;
        }

        int countLinesOnRight(){
            int count = 0;
            double ax, ay, bx, by;
            int ibx, iby;

            for(int i = 0; i < polygon.length && !pointInPolygon; i += 2){
                ibx = (i + 2) % polygon.length;
                iby = (i + 3) % polygon.length;
                ax = MathUtil.cleanNumberTo10dp(polygon[i]);
                ay = MathUtil.cleanNumberTo10dp(polygon[i+1]);
                bx = MathUtil.cleanNumberTo10dp(polygon[ibx]);
                by = MathUtil.cleanNumberTo10dp(polygon[iby]);

                if(DEBUG){
                    System.out.println("ax = " + ax + "; ay = " + ay + "; bx = " + bx + "; by = " + by);
                }

                if((point[0] == ax && point[1] == ay) || (point[0] == bx && point[1] == by)){
                    pointInPolygon = true;
                    if(DEBUG){
                        System.out.println("True because point on polygon vertex");
                    }
                } else if(ay == by && point[1] == ay){
                    if(checkHorizontallyBetween(point[0], ax, bx)){
                        pointInPolygon = true;
                        if(DEBUG){
                            System.out.println("True because point on horizontal line");
                        }
                    }
                } else if(checkVerticallyBetween(point[1], ay, by)){
                    if(DEBUG){
                        System.out.println("Points are between!");
                    }
                    double[] line = {ax, ay, bx, by};
                    double condition = MathUtil.comparePointAndLine(point, line);
                    if(DEBUG){
                        System.out.println("condition = " + condition);
                    }
                    if(condition == 0){
                        pointInPolygon = true;
                        if(DEBUG){
                            System.out.println("True because point on edge");
                        }
                    } else if(condition > 0){
                        count++;
                    }
                } else {
                    if(DEBUG){
                        System.out.println("Points not between!");
                    }
                }
            }

            return count;
        }

        boolean checkVerticallyBetween(double py, double ay, double by){
            boolean isBetween = false;

            if(ay <= py && py < by){
                isBetween = true;
            } else if(by <= py && py < ay){
                isBetween = true;
            }
            return isBetween;
        }

        private boolean checkHorizontallyBetween(double px, double ax, double bx) {
            boolean isBetween = false;

            if(ax <= px && px < bx){
                isBetween = true;
            } else if(bx <= px && px < ax){
                isBetween = true;
            }
            return isBetween;
        }
    }


    private void printPointsList(double[] points){
        System.out.println("list of points:");
        for(int i = 0; i < points.length; i += 2){
            System.out.print("(" + points[i] + "," + points[i+1] + ") ");
            System.out.println();
        }
    }
}
