package animatedPane.sample;

import javax.swing.JFrame;

public class SampleMain {

    public static JFrame mainAnimatedFrame = new JFrame("Animated Panel Sample");
    public static final int XRES = 1000;
    public static final int YRES = 1000;
    private static final int updateRate = 30;
    private static final int maxFps = 200;
    private static final int maxFramesSkippable = 5;

    public static void main(String[] args) {

        mainAnimatedFrame.setSize(XRES, YRES);
        mainAnimatedFrame.setResizable(false);
        mainAnimatedFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainAnimatedFrame.add(new SampleAnimatedGamePane(updateRate, maxFps, maxFramesSkippable));
        mainAnimatedFrame.setVisible(true);

    }
}
