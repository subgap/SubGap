package me.kavin.subgap.botlist;

import org.json.JSONObject;

import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import me.kavin.subgap.Main;
import me.kavin.subgap.consts.Constants;

public class DiscordBotsPostTask implements Runnable {

	@Override
	public void run() {
		JSONObject obj = new JSONObject().put("server_count", Main.api.getGuilds().size());
		try {
			Unirest.post("https://discordbots.org/api/bots/" + Main.api.getSelfUser().getId() + "/stats")
					.header("Authorization", Constants.DBL_TOKEN).header("Content-Type", "application/json")
					.body(obj.toString()).asEmpty();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
	}
}
