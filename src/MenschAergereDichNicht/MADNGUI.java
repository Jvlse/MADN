package MenschAergereDichNicht;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class MADNGUI {
	private Spiel spiel;

	private SpielController controller;
	private JFrame main = new JFrame("Mensch aergere dich nicht");
	private JButton wuerfel = new JButton();
	private JLabel amZug = new JLabel("Am Zug");
	private JButton zug = new JButton();

	private JComboBox<Integer> w_cheat = new JComboBox<Integer>();
	private JTextField feld_cheat = new JTextField();

	int basisx = 40;
	int basisy = 40;
	int basisextra = 50;
	int breite = 600;
	int hoehe = 680;

	private String path = System.getProperty("user.dir");
	private Random rand;

	private boolean cheats;

	public MADNGUI(Spiel s, SpielController c, boolean cheat, int bots) {
		this.spiel = s;
		this.controller = c;

		String feld_bild = ("Bilder/feld.png");

		wuerfel.setBounds(400, 620, 50, 50);
		wuerfel.addActionListener(e -> wuerfeln());
		bildLaden(wuerfel, "0", 50);

		amZug.setBounds(200, 600, 100, 20);
		zug.setBounds(200, 620, 50, 50);
		bildLaden(zug, spiel.getAmZug() + "spieler", 50);

		if (cheat == true) {
			w_cheat.setBounds(480, 620, 80, 50);
			for (int i = 1; i < 7; i++) {
				w_cheat.addItem(i);
			}
			w_cheat.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					spiel.setWuerfel(w_cheat.getSelectedIndex() + 1);
					wuerfel_GUI_anpassen();
					n_Layout_HK();
				}
			});
			main.add(w_cheat);
			feld_cheat.setBounds(50, 620, 80, 50);
			feld_cheat.getDocument().addDocumentListener(new DocumentListener() {

				@Override
				public void removeUpdate(DocumentEvent e) {

				}

				@Override
				public void insertUpdate(DocumentEvent e) {
					wuerfel_GUI_anpassen();
				}

				@Override
				public void changedUpdate(DocumentEvent e) {

				}
			});
			main.add(feld_cheat);
			zug.addActionListener(e -> naechsterzug());
			feld_bild = ("Bilder/feldcheat.png");
		}

		main.add(amZug);
		main.add(zug);
		main.add(wuerfel);
		main.setResizable(false);

		main.setBounds((int) ((Toolkit.getDefaultToolkit().getScreenSize().getWidth() - breite) / 2),
				(int) ((Toolkit.getDefaultToolkit().getScreenSize().getHeight() - hoehe) / 2), breite, hoehe);
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ImagePanel panel = new ImagePanel(new ImageIcon(feld_bild).getImage());
		main.getContentPane().add(panel);
		main.pack();
		main.setLayout(null);
		main.setVisible(true);
		main.addComponentListener(bewegung);

		spiel.erzeugen_Fig(this, bots);
		n_Layout_HK();
	}

	public JFrame getMain() {
		return main;
	}

	public void n_Layout_HK() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				spiel.getFiguren()[i][j].grabFocus();
			}
		}
	}

	public void zug(ActionEvent e) {
		int setzug = -2;
		try {
			setzug = Integer.parseInt(feld_cheat.getText());
		} catch (NumberFormatException nfe) {
			setzug = -2;
		}
		if (setzug > 0 && setzug < 40) {
			if (controller.setFigur(setzug, (Spielfigur) e.getSource())) {

				setfigur_gui((Spielfigur) e.getSource(), setzug);

			}
			wuerfel.setEnabled(true);
		} else {
			if (!controller.zugpruef((Spielfigur) e.getSource())) {
				if (spiel.getaStart()[spiel.getAmZug()] == 4) {
					naechsterzug();

				} else {
					if (!controller.zugnichtmoeglich()) {
						naechsterzug();
					}

				}

			} else {
				controller.zug((Spielfigur) e.getSource());

				if (spiel.getSpielgewonnen() != -1) {
					sieg();
				} else {
					for (int i = 0; i < 4; i++) {
						for (JButton figur : spiel.getFiguren()[i]) {
							figur.setEnabled(false);
						}
					}
					wuerfel.setEnabled(true);

					if (spiel.getWuerfel() != 6) {
						naechsterzug();
					}
				}
			}
		}
	}

	public void zug(int arrayZeile) {
		if (spiel.getSpielgewonnen() == -1) {
			controller.wuerfeln();
			if(spiel.getWuerfel() == 6 && spiel.getaStart()[arrayZeile] > 0) {
				int figur = -1;
				for (int i = 0; i < 4; i++) {
					if (spiel.getFiguren()[arrayZeile][i].getFeld() == -1) {
						figur = i;
						break;
					}
				}
				if (!controller.zugpruef(spiel.getFiguren()[arrayZeile][figur]) || figur == -1) {
					if (controller.zugnichtmoeglich()) {
						naechsterzug();
					} else {
						Spielfigur spieler = spiel.getFiguren()[arrayZeile][controller.randomFigur(arrayZeile)];
						controller.zug(spieler);
					}
				} else {
					controller.startzug(spiel.getFiguren()[arrayZeile][figur]);
				}
			} else {
				for (int i = 0; i < 4; i++) {
					if(controller.zugpruef(spiel.getFiguren()[arrayZeile][i])) {
						int schlagen = controller.schlagPruef(arrayZeile);
						if (schlagen == -1) {
							if (controller.zugnichtmoeglich()) {
								naechsterzug();
							} else {
								Spielfigur spieler = spiel.getFiguren()[arrayZeile][controller.randomFigur(arrayZeile)];
								controller.zug(spieler);
							}
						} else {
							controller.zug(spiel.getFiguren()[arrayZeile][schlagen]);
						}
					}
				}
			}
			
			for (int i = 0; i < 4; i++) {
				for (JButton figur : spiel.getFiguren()[i]) {
					figur.setEnabled(false);
				}
			}
			if (spiel.getWuerfel() != 6) {
				naechsterzug();
			} else {
				zug(arrayZeile);
			}
		} else {
			sieg();
		}
	}

	public void wuerfeln() {

		spiel_sound("wuerfel.wav");
		controller.wuerfeln();
		wuerfel_GUI_anpassen();
	}

	private void spiel_sound(String was) {
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(new File(path + "/Sound/" + was)));
			clip.start();
		} catch (Exception exc) {
			exc.printStackTrace(System.out);
		}

	}

	private void wuerfel_GUI_anpassen() {
		bildLaden(wuerfel, "" + spiel.getWuerfel(), 50);

		wuerfel.setEnabled(false);
		for (int i = 0; i < 4; i++) {
			for (JButton figur : spiel.getFiguren()[i]) {
				figur.setEnabled(true);
			}
		}
	}

	public void naechsterzug() {
		spiel.setAmZug((spiel.getAmZug() + 1) % 4);

		Spielfigur[][] figuren = spiel.getFiguren();
		if (figuren[spiel.getAmZug()][0].getBot() == true) {
			zug(spiel.getAmZug());
			bildLaden(zug, spiel.getAmZug() + "spieler", 50);
		} else {
			for (int i = 0; i < 4; i++) {
				for (JButton figur : spiel.getFiguren()[i]) {
					figur.setEnabled(false);
				}
			}
			wuerfel.setEnabled(true);
			bildLaden(zug, spiel.getAmZug() + "spieler", 50);
		}
	}

	public void sieg() {
		for (int i = 0; i < 4; i++) {
			for (JButton figur : spiel.getFiguren()[i]) {
				figur.setEnabled(false);
			}
		}
		wuerfel.setEnabled(false);
		String tmp = "";
		switch (spiel.getSpielgewonnen()) {
		case 0:
			tmp = ("Schwarz hat gewonnen");
			break;
		case 1:
			tmp = ("Gelb hat gewonnen");
			break;
		case 2:
			tmp = ("Grün hat gewonnen");
			break;
		case 3:
			tmp = ("Rot hat gewonnen");
			break;
		default:
			break;
		}
		spiel_sound("sieg.wav");
		JOptionPane.showMessageDialog(null, tmp);
		main.dispose();
	}

	public void figur_Gui_bewegen(Spielfigur fig) {

		fig.setBounds(basisx + (basisextra * fig.getX()), basisy + (basisextra * fig.getY()), 20, 20);
		n_Layout_HK();

	}

	private void setfigur_gui(Spielfigur figur, int anzahl) {

		figur.setzen(anzahl);
		feld_cheat.setText("");

	}

	ComponentListener bewegung = new ComponentListener() {

		@Override
		public void componentShown(ComponentEvent e) {
			//
		}

		@Override
		public void componentResized(ComponentEvent e) {
			//

		}

		@Override
		public void componentMoved(ComponentEvent e) {
			n_Layout_HK();
		}

		@Override
		public void componentHidden(ComponentEvent e) {
			//

		}
	};

	class ImagePanel extends JPanel {

		private Image img;

		public ImagePanel(String img) {
			this(new ImageIcon(img).getImage());
		}

		public ImagePanel(Image img) {
			this.img = img;
			Dimension size = new Dimension(breite, hoehe);
			setPreferredSize(size);
			setMinimumSize(size);
			setMaximumSize(size);
			setSize(size);
			setLayout(null);
		}

		public void paintComponent(Graphics g) {

			super.paintComponent(g);
			g.drawImage(img, 0, 0, null);
		}

	}

	private void bildLaden(JButton b, String was, int groeße) {

		BufferedImage picture = null;
		try {
			picture = ImageIO.read(new File(path + "/Bilder/" + was + ".png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		b.setIcon(new ImageIcon(picture.getScaledInstance(groeße, groeße, Image.SCALE_FAST)));
		b.revalidate();

	}

}
