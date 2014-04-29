package it.univr.dama.model;

import it.univr.dama.controller.Move;

import java.util.ArrayList;
/**
 * 
 * @author Vischioni Matteo, Parezzan Federico
 *
 */

public class Board {
	/*
	private static final int WHITE_SQUARE = -1;	// NOT USED
	private static final int BLACK_SQUARE = -0;	// NOT USED
	private static final int WHITE_MAN = 1;
	private static final int BLACK_MAN = 2;
	private static final int WHITE_KING = 3;
	private static final int BLACK_KING = 4;
	 */
	
	private int[][] board;
	/**
	 * Costruisce una board e la inizializza allo
	 * stato iniziale
	 */
	public Board(){
		board = new int[8][8];
		
		for(int row = 0; row < 8; row++){
			for(int col = 0; col < 8; col++){
				if((row + col) % 2 == 0){
					if(row < 3)
						board[row][col] = 2; //BLACK_MAN;
					if (row > 4)
						board[row][col] = 1; //WHITE_MAN;
				} 
			} 
		}
	} 
		
	/**
	 * Costruttore di copia
	 * 
	 * @param original Board da copiare
	 */
	public Board(Board original){
		this.board = new int[8][8];
		for(int row = 0; row < 8; row++)
			for(int col = 0; col < 8; col++)
				this.board[row][col] = original.board[row][col];
	}
	
	/**
	 *  Ritorna lo stato della board nella posizione x,y
	 *  
	 * @param x riga
	 * @param y colonna
	 * @return valore del pezzo presente nella cella x,y. In caso di cella non valida ritorna -2
	 */
	public int getState(int x, int y){
		try{
			return board[x][y];
		}catch(ArrayIndexOutOfBoundsException e){
			return -2;
		}
	}
	
	/**
	 * Imposta lo stato della board nella posizione x,y
	 * 
	 * @param x riga
	 * @param y colonna
	 * @param state valore del pezzo
	 */
	public void setState(int x, int y, int state){
		board[x][y] = state;
	}
	
	/**
	 * Calcola la lista delle mosse possibili di un giocatore
	 * 
	 * @param white colore del giocatore, true indica bianco, false nero
	 * @return	lista delle mosse possibili
	 */
	public ArrayList<Move> legalMoves(boolean white){
		ArrayList<Move> moves = new ArrayList<Move>();
		ArrayList<Move> ret = new ArrayList<Move>();
		int player = white?1:2;	// player contiene il valore della pedina del giocatore
		int s = white?+1:-1;	// s è il segno, le pedine bianche muovono "in avanti", quelle nere muovono "indietro"
		int max = 0;
		
		/* Calcolo le possibili prese */
		for(int x = 0; x < 8; x++)
			for(int y = 0; y < 8; y++)
				if(board[x][y] == player || board[x][y] == player+2)	// se è dama o pedina del giocatore corrente
					moves.addAll(captures(x,y));						// aggiungo tutte le prese
		
		
		/* Se è vuoto, aggiungo tutti gli spostamenti */
		if(moves.isEmpty()) {
			for(int x = 0; x < 8; x++)
				for(int y = 0; y < 8; y++){
					if(board[x][y] == player || board[x][y] == player+2){	// se pedina o dama, controllo gli spostamenti nella direzione corretta
						
						if(getState(x-1*s, y-1*s)  == 0)
							ret.add(new Move(x,y,x-1*s,y-1*s,0));
						
						if(getState(x-1*s, y+1*s) == 0)
							ret.add(new Move(x,y,x-1*s,y+1*s,0));
					}
					if(board[x][y] == player+2){							// se dama, guardo anche la direzione opposta
						
						if(getState(x+1*s, y+1*s)  == 0)
							ret.add(new Move(x,y,x+1*s,y+1*s,0));
						
						if(getState(x+1*s, y-1*s) == 0)
							ret.add(new Move(x,y,x+1*s,y-1*s,0));
				}
			}
		} 
		/* Altrimenti considero solo le mosse valide */
		else {
			// caclolo il massimo
			for (Move move : moves)		
				max = Math.max(max, move.getWeight());	// 
			// aggiungo ret solo le mosse con weight == max
			for(Move mossa : moves)
				if(mossa.getWeight() == max)
					ret.add(mossa);	
			
		}
		return ret;
	}
	/**
	 * Calcola il peso di una mossa.
	 * Il peso identifica l'importanza di una mossa.
	 * 
	 * @param move Mossa di cui voglio calcolare il peso
	 * @return	peso della mossa
	 */
	private int calcWeight(Move move){
		int max = 0;
		Board tmpBoard = new Board(this);		// creo board temporanea
		tmpBoard.performMove(move);				// eseguo la mossa sulla temporanea
		ArrayList<Move> captures = tmpBoard.capturesAUX(move.getDestinationX(),move.getDestinationY());	// calcolo le prese senza pesi cumulati

		// calcolo il weight massimo tra le prese (attenzione ricorsiva!)
		for(Move m : captures)
			max = Math.max(max, tmpBoard.calcWeight(m));  
		// il weight della mossa è dato dal max delle prese successive sommato a quell della mossa stessa
		return max + move.getWeight();	
	}
	
