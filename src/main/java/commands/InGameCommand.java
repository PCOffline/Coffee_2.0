package commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.requests.restaction.PermissionOverrideAction;

import java.util.Timer;
import java.util.TimerTask;

public class InGameCommand extends Command {

    private final Timer timer = new Timer();
    private String time;

    public InGameCommand() {
        this.name = "ingame";
        this.aliases = new String[]{"game"};
        this.arguments = "[time]";
        this.category = new Category("H&G");
        this.botPermissions = new Permission[]{Permission.MANAGE_PERMISSIONS, Permission.MANAGE_CHANNEL, Permission.VIEW_CHANNEL};
        this.requiredRole = "USA";
        this.guildOnly = true;
        this.help = "sets the user's voice channel to Push-To-Talk temporarily (up to 4 hours)";
        this.helpBiConsumer = (event, command) -> event.reply(HelpBuilder.build(this));
        this.time = "30m";
    }

    @Override
    protected void execute(CommandEvent event) {
        String args = event.getArgs();
        String regex = "((?:h(?:our(?:s)?)?)|(?:m(?:in(?:ute(?:s))?)?)?)|(?:s(?:ec(?:ond(?:s)?)?)?)"; // h/our/s or m/in/ute/s or s/ec/ond/s

        if (!event.getMember().getVoiceState().inVoiceChannel() || args.split(" ").length > 1) {
            event.replyError("You must be in a voice channel");
            return;
        }

        String regex1 = "[0-9]";
        boolean matches = args.replaceAll(regex1, "").matches(regex);
        if (args.split(" ").length == 1 && matches)
            time = args;
        else if (args.split(" ").length == 1 && !matches) {
            event.reactError();
            return;
        }
        long milliseconds = toMilliseconds(time);
        if (milliseconds > 14400000L || milliseconds < 1000L) {
            event.reactError();
            return;
        }

        VoiceChannel channel = event.getMember().getVoiceState().getChannel();
        PermissionOverrideAction override = channel.putPermissionOverride(event.getGuild().getRolesByName("USA", false).get(0));
        override.setDeny(Permission.VOICE_USE_VAD).queue();


        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                override.setAllow(Permission.VOICE_USE_VAD).queue();
            }
        }, milliseconds);
    }

    private long toMilliseconds(String time) {
        String s = time.replaceAll("[0-9]", "");
        if (s.matches("((?:h(?:our(?:s)?)?))"))
            return time.charAt(0) * 3600000L;
        if (s.matches("(?:m(?:in(?:ute(?:s))?)?)"))
            return time.charAt(0) * 60000L;
        return time.charAt(0) * 1000L;
    }
}
