package animatedPane;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class AnimatedPane extends JPanel {

    private boolean isRunning = true;

    protected final int updateRate;
    protected final double dt;
    protected final int maxRendersSkippable;
    protected final int maxFps;

    protected double alpha;

    protected int currentFps;

    /**
     * Create a new animated pane
     * 
     * @param updateRate The number of physics/numeric updates to be carried out
     *            per second.
     * @param maxFps The total number of frames that can be rendered per second.
     * @param maxRendersSkippable For slow systems, the number of frames that
     *            can be sacrificed to conserve accurate calculations.
     */
    public AnimatedPane(int updateRate, int maxFps, int maxRendersSkippable) {
        super();

        this.setIgnoreRepaint(true);
        this.setDoubleBuffered(true);
        this.setBackground(Color.red);

        this.updateRate = updateRate;
        this.maxFps = maxFps;
        this.maxRendersSkippable = maxRendersSkippable;
        this.dt = 1.0 / updateRate; //delay between game updates (NOT RENDERS) in seconds

        this.currentFps = 0;

        Runnable animator = () -> {
            gameLoop();
        };

        new Thread(animator).start();

    }

    public void endAnimation() {
        this.isRunning = false;
    }

    public void init() {
    }

    /**
     * Add all physics update math in here.
     */
    public void update(double dt) {
    }

    /**
     * Add all rendering in here.
     * 
     * @param g2d the graphics object.
     * @param alpha the render interpolation modifier.
     */
    public void render(Graphics2D g2d, double alpha) {
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        render((Graphics2D) g, alpha);
    }

    public void gameLoop() {

        double currentTime = System.nanoTime() / 1e9; //current time in s
        double accumulator = 0.0;
        double timeSinceLastUpdate;

        double timeTakenForLoop;

        init();

        long fpsTimer = System.nanoTime();
        int fpsCt = 0;

        while (isRunning) {
            timeSinceLastUpdate = (System.nanoTime() / 1e9) - currentTime;
            currentTime = System.nanoTime() / 1e9;

            //Makes sure that the engine doesn't stop if there is a recurring failure to keep time
            if (timeSinceLastUpdate > maxRendersSkippable * dt)
                timeSinceLastUpdate = maxRendersSkippable * dt;

            accumulator += timeSinceLastUpdate;
            while (accumulator >= dt) {
                update(dt);
                accumulator -= dt;
            }

            alpha = accumulator / dt; //percentage time remaining after updates. Used for smooth rendering
            repaint();
            fpsCt++;

            if ((System.nanoTime() - fpsTimer) >= 1e9) {
                currentFps = fpsCt;
                fpsCt = 0;
                fpsTimer = System.nanoTime();
            }

            timeTakenForLoop = (System.nanoTime() / 1e9) - currentTime;
            callSleep((1000.0 / maxFps) - (timeTakenForLoop * 1e3)); //Caps FPS to maxFps

        }
        postAnimationCleanUp();

        System.out.println("EXECUTION FINISHED");
    }

    private void postAnimationCleanUp() {
    }

    /**
     * Pause the main thread for time milliseconds.
     * 
     * @param time the time in ms to pause the thread.
     */
    private void callSleep(double time) {
        if (time > 0) {
            try {
                Thread.sleep((long) time, (int) Math.round(((time % 1) * 1e6)));
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
