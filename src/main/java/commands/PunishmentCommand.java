package commands;

import com.jagrosh.jdautilities.command.CommandEvent;

public class PunishmentCommand extends CommandExt {

    public PunishmentCommand() {
        this.name = getClass().getName().replace("Command", "").toLowerCase();
        this.description = this.name + "s the member";
        this.requiredRole = "Administrator, Moderator";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {

    }
}
