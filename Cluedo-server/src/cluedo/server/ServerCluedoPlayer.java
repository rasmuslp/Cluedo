package cluedo.server;

import java.util.concurrent.LinkedBlockingQueue;

import crossnet.message.Message;

public class ServerCluedoPlayer {

	public LinkedBlockingQueue< Message > incommingMessages = new LinkedBlockingQueue<>();

	/**
	 * Denotes whether this player is an active part of the game.
	 * 
	 * {@code False} iff this player has lost the game by making a false accusation.
	 */
	private boolean active = true;

	/**
	 * Sets this player as not active.
	 */
	public void setPassive() {
		this.active = false;
	}

	/**
	 * Checks whether this player is active.
	 * 
	 * @return {@code True} iff this player is active. {@code False} otherwise.
	 */
	public boolean isActive() {
		return this.active;
	}

}
