package commands;

import com.jagrosh.jdautilities.command.CommandEvent;
import global.Constants;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;

public class BanCommand extends ModCommand {
    public BanCommand() {
        super();
        this.arguments = "<member> <time> [reason]";
        this.usage = Constants.PREFIX + this.name + " " + arguments;
        this.example = description.replace(arguments, "FPGa#9995 he's too cool");
        this.category = MOD;
        this.botPermissions = new Permission[]{Permission.VOICE_MUTE_OTHERS};
        this.requiredRole = "Moderator, Administrator";
        this.helpBiConsumer = (event, command) -> event.reply(HelpBuilder.build(this));
        this.guildOnly = true;
        this.help = "`" + this.usage + "`" + "\n" + this.description + "\n`" + this.example + "`";
    }

    @Override
    protected void modLog(CommandEvent commandEvent) {
        String[] args = commandEvent.getArgs().split(" ");
        StringBuilder reason = new StringBuilder();
        for (String s : args) {
            reason.append(s.equals(args[0]) && s.equals(args[1]) ? "" : s);
        }
        commandEvent.getGuild().getTextChannelById(Constants.MOD_LOG).sendMessage(Constants.HAMMER + "**BANNED** " + args[0] + " for " + reason + " by " + commandEvent.getAuthor().getAsMention()).queue();
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        if (!validate(commandEvent))
            return;
        Guild guild = commandEvent.getGuild();
        guild.getController().ban(guild.getMemberById(commandEvent.getArgs().split(" ")[0].replace("<@", "").replace(">", "")), ).queue();
        modLog(commandEvent);
    }
}
