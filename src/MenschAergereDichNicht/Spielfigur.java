package MenschAergereDichNicht;

import java.util.Iterator;

import javax.swing.JButton;

public class Spielfigur extends JButton {
	private int feld;
	private boolean zugmoeglich;
	private boolean nullPassed;
	private int x, y;
	private int spieler, nummer;
	private MADNGUI zuhause;

	private boolean bot = false;
	private int arrayZeile;

	private int[] xpos = { 50, 50, 505, 505 };
	private int[] ypos = { 505, 50, 50, 505 };
	private int[] xver = { 0, 20, 0, 20 };
	private int[] yver = { 0, 0, 20, 20 };

	private int[] xstart = { 4, 0, 6, 10 };
	private int[] ystart = { 10, 4, 0, 6 };

	private int[] xende = { 5, 0, 5, 10 };
	private int[] yende = { 10, 5, 0, 5 };

	public Spielfigur(int farbe, int nr, MADNGUI gui) {
		spieler = farbe;
		nummer = nr;
		feld = -1;
		zugmoeglich = false;
		nullPassed = false;
		bot = false;
		x = -1;
		y = -1;
		zuhause = gui;
		setBounds(xpos[farbe] + xver[nr], ypos[farbe] + yver[nr], 20, 20);

	}

	public void rausziehen() {

		x = xstart[spieler];
		y = ystart[spieler];
		zuhause.figur_Gui_bewegen(this);

	}

	public void ziehen(int i) {

		for (int j = 0; j < i; j++) {
			position_brechnen();
		}
		zuhause.figur_Gui_bewegen(this);
	}

	public void geschlagen() {

		x = -1;
		y = -1;
		setBounds(xpos[spieler] + xver[nummer], ypos[spieler] + yver[nummer], 20, 20);
		zuhause.n_Layout_HK();
	}

	public void heimkommen(int i) {

		x = xende[spieler];
		y = yende[spieler];

		for (int j = 0; j < i; j++) {
			switch (spieler) {
			case 0:
				y--;
				break;
			case 1:
				x++;
				break;
			case 2:
				y++;
				break;
			case 3:
				x--;
				break;

			default:
				break;
			}
		}

		zuhause.figur_Gui_bewegen(this);

	}

	public void setzen(int anzahl) {

		x = 4;
		y = 10;
		ziehen(anzahl);

	}

	public int position_brechnen() {

		if (x == 0 && y == 5) {
			y--;
			return 0;
		}
		if (x == 5 && y == 0) {
			x++;
			return 0;
		}
		if (x == 10 && y == 5) {
			y++;
			return 0;
		}
		if (x == 5 && y == 10) {
			x--;
			return 0;
		}

		if (x == 4) {

			if (y == 6) {
				x--;
				return 0;
			} else if (y == 0) {
				x++;
				return 0;
			} else {
				y--;
				return 0;
			}

		}

		if (x == 6) {

			if (y == 4) {
				x++;
				return 0;
			} else if (y == 10) {
				x--;
				return 0;
			} else {
				y++;
				return 0;
			}

		}

		if (y == 6) {

			if (x == 0) {
				y--;
				return 0;
			} else if (x == -1) {
				//
			} else {
				x--;
				return 0;
			}

		}

		if (y == 4) {

			if (x == 10) {
				y++;
				return 0;
			} else if (x == -1) {
				//
			} else {
				x++;
				return 0;
			}

		}
		return -1;

	}

	public boolean getBot() {
		return bot;
	}

	public void setBot(boolean bot) {
		this.bot = bot;
	}

	public int getArrayZeile() {
		return arrayZeile;
	}

	public void setArrayZeile(int arrayZeile) {
		this.arrayZeile = arrayZeile;
	}

	public int[] getXpos() {
		return xpos;
	}

	public int[] getYpos() {
		return ypos;
	}

	public int[] getXver() {
		return xver;
	}

	public int[] getYver() {
		return yver;
	}

	public int[] getXstart() {
		return xstart;
	}

	public int[] getYstart() {
		return ystart;
	}

	public int getFeld() {
		return feld;
	}

	public void setFeld(int feld) {
		this.feld = feld;
	}

	public boolean isZugmoeglich() {
		return zugmoeglich;
	}

	public void setZugmoeglich(boolean zugmoeglich) {
		this.zugmoeglich = zugmoeglich;
	}

	public boolean isNullPassed() {
		return nullPassed;
	}

	public void setNullPassed(boolean nullPassed) {
		this.nullPassed = nullPassed;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getSpieler() {
		return spieler;
	}

	public int getNummer() {
		return nummer;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
}
