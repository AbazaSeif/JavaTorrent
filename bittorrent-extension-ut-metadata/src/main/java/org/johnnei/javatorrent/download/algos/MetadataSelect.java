package org.johnnei.javatorrent.download.algos;

import java.util.Optional;

import org.johnnei.javatorrent.torrent.Torrent;
import org.johnnei.javatorrent.torrent.algos.pieceselector.IPieceSelector;
import org.johnnei.javatorrent.torrent.files.BlockStatus;
import org.johnnei.javatorrent.torrent.files.Piece;
import org.johnnei.javatorrent.torrent.peer.Peer;

public class MetadataSelect implements IPieceSelector {

	private Torrent torrent;

	public MetadataSelect(Torrent torrent) {
		this.torrent = torrent;
	}

	@Override
	public Optional<Piece> getPieceForPeer(Peer p) {
		Piece piece = torrent.getFiles().getPiece(0);
		if (piece.hasBlockWithStatus(BlockStatus.Needed)) {
			return Optional.of(piece);
		}

		return Optional.empty();
	}

}
