package org.johnnei.javatorrent.protocol.messages.ut_metadata;

import org.johnnei.javatorrent.protocol.UTMetadata;
import org.johnnei.javatorrent.torrent.files.BlockStatus;
import org.johnnei.javatorrent.torrent.peer.Peer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageReject extends Message {

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageReject.class);

	public MessageReject() {
		super();
	}

	public MessageReject(int piece) {
		super(piece);
	}

	@Override
	public void process(Peer peer) {
		int blockIndex = (int) dictionary.get("piece");
		LOGGER.warn("Piece Request got rejected: " + blockIndex);
		peer.getTorrent().getFileSet().getPiece(0).setBlockStatus(blockIndex, BlockStatus.Needed);
		peer.onReceivedBlock(0, blockIndex);
		peer.getBitTorrentSocket().close();
	}

	@Override
	public int getLength() {
		return bencodedData.length();
	}

	@Override
	public int getId() {
		return UTMetadata.REJECT;
	}

	@Override
	public String toString() {
		return "UT_Metadata Reject";
	}

}