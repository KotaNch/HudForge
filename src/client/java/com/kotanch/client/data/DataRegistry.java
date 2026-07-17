package com.kotanch.client.data;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import com.kotanch.client.element.HudLine;
import com.kotanch.client.element.ItemElement;
import com.kotanch.client.element.TextElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;

public final class DataRegistry {
    private static final Map<String, DataSource> SOURCES = new LinkedHashMap<>();

    static {
        register("x", mc -> mc.player == null ? "?" : String.format("%.1f", mc.player.getX()));
        register("y", mc -> mc.player == null ? "?" : String.format("%.1f", mc.player.getY()));
        register("z", mc -> mc.player == null ? "?" : String.format("%.1f", mc.player.getZ()));
        register("day", mc -> mc.world == null ? "?" : String.valueOf(mc.world.getTimeOfDay()/ 24000L));
        register("fps", mc -> String.valueOf(mc.getCurrentFps()));
        register("durability", mc -> {
            if (mc.player == null) return "?";
            ItemStack stack = mc.player.getMainHandStack();
            if (stack.isEmpty() || !stack.isDamageable()) return "-";
            int left = stack.getMaxDamage() - stack.getDamage();
            return left + "/" + stack.getMaxDamage();
        });
        register("durability_percent", mc ->{
            if (mc.player == null) return "?";
            ItemStack stack = mc.player.getMainHandStack();
            if(stack.isEmpty() || !stack.isDamageable()) return "-";
            int left = stack.getMaxDamage() - stack.getDamage();
            int pct = Math.round(100f * left / stack.getMaxDamage());
            return pct + "%";
        });
        register("helmet", mc -> slotDurability(mc, EquipmentSlot.HEAD));
        register("chestplate", mc -> slotDurability(mc, EquipmentSlot.CHEST));
        register("leggings", mc -> slotDurability(mc, EquipmentSlot.LEGS));
        register("boots", mc -> slotDurability(mc, EquipmentSlot.FEET));
        register("health", mc -> mc.player == null ? "?" : String.valueOf(Math.round(mc.player.getHealth())));
        register("max_health", mc -> mc.player == null ? "?" : String.valueOf(Math.round(mc.player.getMaxHealth())));
        register("hunger", mc -> mc.player == null ? "?" : String.valueOf(mc.player.getHungerManager().getFoodLevel()));
        register("armor_points",mc -> mc.player == null ? "?" : String.valueOf(mc.player.getArmor()));
        register("xp_level", mc -> mc.player == null ? "?" : String.valueOf(mc.player.experienceLevel));
        register("speed", mc -> {
            if (mc.player == null) return "?";
            double dx = mc.player.getX() - mc.player.lastX;
            double dz = mc.player.getZ() - mc.player.lastZ;
            double blocksPerSec = Math.sqrt(dx * dx + dz * dz) * 20.0;
            return String.format("%.1f", blocksPerSec);
        });
        register("chunk_x", mc -> mc.player == null ? "?" : String.valueOf(mc.player.getBlockX() >> 4));
        register("chunk_z", mc -> mc.player == null ? "?" : String.valueOf(mc.player.getBlockZ() >> 4));
        register("in_chunk_x", mc -> mc.player == null ? "?" : String.valueOf(mc.player.getBlockX() & 15));
        register("in_chunk_z", mc -> mc.player == null ? "?" : String.valueOf(mc.player.getBlockZ() & 15));
        register("light", mc -> {
            if (mc.world == null || mc.player == null) return "?";
            return String.valueOf(mc.world.getLightLevel(mc.player.getBlockPos()));
        });
        register("difficulty", mc -> mc.world == null ? "?" : mc.world.getDifficulty().getName());
        register("rl_time", mc ->{
            java.time.LocalTime now = java.time.LocalTime.now();
            return String.format("%02d:%02d", now.getHour(),now.getMinute());
        });
        register("walked", mc -> {
            if(mc.player == null) return  "?";
            int cm = mc.player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.WALK_ONE_CM));
            return String.format("%.1f m", cm / 100.0);
        });
    }

    private static String slotDurability(MinecraftClient mc, EquipmentSlot slot) {
        if (mc.player == null) return  "?";
        ItemStack stack = mc.player.getEquippedStack(slot);
        if (stack.isEmpty() || stack.getMaxDamage() <= 0) return "-";
        int left = stack.getMaxDamage() - stack.getDamage();
        return left + "/" + stack.getMaxDamage();
    }

    private  DataRegistry() {}

    public static void register(String id, DataSource source){
        SOURCES.put(id, source);
    }

    public static Map<String, DataSource> sources() {
        return SOURCES;
    }

    public static String apply(String template, MinecraftClient mc){
        if (template == null || template.isEmpty()) return  "";

        StringBuilder out = new StringBuilder();
        int i = 0;
        while (i < template.length()){
            char c = template.charAt(i);
            if (c == '{'){
                int end = template.indexOf('}', i);
                if (end > i) {
                    String id = template.substring(i + 1, end);
                    DataSource src = SOURCES.get(id);
                    if (src != null) {
                        out.append(src.get(mc));
                    } else {
                        out.append('{').append(id).append('}');
                    }
                    i = end + 1;
                    continue;
                }
            }
            out.append(c);
            i++;
        }
        return out.toString();
    }



    private static final Map<String, Function<MinecraftClient, ItemStack>> ICONS = new LinkedHashMap<>();

    static {
        registerIcon("helmet_icon", mc -> equipped(mc, EquipmentSlot.HEAD));
        registerIcon("chestplate_icon", mc -> equipped(mc, EquipmentSlot.CHEST));
        registerIcon("leggings_icon", mc -> equipped(mc, EquipmentSlot.LEGS));
        registerIcon("boots_icon", mc -> equipped(mc, EquipmentSlot.FEET));
        registerIcon("hand_icon",       mc -> mc.player == null ? ItemStack.EMPTY : mc.player.getMainHandStack());
    }

    public static void registerIcon(String id, Function<MinecraftClient, ItemStack> src){
        ICONS.put(id,src);
    }

    private static ItemStack equipped(MinecraftClient mc, EquipmentSlot slot){
        return  mc.player == null ? ItemStack.EMPTY : mc.player.getEquippedStack(slot);
    }

    public static HudLine buildLine(String template, MinecraftClient mc){
        HudLine line = new HudLine();
        if (template == null || template.isEmpty()) return  line;

        StringBuilder text = new StringBuilder();
        int i = 0;
        while (i < template.length()) {
            char c = template.charAt(i);
            if (c == '{'){
                int end = template.indexOf('}', i);
                if (end > i) {
                    String id = template.substring(i + 1, end);
                    if (ICONS.containsKey(id)) {
                        if (text.length() > 0) {
                            line.add(new TextElement(text.toString()));
                            text.setLength(0);
                        }
                        ItemStack stack = ICONS.get(id).apply(mc);
                        line.add(new ItemElement(stack));
                        i = end + 1;
                        continue;
                    } else if (SOURCES.containsKey(id)){
                        text.append(SOURCES.get(id).get(mc));
                        i = end + 1;
                        continue;
                    } else {
                        text.append('{').append(id).append('}');
                        i = end + 1;
                        continue;
                    }
                }
            }
            text.append(c);
            i++;
        }
        if (text.length() > 0) line.add(new TextElement(text.toString()));
        return line;
    }
}
