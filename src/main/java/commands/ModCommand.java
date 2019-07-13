package commands;

import com.jagrosh.jdautilities.command.CommandEvent;

public abstract class ModCommand extends CommandExt {

    ModCommand() {
        this.name = getClass().getName().replace("Command", "").replace("commands.", "").toLowerCase();
        this.description = this.name + "s the member";
        this.requiredRole = "Moderators";
    }

    protected abstract void modLog(CommandEvent event);
}
