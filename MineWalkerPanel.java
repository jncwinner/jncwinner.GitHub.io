import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import java.lang.Object;
import java.awt.*;
import javax.swing.*;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * 
 * @author Justin Carpenter
 * 
 *         MineWalker
 * 
 *         this program creates a game that allows the user to select steps
 *         through a mine field to get to the finish point.
 *
 */

public class MineWalkerPanel {
	private JPanel basePanel;
	private JPanel drawingPanel;
	private JPanel controlPanel;
	private JPanel GreenPanel;
	private JPanel YellowPanel;
	private JPanel OrangePanel;
	private JPanel RedPanel;
	private JPanel GrayPanel;
	private JPanel colorPanel;
	private JPanel colorChooserPanel;
	private JPanel MagentaPanel;
	private JPanel CyanPanel;

	private JButton randomButton;
	private JButton randomButton2;
	private JButton randomButton3;
	private JButton randomButton4;
	private JButton[][] panels;

	private JLabel textLabel;
	private JLabel textLabel2;
	private JLabel textLabel3;
	private JLabel textLabel4;
	private JLabel textLabel5;
	private JLabel textLabel6;
	private JLabel textLabel7;
	private JLabel colortextLabel;
	private JLabel colortextLabel2;
	private JLabel colortextLabel3;
	private JLabel colortextLabel4;
	private JLabel colortextLabel5;
	private JLabel colortextLabel6;
	private JLabel colortextLabel7;

	private int GRID_SIZE = 10;
	private int ROWS = 10;
	private int COLM = 10;
	private int LIVES;
	private int SCORE;
	private int size;
	private double Difficulty;

	private JTextField text;
	private JTextField text2;
	private JTextField text3;
	private JTextField text4;

	private Color[] colors = { Color.GRAY, Color.GREEN, Color.ORANGE, Color.YELLOW, Color.BLACK, Color.RED,
			Color.MAGENTA, Color.CYAN };

	private Timer t;

	private static Random random;

	public JSlider slider;

	private Point curPosition;
	private Point POSITION;
	private Point VPOSITION;
	Point[] points = new Point[4];
	Point point;
	Point point1;
	Point point2;
	Point point3;
	Point point4;

	ArrayList<Point> path = new ArrayList<Point>();
	ArrayList<Point> mines = new ArrayList<Point>();
	ArrayList<Point> HitMines = new ArrayList<Point>();

	RandomWalk walk;

	/**
	 * MineWalkerPanel Constructor
	 *
	 */
	public MineWalkerPanel() {
		basePanel = new JPanel();
		colorChooserPanel = new JPanel();
		t = new Timer(500, new TimerListener());
		SliderIntro();
		CreatePanels();
		CreateGame();
		Text();
		textLabel();

		randomButton();
		drawingPanel();
		controlPanel();
		ColorPanel();
		basePanel();
		getPanel();
		CreateGrid();
	}

