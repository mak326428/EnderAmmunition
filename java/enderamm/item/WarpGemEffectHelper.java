package enderamm.item;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Maxim
 * Date: 16.07.14
 * Time: 9:06
 * To change this template use File | Settings | File Templates.
 */
public class WarpGemEffectHelper {
    public static int oldColor;
    public static int newColor;
    public static int stage = -1;
    public static int gemColor;
    public static int STAGES = 60;

    public static int getR(int color) {
        return color >> 16 & 255;
    }

    public static int getG(int color) {
        return color >> 8 & 255;
    }

    public static int getB(int color) {
        return color & 255;
    }

    public static void generateWarpGemColor() {
        if (stage == -1) {
            Random rnd = new Random();
            oldColor = newColor;
            newColor = combine(rnd.nextInt(256), rnd.nextInt(256),
                    rnd.nextInt(256));
            stage = 0;
        }
        if (stage >= STAGES - 1) {
            stage = -1;
            return;
        }
        stage++;
        gemColor = combine(
                interpolate(getR(oldColor), getR(newColor), stage, STAGES),
                interpolate(getG(oldColor), getG(newColor), stage, STAGES),
                interpolate(getB(oldColor), getB(newColor), stage, STAGES));

    }

    public static int interpolateColor(int c1, int c2, int st, int sts) {
        return combine(interpolate(getR(c1), getR(c2), st, sts),
                interpolate(getG(c1), getG(c2), st, sts),
                interpolate(getB(c1), getB(c2), st, sts));
    }

    public static int interpolate(float firstColor, float secondColor,
                                  float stage, float maxStages) {
        return (int) (firstColor + (secondColor - firstColor) * stage
                / maxStages);
    }

    public static int combine(int r, int g, int b) {
        int rgb = (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF;
        return rgb;
    }
}