	/**
	 * Metodo ausiliario per il calcolo delle prese, non cumula i pesi delle prese multiple
	 * 
	 * @param x	riga
	 * @param y colonna
	 * @return prese possibili
	 */
	private ArrayList<Move> capturesAUX(int x, int y){
		/*
		pedina - pedina --> 100
		dama   - pedina --> 110
		dama   - dama   --> 111 
		*/		
		boolean white = getState(x,y)%2 == 1;	// calcolo il turno in base al pezzo in posizione x,y
		ArrayList<Move> moves = new ArrayList<Move>();
		int player = white?1:2;		// player contiene il valore della pedina del giocatore
		int opponent = white?2:1;	// opponent contiene il valore della pedina dell'avvesario
		int s = white?+1:-1;		// s è il segno, le pedine bianche muovono "in avanti", quelle nere muovono "indietro"

		if(board[x][y] == player+2){ 	// se è una dama, controllo se può mangiare dame e pedine nelle quattro direzioni

			if((getState(x-1*s, y-1*s) == opponent+2 && getState(x-2*s, y-2*s) == 0))
				moves.add(new Move(x,y,x-2*s,y-2*s,111));
			
			if((getState(x-1*s, y+1*s) == opponent+2 && getState(x-2*s, y+2*s) == 0))
				moves.add(new Move(x,y,x-2*s,y+2*s,111));
				
			if((getState(x+1*s, y+1*s) == opponent+2) && getState(x+2*s, y+2*s) == 0)
				moves.add(new Move(x,y,x+2*s,y+2*s,111));
			
			if((getState(x+1*s, y-1*s) == opponent+2)&& getState(x+2*s, y-2*s) == 0)
				moves.add(new Move(x,y,x+2*s,y-2*s,111));
			
			if((getState(x+1*s, y+1*s) == opponent) && getState(x+2*s, y+2*s) == 0)
				moves.add(new Move(x,y,x+2*s,y+2*s,101));
			
			if((getState(x+1*s, y-1*s) == opponent)&& getState(x+2*s, y-2*s) == 0)
				moves.add(new Move(x,y,x+2*s,y-2*s,101));
		
			if(getState(x-1*s, y-1*s) == opponent && getState(x-2*s, y-2*s) == 0)
				moves.add(new Move(x,y,x-2*s,y-2*s,101));
			
			if(getState(x-1*s, y+1*s) == opponent && getState(x-2*s, y+2*s) == 0)
				moves.add(new Move(x,y,x-2*s,y+2*s,101));
		
		} else if (board[x][y] == player) {		// se è pedina, controllo se può mangiare pedine nella direzione "corretta"
		
			if(getState(x-1*s, y-1*s) == opponent && getState(x-2*s, y-2*s) == 0)
				moves.add(new Move(x,y,x-2*s,y-2*s,100));
			
			if(getState(x-1*s, y+1*s) == opponent && getState(x-2*s, y+2*s) == 0)
				moves.add(new Move(x,y,x-2*s,y+2*s,100));
		}
		return moves;
	}
	/**
	 * Ritorna la lista delle prese che possono essere effettuate dal pezzo in posizione x,y
	 * 
	 * @param x	riga
	 * @param y colonna
	 * @return prese possibili
	 */
	public ArrayList<Move> captures(int x, int y){
		ArrayList<Move> moves = capturesAUX(x, y);	// calcolo le prese (senza pesi cumulati)
		int max = 0;
		for(Move capture : moves){
			// se presenti, sono prese
			capture.setWeight(calcWeight(capture)); 	// imposto il peso
			max = Math.max(capture.getWeight(), max);	// tengo traccia del weight massimo
		}
				
		ArrayList<Move> ret = new ArrayList<Move>();
		
		// aggiungo alla lista di ritorno solo le mosse di peso massimo
		for(Move move : moves){
			if(move.getWeight() == max)
				ret.add(move);	
		}
		return ret;
	}
	
	/**
	 * Promuove a dama le pedine che sono arrivate in fondo alla damiera
	 */
	public void createKings(){
		for(int col = 0; col < 8; col++){
			if(board[0][col] == 1)
				board[0][col] = 3;
			if(board[7][col] == 2)
				board[7][col] = 4;
		}
	}
	
	/**
	 * Esegue una mossa
	 * @param move	mossa da eseguire
	 */
	public void performMove(Move move){
		// copio il prezzo nella casella di destinazione
		setState(move.getDestinationX(), move.getDestinationY(), getState(move.getSourceX(), move.getSourceY()));
		// lo rimuovo dalla sorgente
		setState(move.getSourceX(), move.getSourceY(), 0);
		// se è una presa, rimuovo il pezzo avverario
		if(move.isCapture())
			setState((move.getSourceX()+move.getDestinationX())/2, (move.getSourceY()+move.getDestinationY())/2, 0);
	}
}
	
