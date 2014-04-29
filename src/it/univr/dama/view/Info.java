package it.univr.dama.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import it.univr.dama.controller.Move;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;
/**
 * 
 * @author Vischioni Matteo, Parezzan Federico
 *
 */
public class Info extends JFrame{
	private static final int HEIGHT = 150;
	private static final int WIDTH = 300;

	private String historyStr;
	private Move last;
	private JLabel msg;
	
	private ScrollTextFrame history;
	
	private boolean turn;

	/**
	 * Costruisce un frame di informazioni, dato il turno corrente
	 * @param turn turno corrente
	 */
	public Info(boolean turn) {
		
		setSize(WIDTH, HEIGHT);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		historyStr = " ";	// lo spazio è più importante di quello che sembra --> la stringa non è vuota!!
		this.turn = turn;
		this.setLocation(700, 20);
		msg = new JLabel("");
		msg.setHorizontalAlignment(SwingConstants.CENTER);

		history = new ScrollTextFrame();
		
		createMenu();
		add(msg);
		
        showInfo();
	}

	/**
	 * Costruisce un frame di informazioni, con turno bianco
	 */
	public Info(){
		this(true);
	}
	

	/**
	 * Cambia il turno e lo mostra a video
	 */
	public void changeTurn(){
		turn = !turn;
	}
	
	public void showInfo(){
		msg.setText("Turno del "+(turn?"Bianco":"Rosso"));
		history.setText(historyStr);
	}
	
	
	/**
	 * Scrive una mossa sullo storico
	 * @param move mossa 
	 */
	public void writeMove(Move move){
		// prima mossa, è sicuramente uno spostamento...
		if(last == null){ 
			historyStr = (turn?"Bianco":"Rosso") + ": " + cartesianToNumber(move.getSourceX(), move.getSourceY());
			historyStr +=  "-";	//move.isCapture()?"x":"-"; 
			historyStr += cartesianToNumber(move.getDestinationX(), move.getDestinationY());
		// mossa con sorgente == destinazione precedente, sicuramente presa
		} else if(last.getDestinationX() == move.getSourceX() && last.getDestinationY() == move.getSourceY()){	
			historyStr += "x";	//move.isCapture()?"x":"-";
			historyStr += cartesianToNumber(move.getDestinationX(), move.getDestinationY());
		// nuova mossa
		} else {	
			historyStr += "\n";
			historyStr += (turn?"Bianco":"Rosso") + ": " + cartesianToNumber(move.getSourceX(), move.getSourceY());
			historyStr += move.isCapture()?"x":"-";
			historyStr += cartesianToNumber(move.getDestinationX(), move.getDestinationY());
		}
		last = move;
	}

	private int cartesianToNumber(int x, int y){
		return x*4+y/2+1;
	}
	
	private void createMenu(){
		// creo la barra
		JMenuBar menuBar = new JMenuBar();	
	    
		// creo i menu
		JMenu fileMenu = new JMenu("File");	
	    JMenu viewMenu = new JMenu("View");
	    JMenu infoMenu = new JMenu("?");

	    // creo gli oggetti (sotto menu)
	    JMenuItem exitItem = new JMenuItem("Exit");
	    JMenuItem historyItem = new JMenuItem("History");
	    JMenuItem aboutItem = new JMenuItem("About");
//	    JMenuItem rulesItem = new JMenuItem("Rules");
	    
	    // aggiungo i listener
	    
	    exitItem.addActionListener(
	    		new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent arg0) {
						System.exit(0);
						
					}
	    		}
	    );
	    
	    historyItem.addActionListener(
	    		new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent arg0) {
						if(history.isVisible())
							history.setVisible(false);
						else
							history.setVisible(true);
					}
	    		}
	    );
	    
	    aboutItem.addActionListener(
	    		new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent arg0) {
						JOptionPane.showMessageDialog(null, "Gioco della Dama Italiana\n v 1.01");
						
					}
	    		}
	    );
	    
	    // aggiungo i sotto menu ai menu
	    fileMenu.add(exitItem);
	    viewMenu.add(historyItem);
	    infoMenu.add(aboutItem);
//	    infoMenu.add(rulesItem);
	    
	    
	    // aggiungo i menu alla barra
	    menuBar.add(fileMenu);
	    menuBar.add(viewMenu);
	    menuBar.add(infoMenu);
	    
	    // imposto la barra su frame
	    setJMenuBar(menuBar);
	}
	
}
