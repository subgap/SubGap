package me.kavin.subgap.utils;

import java.util.concurrent.ExecutionException;

import me.kavin.subgap.consts.Constants;

public class Channel {

	private String name;
	private String id;
	private int subscribers;

	public Channel(String name, String id) {
		this.name = name;
		this.id = id;
	}

	public int getSubscribers() {
		return subscribers;
	}

	public String getSubscribersDisplay() {
		return Constants.df.format(subscribers);
	}

	public void setSubscribers(int subscribers) {
		this.subscribers = subscribers;
	}

	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}

	public int getVotes() throws InterruptedException, ExecutionException {
		return FirebaseUtils.getVotes(name);
	}

	public String getVotesDisplay() throws InterruptedException, ExecutionException {
		return Constants.df.format(getVotes());
	}
}
