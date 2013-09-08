package ass1;

import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

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

        for(GameObject object : GameObject.ALL_OBJECTS){
            if(object instanceof PolygonalGameObject){
                PolygonalGameObject polygon = (PolygonalGameObject) object;
                PointAndPolygon temp = new PointAndPolygon(p, polygon.getPoints());
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
            this.polygon = polygon;

            count = countLinesOnRight();

            if(!pointInPolygon){
                if(count % 2 == 1){
                    pointInPolygon = true;
                }
            }
        }

        private boolean isPointInPolygon() {
            return pointInPolygon;
        }

        int countLinesOnRight(){
            int count = 0;
            double ax, ay, bx, by;

            for(int i = 0; i < polygon.length - 2 && !pointInPolygon; i += 2){
                ax = polygon[i];
                ay = polygon[i+1];
                bx = polygon[i+2];
                by = polygon[i+3];

                if(checkVerticallyBetween(point[1], ay, by)){
                    double[] line = {ax, ay, bx, by};
                    double condition = MathUtil.comparePointAndLine(point, line);
                    if(condition == 0){
                        pointInPolygon = true;
                    } else if(condition > 0){
                        count++;
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
    }
}
