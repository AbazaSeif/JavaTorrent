package org.johnnei.javatorrent.network.network.protocol.utp.packet;

import org.johnnei.javatorrent.network.protocol.UtpSocket;
import org.johnnei.javatorrent.network.protocol.utp.ConnectionState;
import org.johnnei.javatorrent.network.Stream;

public class PacketState extends Packet {
	
	public PacketState() {
	}
	
	public PacketState(int ackNumber) {
		this.acknowledgeNumber = ackNumber;
	}

	@Override
	protected void writePacket(Stream outStream) {
	}

	@Override
	protected void readPacket(Stream inStream) {
	}

	@Override
	public void processPacket(UtpSocket socket) {
		//System.out.println(socket.getPeerClient().getConnectionId() + "| ACKed " + acknowledgeNumber);
		if(acknowledgeNumber == 1 && socket.getConnectionState() == ConnectionState.CONNECTING) {
			socket.setConnectionState(ConnectionState.CONNECTED);
			socket.setUtpInputNumber(sequenceNumber);
		}
	}

	@Override
	public int getId() {
		return UtpProtocol.ST_STATE;
	}

	@Override
	public int getSize() {
		return 20;
	}
}