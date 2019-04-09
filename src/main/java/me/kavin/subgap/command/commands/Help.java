package me.kavin.subgap.command.commands;

import me.kavin.subgap.command.Command;
import me.kavin.subgap.command.CommandManager;
import me.kavin.subgap.consts.Constants;
import me.kavin.subgap.consts.Emojis;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Help extends Command {
	public Help() {
		super(">help", "`Shows this message`");
	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) throws Throwable {
		{
			EmbedBuilder meb = new EmbedBuilder();
			meb.setColor(Constants.NO_COLOR_EMBED);
			meb.setTitle("Sub Gap's Commands:");
			meb.setDescription("Help sent! Check DMs! " + Emojis.WHITE_CHECK_MARK
					+ "\n If you have dm's disabled, click [here](https://github.com/subgap/SubGap/blob/master/COMMANDS.md)");
			event.getChannel().sendMessage(meb.build()).complete();
		}
		{
			EmbedBuilder meb = new EmbedBuilder();
			meb.setColor(Constants.NO_COLOR_EMBED);
			meb.setTitle("Sub Gap's Commands:");
			PrivateChannel pc = event.getAuthor().openPrivateChannel().submit().get();
			for (Command cmd : CommandManager.commands) {
				if (meb.getFields().size() >= 25) {
					pc.sendMessage(meb.build()).complete();
					meb.clearFields();
					meb.setTitle(" ");
					meb.addField(cmd.getPrefix(), cmd.getHelp() + '\n', false);
				} else {
					meb.addField(cmd.getPrefix(), cmd.getHelp() + '\n', false);
				}

			}
			if (meb.getFields().size() > 0)
				pc.sendMessage(meb.build()).complete();
		}
	}
}