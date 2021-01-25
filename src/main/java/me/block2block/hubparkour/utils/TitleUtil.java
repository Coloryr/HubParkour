package me.block2block.hubparkour.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

public class TitleUtil {

    public static void sendActionBar(Player player, String message, ChatColor color, boolean bold) {
        try {
            Object chatTitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\": \"" + message + "\",\"color\":\"" + color.name().toLowerCase() + "\",\"bold\":\"" + ((bold)?"true":"false") + "\"}");

            Constructor<?> titleConstructor = getNMSClass("PacketPlayOutChat").getConstructor(getNMSClass("IChatBaseComponent"), byte.class);

            Object packet = titleConstructor.newInstance(chatTitle, (byte) 2);
            sendPacket(player, packet);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendPacket(Player player, Object packet)
    {
        try
        {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private static Class<?> getNMSClass(String name)
    {
        try
        {
            return Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + name);
        }
        catch(ClassNotFoundException ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

}
