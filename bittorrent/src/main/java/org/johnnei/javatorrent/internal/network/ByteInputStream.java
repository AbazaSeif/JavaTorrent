package org.johnnei.javatorrent.internal.network;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ByteInputStream extends DataInputStream {

	/**
	 * The speed in bytes that this inputStream is being read
	 */
	private int speed;

	public ByteInputStream(InputStream in) {
		super(in);
		speed = 0;
	}

	@Override
	public int read() throws IOException {
		int b = super.read();
		if (b != -1) {
			++speed;
		}
		return b;
	}

	public String readString(int length) throws IOException {
		String s = "";
		while (length > 0) {
			s += (char) (read() & 0xFF);
			length--;
		}
		return s;
	}

	public byte[] readByteArray(int length) throws IOException {
		byte[] array = new byte[length];
		readFully(array);
		return array;
	}

	public int pollSpeed() {
		int polledSpeed = speed;
		speed -= polledSpeed;
		return polledSpeed;
	}

}