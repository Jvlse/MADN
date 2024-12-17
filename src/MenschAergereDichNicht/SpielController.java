package MenschAergereDichNicht;

import java.util.Random;

public class SpielController {
	private Spiel spiel;

	public SpielController(Spiel spiel) {
		this.spiel = spiel;
	}

	public void wuerfeln() {
		Random zufall = new Random();
		spiel.setWuerfel(zufall.nextInt(6) + 1);
	}

	public boolean zugpruef(Spielfigur figur) {
		Spielfigur spieler = figur;

		if (!(figur.getSpieler() == (spiel.getAmZug()))) {
			return false;
		}

		if (spieler.getFeld() == -1 && spiel.getWuerfel() != 6) {
			return false;
		}

		if (spieler.getFeld() == -2) {

			return false;
		}

		blocktest(spieler);

		starttest(spieler);

		sperrtest(spieler);

		return spieler.isZugmoeglich();
	}

	public void starttest(Spielfigur spieler) {
		boolean temp = true;
		if (spiel.getWuerfel() == 6 && spiel.getaStart()[spiel.getAmZug()] != 0) {

			if (spiel.getFeldBesetzt()[spiel.getAmZug() * 10]) {

				for (Spielfigur blocker : spiel.getFiguren()[spiel.getAmZug()]) {
					if (blocker.getFeld() == (spiel.getAmZug() * 10)) {
						temp = false;

					}
				}
			}

			if (spieler.getFeld() == -1) {
				spieler.setZugmoeglich(temp);
			} else {
				spieler.setZugmoeglich(false);
			}

		}

	}

	public void blocktest(Spielfigur spieler) {
		int endfeld = spiel.getAmZug() * 10 - 1;
		if (endfeld == -1) {
			endfeld = 39;
		}

		boolean temp = true;
		if (spieler.getFeld() + spiel.getWuerfel() > (endfeld) && spieler.isNullPassed()) {

			if ((spieler.getFeld() + spiel.getWuerfel()) % (endfeld + 1) < 4
					&& !spiel.getEndeBesetzt()[spiel.getAmZug()][(spieler.getFeld() + spiel.getWuerfel())
							% (endfeld + 1)]) {

				spieler.setZugmoeglich(true);

			} else {
				spieler.setZugmoeglich(false);
			}

		} else {
			if (spiel.getFeldBesetzt()[(spieler.getFeld() + spiel.getWuerfel()) % 40]) {
				for (Spielfigur blocker : spiel.getFiguren()[spiel.getAmZug()]) {
					if ((spieler.getFeld() + spiel.getWuerfel()) % 40 == (blocker.getFeld())) {
						temp = false;
					}
				}
			}
			spieler.setZugmoeglich(temp);
		}
	}

	public void sperrtest(Spielfigur spieler) {
		boolean temp = true;
		boolean temp2 = true;
		if (spiel.getFeldBesetzt()[spiel.getAmZug() * 10]) {
			if (spieler.getFeld() == spiel.getAmZug() * 10) {
				blocktest(spieler);
			} else {
				for (Spielfigur blocker : spiel.getFiguren()[spiel.getAmZug()]) {
					if (blocker.getFeld() > -1) {
						blocktest(blocker);
					}
					if (blocker.getFeld() == spiel.getAmZug() * 10) {

						if (spieler.getFeld() == -1) {
							temp2 = false;
						} else {
							if (!blocker.isZugmoeglich()) {
								temp = false;

							} else {

								spieler.setZugmoeglich(false);
								return;
							}

						}

					}

					if (temp == false) {

						blocktest(spieler);
					}
				}
				if (temp2 == false) {
					spieler.setZugmoeglich(false);
				}

			}
		}

	}

	public void zug(Spielfigur figur) {
		int endfeld = spiel.getAmZug() * 10 - 1;
		if (endfeld == -1) {
			endfeld = 39;
		}

		Spielfigur spieler = figur;

		if (spieler.getFeld() == -1) {
			startzug(spieler);
			return;
		}
		if (((spieler.getFeld() + spiel.getWuerfel()) > endfeld && spieler.isNullPassed())
				&& (spieler.getFeld() + spiel.getWuerfel()) % (endfeld + 1) < 4) {

			endzug(spieler);
			return;
		} else {
			standardzug(spieler);
			return;
		}
	}

