package global;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import commands.*;
import crypto.Secret;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.react.PrivateMessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.user.update.UserUpdateGameEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.GuildController;
import net.dv8tion.jda.core.requests.RestAction;
import net.dv8tion.jda.core.requests.restaction.MessageAction;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Main class - Boot Operations
 */

public class Main extends ListenerAdapter {

    private static JDA jda;
    private String id;

    public static void main(String[] args) throws LoginException, InterruptedException {
        CommandClient commandClient = new CommandClientBuilder()
                .setPrefix(Constants.PREFIX)
                .setOwnerId("168066189128499200")
                .setGame(Game.playing(Constants.GAME))
                .useHelpBuilder(true)
                .setEmojis(Constants.SUCCESS, Constants.WARNING, Constants.ERROR)
                .addCommands(
                        new InGameCommand(),
                        new VoteCommand(),
                        new KickCommand(),
                        new MuteCommand(),
                        new BanCommand(),
                        new UnmuteCommand(),
                        new MagicShellCommand()
                ).build();
        jda = new JDABuilder(Secret.TOKEN).addEventListener(new Main(), commandClient).setAutoReconnect(true).build().awaitReady();

        jda.setAutoReconnect(true);

    }

    public static MessageEmbed embed(String title, String titleURL, String img, String content, Color color, String footer, String footerURL, String thumbnail, String author) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(title, titleURL);
        builder.setImage(img);
        builder.setDescription(content);
        builder.setColor(color);
        builder.setFooter(footer, footerURL);
        builder.setThumbnail(thumbnail);
        builder.setAuthor(author);
        return builder.build();
    }

    public static TextChannel getChannel(long id) {
        return jda.getGuildById(Constants.GUILD).getTextChannelById(id);
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent e) {
        User user = e.getUser();
        RestAction<PrivateChannel> privateChannelRestAction = user.openPrivateChannel();

        privateChannelRestAction.queue(channel -> {
            String asMention = user.getAsMention();

            channel.sendMessage(Constants.WELCOME.replace("_USER_", asMention)).queue((message -> {
                message.addReaction(Constants.AGREE).queue();
                this.id = message.getId();
            }), ignored -> e.getGuild().getTextChannelById(531472318250090497L).sendMessage(Constants.WELCOME.replace("_USER_", asMention)).queue(message -> {
                message.addReaction(Constants.AGREE).queue();
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
    public void onMessageReactionAdd(MessageReactionAddEvent e) {
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
        Message message = e.getMessage();
        String contentRaw = message.getContentRaw();
        String[] split = contentRaw.split(" ");
        MessageChannel messageChannel = e.getChannel();
        User author = e.getAuthor();
        Guild guild = e.getGuild();
        String first = split[0];
        String lowerCase = contentRaw.toLowerCase();
        Member member = e.getMember();

        if (guild == null || messageChannel.getType().equals(ChannelType.PRIVATE)) {
            return;
        }

        List<Member> mentionedMembers = message.getMentionedMembers();
        Member mentionedMember = mentionedMembers != null && !mentionedMembers.isEmpty() ? mentionedMembers.get(0) : null;
        User user = mentionedMember != null ? mentionedMember.getUser() : e.getAuthor();
        String avatarUrl = user.getAvatarUrl();
        long line = FileWrite.getLine(Constants.MUTED, author.getId(), true, false);
        if (line != -1) {
            String[] s = FileWrite.goLine(Constants.MUTED, line).equals("") ? null : FileWrite.goLine(Constants.MUTED, line).split(" ");
            if (s != null && !s[1].equals("-1") && OffsetDateTime.now().isAfter(ZonedDateTime.parse(s[1], DateTimeFormatter.ofPattern("yyyy:MM:dd:HH:mm").withZone(ZoneId.of("Europe/London"))).toOffsetDateTime())) {
                FileWrite.deleteLine(Constants.MUTED, line);
            } else if (s != null && !s[1].equals("-1") && OffsetDateTime.now().isBefore(OffsetDateTime.parse(s[1]))) {
                e.getMessage().delete().queue();
                e.getChannel().sendMessage((e.getAuthor().getAsMention() + " you are still muted, your mute will expire at " + s[1])).queue(msg -> {
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            msg.delete().queue();
                        }
                    }, 5000);
                });
            } else if (s != null && s[1].equals("-1")) {
                e.getMessage().delete().queue();
                e.getChannel().sendMessage(e.getAuthor().getAsMention() + " you are permanently muted!").queue(msg -> {
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            msg.delete().queue();
                        }
                    }, 5000);
                });
            }
        }

        if (messageChannel.getIdLong() == 574276000414826506L && first.equalsIgnoreCase(Constants.PREFIX + "class")) {
            if ((mentionedMembers != null ? mentionedMembers.size() : 0) == 1) {
                messageChannel.sendMessage(embed((mentionedMember != null ? mentionedMember.getEffectiveName() : null) + " Classes", null, null, ResourceWriter.getClasses(user.getIdLong()), Color.cyan, null, null, avatarUrl, null)).queue();
            } else {
                MessageAction invalidUseOfClassCommand = messageChannel.sendMessage("Invalid use of class command");
                if (split.length < 3)
                    invalidUseOfClassCommand.queue();
                else {
                    if (split.length > 3) {
                        switch (split[1].toLowerCase()) {
                            case "inf":
                            case "infantry":
                                ResourceWriter.addToClass(0);
                                ResourceWriter.addToClass(0, e.getAuthor().getIdLong(), split[2], contentRaw.substring(first.length() + split[1].length() + split[2].length() + 3));
                                break;
                            case "para":
                            case "parachute":
                            case "paratrooper":
                                ResourceWriter.addToClass(1);
                                ResourceWriter.addToClass(1, e.getAuthor().getIdLong(), split[2], contentRaw.substring(first.length() + split[1].length() + split[2].length() + 3));
                                break;
                            case "recon":
                            case "sniper":
                                ResourceWriter.addToClass(2);
                                ResourceWriter.addToClass(2, e.getAuthor().getIdLong(), split[2], contentRaw.substring(first.length() + split[1].length() + split[2].length() + 3));
                                break;
                            case "tank":
                            case "tanker":
                                ResourceWriter.addToClass(3);
                                ResourceWriter.addToClass(3, e.getAuthor().getIdLong(), split[2], contentRaw.substring(first.length() + split[1].length() + split[2].length() + 3));
                                break;
                            case "pilot":
                            case "plane":
                            case "fighter":
                            case "fighterpilot":
                                ResourceWriter.addToClass(4);
                                ResourceWriter.addToClass(4, e.getAuthor().getIdLong(), split[2], contentRaw.substring(first.length() + split[1].length() + split[2].length() + 3));
                                break;
                            default:
                                invalidUseOfClassCommand.queue();
                                return;
                        }
                    } else if (split[2].equalsIgnoreCase("18")) {
                        ResourceWriter.addToClass(5);
                        switch (split[1].toLowerCase()) {
                            case "inf":
                            case "infantry":
                                ResourceWriter.addToClass(5, e.getAuthor().getIdLong());
                                break;
                            case "para":
                            case "parachute":
                            case "paratrooper":
                                ResourceWriter.addToClass(1, e.getAuthor().getIdLong());
                                break;
                            case "recon":
                            case "sniper":
                                ResourceWriter.addToClass(2, e.getAuthor().getIdLong());
                                break;
                            case "tank":
                            case "tanker":
                                ResourceWriter.addToClass(3, e.getAuthor().getIdLong());
                                break;
                            case "pilot":
                            case "plane":
                            case "fighter":
                            case "fighterpilot":
                                ResourceWriter.addToClass(4, e.getAuthor().getIdLong());
                                break;
                            default:
                                invalidUseOfClassCommand.queue();
                                return;
                        }
                    } else {
                        invalidUseOfClassCommand.queue();
                        return;
                    }

                    if (split[2].matches("([1-9]|1[0-8])"))
                        messageChannel.sendMessage("Successfully added " + split[1] + " to your classes!").queue();
                    else
                        invalidUseOfClassCommand.queue();
                }
            }
        }

        if (split.length == 1 && contentRaw.equalsIgnoreCase(Constants.PREFIX + "classes"))
            messageChannel.sendMessage(embed("Classes", null, null, ResourceWriter.getClasses(), Color.GRAY, null, null, null, null)).queue();

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

    @Override
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent e) {
        if (e.getRoles().get(0).getId().equals((Constants.USA + "").replace("L", ""))) {
            Member member = e.getMember();
            getChannel(Constants.USA_CHAT).sendMessage("Welcome " + member.getAsMention() + "! Congratulations on your USA role! :D Please write down your classes at " + getChannel(Constants.CLASSES)).queue();
        }
    }
}