package it.univr.dama.view;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
/**
 * 
 * @author Vischioni Matteo, Parezzan Federico
 *
 */
public class ScrollTextFrame extends JFrame {
	private static final int HEIGHT = 150;
	private static final int WIDTH = 300;
	
	private JScrollPane scroll;
	private JTextArea display;
	
	/**
	 * Crea un nuovo frame, contenente un campo di testo non editabile, con scorrimento
	 */
	public ScrollTextFrame(){
		setSize(HEIGHT, WIDTH);
		setLocation(700, 200);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		display = new JTextArea();
	    display.setEditable(false);
	    display.setBackground(Color.lightGray);
	    display.setFont(new Font("Helvetica", Font.BOLD, 12));
	    scroll = new JScrollPane(display);	// aggiungo una batta di scorrimento a display
	    scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS); // la barra viene sempre visualizzata
	    add(scroll);
	}
	
	/**
	 * Imposta il testo
	 * @param t testo da visualizzare
	 */
	public void setText(String t){
		display.setText(t);
	}

}
