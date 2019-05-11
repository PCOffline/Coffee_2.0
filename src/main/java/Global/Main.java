package Global;

import crypto.Secret;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.RichPresence;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.user.update.UserUpdateGameEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.GuildController;

import javax.security.auth.login.LoginException;
import java.util.Random;

/**
 * Main class - Boot Operations
 */

public class Main extends ListenerAdapter {

    private static JDA jda;

    public static void main(String[] args) throws LoginException, InterruptedException {
        jda = new JDABuilder(Secret.token).build().awaitReady();
        jda.addEventListener(new Main());

        jda.setAutoReconnect(true);
        jda.getPresence().setPresence(OnlineStatus.ONLINE, RichPresence.playing("Drinking Tea").asRichPresence());
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent e) {
        e.getUser().openPrivateChannel().queue((channel) -> channel.sendMessage(Strings.WELCOME.replace("_USER_", e.getUser().getAsMention())).queue());
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getMessage().getContentRaw().split(" ")[0].equalsIgnoreCase(Strings.prefix + "magicshell")
                || e.getMessage().getContentRaw().split(" ")[0].equalsIgnoreCase(Strings.prefix + "ms")) {
            e.getChannel().sendMessage(e.getAuthor().getAsMention() + " " + Strings.magicShellYesNo[new Random().nextInt(Strings.magicShellYesNo.length)]).queue();
        }

        if (e.getMessage().getContentRaw().equals(Secret.key)) {
            e.getMessage().delete().queue();
            e.getMember().getUser().openPrivateChannel().queue(channel -> channel.sendMessage("Hello master, how shall I serve you?").queue());
        }

    }

    @Override
    public void onUserUpdateGame(UserUpdateGameEvent e) {
        System.out.print("UserUpdateGame");
        Member member = e.getMember();
        Game game = member.getGame();
        String hng = "Heroes & Generals";
        GuildController controller = e.getGuild().getController();
        Role role = e.getGuild().getRolesByName(hng, true).get(0);
        String regex = "Heroes (and|&) Generals( WWII)?";

        if (game != null && (game.getName().matches(regex)))
            controller.addSingleRoleToMember(member, role).queue();
        else if (member.getRoles().contains(role))
            controller.removeSingleRoleFromMember(member, role).queue();
    }
}