package org.johnnei.javatorrent.internal.utp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.johnnei.javatorrent.internal.utp.protocol.PacketType;
import org.johnnei.javatorrent.internal.utp.protocol.packet.Payload;
import org.johnnei.javatorrent.internal.utp.protocol.packet.UtpHeader;
import org.johnnei.javatorrent.internal.utp.protocol.packet.UtpPacket;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PacketLossHandlerTest {

	private UtpSocket socket;

	private PacketLossHandler cut;

	private UtpPacket sentPacket;
	private UtpPacket sentPacket2;

	private UtpPacket packet;

	@BeforeEach
	public void setUp() {
		socket = mock(UtpSocket.class);
		cut = new PacketLossHandler(socket);

		Payload payload = mock(Payload.class);
		when(payload.getType()).thenReturn(PacketType.STATE);

		packet = mock(UtpPacket.class);
		UtpHeader header = mock(UtpHeader.class);
		when(packet.getHeader()).thenReturn(header);
		when(packet.getPayload()).thenReturn(payload);
		when(header.getAcknowledgeNumber()).thenReturn((short) 4);

		sentPacket = mock(UtpPacket.class);
		UtpHeader header3 = mock(UtpHeader.class);
		when(sentPacket.getHeader()).thenReturn(header3);
		when(header3.getSequenceNumber()).thenReturn((short) 4);

		sentPacket2 = mock(UtpPacket.class);
		UtpHeader header4 = mock(UtpHeader.class);
		when(sentPacket2.getHeader()).thenReturn(header4);
		when(header4.getSequenceNumber()).thenReturn((short) 5);
	}

	@Test
	public void testOnReceivedPacketNotBeenAckedAtLeastTrice() {
		// Register packet as in flight
		cut.onSentPacket(sentPacket);

		cut.onReceivedPacket(packet);
		cut.onReceivedPacket(packet);

		verify(socket, never()).resend(any());
	}

	@Test
	public void testOnReceivedPacketLostNotSentPacket() {
		// Register packet as in flight
		cut.onSentPacket(sentPacket);

		// Handle as if was lost.
		cut.onReceivedPacket(packet);
		cut.onReceivedPacket(packet);
		cut.onReceivedPacket(packet);

		verify(socket, never()).resend(any());
	}

	@Test
	public void testOnReceivedPacket() {
		// Register packet as in flight
		cut.onSentPacket(sentPacket);
		cut.onSentPacket(sentPacket2);

		// Handle as if was lost.
		cut.onReceivedPacket(packet);
		cut.onReceivedPacket(packet);
		cut.onReceivedPacket(packet);
		cut.onReceivedPacket(packet);

		verify(socket).resend(sentPacket2);
	}

	@Test
	public void testOnReceivedPacketPreventDoubleResent() {
		// Register packet as in flight
		cut.onSentPacket(sentPacket);
		cut.onSentPacket(sentPacket2);

		// Handle as if was lost.
		cut.onReceivedPacket(packet);
		cut.onReceivedPacket(packet);
		cut.onReceivedPacket(packet);
		cut.onReceivedPacket(packet);

		// At this point the resend should be triggered.
		verify(socket).resend(sentPacket2);

		cut.onReceivedPacket(packet);

		// At this point no second resend should be triggered as the packet was not sent yet.
		verify(socket).resend(sentPacket2);
	}

	@Test
	public void testOnReceivedPacketAllowResendAfterResend() {
		// Register packet as in flight
		cut.onSentPacket(sentPacket);
		cut.onSentPacket(sentPacket2);

		// Handle as if was lost.
		cut.onReceivedPacket(packet);
		cut.onReceivedPacket(packet);
		cut.onReceivedPacket(packet);
		cut.onReceivedPacket(packet);

		verify(socket).resend(sentPacket2);

		// This should clear the resend queue
		cut.onSentPacket(sentPacket2);

		// This should trigger second resend
		cut.onReceivedPacket(packet);

		verify(socket, times(2)).resend(sentPacket2);
	}
}
