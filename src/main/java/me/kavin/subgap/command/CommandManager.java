package me.kavin.subgap.command;

import java.util.ArrayList;

import me.kavin.subgap.command.commands.Help;
import me.kavin.subgap.command.commands.SubGap;
import me.kavin.subgap.command.commands.Vote;
import me.kavin.subgap.command.commands.Votes;

public class CommandManager {

	public static ArrayList<Command> commands = new ArrayList<Command>();

	public CommandManager() {
		commands.add(new Help());
		commands.add(new SubGap());
		commands.add(new Vote());
		commands.add(new Votes());
	}
}