	public void startzug(Spielfigur spieler) {
		schlag(10 * spiel.getAmZug());
		spieler.setFeld(10 * spiel.getAmZug());
		spiel.getaStart()[spiel.getAmZug()]--;

		spiel.getFeldBesetzt()[10 * spiel.getAmZug()] = true;

		spieler.rausziehen();
	}

	public void endzug(Spielfigur spieler) {
		int endfeld = spiel.getAmZug() * 10 - 1;
		if (endfeld == -1) {
			endfeld = 39;
		}

		spieler.heimkommen((spieler.getFeld() + spiel.getWuerfel()) % (endfeld));

		spiel.getEndeBesetzt()[spiel.getAmZug()][(spieler.getFeld() + spiel.getWuerfel()) % (endfeld + 1)] = true;
		spiel.getFeldBesetzt()[spieler.getFeld()] = false;
		spieler.setFeld(-2);
		spiel.getaEnde()[spiel.getAmZug()]++;

		siegprüfung();
	}

	public void standardzug(Spielfigur spieler) {
		schlag((spieler.getFeld() + spiel.getWuerfel()) % 40);

		spiel.getFeldBesetzt()[spieler.getFeld()] = false;
		spieler.setFeld((spieler.getFeld() + spiel.getWuerfel()) % 40);
		spiel.getFeldBesetzt()[spieler.getFeld()] = true;

		if (spieler.getFeld() < spiel.getAmZug() * 10) {
			spieler.setNullPassed(true);
		}

		spieler.ziehen(spiel.getWuerfel());

	}

	public void schlag(int feld) {
		for (int i = 0; i < 4; i++) {
			if (i != spiel.getAmZug()) {
				for (Spielfigur blocker : spiel.getFiguren()[i]) {
					if (blocker.getFeld() == feld) {
						spiel.getFeldBesetzt()[blocker.getFeld()] = false;
						blocker.setFeld(-1);
						spiel.getaStart()[i]++;
						if (i != 0) {
							blocker.setNullPassed(false);
						}

						blocker.geschlagen();

					}
				}
			}
		}
	}

	public void siegprüfung() {
		if (spiel.getaEnde()[spiel.getAmZug()] == 4) {
			spiel.setSpielgewonnen(spiel.getAmZug());
		}
	}

	public boolean setFigur(int feld, Spielfigur spieler) {

		if (spieler.getFeld() == -2) {
			return false;
		}
		if (spiel.getFeldBesetzt()[feld]) {
			for (Spielfigur blocker : spiel.getFiguren()[spieler.getArrayZeile()]) {
				if (feld == (blocker.getFeld())) {
					return false;
				}
			}
			schlag(feld);
		}
		if (spieler.getFeld() == -1) {
			spiel.getaStart()[spieler.getArrayZeile()]--;
		} else {
			spiel.getFeldBesetzt()[spieler.getFeld()] = false;
		}
		spieler.setFeld(feld);
		spiel.getFeldBesetzt()[feld] = true;
		if (spieler.getFeld() < spiel.getAmZug() * 10) {
			spieler.setNullPassed(true);
		}
		return true;
	}

	public int schlagPruef(int arrayZeile) {
		boolean schlagen = false;
		for (int i = 0; i < 4; i++) {
			if (spiel.getFiguren()[arrayZeile][i].getFeld() != -1
					&& spiel.getFiguren()[arrayZeile][i].getFeld() != -2) {
				for (Spielfigur spieler : spiel.getFiguren()[arrayZeile]) {
					if (spieler.getFeld() != (spiel.getFiguren()[arrayZeile][i].getFeld() + spiel.getWuerfel()) % 40
							&& !spiel
									.getFeldBesetzt()[(spiel.getFiguren()[arrayZeile][i].getFeld() + spiel.getWuerfel())
											% 40]) {
						schlagen = true;
					}
				}
				if (schlagen) {
					return i;
				}
			}
		}
		return -1;
	}

	public int randomFigur(int arrayZeile) {
		Random zufall = new Random();
		int figur;
		do {
			figur = zufall.nextInt(4);
		} while (!zugpruef(spiel.getFiguren()[arrayZeile][figur])
				|| spiel.getFeldBesetzt()[(spiel.getFiguren()[arrayZeile][figur].getFeld() + spiel.getWuerfel()) % 40]);
		return figur;
	}

	public boolean zugnichtmoeglich() {
		boolean temp = false;
		for (Spielfigur spieler : spiel.getFiguren()[spiel.getAmZug()]) {
			if (zugpruef(spieler)) {
				temp = true;
			}
		}
		return temp;
	}
}