	/**
	 * method to set the tiles in the random safe path to green.
	 *
	 */
	public void ShowPath() {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLM; j++) {
				Point point = new Point(i, j);
				if (path.contains(point)) {
					panels[i][j].setBackground(colors[1]);
				}
			}
		}
		panels[VPOSITION.x][VPOSITION.y].setBackground(colors[6]);
	}

	/**
	 * method to set the tiles in the random safe path back to black.
	 *
	 */
	public void HidePath() {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLM; j++) {
				Point point = new Point(i, j);
				if (path.contains(point)) {
					panels[i][j].setBackground(colors[4]);
				}
			}
		}
		panels[VPOSITION.x][VPOSITION.y].setBackground(colors[6]);
	}

	/**
	 * method to create the slider.
	 *
	 */
	public void SliderIntro() {
		slider = new JSlider(JSlider.VERTICAL, 0, 100, 25);
		slider.setMinorTickSpacing(1);
		slider.setMajorTickSpacing(25);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);

		SliderListener l = new SliderListener();
		slider.addChangeListener(l);
	}

	/**
	 * method to create the slider listener, creating a new game with the new
	 * variable from the slider
	 *
	 */
	public class SliderListener implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			JSlider slider = (JSlider) e.getSource();
			if (!slider.getValueIsAdjusting()) {
				Difficulty = (double) (((double) slider.getValue()));
				ResetGame();
				ScoreBoard();
			}
		}
	}

	/**
	 * method to get the basePanel
	 *
	 * @return basePanel
	 *
	 */
	public JPanel getPanel() {
		return basePanel;
	}

	/**
	 * method to get the color and not check if it is a valid move nor set the score
	 * or lives,
	 * 
	 * this is used for the timer to get the color again and again.
	 *
	 */
	public void BlinkColor() {
		point1 = new Point(POSITION.x + 1, POSITION.y);
		point2 = new Point(POSITION.x, POSITION.y + 1);
		point3 = new Point(POSITION.x - 1, POSITION.y);
		point4 = new Point(POSITION.x, POSITION.y - 1);
		if (mines.contains(POSITION)) {
			if (HitMines.contains(POSITION)) {
				return;
			} else {
				panels[POSITION.x][POSITION.y].setBackground(colors[0]);
				HitMines.add(POSITION);
				EndGame();
				return;
			}
		} else if (mines.contains(point1) && mines.contains(point2) && mines.contains(point3)
				|| mines.contains(point1) && mines.contains(point2) && mines.contains(point4)
				|| mines.contains(point1) && mines.contains(point3) && mines.contains(point4)
				|| mines.contains(point2) && mines.contains(point3) && mines.contains(point4)) {
			panels[POSITION.x][POSITION.y].setBackground(colors[5]);
		} else if (mines.contains(point1) && mines.contains(point2) || mines.contains(point1) && mines.contains(point3)
				|| mines.contains(point1) && mines.contains(point4) || mines.contains(point2) && mines.contains(point3)
				|| mines.contains(point2) && mines.contains(point4)
				|| mines.contains(point3) && mines.contains(point4)) {
			panels[POSITION.x][POSITION.y].setBackground(colors[2]);
		} else if (mines.contains(point1) || mines.contains(point2) || mines.contains(point3)
				|| mines.contains(point4)) {
			panels[POSITION.x][POSITION.y].setBackground(colors[3]);
		} else {
			panels[POSITION.x][POSITION.y].setBackground(colors[1]);
		}
	}

	/**
	 * method to get the color and check to make sure it is a valid move, change and
	 * set the new score and lives, and check to make sure the move is not a repeat
	 * bomb hit.
	 *
	 */
	public void DecideColor() {
		for (int i = 0; i < points.length; i++) {
			if (POSITION.equals(points[i])) {
				if (mines.contains(curPosition)) {
					if (HitMines.contains(curPosition)) {
						timer();
						return;
					} else {
						panels[curPosition.x][curPosition.y].setBackground(colors[0]);
						SCORE -= 50;
						LIVES -= 1;
						HitMines.add(curPosition);
						EndGame();
						timer();
						return;
					}
				} else if (mines.contains(point1) && mines.contains(point2) && mines.contains(point3)
						|| mines.contains(point1) && mines.contains(point2) && mines.contains(point4)
						|| mines.contains(point1) && mines.contains(point3) && mines.contains(point4)
						|| mines.contains(point2) && mines.contains(point3) && mines.contains(point4)) {
					panels[curPosition.x][curPosition.y].setBackground(colors[5]);
					SCORE -= 25;
				} else if (mines.contains(point1) && mines.contains(point2)
						|| mines.contains(point1) && mines.contains(point3)
						|| mines.contains(point1) && mines.contains(point4)
						|| mines.contains(point2) && mines.contains(point3)
						|| mines.contains(point2) && mines.contains(point4)
						|| mines.contains(point3) && mines.contains(point4)) {
					panels[curPosition.x][curPosition.y].setBackground(colors[2]);
					SCORE -= 10;
				} else if (mines.contains(point1) || mines.contains(point2) || mines.contains(point3)
						|| mines.contains(point4)) {
					panels[curPosition.x][curPosition.y].setBackground(colors[3]);
					SCORE -= 5;
				} else {
					panels[curPosition.x][curPosition.y].setBackground(colors[1]);
					SCORE -= 2;
				}
				POSITION = curPosition;
				WinGame();
			}
		}
	}

	/**
	 * method to get the new Difficulty value from the slider
	 *
	 */
	public void changeDificulty() {
		Difficulty = slider.getValue();
	}

	/**
	 * method is called in the listener of the JButtons, to get the current position
	 * and set it to curPosition, then create the array used to check if it is a
	 * valid move, then calls Decide color and the timer to get the colors.
	 *
	 */
	public void changeColor(Object object) {
		BlinkColor();
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLM; j++) {
				Point point = new Point(i, j);
				if (object.equals(panels[i][j])) {
					curPosition = point;
				}
			}
		}
		point1 = new Point(curPosition.x + 1, curPosition.y);
		point2 = new Point(curPosition.x, curPosition.y + 1);
		point3 = new Point(curPosition.x - 1, curPosition.y);
		point4 = new Point(curPosition.x, curPosition.y - 1);

		points[0] = point1;
		points[1] = point2;
		points[2] = point3;
		points[3] = point4;

		DecideColor();
		timer();
	}

	/**
	 * listener for the show/hide path button, this toggles the button and checks to
	 * see what action to perform depending on the name of the button.
	 * 
	 */
	public class ShowPathListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			if (source instanceof JButton) {
				if (randomButton2.getText().equals("Show Path")) {
					ShowPath();
					randomButton2.setText("Hide Path");
				} else if (randomButton2.getText().equals("Hide Path")) {
					HidePath();
					randomButton2.setText("Show Path");
				}
			}
		}
	}

	/**
	 * listener for the show/hide Mines button, this toggles the button and checks
	 * to see what action to perform depending on the name of the button.
	 * 
	 */
	public class ShowMinesListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			if (source instanceof JButton) {
				if (randomButton.getText().equals("Show Mines")) {
					showMine();
					randomButton.setText("Hide Mines");
				} else if (randomButton.getText().equals("Hide Mines")) {
					HideMine();
					randomButton.setText("Show Mines");
				}
			}
		}
	}

	/**
	 * listener for the give up/ new game button, this toggles the button and checks
	 * to see what action to perform depending on the name of the button.
	 * 
	 */
	public class ResetGameListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			if (source instanceof JButton) {
				if (randomButton3.getText().equals("Give Up")) {
					showMine();
					randomButton3.setText("New Game");
					LIVES = 0;
				} else if (randomButton3.getText().equals("New Game")) {
					ResetGame();
					ScoreBoard();
					randomButton3.setText("Give Up");
				}
			}
		}
	}

	/**
	 * listener for the all the JButtons to check if it is a valid move, change the
	 * color if it is, then refresh the score board
	 * 
	 */
	public class ColorClickListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			changeColor(e.getSource());
			ScoreBoard();
		}
	}

	/**
	 * listener for the update grid size button, it calls the CreatGrid() method
	 * that deletes the old grid and adds the new sized one
	 * 
	 */
	public class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			CreateGrid();
		}
	}

	/**
	 * listener for the timer to blink, it checks to see if the color is one of the
	 * valid move tiles, if it is it sets it to black, if not it checks to see if it
	 * is a valid move and blinks every 500ms
	 * 
	 */
	public class TimerListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (panels[POSITION.x][POSITION.y].getBackground() == colors[7]
					|| panels[POSITION.x][POSITION.y].getBackground() == colors[1]
					|| panels[POSITION.x][POSITION.y].getBackground() == colors[2]
					|| panels[POSITION.x][POSITION.y].getBackground() == colors[3]
					|| panels[POSITION.x][POSITION.y].getBackground() == colors[5])
				panels[POSITION.x][POSITION.y].setBackground(colors[4]);
			else
				BlinkColor();
		}
	}

	/**
	 * method to check if the current position is at the finish line or not, if it
	 * is it will end the game saying you won and ask if you want to play again, if
	 * not will exit the program.
	 * 
	 */
	public void WinGame() {
		VPOSITION = new Point(0, (GRID_SIZE - 1));
		panels[VPOSITION.x][VPOSITION.y].setBackground(colors[6]);
		if (VPOSITION.equals(POSITION)) {
			showMine();
			if (JOptionPane.showConfirmDialog(null, "YOU WIN!!!" + "\nWant To PLay Again?", "GAME OVER",
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				ResetGame();
				ScoreBoard();
				curPosition.setLocation(GRID_SIZE - 1, 0);
			} else {
				System.exit(0);
			}
		}
	}

	/**
	 * method to start the timer
	 * 
	 */
	public void timer() {
		t.start();
	}

	/**
	 * method to check if the current position is you still have lives, if not it
	 * will end the game saying you are out of lives and ask if you want to play
	 * again, if not will exit the program.
	 * 
	 */
	public void EndGame() {
		if (LIVES <= 0) {
			showMine();
			if (JOptionPane.showConfirmDialog(null, "Want To PLay Again?", "OUT OF LIVES",
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				ResetGame();
				ScoreBoard();
				curPosition.setLocation(GRID_SIZE - 1, 0);
			} else {
				System.exit(0);
			}
		}
	}

	/**
	 * method to refresh the scoreboard, changing the lives, score, and grid size.
	 * it also checks if the new number of lives is valid, if it is 0 or less it
	 * will call EndGame() and end the game.
	 * 
	 */
	public void ScoreBoard() {
		text.setText("" + LIVES);
		text2.setText("" + SCORE);
		text3.setText("" + GRID_SIZE);
		text4.setText("" + GRID_SIZE);
		EndGame();
	}

	/**
	 * method to change the tiles in the mines array to gray
	 * 
	 */
	public void showMine() {
		for (Point i : mines) {
			panels[i.x][i.y].setBackground(colors[0]);
		}
	}

	/**
	 * method to change the tiles in the mines array to black
	 * 
	 */
	public void HideMine() {
		for (Point i : mines) {
			panels[i.x][i.y].setBackground(colors[4]);
		}
	}

	/**
	 * this creates new JButtons and adds them to the panels array as a new JButton
	 * with the ColorClickListener and sets them to black
	 * 
	 */
	public void CreatePanels() {
		panels = new JButton[ROWS][COLM];
		for (int r = 0; r < ROWS; r++) {
			for (int i = 0; i < COLM; i++) {
				panels[r][i] = new JButton();
				panels[r][i].setBackground(colors[4]);
				panels[r][i].addActionListener(new ColorClickListener());
				colorChooserPanel.add(panels[r][i]);
			}
		}
	}

	/**
	 * method to create a new game, new path, new random mines, reset the lives
	 * score, position and current position back to defalt.
	 * 
	 */
	public void CreateGame() {
		CreatePath();

		Difficulty = slider.getValue();
		Difficulty = Difficulty / 100;

		for (int i = 0; i < ((COLM * ROWS) * Difficulty); i++) {
			CreateMine();
		}
		LIVES = 5;
		SCORE = 500;
		POSITION = new Point((GRID_SIZE - 1), 0);
		panels[POSITION.x][POSITION.y].setBackground(colors[7]);

		WinGame();
	}

	/**
	 * method to check to make sure no mines are added on the safe path then creates
	 * random points and add them to the mines array.
	 * 
	 */
	public void CreateMine() {
		random = new Random();
		int x = random.nextInt(COLM);
		int y = random.nextInt(COLM);
		Point point = new Point(x, y);
		if (path.contains(point)) {
		} else {
			mines.add(point);
		}
	}

	/**
	 * method to create a new random path and add it to the path array
	 * 
	 */
	public void CreatePath() {
		walk = new RandomWalk(GRID_SIZE);
		walk.createWalk();
		this.path = walk.getPath();
	}

	/**
	 * method to create the text fields and add the values of lives, score, and grid
	 * size
	 * 
	 */
	public void Text() {
		text = new JTextField(2);
		text2 = new JTextField(2);
		text3 = new JTextField(2);
		text4 = new JTextField(2);

		text.setText("" + LIVES);
		text2.setText("" + SCORE);
		text3.setText("" + GRID_SIZE);
		text4.setText("" + GRID_SIZE);
	}

	/**
	 * method to change the grid, it checks to see if the user entered any amount if
	 * not it sets the grid to the default size, if they did enter a valid number,
	 * it creates a new game deleting the old grid and adding the new one with the
	 * new grid size.
	 * 
	 */
	public void CreateGrid() {
		if (colorChooserPanel != null) {
			basePanel.remove(colorChooserPanel);
			colorChooserPanel = null;
		}
		try {
			size = Integer.parseInt(text4.getText());
			if (size < 10)
				size = 10;
			if (size > 50)
				size = 50;
		} catch (NumberFormatException e) {
			size = GRID_SIZE;
			text4.setText("" + size);
		}
		colorChooserPanel = new JPanel();
		colorChooserPanel.setLayout(new GridLayout(size, size));

		GRID_SIZE = size;
		ROWS = size;
		COLM = size;

		CreatePanels();
		ResetGame();
		ScoreBoard();

		basePanel.add(colorChooserPanel, BorderLayout.CENTER);
		basePanel.revalidate();
	}

	/**
	 * method to set the labels in the game
	 * 
	 */
	public void textLabel() {
		textLabel = new JLabel("LIVES:  ");
		textLabel2 = new JLabel("SCORE:  ");
		textLabel3 = new JLabel("Grid Size:  ");
		textLabel4 = new JLabel("Enter new grid size");
		textLabel7 = new JLabel("between 10 and 50, then");
		textLabel6 = new JLabel("select Update Grid Size:");
		textLabel5 = new JLabel("Select difficulty:");
	}

	/**
	 * method to reset the buttons when resetting the game
	 * 
	 */
	public void randomButtonReset() {
		randomButton3.setText("Give Up");
		randomButton.setText("Show Mines");
		randomButton2.setText("Show Path");
	}

	/**
	 * method to set the JButtons and add the action listeners
	 * 
	 */
	public void randomButton() {
		randomButton = new JButton("Show Mines");
		randomButton.addActionListener(new ShowMinesListener());
		randomButton2 = new JButton("Show Path");
		randomButton2.addActionListener(new ShowPathListener());
		randomButton3 = new JButton("Give Up");
		randomButton3.addActionListener(new ResetGameListener());
		randomButton4 = new JButton("Update Grid Size");
		randomButton4.addActionListener(new ButtonListener());
	}

	/**
	 * method set the panel that consists of all the JButtons and labels, this is
	 * where all the gluing to the correct locations take place.
	 * 
	 */
	public void drawingPanel() {
		drawingPanel = new JPanel();
		drawingPanel.setLayout(new BoxLayout(drawingPanel, BoxLayout.Y_AXIS));
		drawingPanel.setBackground(Color.GRAY);
		drawingPanel.add(Box.createVerticalGlue());
		drawingPanel.add(Box.createRigidArea(new Dimension(10, 25)));
		drawingPanel.add(randomButton);
		drawingPanel.add(randomButton2);
		drawingPanel.add(randomButton3);
		drawingPanel.add(randomButton4);
		drawingPanel.add(Box.createRigidArea(new Dimension(10, 25)));
		textLabel4.setForeground(new Color(101, 255, 45));
		textLabel6.setForeground(new Color(101, 255, 45));
		textLabel7.setForeground(new Color(101, 255, 45));
		drawingPanel.add(textLabel4);
		drawingPanel.add(textLabel7);
		drawingPanel.add(textLabel6);
		Dimension panelD = new Dimension(24, 24);
		text4.setMaximumSize(panelD);
		drawingPanel.add(text4);
		drawingPanel.add(Box.createRigidArea(new Dimension(10, 25)));
		textLabel5.setForeground(new Color(101, 255, 45));
		drawingPanel.add(Box.createVerticalGlue());
		drawingPanel.add(textLabel5);
		drawingPanel.add(slider);

	}

	/**
	 * method to set all the info of what each color means on the left
	 * 
	 */
	public void ColorPanel() {
		colortextLabel = new JLabel("0 mines near");
		colortextLabel2 = new JLabel("1 mine near");
		colortextLabel3 = new JLabel("2 mine near");
		colortextLabel4 = new JLabel("3 mine near");
		colortextLabel5 = new JLabel("Bomb");
		colortextLabel6 = new JLabel("FINISH");
		colortextLabel7 = new JLabel("START");

		CyanPanel = new JPanel();
		colorPanel = new JPanel();
		GreenPanel = new JPanel();
		YellowPanel = new JPanel();
		OrangePanel = new JPanel();
		RedPanel = new JPanel();
		GrayPanel = new JPanel();
		MagentaPanel = new JPanel();

		CyanPanel.setBackground(Color.CYAN);
		CyanPanel.add(colortextLabel7);

		MagentaPanel.setBackground(Color.MAGENTA);
		MagentaPanel.add(colortextLabel6);

		GreenPanel.setBackground(Color.GREEN);
		GreenPanel.add(colortextLabel);

		YellowPanel.setBackground(Color.YELLOW);
		YellowPanel.add(colortextLabel2);

		OrangePanel.setBackground(Color.ORANGE);
		OrangePanel.add(colortextLabel3);

		RedPanel.setBackground(Color.RED);
		RedPanel.add(colortextLabel4);

		GrayPanel.setBackground(Color.GRAY);
		GrayPanel.add(colortextLabel5);

		colorPanel.setLayout(new BoxLayout(colorPanel, BoxLayout.Y_AXIS));
		colorPanel.add(Box.createVerticalGlue());
		colorPanel.add(CyanPanel);
		colorPanel.add(GreenPanel);
		colorPanel.add(YellowPanel);
		colorPanel.add(OrangePanel);
		colorPanel.add(RedPanel);
		colorPanel.add(GrayPanel);
		colorPanel.add(MagentaPanel);
		colorPanel.add(Box.createVerticalGlue());

	}

	/**
	 * method to set all the labels on top or the score board
	 * 
	 */
	public void controlPanel() {
		controlPanel = new JPanel();
		controlPanel.setBackground(Color.WHITE);
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
		controlPanel.add(Box.createHorizontalGlue());
		controlPanel.add(textLabel);
		controlPanel.add(text);
		controlPanel.add(textLabel2);
		controlPanel.add(text2);
		controlPanel.add(textLabel3);
		controlPanel.add(text3);
		controlPanel.add(Box.createHorizontalGlue());
	}

	/**
	 * method to set a box layout with all the panels on it
	 * 
	 */
	public void basePanel() {
		basePanel.setLayout(new BorderLayout());
		basePanel.add(colorChooserPanel, BorderLayout.CENTER);
		basePanel.add(drawingPanel, BorderLayout.EAST);
		basePanel.add(controlPanel, BorderLayout.NORTH);
		basePanel.add(colorPanel, BorderLayout.WEST);
	}

	/**
	 * method reset all the arrays, buttons, positions, and create a new game
	 * 
	 */
	public void ResetGame() {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLM; j++) {
				panels[i][j].setBackground(colors[4]);
			}
		}
		POSITION.setLocation(GRID_SIZE - 1, 0);
		panels[GRID_SIZE - 1][0].setBackground(colors[7]);
		path.clear();
		mines.clear();
		randomButtonReset();
		CreateGame();
	}

}