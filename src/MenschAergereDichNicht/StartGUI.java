package MenschAergereDichNicht;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class StartGUI {

	private JFrame main = new JFrame("M");
	private JButton los = new JButton("Los");
	private JLabel bots = new JLabel("Anzahl Bots");
	private JLabel cheat = new JLabel("Cheat-Modus");
	private JComboBox<Integer> b = new JComboBox<Integer>();
	private JCheckBox ch = new JCheckBox();

	public StartGUI() {

		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main.setResizable(false);
		main.setBounds((int) ((Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 200) / 2),
				(int) ((Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 200) / 2), 200, 200);

		for (int i = 0; i < 4; i++) {
			b.addItem(i);
		}

		bots.setBounds(10, 10, 100, 20);
		cheat.setBounds(10, 60, 100, 20);
		b.setBounds(100, 10, 50, 20);
		ch.setBounds(100, 60, 100, 20);
		los.setBounds(50, 110, 100, 40);

		b.setSelectedIndex(3);
		ch.setSelected(true);

		main.add(bots);
		main.add(cheat);
		main.add(b);
		main.add(ch);
		main.add(los);

		main.setLayout(null);
		main.setVisible(true);

		los.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				Spiel a = new Spiel();
				SpielController c = new SpielController(a);
				MADNGUI gui = new MADNGUI(a, c, ch.isSelected(), b.getSelectedIndex());
				main.dispose();
			}
		});
	}
}
