package ass1;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

/**
 * A game object that has a polygonal shape.
 * 
 * This class extend ass1.GameObject to draw polygonal shapes.
 *
 * @author AndrewLem
 */
public class PolygonalGameObject extends GameObject {

    private double[] myPoints;
    private double[] myFillColour;
    private double[] myLineColour;

    private static boolean DEBUG = false;

    /**
     * Create a polygonal game object and add it to the scene tree
     * 
     * The polygon is specified as a list of doubles in the form:
     * 
     * [ x0, y0, x1, y1, x2, y2, ... ]
     * 
     * The line and fill colours can possibly be null, in which case that part of the object
     * should not be drawn.
     *
     * @param parent The parent in the scene tree
     * @param points A list of points defining the polygon
     * @param fillColour The fill colour in [r, g, b, a] form
     * @param lineColour The outlien colour in [r, g, b, a] form
     */
    public PolygonalGameObject(GameObject parent, double points[],
            double[] fillColour, double[] lineColour) {
        super(parent);

        myPoints = points;
        myFillColour = fillColour;
        myLineColour = lineColour;
    }

    /**
     * Get the polygon
     * 
     * @return
     */
    public double[] getPoints() {        
        return myPoints;
    }

    /**
     * Set the polygon
     * 
     * @param points
     */
    public void setPoints(double[] points) {
        myPoints = points;
    }

    /**
     * Get the fill colour
     * 
     * @return
     */
    public double[] getFillColour() {
        return myFillColour;
    }

    /**
     * Set the fill colour.
     * 
     * Setting the colour to null means the object should not be filled.
     * 
     * @param fillColour The fill colour in [r, g, b, a] form 
     */
    public void setFillColour(double[] fillColour) {
        myFillColour = fillColour;
    }

    /**
     * Get the outline colour.
     * 
     * @return
     */
    public double[] getLineColour() {
        return myLineColour;
    }

    /**
     * Set the outline colour.
     * 
     * Setting the colour to null means the outline should not be drawn
     * 
     * @param lineColour
     */
    public void setLineColour(double[] lineColour) {
        myLineColour = lineColour;
    }

    // ===========================================
    // COMPLETE THE METHODS BELOW
    // ===========================================
    

    /**
     * TODO: Draw the polygon
     * 
     * if the fill colour is non-null, fill the polygon with this colour
     * if the line colour is non-null, draw the outline with this colour
     * 
     * @see ass1.GameObject#drawSelf(javax.media.opengl.GL2)
     */
    @Override
    public void drawSelf(GL2 gl) {
//        objectFields
//        private double[] myPoints;
//        private double[] myFillColour;
//        private double[] myLineColour;
//        * @param parent The parent in the scene tree
//        * @param points A list of points defining the polygon
//        * @param fillColour The fill colour in [r, g, b, a] form
//        * @param lineColour The outlien colour in [r, g, b, a] form
        // TODO: Write this method

        if(myFillColour != null){
            gl.glColor4d(myFillColour[0], myFillColour[1], myFillColour[2], myFillColour[3]);
            gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
            drawPolygon(gl);
        }

        if(myLineColour != null){
            gl.glColor4d(myLineColour[0], myLineColour[1], myLineColour[2], myLineColour[3]);
            gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
            drawPolygon(gl);
        }

        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
    }

    void drawPolygon(GL2 gl){
        gl.glBegin(GL2.GL_POLYGON);

        for (int i = 0; i < myPoints.length; i=i+2) {
            gl.glVertex2d(myPoints[i], myPoints[i+1]);
        }

        gl.glEnd();

    }

    double[] getGlobalPoints(){
        double[] globalPoints = getPoints().clone();
        double[][] globalMatrix = getGlobalMatrix();
        double[] point = new double[3];

        point[2] = 1;

        if(DEBUG){
            System.out.println("Global Matrix:");
            MathUtil.printMatrix(globalMatrix);
        }

        for(int i = 0; i < globalPoints.length; i += 2){
            point[0] = globalPoints[i];
            point[1] = globalPoints[i+1];
            if(DEBUG){
                System.out.print("Point A: ");
                MathUtil.printVector(point);
            }
            point = MathUtil.multiply(globalMatrix, point);
            globalPoints[i] = point[0];
            globalPoints[i+1] = point[1];
            if(DEBUG){
                System.out.print("Point B: ");
                MathUtil.printVector(point);
            }
        }

        return globalPoints;
    }
}
