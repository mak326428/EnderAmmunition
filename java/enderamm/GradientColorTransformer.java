package enderamm;

import enderamm.item.ItemWarpGem;
import enderamm.item.WarpGemEffectHelper;

public class GradientColorTransformer implements IColorTransformer {

    private int fColor, sColor, period, currColor, maxPeriod, maxPeriodToSet;

    public GradientColorTransformer(int f, int s, int p) {
        this.fColor = f;
        this.sColor = s;
        this.maxPeriod = p;
    }

    public void setMaxPeriod(int mP) {
        this.maxPeriodToSet = mP;
    }

    @Override
    public int getColor() {
        return WarpGemEffectHelper.interpolateColor(fColor, sColor, period, maxPeriod);
    }

    @Override
    public void update() {
        if (period < maxPeriod) {
            period++;
        } else {
            int oldF = fColor;
            fColor = sColor;
            sColor = oldF;
            period = 0;
            maxPeriod = maxPeriodToSet;
        }
    }

}
