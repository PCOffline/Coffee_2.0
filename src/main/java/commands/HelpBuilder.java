package commands;

import com.jagrosh.jdautilities.command.Command;
import global.Main;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.awt.*;

class HelpBuilder {

    private HelpBuilder() {
    }

    static MessageEmbed build(Command command) {
        String name = command.getName();
        String s = "`\n";
        String title = name.replace(name.charAt(0), name.substring(0, 1).toUpperCase().toCharArray()[0]) + " Help";
        return Main.embed(title, null, null,
                "**Usage: **`" + name + (command.getAliases().length == 0 ? "" : "/" + aliases(command))
                        + (command.getArguments().equals("") ? s : " " + command.getArguments() + s)
                        + "**Description: **`" + command.getHelp() + s
                        + "**Category: **`" + command.getCategory().getName() + s
                        + "**Guild Only: **`" + command.isGuildOnly() + s
                        + (command.getCooldown() == 0 ? "" : "**Cooldown: **`" + command.getCooldown() + s)
                        + (command.getUserPermissions().length == 0 ? "" : "**Required User Permissions: **`" + userPerms(command) + s)
                        + (command.getRequiredRole().isEmpty() ? "" : "**Requires User Role: **`" + command.getRequiredRole() + s)
                        + (command.getChildren().length == 0 ? "" : "**Children Commands: **`" + children(command) + s), Color.CYAN, null, null, "http://icons.iconarchive.com/icons/dakirby309/simply-styled/256/Help-and-Support-icon.png", null);
    }

    private static String aliases(Command command) {
        StringBuilder res = new StringBuilder();
        for (String s : command.getAliases())
            res.append(s).append("/");
        return res.substring(0, res.length() - 1);
    }

    private static String userPerms(Command command) {
        StringBuilder res = new StringBuilder();
        for (Permission p : command.getUserPermissions())
            res.append(p.getName()).append(", ");
        return res.substring(0, res.length() - 2);
    }

    private static String children(Command command) {
        StringBuilder res = new StringBuilder();
        for (Command c : command.getChildren())
            res.append(c.getName()).append(", ");
        return res.substring(0, res.length() - 2);
    }

}
