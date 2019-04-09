package me.kavin.subgap.listener;

import me.kavin.subgap.Main;
import me.kavin.subgap.command.Command;
import me.kavin.subgap.command.CommandExecutor;
import me.kavin.subgap.command.CommandManager;
import me.kavin.subgap.consts.Constants;
import me.kavin.subgap.utils.FirebaseUtils;
import me.kavin.subgap.utils.Multithreading;
import me.kavin.subgap.utils.SubscribersUtil;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Game.GameType;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class DiscordListener extends ListenerAdapter {

	public static void init() {
		Main.api.addEventListener(new DiscordListener());
	}

	private void setPresence() {
		Main.api.getPresence().setGame(
				Game.of(GameType.WATCHING, " the sub gap! | >help | " + Main.api.getGuilds().size() + " Servers!"));
	}

	@Override
	public void onReady(ReadyEvent event) {
		Main.api.getPresence().setStatus(OnlineStatus.ONLINE);
		setPresence();
		SubscribersUtil.initialise();
		FirebaseUtils.initialise();
		new CommandManager();
	}

	@Override
	public void onGuildJoin(GuildJoinEvent event) {
		setPresence();
	}

	@Override
	public void onTextChannelCreate(TextChannelCreateEvent event) {
		if (event.getChannel().getName().equalsIgnoreCase("Sub-Gap")) {
			EmbedBuilder meb = new EmbedBuilder();
			meb.setColor(Constants.NO_COLOR_EMBED);
			meb.setDescription(
					"You have created a sub-gap channel! I will periodically post automated updates about the subscriber war!");
			event.getChannel().sendMessage(meb.build()).queue();
		}
	}

	@Override
	public void onGuildLeave(GuildLeaveEvent event) {
		setPresence();
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.isFromType(ChannelType.PRIVATE) || event.getAuthor() == Main.api.getSelfUser()
				|| event.getAuthor().isBot())
			return;

		{
			if (event.getMessage().getContentRaw().length() > 0 && event.getMessage().getContentRaw().charAt(0) == '>')
				for (Command cmd : CommandManager.commands)
					if (event.getMessage().getContentRaw().split(" ")[0].equalsIgnoreCase(cmd.getPrefix()))
						Multithreading.runAsync(new CommandExecutor(cmd, event));
		}

	}
}