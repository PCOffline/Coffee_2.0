package commands;

import com.jagrosh.jdautilities.command.CommandEvent;
import global.Constants;
import global.FileWrite;
import net.dv8tion.jda.core.Permission;

public class UnmuteCommand extends ModCommand {
    public UnmuteCommand() {
        super();
        this.arguments = "<member>";
        this.usage = Constants.PREFIX + this.name + " " + arguments;
        this.example = description.replace(arguments, "@FPGa#9995");
        this.category = MOD;
        this.botPermissions = new Permission[]{Permission.VOICE_MUTE_OTHERS};
        this.requiredRole = "Moderator, Administrator";
        this.helpBiConsumer = (event, command) -> event.reply(HelpBuilder.build(this));
        this.guildOnly = true;
        this.help = "`" + this.usage + "`" + "\n" + this.description + "\n`" + this.example + "`";
    }

    @Override
    protected void modLog(CommandEvent commandEvent) {
        commandEvent.getGuild().getTextChannelById(Constants.MOD_LOG).sendMessage(Constants.SPEAKER + "**UNMUTED** " + commandEvent.getArgs() + " by " + commandEvent.getAuthor().getAsMention()).queue();
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        long line = FileWrite.getLine(Constants.MUTED, commandEvent.getAuthor().getId(), true, false);
        if (line != -1) {
            FileWrite.deleteLine(Constants.MUTED, line);
            commandEvent.getGuild().getController().setMute(commandEvent.getMember(), false).queue();
        } else
            commandEvent.replyError("This member wasn't muted");
    }

}
