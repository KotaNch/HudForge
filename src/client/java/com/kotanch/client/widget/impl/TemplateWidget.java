package com.kotanch.client.widget.impl;

import com.kotanch.client.data.DataRegistry;
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
    protected List<Text> lines(MinecraftClient mc){
        if (config == null || config.template == null || config.template.isEmpty()){
            return  List.of();
        }
        String text = DataRegistry.apply(config.template, mc);
        return  List.of(Text.literal(text));
    }
}
