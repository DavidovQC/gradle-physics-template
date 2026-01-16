package lwjgl.physics;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_POINTS;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_FAN;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glColor3f;
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
        Particle myParticle1 = new Particle(-1,.9 , .01, 0, 0, -0.001); //initial x velocity 
        Particle myParticle2 = new Particle(0,.9 , .0, 0, 0, -0.001); //no initial x velocity
        
        while (!GLFW.glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT);
            myParticle1.update();
            myParticle2.update();
            
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

    private void createHorizontalLine(double y, String color) {
        if(color == "blue"){
              glColor3f(0, 0, 1);   
        } 

        if(color == "red"){
            glColor3f(1, 0, 0);   
        }

        glBegin(GL_LINES);
        glVertex2d(-1, y);
        glVertex2d(1, y);
        glEnd();
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
        
        public Particle(double x, double y, double xForce, double yForce){
            this.x = x;
            this.y = y;

            this.mass = 1;

            this.xAcceleration = xForce;
            this.yAcceleration = yForce;
        }

        public Particle(double x, double y, double xVelocity, double yVelocity, double xAcceleration, double yAcceleration){
            this.x = x;
            this.y = y;

            this.mass = 1;

            this.xVelocity = xVelocity;
            this.yVelocity = yVelocity;

            this.xAcceleration = xAcceleration;
            this.yAcceleration = yAcceleration;
        }
        
        public void update(){
            xVelocity = xVelocity + xAcceleration;
            yVelocity = yVelocity + yAcceleration;

            x = x + xVelocity;
            y = y + yVelocity;
            
            if(Math.abs(x) >= 1){
                x= x > 0 ? 1 : -1;
                xVelocity = -xVelocity;
            }

            if(Math.abs(y) >= 1){
                y= y > 0 ? 1 : -1;
                yVelocity=-yVelocity;
            }
       
            createCircle(this.x, this.y, 0.01);
            if(x == 0) createHorizontalLine(this.y, "blue");
            else createHorizontalLine(this.y, "red");
        }
    }   
}
