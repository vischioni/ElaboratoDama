package it.univr.dama.controller;

import java.util.ArrayList;
import it.univr.dama.model.Board;

/**
 * 
 * @author Vischioni Matteo, Parezzan Federico
 *
 */
public class ArtificialIntelligence {

	private Board board;
	private boolean white;

	/**
	 * Costruisce un'intelligenza artificiale
	 * 
	 * @param board Damiera su cui deve giocare
	 * @param white	Giocatore su cui opera IA (true per il bianco, false per il nero)
	 */
	public ArtificialIntelligence(Board board, boolean white) {
		this.board = board;
		this.white = white;
	}

	/**
	 * Calcola le mosse da eseguire
	 * 
	 * @return Lista delle mosse da eseguire
	 */
	public ArrayList<Move> calcMoves() {
		ArrayList<Move> oppoMoves, myMoves, tmpMoves;
		ArrayList<ArrayList<Move>> mosseRet = new ArrayList<ArrayList<Move>>();
		Board tmpBoard;
		int pesoMax = -1200;
		int peso;
		Move next;
		tmpMoves = new ArrayList<Move>();
		myMoves = this.board.legalMoves(white); // calcolo le mie mosse

		// per ogni mossa
		for (Move move : myMoves) {			
			tmpMoves.clear();
			tmpBoard = new Board(board);	// creo board temporanea	
			
			
			tmpBoard.performMove(move);		// la simulo
			tmpMoves.add(move);				// la aggiungo alla lista
			
			
			// per ogni altro pezzo che posso mangiare
			next = new Move(move);
			for(int i = 1; i < move.capturedPieces();i++){ 
				next = tmpBoard.captures(next.getDestinationX(), next.getDestinationY()).get(0);	// calcolo la prossima mossa
				tmpBoard.performMove(next);	// la simulo
				tmpMoves.add(next);			// la aggiungo alla lista
			}

			oppoMoves = tmpBoard.legalMoves(!white); // calcolo le mosse dell'avversario
												
			if (oppoMoves.isEmpty())		// se è vuoto ho vinto
				return tmpMoves; 			// ritorno le mosse!

			// calcolo il peso come weight mossa mia - weight mossa avversario
			// ignoro l'ultima cifra, mi fornisce informazioni sul pezzo mosso, non su quello mangiato
			peso = move.getWeight() - oppoMoves.get(0).getWeight();
			
			if (peso > pesoMax) {	// se ho trovato una mossa migliore delle precedenti
				pesoMax = peso;		// aggiorno il peso massimo
				mosseRet.clear();	// pulisco la lista
				mosseRet.add(new ArrayList<Move>(tmpMoves));	// la aggiungo
			}

			else if (peso == pesoMax) {	// se ho trovato una mossa con peso uguale al massimo
				mosseRet.add(new ArrayList<Move>(tmpMoves));	// la aggiungo alla lista
			}
		}	// fine for-each
		
		// scelgo una mossa a caso tra quelle calcolate
		int indexMossa = (int) ((Math.random() * 1234) % mosseRet.size());
		return mosseRet.get(indexMossa);

	}
}
