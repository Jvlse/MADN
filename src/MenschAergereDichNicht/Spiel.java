package MenschAergereDichNicht;

import java.awt.Color;
import java.util.Arrays;

import javax.swing.JButton;

public class Spiel {

	private Spielfigur[][] figuren = new Spielfigur[4][4];
	private int[] aStart = { 4, 4, 4, 4 };
	private int[] aEnde = { 0, 0, 0, 0 };
	private int wuerfel;
	private int amZug;
	private boolean[] feldBesetzt = new boolean[40];
	private boolean[][] endeBesetzt = new boolean[4][4];
	private int spielgewonnen;

	public Spiel() {
		Arrays.fill(feldBesetzt, false);
		for (int i = 0; i < 4; i++) {
			Arrays.fill(endeBesetzt[i], false);
		}

		spielgewonnen = -1;

	}

	public void erzeugen_Fig(MADNGUI gui, int botanzahl) {
		int bots = botanzahl;

		for (int i = 3; i >= 0; i--) {
			for (int j = 0; j < 4; j++) {
				Spielfigur tmp = new Spielfigur(i, j, gui);
				figuren[i][j] = tmp;
				if (i == 0) {
					figuren[i][j].setNullPassed(true);
				}
				figuren[i][j].addActionListener(e -> gui.zug(e));
				figuren[i][j].setName(i + " " + j);

				switch (i) {
				case 0:
					if (bots > 0) {
						figuren[i][j].setArrayZeile(i);
						figuren[i][j].setBot(true);
						if (j == 3)
							bots--;
					}
					figuren[i][j].setBackground(Color.black);
					break;
				case 1:
					if (bots > 0) {
						figuren[i][j].setArrayZeile(i);
						figuren[i][j].setBot(true);
						if (j == 3)
							bots--;
					}
					figuren[i][j].setBackground(Color.yellow);
					break;
				case 2:
					if (bots > 0) {
						figuren[i][j].setArrayZeile(i);
						figuren[i][j].setBot(true);
						if (j == 3)
							bots--;
					}
					figuren[i][j].setBackground(Color.green);
					break;
				case 3:
					if (bots > 0) {
						figuren[i][j].setArrayZeile(i);
						figuren[i][j].setBot(true);
						if (j == 3)
							bots--;
					}
					figuren[i][j].setBackground(Color.red);
					break;
				default:
					break;
				}
				figuren[i][j].setEnabled(false);

				gui.getMain().add(figuren[i][j]);
			}

		}

	}

	public Spielfigur[][] getFiguren() {
		return figuren;
	}

	public void setFiguren(Spielfigur[][] figuren) {
		this.figuren = figuren;
	}

	public int[] getaStart() {
		return aStart;
	}

	public void setaStart(int[] aStart) {
		this.aStart = aStart;
	}

	public int getWuerfel() {
		return wuerfel;
	}

	public void setWuerfel(int wuerfel) {
		this.wuerfel = wuerfel;
	}

	public int getAmZug() {
		return amZug;
	}

	public void setAmZug(int amZug) {
		this.amZug = amZug;
	}

	public boolean[] getFeldBesetzt() {
		return feldBesetzt;
	}

	public void setFeldBesetzt(boolean[] feldBesetzt) {
		this.feldBesetzt = feldBesetzt;
	}

	public boolean[][] getEndeBesetzt() {
		return endeBesetzt;
	}

	public void setEndeBesetzt(boolean[][] endeBesetzt) {
		this.endeBesetzt = endeBesetzt;
	}

	public int getSpielgewonnen() {
		return spielgewonnen;
	}

	public void setSpielgewonnen(int spielgewonnen) {
		this.spielgewonnen = spielgewonnen;
	}

	public int[] getaEnde() {
		return aEnde;
	}

	public void setaEnde(int[] aEnde) {
		this.aEnde = aEnde;
	}
}
