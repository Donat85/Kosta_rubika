package tutorial3D;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.SystemColor;


public class window  implements ActionListener{

	private JFrame frame;
	private JButton kostka;
	private JButton reset;
	private Kostka Pepega;
	private JTextArea txtPoigkProjektSymulator;
	private JButton btnNewButton;
	private JButton btnNewButton_1;
	boolean utworzono_kostke = false;
	

	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window window = new window(); // stworzenie okna powitalnego
					window.frame.setVisible(true);
					window.frame.setResizable(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	public window() {
		initialize2();
		kostka.addActionListener(this);	//dodanie przycisków do okna
		reset.addActionListener(this);
		btnNewButton.addActionListener(this);
		btnNewButton_1.addActionListener(this);
	}

	
	private void initialize2() {
		
		
		
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		kostka = new JButton("Uruchom program");
		frame.getContentPane().add(kostka, BorderLayout.NORTH);
		
		reset = new JButton("Resetuj kostkê");
		frame.getContentPane().add(reset, BorderLayout.SOUTH);
		
		txtPoigkProjektSymulator = new JTextArea();
		txtPoigkProjektSymulator.setBackground(SystemColor.controlShadow);
		txtPoigkProjektSymulator.setEditable(false);
		txtPoigkProjektSymulator.setFont(new Font("Monotype Corsiva", Font.PLAIN, 18));
		txtPoigkProjektSymulator.setText("                   POiGK projekt"+"\n"+ "              Symulator kostki Rubika"+"\n"+" Donat Stankiewicz & Jan Majewski");
		frame.getContentPane().add(txtPoigkProjektSymulator, BorderLayout.CENTER);
		txtPoigkProjektSymulator.setColumns(10);
		
		btnNewButton = new JButton("Instrukcja");
		frame.getContentPane().add(btnNewButton, BorderLayout.WEST);
		
		btnNewButton_1 = new JButton("Instrukcja");
		frame.getContentPane().add(btnNewButton_1, BorderLayout.EAST);
		
		
		
		
	}

	
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource()==kostka) {

			utworzono_kostke=true;
			Pepega = new Kostka();
			Pepega.resetuj();
		}
		else if(e.getSource()==reset) {
			if(utworzono_kostke)
				Pepega.resetuj();
			else {
				JOptionPane.showMessageDialog(frame, "Stwórz najpierw kostke :/");
		    }
		}
		else if(e.getSource()==btnNewButton || e.getSource()==btnNewButton_1) {
			JOptionPane.showMessageDialog(frame, "Za pomoc¹ klawiszy wsadeq obracamy scianami."+"\n"+"Za pomoc¹ strza³ek zmieniamy oœ wokó³ której chcemy obracaæ."+"\n"+"Za pomoc¹ myszki obracamy kamer¹ wokó³ kostki."+"\n"+"Ruchy wykonujemy na scianie, na któr¹ patrzymy.");
		}
		
		
		
	}

}
