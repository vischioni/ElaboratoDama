package it.univr.dama.controller;

/*
 	private static final int WHITE_SQUARE = -1;
	private static final int BLACK_SQUARE = 0;
	private static final int WHITE_MAN = 1;
	private static final int BLACK_MAN = 2;
	private static final int WHITE_KING = 3;
	private static final int BLACK_KING = 4; 
 
 */
/**
 * 
 * @author Vischioni Matteo, Parezzan Federico
 *
 */
public class Move {
	private int sourceX;
	private int sourceY;
	private int destinationX;
	private int destinationY;
	private int weight;
	
	/**

	 */
	public Move(int sourceX, int sourceY, int destinationX, int destinationY) {
		this.sourceX = sourceX;
		this.sourceY = sourceY;
		this.destinationX = destinationX;
		this.destinationY = destinationY;
	}
	
	/**
     * Costruisce una mossa, data posizione di partenza e di destinazione e peso
	 * 
	 * @param sourceX riga di partenza
	 * @param sourceY colonna di partenza
	 * @param destinationX riga di destinazione
	 * @param destinationY colonna di destinazione
	 * @param weight peso della mossa
	 */
	public Move(int sourceX, int sourceY, int destinationX, int destinationY, int weight) {
		this.sourceX = sourceX;
		this.sourceY = sourceY;
		this.destinationX = destinationX;
		this.destinationY = destinationY;
		this.weight = weight;
	}
	
	/**
	 * Costruttore di copia
	 * @param other Mossa da copiare
	 */
	public Move(Move other) {
		this.sourceX = other.sourceX;
		this.sourceY = other.sourceY;
		this.destinationX = other.destinationX;
		this.destinationY = other.destinationY;
		this.weight = other.weight;
	}

	/**
	 * 
	 * @return riga di partenza 
	 */
	public int getSourceX(){
		return sourceX;
	}
	
	/**
	 * 
	 * @return colonna di partenza
	 */
	public int getSourceY(){
		return sourceY;
	}
	
	/**
	 * 
	 * @return riga di destinazione
	 */
	public int getDestinationX(){
		return destinationX;
	}
	
	/**
	 * 
	 * @return colonna di destinazione
	 */
	public int getDestinationY(){
		return destinationY;
	}

	/**
	 * 
	 * @return true se la mossa è una presa
	 */
	public boolean isCapture(){
		return capturedPieces() > 0;		
	}
	
	/**
	 * 
	 * @return numero di pezzi catturati
	 */
	public int capturedPieces(){
		return weight/100;
	}
	
	/**
	 * 
	 * @return peso della mossa
	 */
	public int getWeight(){
		return weight;
	}
	
	/**
	 * Aggiunge il peso
	 * @param x peso da aggiungere
	 */
	public void addWeight(int x){
		weight += x;
	}
	
	/**
	 * Imposta il peso della mossa
	 * @param x peso da impostare
	 */
	public void setWeight(int x){
		weight = x;
	}	
}


