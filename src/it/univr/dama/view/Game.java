package it.univr.dama.view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import java.util.ArrayList;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import it.univr.dama.controller.ArtificialIntelligence;
import it.univr.dama.controller.Move;
import it.univr.dama.model.Board;

/**
 * 
 * @author Vischioni Matteo, Parezzan Federico
 *
 */
public class Game extends JFrame {
	private static final int HEIGHT = 640;
	private static final int WIDTH = 640;
	private final Board board;

	private ArrayList<Move> moves;
	private ArrayList<Move> moves2;
	private ArtificialIntelligence ia;
	private boolean turn;
	
	private Timer t;
	private Info info;
	
	
	/**
	 * Costruisce un gioco a partire da una damiera
	 * @param board Damiera che rappresenta la situazione del gioco
	 */
	public Game(Board board){
		super("Go ahead, make my day...");
		setLayout(new GridLayout(8, 8));
		setSize(WIDTH, HEIGHT);
		setLocation(20, 20);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		
		this.board = board;
		turn = true;
		moves = board.legalMoves(turn);						// calcolo le mosse del giocatore
		moves2 = new ArrayList<Move>();
		info = new Info(turn);
		info.setVisible(true);
		ia = new ArtificialIntelligence(board, false);		// ia gioca sul nero
		showBoard();			
	}
	/**
	 * Costruisce un gioco con una damiera in posizione di partenza
	 */
	public Game(){
		this(new Board());
	}
	/**
	 * Ridisegna a video la situazione di gioco, cancellando quella precedente.
	 */
	private void showBoard() {
		getContentPane().removeAll();		// rimuovo tutto
		for (int x = 0; x < 8; x++){
			for (int y = 0; y < 8; y++){
				if((x+y)%2==1)
					addWhiteSquare();
				else
					switch(board.getState(x, y)) {
					case 1:
						addWhiteMan(x, y);
						break;
					case 2:
						addBlackMan(x,y);
						break;
					case 3:
						addWhiteKing(x,y);
						break;
					case 4:
						addBlackKing(x,y);
						break;
					default:
						addBlackSquare(x,y);
						break;
				}
			}
		}
		info.showInfo();
		invalidate();
		validate();
	}	
	
	private void addWhiteSquare(){
		JButton nuovo = new WhiteSquare();
		add(nuovo);
	}
	
	private void addBlackSquare(final int x, final int y){
		JButton nuovo = new BlackSquare();
		if(turn)
			nuovo.addActionListener(new SquareListener(x,y));
		add(nuovo);
	}
	
	private void addBlackMan(final int x, final int y){
		add(new BlackMan());
	}
	
	private void addBlackKing(final int x, final int y){
		add(new BlackKing());	
	}
	
	private void addWhiteMan(final int x, final int y){
		JButton nuovo = new WhiteMan();
		if(turn) 
			nuovo.addActionListener(new PieceListener(x,y));
		add(nuovo);
	}

	private void addWhiteKing(final int x, final int y){
		JButton nuovo = new WhiteKing();
		if(turn) 
			nuovo.addActionListener(new PieceListener(x,y));
		add(nuovo);
	}
	
	private void changeTurn(){
		board.createKings();	// creo le dame del giocatore che ha appena finito
		info.changeTurn();		// cambio il turno sulle info
		turn = !turn;			// cambio il turno
		moves = board.legalMoves(turn);	// calcolo le mosse 
		moves2.clear();			// svuoto la lista
		
		showBoard();
		
		if(moves.isEmpty()){	// se non ho mosse disponibili, ho perso!
			JOptionPane.showMessageDialog(null, "Vince il giocatore " + (turn?"Rosso"/*"Nero"*/:"Bianco"));
			System.exit(0);
		} 
		
		if(!turn)	// se turn è false, tocca all'ia
			iaTurn();
	}

	private void iaTurn(){
		// creo un nuovo time: delay=700ms, ActionListener=mover sulle mosse dell'ia
		t = new Timer(700, new IAmover(ia.calcMoves()));	
		t.start();
	}

	public static void main(String[] args){
		new Game().setVisible(true);
	}
	
	private void performMove(Move move){
		board.performMove(move);	// eseguo la mossa
		info.writeMove(move);		// la scrivo sullo storico
	}
	
	private class PieceListener implements ActionListener{
		private int x;
		private int y;
		public PieceListener(int x, int y){
			this.x = x;
			this.y = y;
		}
		public void actionPerformed(ActionEvent arg0) {
			moves2.clear();
			// per ogni mossa possibile
			for(Move move : moves)	{
				// se ne esiste una con sorgente = casella cliccata
				if(move.getSourceX() == x && move.getSourceY() == y)	 
					moves2.add(move);	// la aggiungo
			}
		}
	}	// classe int
	
	protected class SquareListener implements ActionListener{
		private int x;
		private int y;
		public SquareListener(int x, int y){
			this.x = x;
			this.y = y;
		}
		
		public void actionPerformed(ActionEvent arg0) {
			// per ogni mossa nella lista di quelle originate da una casella cliccata
			for(Move move : moves2)	{
				// se ne esiste una con sorgente = casella cliccata
				if(move.getDestinationX() == x && move.getDestinationY() == y){
					performMove(move);			// la eseguo
					if(move.isCapture()){		// se è una presa
						moves = board.captures(x, y);	// le mosse possibili diventano le prese successive
						
						if((moves.isEmpty()))	// se non ce ne sono più
							 changeTurn();		// cambio il turno
						else					// altrimenti sono in presa multipla
							moves2 = new ArrayList<Move>(moves); // tengo selezionata lo stesso pezzo
					
					} else						// altrimenti è uno spostamento
						changeTurn();			// cambio il turno
					
					break;						// esco, data sorgente e destinazione esiste una e una sola mossa!
				}	// if-else
			}  // for-each
			showBoard();
		} // metodo	
	}	// classe int

	protected class IAmover implements ActionListener{
		private int index;
		private ArrayList<Move> moves;
		
		public IAmover(ArrayList<Move> moves){
			index = 0;
			this.moves = moves;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			if(index < moves.size()){					// se ho ancora mosse da fare
				board.performMove(moves.get(index++));	// recupero la prossima mossa e la eseguo (post-incremento!)
				showBoard();							// mostro la board aggiornata
			}
			if(index >= moves.size()){					// se non ho più mosse disponibili (basterebbe ==)
				t.stop();								// fermo il timer
				changeTurn();							// cambio il turno
			}
		} 
	}	// classe int
}
