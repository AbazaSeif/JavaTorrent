package org.johnnei.javatorrent.network.protocol;

import org.johnnei.javatorrent.network.InStream;
import org.johnnei.javatorrent.network.OutStream;
import org.johnnei.javatorrent.torrent.download.peer.Peer;

public interface IMessage {

	/**
	 * Writes the message to the output stream
	 *
	 * @param outStream
	 */
	public void write(OutStream outStream);

	/***
	 * Read a message from the inputStream
	 *
	 * @param inStream
	 */
	public void read(InStream inStream);

	/**
	 * Process the message
	 *
	 * @param peer The client which should process this message
	 */
	public void process(Peer peer);

	/**
	 * The length of the message (only needed on writing)<br/>
	 * The length should include the byte for the message id
	 *
	 * @return integer
	 */
	public int getLength();

	/**
	 * The id of this message
	 *
	 * @return id
	 */
	public int getId();

	/**
	 * Sets the read duration of this message
	 *
	 * @param duration The duration in milliseconds it took to read this message
	 */
	public void setReadDuration(int duration);

	@Override
	public String toString();

}
