package commands;

import com.jagrosh.jdautilities.command.CommandEvent;

public abstract class ModCommand extends CommandExt {

    ModCommand() {
        this.name = getClass().getName().replace("Command", "").toLowerCase();
        this.description = this.name + "s the member";
        this.requiredRole = "Administrator, Moderator";
    }

    protected abstract void modLog(CommandEvent event);
}
