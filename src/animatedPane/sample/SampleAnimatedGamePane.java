package animatedPane.sample;

import java.awt.Color;
import java.awt.Graphics2D;

import animatedPane.AnimatedPane;

public class SampleAnimatedGamePane extends AnimatedPane {

    private double[] interpSquarePos;
    private double[] prevInterpSquarePos;
    private double interpSquareSpeed;

    private double[] regSquarePos;
    private double regSquareSpeed;

    private final int sideLength = 100;

    /**
     * A sample animated panel which can be used to see the difference between
     * interp on and off.
     * 
     * @param updateRate the number of times physics should update per second.
     * @param maxFps the maximum number of renders per second.
     * @param maxRendersSkippable
     */
    public SampleAnimatedGamePane(int updateRate, int maxFps, int maxRendersSkippable) {
        super(updateRate, maxFps, maxRendersSkippable);
    }

    @Override
    public void init() {
        setBackground(Color.black);

        interpSquarePos = new double[] { 100, 300 };
        prevInterpSquarePos = interpSquarePos;

        regSquarePos = new double[] { 100, 600 };

        regSquareSpeed = interpSquareSpeed = 500; // px/s
    }

    @Override
    public void update(double dt) {
        //Create a copy.
        prevInterpSquarePos = interpSquarePos.clone();

        interpSquarePos[0] += interpSquareSpeed * dt;

        regSquarePos[0] += regSquareSpeed * dt;

        if (interpSquarePos[0] + sideLength >= SampleMain.XRES)
            interpSquareSpeed = -Math.abs(interpSquareSpeed);
        else if (interpSquarePos[0] < 0)
            interpSquareSpeed = Math.abs(interpSquareSpeed);

        if (regSquarePos[0] + sideLength >= SampleMain.XRES)
            regSquareSpeed = -Math.abs(regSquareSpeed);
        else if (regSquarePos[0] < 0)
            regSquareSpeed = Math.abs(regSquareSpeed);
    }

    @Override
    public void render(Graphics2D g2d, double alpha) {
        g2d.setColor(Color.red);

        g2d.fillRect((int) regSquarePos[0], (int) regSquarePos[1], sideLength, sideLength);

        //Yes, this is a slight bit more complex, but the result is well worth it.
        int xCoord = (int) Math.round((interpSquarePos[0] * alpha) + prevInterpSquarePos[0] * (1.0 - alpha));
        int yCoord = (int) Math.round((interpSquarePos[1] * alpha) + prevInterpSquarePos[1] * (1.0 - alpha));

        g2d.setColor(Color.green);
        g2d.fillRect(xCoord, yCoord, sideLength, sideLength);
    }

}
