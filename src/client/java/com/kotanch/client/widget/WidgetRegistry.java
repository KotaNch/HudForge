package com.kotanch.client.widget;

import com.kotanch.client.config.WidgetConfig;
import com.kotanch.client.widget.impl.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static com.ibm.icu.text.PluralRules.Operand.w;

public class WidgetRegistry {
    public static final Map<String, Supplier<HudWidget>> FACTORIES = new LinkedHashMap<>();

    static {
        register("coordinates", CoordinatesWidget::new);
        register("days", DaysWidget::new);
        register("time", TimeWidget::new);
        register("fps", FpsWidget::new);
        register("direction", DirectionWidget::new);
        register("biome", BiomeWidget::new);
        register("template", TemplateWidget::new);

    }

    private WidgetRegistry() {}

    private static void register(String id, Supplier<HudWidget> factory){
        FACTORIES.put(id,factory);
    }

    public static HudWidget create(WidgetConfig cfg) {
        Supplier<HudWidget> f = FACTORIES.get(cfg.type);
        if (f ==null) return null;
        HudWidget w = f.get();
        w.attach(cfg);
        return w;
    }

    public static Map<String, Supplier<HudWidget>> factories(){
        return FACTORIES;
    }

    public static List<String[]> available(){
        List<String[]> out = new ArrayList<>();
        for (String id : FACTORIES.keySet()){
            WidgetConfig probe = new WidgetConfig();
            probe.type = id;
            HudWidget w = create(probe);
            if (w != null){
                out.add(new String[]{id, w.displayName()});
            }
        }
        return out;
    }
}


