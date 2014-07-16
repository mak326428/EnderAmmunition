package enderamm;

import enderamm.item.ItemWarpGem;
import enderamm.item.WarpGemEffectHelper;

public class TickUtil {
    /**
     * Incremented each client tick.
     */
    public static long CLIENT_TICKER = 0;
    /**
     * Incremented each render tick
     */
    public static long RENDER_TICKER = 0;

    public static void harnessValues() {
        //if (CLIENT_TICKER >= 0xFFFFFFFF)
        //	CLIENT_TICKER = 0;
        //if (SERVER_TICKER >= 0xFFFFFFFF)
        //	SERVER_TICKER = 0;
        //System.out.println(CLIENT_TICKER);

    }

    public static void onRenderTick() {
        WarpGemEffectHelper.generateWarpGemColor();
        ColorRegistry.updateGlobal();
    }
}
