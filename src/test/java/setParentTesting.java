import ass1.Camera;
import ass1.GameEngine;
import ass1.GameObject;
import com.jogamp.opengl.util.FPSAnimator;

import javax.media.opengl.GL2;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.swing.*;
import java.awt.*;

/**
 * A simple axes object for debugging the assignment.
 * 
 * This class implements a GameObject that draws a set of axes.
 * 
 * The x-axis is drawn as a red line of length 1 from (0,0) to (1,0)
 * The y-axis is drawn as a green line of length 1 from (0,0) to (0,1)
 *
 * @author malcolmr
 */
public class setParentTesting extends GameObject {
    String name;

    public setParentTesting(GameObject parent, String name) {
        super(parent);
        this.name = name;
    }

    @Override
    public void drawSelf(GL2 gl) {
        gl.glLineWidth(2);

        // draw the x-axis in red
        gl.glColor3d(1,0,0);
        gl.glBegin(GL2.GL_LINES);
        {
            gl.glVertex2d(0, 0);
            gl.glVertex2d(1, 0);
        }
        gl.glEnd();

        // draw the y-axis in green
        gl.glColor3d(0,1,0);
        gl.glBegin(GL2.GL_LINES);
        {
            gl.glVertex2d(0, 0);
            gl.glVertex2d(0, 1);
        }
        gl.glEnd();

    }


    /**
     * An example of how to use this class.
     * 
     * @param args
     */
    public static void main(String[] args) {
        // Initialise OpenGL
        GLProfile glprofile = GLProfile.getDefault();
        GLCapabilities glcapabilities = new GLCapabilities(glprofile);
        
        // create a GLJPanel to draw on
        GLJPanel panel = new GLJPanel(glcapabilities);

        // Create a camera
        Camera camera = new Camera(GameObject.ROOT);
        camera.setScale(2); // scale up the camera so we can see more of the world  
        
        // A set of axes showing the world coordinate frame 
        setParentTesting axesWorld = new setParentTesting(GameObject.ROOT, "axesWorld");
        
        // A set of axes showing a transformed coordinate frame
        setParentTesting axesParent1 = new setParentTesting(GameObject.ROOT, "axesParent1");
        axesParent1.setPosition(1, 2);
        axesParent1.setRotation(60);
        axesParent1.setScale(0.5);

        // A set of axes that are a child of the above
        setParentTesting axesChild1 = new setParentTesting(axesParent1, "axesChild1");
        axesChild1.setPosition(-2, -1);
        axesChild1.setRotation(-90);
        axesChild1.setScale(2);

        // A set of axes showing a transformed coordinate frame
        setParentTesting axesParent2 = new setParentTesting(GameObject.ROOT, "axesParent2");
        axesParent2.setPosition(-1, -1);
        axesParent2.setRotation(-85);
        axesParent2.setScale(.25);

        // A set of axes that are a child of the above
        setParentTesting axesChild2 = new setParentTesting(axesParent2, "axesChild2");
        axesChild2.setPosition(2, 1);
        axesChild2.setRotation(90);
        axesChild2.setScale(3);

        axesParent2.printDetails();
        axesChild2.printDetails();

        axesParent2.setParent(axesParent1);

        axesParent2.printDetails();
        axesChild2.printDetails();

        // Add the game engine
        GameEngine engine = new GameEngine(camera);
        panel.addGLEventListener(engine);

        // Add an animator to call 'display' at 60fps        
        FPSAnimator animator = new FPSAnimator(60);
        animator.add(panel);
        animator.start();

        // Put it in a window
        JFrame jFrame = new JFrame("Test");
        jFrame.getContentPane().add(panel, BorderLayout.CENTER);
        jFrame.setSize(400, 400);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    void printDetails(){
        System.out.println("Object name: " + name);
        System.out.println("Object global position: " + getGlobalPosition()[0] + ", " + getGlobalPosition()[1]);
        System.out.println("Object global rotation: " + getGlobalRotation());
        System.out.println("Object global scale: " + getGlobalScale());
    }
}
