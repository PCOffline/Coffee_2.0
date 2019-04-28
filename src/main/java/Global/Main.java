package Global;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;

/**
 * Main class - Boot Operations
 */

public class Main extends ListenerAdapter {

    public static void main(String[] args) throws LoginException {
        JDA jda = new JDABuilder(Secret.token).build();
        jda.addEventListener(new Main());

    }


}
