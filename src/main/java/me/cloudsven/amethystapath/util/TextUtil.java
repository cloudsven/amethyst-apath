package me.cloudsven.amethystapath.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public final class TextUtil {
    private TextUtil() {}

    public static Component empty() {
        return Component.empty();
    }

    public static Component text(String text) {
        return Component.text(text)
                .color(NamedTextColor.GRAY)
                .decoration(TextDecoration.ITALIC, false)
                .decoration(TextDecoration.BOLD, false);
    }

    public static Component text(String text, NamedTextColor color) {
        return Component.text(text)
                .color(color)
                .decoration(TextDecoration.ITALIC, false)
                .decoration(TextDecoration.BOLD, false);
    }

    public static Component text(String text, NamedTextColor color, boolean bold, boolean italic) {
        return Component.text(text)
                .color(color)
                .decoration(TextDecoration.ITALIC, italic)
                .decoration(TextDecoration.BOLD, bold);
    }
}
