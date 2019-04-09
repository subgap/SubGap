package me.kavin.subgap;

import me.kavin.subgap.consts.Constants;
import me.kavin.subgap.listener.DiscordListener;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class Main {

	public static JDA api;

	public static void main(String[] args) throws Exception {
		JDABuilder builder = new JDABuilder(AccountType.BOT).setToken(Constants.BOT_TOKEN);
		api = builder.build();
		api.setAutoReconnect(true);
		DiscordListener.init();
	}
}