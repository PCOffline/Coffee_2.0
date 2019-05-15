package global;

import crypto.Secret;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.react.PrivateMessageReactionAddEvent;
import net.dv8tion.jda.core.events.user.update.UserUpdateGameEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.GuildController;
import net.dv8tion.jda.core.requests.RestAction;

import javax.security.auth.login.LoginException;
import java.util.Random;

/**
 * Main class - Boot Operations
 */

public class Main extends ListenerAdapter {

    private String id;

    public static void main(String[] args) throws LoginException, InterruptedException {
        JDA jda = new JDABuilder(Secret.TOKEN).build().awaitReady();
        jda.addEventListener(new Main());

        jda.setAutoReconnect(true);
        jda.getPresence().setPresence(OnlineStatus.ONLINE, RichPresence.playing("Drinking Tea").asRichPresence());
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent e) {
        User user = e.getUser();
        RestAction<PrivateChannel> privateChannelRestAction = user.openPrivateChannel();

        privateChannelRestAction.queue(channel -> {
            String asMention = user.getAsMention();

            channel.sendMessage(Strings.WELCOME.replace("_USER_", asMention)).queue((message -> {
                message.addReaction("\uD83D\uDC4C").queue();
                this.id = message.getId();
            }));
        });
    }

    @Override
    public void onPrivateMessageReactionAdd(PrivateMessageReactionAddEvent e) {
        User user = e.getUser();
        String name = e.getReaction().getReactionEmote().getName();
        boolean idEquals = e.getMessageId().equals(id);
        boolean nameEquals = name.equals("\uD83D\uDC4C");

        if (!user.isBot() && nameEquals && idEquals) {
            Guild guild = user.getMutualGuilds().get(0);
            GuildController controller = guild.getController();
            Member member = guild.getMember(user);
            Role role = guild.getRoleById("571626241216610304");

            controller.addSingleRoleToMember(member, role).queue();
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        String regex = "\\" + Strings.PREFIX + "(ms|magicshell)";
        Message message = e.getMessage();
        String contentRaw = message.getContentRaw();
        String[] split = contentRaw.split(" ");
        MessageChannel messageChannel = e.getChannel();
        Member member = e.getMember();
        User user = member.getUser();
        RestAction<PrivateChannel> privateChannel = user.openPrivateChannel();
        User author = e.getAuthor();
        String asMention = author.getAsMention();
        String messageId = message.getId();

        if (split[0].matches(regex)) {
            messageChannel.sendMessage(asMention + " " + Strings.magicShellYesNo[new Random().nextInt(Strings.magicShellYesNo.length)]).queue();
        }

        if (contentRaw.equals(Secret.KEY)) {
            message.delete().queue();
            privateChannel.queue(channel -> channel.sendMessage("Hello master, how shall I serve you?").queue());
        }

        if (split[0].equalsIgnoreCase(Strings.PREFIX + "vote")
                && split.length > 1) {
            messageChannel.addReactionById(messageId, "\u2705").queue();
            messageChannel.addReactionById(messageId, "\u274C").queue();
        }

    }

    @Override
    public void onUserUpdateGame(UserUpdateGameEvent e) {
        Member member = e.getMember();
        Game game = member.getGame();
        String hng = "573827985103388687";
        GuildController controller = e.getGuild().getController();
        Role role = e.getGuild().getRoleById(hng);
        String regex = "Heroes (and|&) Generals( WWII)?";

        if (game != null && (game.getName().matches(regex)))
            controller.addSingleRoleToMember(member, role).queue();
        else if (member.getRoles().contains(role))
            controller.removeSingleRoleFromMember(member, role).queue();
    }

}