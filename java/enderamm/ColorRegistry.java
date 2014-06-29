package enderamm;

import com.google.common.collect.Maps;

import java.util.Map;

public class ColorRegistry {
    private static Map<String, IColorTransformer> transformers = Maps.newHashMap();

    public static final String HEALING_GEM_OVERLAY = "healGemOverlay";
    public static final String HEALING_GEM = "healGem";

    private static int FPS_measuring = 0;
    private static long lastMS = 0;
    public static int FPS = 0;

    static {
        register(HEALING_GEM, new GradientColorTransformer(0xD800FF, 0x00BCBC, 180));
        register(HEALING_GEM_OVERLAY, new GradientColorTransformer(0x009DFF, 0x009E2F, 180));

    }

    public static void updateGlobal() {
        for (IColorTransformer trs : transformers.values())
            trs.update();
        FPS_measuring++;
        if (System.currentTimeMillis() - lastMS > 1000) {
            lastMS = System.currentTimeMillis();
            FPS = FPS_measuring;
            FPS_measuring = 0;
        }
        //System.out.println(FPS);
        ((GradientColorTransformer) get(HEALING_GEM_OVERLAY)).setMaxPeriod(FPS * 2);
        ((GradientColorTransformer) get(HEALING_GEM)).setMaxPeriod(FPS * 2);

    }

    public static void register(String name, IColorTransformer tr) {
        transformers.put(name, tr);
    }

    public static IColorTransformer get(String name) {
        if (transformers.containsKey(name))
            return transformers.get(name);
        return null;
    }
}
