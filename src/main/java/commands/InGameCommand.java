package commands;

import com.jagrosh.jdautilities.command.CommandEvent;
import global.Constants;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.requests.restaction.PermissionOverrideAction;

import java.util.Timer;
import java.util.TimerTask;

public class InGameCommand extends CommandExt {

    private final Timer timer = new Timer();
    private String time;

    public InGameCommand() {
        this.name = "ingame";
        this.aliases = new String[]{"game"};
        this.arguments = "[time]";
        this.usage = Constants.PREFIX + this.name + " 30m";
        this.category = HNG;
        this.botPermissions = new Permission[]{Permission.MANAGE_PERMISSIONS, Permission.MANAGE_CHANNEL, Permission.VIEW_CHANNEL};
        this.requiredRole = "USA";
        this.guildOnly = true;
        this.help = "sets the user's voice channel to Push-To-Talk temporarily (up to 4 hours)";
        this.helpBiConsumer = (event, command) -> event.reply(HelpBuilder.build(this));
        this.time = "30m";
    }

    @Override
    protected void execute(CommandEvent event) {
        if (validate(event))
            return;
        String args = event.getArgs();
        String regex = "((?:h(?:our(?:s)?)?)|(?:m(?:in(?:ute(?:s))?)?)?)|(?:s(?:ec(?:ond(?:s)?)?)?)"; // h/our/s or m/in/ute/s or s/ec/ond/s

        if (!event.getMember().getVoiceState().inVoiceChannel() || args.split(" ").length > 1) {
            event.replyError("You must be in a voice channel");
            return;
        }

        String regex1 = "[0-9]";
        boolean matches = args.replaceAll(regex1, "").matches(regex);
        if (args.split(" ").length == 1 && matches)
            this.time = args;
        else if (args.split(" ").length == 1 && !matches) {
            event.reactError();
            return;
        }
        long milliseconds = toMilliseconds();
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

    private long toMilliseconds() {
        String s = this.time.replaceAll("[0-9]", "");
        if (s.matches("((?:h(?:our(?:s)?)?))"))
            return this.time.charAt(0) * 3600000L;
        if (s.matches("(?:m(?:in(?:ute(?:s))?)?)"))
            return this.time.charAt(0) * 60000L;
        return this.time.charAt(0) * 1000L;
    }
}
