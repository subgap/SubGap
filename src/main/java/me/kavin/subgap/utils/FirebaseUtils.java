package me.kavin.subgap.utils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import kong.unirest.Unirest;
import me.kavin.subgap.consts.Constants;

public class FirebaseUtils {

	private static Firestore db;
	private static Object2IntOpenHashMap<String> votes = new Object2IntOpenHashMap<>();

	public static void initialise() {
		try {
			GoogleCredentials credentials = GoogleCredentials.fromStream(IOUtils
					.toInputStream(Unirest.get(Constants.FIREBASE_CREDENTIALS_URL).asString().getBody(), "UTF-8"));
			FirestoreOptions options = FirestoreOptions.newBuilder().setTimestampsInSnapshotsEnabled(true)
					.setCredentials(credentials).build();

			db = options.getService();
		} catch (Exception e) {
		}
	}

	public static int getVotes(String channel) throws InterruptedException, ExecutionException {
		if (votes.containsKey(channel)) {
			return votes.getInt(channel);
		} else {
			return votes.put(channel, db.collection("bot").document("votes").get().get().getLong(channel).intValue());
		}
	}

	public static void addVote(String channel) throws InterruptedException, ExecutionException {
		votes.put(channel, getVotes(channel) + 1);
		db.collection("bot").document("votes").update(channel, getVotes(channel) + 1);
	}

	public static boolean canVote(long id) throws InterruptedException, ExecutionException {
		DocumentSnapshot snapshot = db.collection("bot").document("voters").get().get();
		if (snapshot.contains(String.valueOf(id)))
			return (System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1) - snapshot.getLong(String.valueOf(id))) > 0;
		else
			return true;
	}

	public static void setVoted(long id) throws InterruptedException, ExecutionException {
		db.collection("bot").document("voters").update(String.valueOf(id), System.currentTimeMillis());
	}
}
