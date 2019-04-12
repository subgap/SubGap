package me.kavin.subgap.threads;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.jsoniter.JsonIterator;

import me.kavin.subgap.consts.Constants;
import me.kavin.subgap.utils.FirebaseUtils;

public class WebServer extends Thread {

	int port;
	private Server server;

	public WebServer(final int port) {
		super("WebServer Thread");
		this.port = port;
	}

	@Override
	public void run() {
		try {
			this.server = new Server(this.port);
			this.server.setHandler(new Handler());
			this.server.start();
			this.server.join();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private class Handler extends AbstractHandler {

		@Override
		public void handle(final String target, final Request baseRequest, final HttpServletRequest request,
				final HttpServletResponse response) throws IOException, ServletException {
			try {
				if (target.equals("/vote") && baseRequest.getMethod().equals("POST")) {

					String secret = baseRequest.getHeader("Authorization");

					if (secret.equals(Constants.DBL_SECRET)) {
						FirebaseUtils.addVote(JsonIterator
								.deserialize(IOUtils.toString(request.getInputStream(), "UTF-8")).toLong("user"));
						IOUtils.write("Vote successfull!", response.getOutputStream(), "UTF-8");
					}

					response.setContentType("text/html");
				}
				response.setStatus(200);
				baseRequest.setHandled(true);
			} catch (Exception e) {
				response.setStatus(200);
				baseRequest.setHandled(true);
				e.printStackTrace();
			}
		}
	}
}
