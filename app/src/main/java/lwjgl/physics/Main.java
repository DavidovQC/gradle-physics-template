package lwjgl.physics;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_POINTS;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_FAN;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPointSize;
import static org.lwjgl.opengl.GL11.glVertex2d;
public class Main {

    private long window;

    public static void main(String[] args) {
        
        new Main().run();
    }

    public void run() {
        init();
        loop();
        GLFW.glfwTerminate();
    }

    private void init() {
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        window = GLFW.glfwCreateWindow(800, 800, "Negative Mass Sim", 0, 0);
        if (window == 0) {
            throw new RuntimeException("Failed to create window");
        }

        GLFW.glfwMakeContextCurrent(window);
        GLFW.glfwShowWindow(window);

        GL.createCapabilities();
    }

    private void loop() {

        double yAxisCoordinate = 0;
        double xAxisCoordinate = 0;
        double dy = -0.015;
        double dx = 0.01;
        
        while (!GLFW.glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT);

            // Here is where we will render your physics later
            dy = Math.abs(yAxisCoordinate) >= 1 ? -dy : dy;
            dx = Math.abs(xAxisCoordinate) >= 1 ? -dx : dx;
            
            createCircle(xAxisCoordinate, yAxisCoordinate, .01);
            yAxisCoordinate = yAxisCoordinate + dy;
            xAxisCoordinate = xAxisCoordinate + dx;

            // createPoint(0.3, 0.3, 100);
            // createTriangle(-0.1, -0.2);
            
            GLFW.glfwSwapBuffers(window);
            GLFW.glfwPollEvents();
        }
    }

    private void createPoint(double x, double y, int size){    
        glPointSize(size);         // size of the point in pixels    
        glBegin(GL_POINTS);        // start drawing points
        glVertex2d(x, y);          // coordinates of the point (center)
        glEnd();                   // finish drawing
    }

    private void createTriangle(double x, double y){    
        glBegin(GL_TRIANGLES);
        glVertex2d(x, y + 0.1);
        glVertex2d(x-0.1, y - 0.1);
        glVertex2d(x+0.1, y - 0.1);
        glEnd();
    }

    private void createCircle(double x, double y, double radius){
        int segments = 50;
        glBegin(GL_TRIANGLE_FAN);
        glVertex2d(x, y);

        for(int i = 0; i <= segments; i++){
            double angle = 2 * Math.PI * i / segments;
            double dx = x + radius * Math.cos(angle);
            double dy = y + radius * Math.sin(angle);
            glVertex2d(dx, dy);
        }

        glEnd();
    }

    public class Particle{
        public double x;
        public double y;
        public double mass;

        public double xVelocity;
        public double yVelocity;

        public double xAcceleration;
        public double yAcceleration;
        
        public Particle(double x, double y){
            this.x = x;
            this.y = y;
            this.mass = 1;
        }

        public Particle(double x, double y, double mass){
            if(mass <= 0){
                throw new RuntimeException("Error, invalid mass value");
            }

            this.x = x;
            this.y = y;
            this.mass = mass;
        }        
    }
    
}
