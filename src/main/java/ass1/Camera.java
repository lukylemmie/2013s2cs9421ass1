package ass1;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

/**
 * The camera is a ass1.GameObject that can be moved, rotated and scaled like any other.
 *
 */
public class Camera extends GameObject {

    private float[] myBackground;

    public Camera(GameObject parent) {
        super(parent);

        myBackground = new float[4];
    }

    public Camera() {
        this(GameObject.ROOT);
    }
    
    public float[] getBackground() {
        return myBackground;
    }

    public void setBackground(float[] background) {
        myBackground = background;
    }

    // ===========================================
    // COMPLETE THE METHODS BELOW
    // ===========================================
   
    
    public void setView(GL2 gl) {
        
        //clear the view to the background colour

        gl.glClearColor(myBackground[0],myBackground[1],myBackground[2],myBackground[3]);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        
        //set the view matrix to account for the camera's position
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glScaled(1.0 / getGlobalScale(), 1.0 / getGlobalScale(), 1);
        gl.glRotated(-getGlobalRotation(), 0, 0, 1);
        gl.glTranslated(-getGlobalPosition()[0], -getGlobalPosition()[1], 0);
    }

    public void reshape(GL2 gl, int x, int y, int width, int height) {
        // match the projection aspect ratio to the viewport
        // to avoid stretching

        // source: Malcolm Ryan's Triangle Example :D
        // calculate the aspect ratio of window
        double aspect = 1d * width / height;

        // in OpenGL terms, we are changing the 'projection'
        // this will be explained later
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        // Use the GLU library to compute the new projection
        GLU glu = new GLU();
        glu.gluOrtho2D(-aspect, aspect, -1.0, 1.0);  // left, right, top, bottom
    }
}
