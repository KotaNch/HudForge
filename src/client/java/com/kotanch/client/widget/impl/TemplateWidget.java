package com.kotanch.client.widget.impl;

import com.kotanch.client.data.DataRegistry;
import com.kotanch.client.element.HudLine;
import com.kotanch.client.element.TextElement;
import com.kotanch.client.widget.HudWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.List;

public class TemplateWidget extends HudWidget {
    @Override
    public String displayName() {
        if (config != null && config.template != null && !config.template.isEmpty()) {
            return config.template;
        }
        return "Template";
    }

    @Override
    public String typeId(){
        return "template";
    }

    @Override
    protected List<HudLine> lines(MinecraftClient mc){
        if (config == null || config.template == null || config.template.isEmpty()){
            return  List.of();
        }
        List<HudLine> out = new java.util.ArrayList<>();
        for (String part : config.template.split("\\||\\n", -1)) {
            out.add(DataRegistry.buildLine(part, mc));
        }
        return  out;
    }
}
