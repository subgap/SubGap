package me.kavin.subgap.listener;

import me.kavin.subgap.Main;
import me.kavin.subgap.command.Command;
import me.kavin.subgap.command.CommandExecutor;
import me.kavin.subgap.command.CommandManager;
import me.kavin.subgap.utils.FirebaseUtils;
import me.kavin.subgap.utils.Multithreading;
import me.kavin.subgap.utils.SubscribersUtil;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Game.GameType;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.events.guild.update.GuildUpdateOwnerEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
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
	public void onGuildUpdateOwner(GuildUpdateOwnerEvent event) {
		event.getGuild().getOwner().getUser().openPrivateChannel().complete()
				.sendMessage("Congrats on becoming the new owner of `" + event.getGuild().getName() + "`!").complete();
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

	@Override
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
		if (event.getAuthor() != Main.api.getSelfUser() && event.getMessage().getContentRaw().startsWith(">"))
			event.getMessage().getChannel().sendMessage("Error: I don't reply to PM's!").complete();
	}
}