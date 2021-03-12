package a9;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

public class Game extends JPanel implements ActionListener, MouseListener, KeyListener {

	// Final variables for game
	private static final long serialVersionUID = 1L;
	private static final int NUM_ROWS = 5;
	private static final int NUM_COLS = 7;
	private static final int CELL_SIZE = 50;
	private static final int ROW_SPACING = 10;
	private static final int GAME_SPEED = 30;
	private static final int OLD_COST = 10; // Cost of Old Computer plant
	private static final int NEW_COST = 20; // Cost of New Computer plant

	// Initial counts for changing variables
	private static final int INIT_BITCOIN = 50;
	private static final int INIT_TIME = 0;
	private static final int INIT_DIFFICULTY = 1;

	// JFrame panels, labels, and buttons for the user interface.
	private JPanel topBar;
	private JButton oldComputerButton;
	private JButton newComputerButton;

	private JPanel bitcoinCounterPanel;
	private JLabel bitcoinNameLabel;
	private JLabel bitcoinCounterLabel;

	private JPanel timeCounterPanel;
	private JLabel timeNameLabel;
	private JLabel timeCounterLabel;

	private JPanel difficultyCounterPanel;
	private JLabel difficultyNameLabel;
	private JLabel difficultyCounterLabel;

	private JLabel gameOver; // Game over screen

	private JTextField keyDetector; // This text field is for detecting when the space bar is pressed for
									// reselecting last plant to place, or for starting a new game.

	private Timer timer; // Timer for game speed
	static ArrayList<Actor> actors; // Plants and Zombies all go in here
	private Random randGenerator;

	// Changing variables shown on user interface.
	private int bitcoinCount;
	private int difficulty;
	private double timeCount;

	// This is used for the program to tell if a plant has been bought so another
	// one isn't bought until the first has been placed. It also keeps track of
	// which type has been queued for placement.
	private boolean oldComputerQueued;
	private boolean newComputerQueued;

	// Game background image and game over screen image.
	Image backgroundImage = null;
	Image gameOverImage = null;

	/***
	 * Creates a new game JFrame user interface, formats said user interface. Brings
	 * in game images and then creates an array list for game actors. Also creates
	 * and starts a game timer to loop over the game loop. Creates a random number
	 * generator to be used later. Also starts game.
	 */
	public Game() {
		super();

		// Creates new JPanel for the top bar in the user interface.
		topBar = new JPanel();

		// Adds buttons to the user interface, allowing the user to buy a plant
		oldComputerButton = new JButton("<html>" + OLD_COST + "<br>Coins</html>");
		newComputerButton = new JButton("<html>" + NEW_COST + "<br>Coins</html>");

		// Creates 3 new sub JPanels containing two labels, one for the title of the
		// variable, and one for said variable. Labels are then added to the JPanel.
		// Formatting for these JPanels is also done here.
		bitcoinCounterPanel = new JPanel();
		bitcoinNameLabel = new JLabel("<html><br>Bitcoin:", JLabel.CENTER);
		bitcoinCounterLabel = new JLabel("           " + String.valueOf(bitcoinCount));
		bitcoinCounterPanel.add(bitcoinNameLabel);
		bitcoinCounterPanel.add(bitcoinCounterLabel);
		bitcoinCounterPanel.setLayout(new BoxLayout(bitcoinCounterPanel, BoxLayout.PAGE_AXIS));
		bitcoinCounterPanel.setBackground(new Color(50, 50, 50));
		bitcoinNameLabel.setForeground(Color.WHITE);
		bitcoinCounterLabel.setForeground(Color.WHITE);

		timeCounterPanel = new JPanel();
		timeNameLabel = new JLabel("<html><br>Time: ", JLabel.CENTER);
		timeCounterLabel = new JLabel("           " + String.valueOf((int) timeCount / 1000));
		timeCounterPanel.add(timeNameLabel);
		timeCounterPanel.add(timeCounterLabel);
		timeCounterPanel.setLayout(new BoxLayout(timeCounterPanel, BoxLayout.PAGE_AXIS));
		timeCounterPanel.setBackground(new Color(50, 50, 50));
		timeNameLabel.setForeground(Color.WHITE);
		timeCounterLabel.setForeground(Color.WHITE);

		difficultyCounterPanel = new JPanel();
		difficultyNameLabel = new JLabel("<html><br>Difficulty: ", JLabel.CENTER);
		difficultyCounterLabel = new JLabel("           " + String.valueOf(difficulty));
		difficultyCounterPanel.add(difficultyNameLabel);
		difficultyCounterPanel.add(difficultyCounterLabel);
		difficultyCounterPanel.setLayout(new BoxLayout(difficultyCounterPanel, BoxLayout.PAGE_AXIS));
		difficultyCounterPanel.setBackground(new Color(50, 50, 50));
		difficultyNameLabel.setForeground(Color.WHITE);
		difficultyCounterLabel.setForeground(Color.WHITE);

		// Sets the preferred size of the game panel and the top bar.
		setPreferredSize(new Dimension((NUM_COLS + 3) * CELL_SIZE, (NUM_ROWS + 2) * (CELL_SIZE + ROW_SPACING)));
		topBar.setPreferredSize(new Dimension((NUM_COLS + 3) * CELL_SIZE, CELL_SIZE + ROW_SPACING + ROW_SPACING));

		// Formats the top bar itself
		topBar.setLayout(new GridLayout(1, 5));
		topBar.setBackground(new Color(50, 50, 50));

		// Plant images two be set as button icons
		Image oldComputerImage = null;
		Image newComputerImage = null;

		// try catch for both the background images and the button icons
		try {
			backgroundImage = ImageIO.read(new File("src/a9/game-icons/background.png"));
			gameOverImage = ImageIO.read(new File("src/a9/game-icons/gameOver.png"));
			oldComputerImage = ImageIO.read(new File("src/a9/game-icons/computer0.png"));
			newComputerImage = ImageIO.read(new File("src/a9/game-icons/computer1.png"));
		} catch (IOException e) {
			System.out.println("A file was not found");
			System.exit(0);
		}

		// sets button icons with imported images
		oldComputerButton.setIcon(new ImageIcon(oldComputerImage));
		newComputerButton.setIcon(new ImageIcon(newComputerImage));

		// Creates an action listener for the buttons
		oldComputerButton.addActionListener(this);
		newComputerButton.addActionListener(this);

		// Adds all the JPanel components to the top bar and then adds the top bar to
		// the game panel
		topBar.add(oldComputerButton);
		topBar.add(newComputerButton);
		topBar.add(bitcoinCounterPanel);
		topBar.add(difficultyCounterPanel);
		topBar.add(timeCounterPanel);
		add(topBar);

		// Creates a hidden game over screen and adds a Keylistener for to start a new
		// game when on this screen
		gameOver = new JLabel(new ImageIcon(gameOverImage));
		keyDetector = new JTextField();
		keyDetector.addKeyListener(this);
		gameOver.add(keyDetector);

		// Random number generator
		randGenerator = new Random();

		// Store all the plants and Zombies in here.
		actors = new ArrayList<>();

		// The timer updates the game each time it goes.
		// Get the javax.swing Timer, not from util.
		timer = new Timer(GAME_SPEED, this);

		// Starts the game and resets variables as well as the actors
		startGame();
	}

	/***
	 * Clears any actors on screen, adds the top bar and removes the game over
	 * screen. Then resets all game variables and starts the timer to begin the
	 * game.
	 */
	public void startGame() {
		actors.clear();
		add(topBar);
		remove(gameOver);
		setBitcoin(INIT_BITCOIN);
		setTime(INIT_TIME);
		setDifficulty(INIT_DIFFICULTY);
		oldComputerQueued = false;
		newComputerQueued = false;
		timer.start();
	}

	/***
	 * Brings up the game over screen, hides the top bar, repaints the screen and
	 * stops the timer to end the game.
	 */
	public void gameOver() {
		add(gameOver);
		remove(topBar);
		revalidate();
		timer.stop();
	}

	/***
	 * Sets the Bitcoin variable and sets user interface text.
	 * 
	 * @param ammountToAdd
	 */
	public void setBitcoin(int ammountToSet) {
		bitcoinCount = ammountToSet;
		bitcoinCounterLabel.setText("           " + String.valueOf(bitcoinCount));
	}

	/***
	 * Adds to Bitcoin variable and sets user interface text.
	 * 
	 * @param ammountToAdd
	 */
	public void addBitcoin(int ammountToAdd) {
		bitcoinCount += ammountToAdd;
		bitcoinCounterLabel.setText("           " + String.valueOf(bitcoinCount));
	}

	// Sets difficulty variable and sets user interface text.
	public void setDifficulty(int ammountToSet) {
		difficulty = ammountToSet;
		difficultyCounterLabel.setText("           " + String.valueOf(difficulty));
	}

	/***
	 * Adds one to difficulty variable and sets user interface text.
	 */
	public void addDifficulty() {
		if (difficulty < 400) {
			difficulty++;
			difficultyCounterLabel.setText("           " + String.valueOf(difficulty));

		}
	}

	/***
	 * Sets the time variable and sets user interface text.
	 * 
	 * @param ammountToAdd
	 */
	public void setTime(int ammountToSet) {
		timeCount = ammountToSet;
		timeCounterLabel.setText("           " + String.valueOf((int) timeCount / 1000));
	}

	/***
	 * Adds to time variable and sets user interface text.
	 * 
	 * @param ammountToAdd
	 */
	public void addTime(double ammountToAdd) {
		timeCount += ammountToAdd;
		timeCounterLabel.setText("           " + String.valueOf((int) timeCount / 1000));
	}

	/***
	 * Implement the paint method to draw the plants and background image
	 * 
	 * @param g
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(backgroundImage, 0, 0, null); // Paints background image
		for (Actor actor : actors) {
			actor.draw(g, 0);
			actor.drawHealthBar(g);
		}
	}

	/**
	 * 
	 * This is triggered by the timer. It is the game loop of this test.
	 * 
	 * @param e
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// Adds the game tick speed to the time every loop to count up in time on the
		// user interface
		addTime(GAME_SPEED);

		// Increases the game difficulty every 30 seconds
		if (timeCount % 10000 == 0) {
			addDifficulty();
		}

		// Randomly adds Bitcoin based of the amount of Plant place
		for (Actor actor : actors) {
			if (actor instanceof Plant) {
				if (randGenerator.nextInt(100) > 98) {
					addBitcoin(1);
				}
			}
		}

		// Checks if a button has been pressed, then which button it is and removes the
		// number of Bitcoin required to purchase that plant. If a plant is already
		// queued, a new button press will do nothing until said plant is placed.
		if (e.getSource() == oldComputerButton) {
			if (bitcoinCount >= OLD_COST && !oldComputerQueued && !newComputerQueued) {
				addBitcoin(-OLD_COST);
				oldComputerQueued = true;
			}
		}
		if (e.getSource() == newComputerButton) {
			if (bitcoinCount >= NEW_COST && !oldComputerQueued && !newComputerQueued) {
				addBitcoin(-NEW_COST);
				newComputerQueued = true;
			}
		}

		// Generates a random row to place a Zombie at.
		int randRow = (randGenerator.nextInt(NUM_ROWS) + 2) * (CELL_SIZE + ROW_SPACING);

		// Randomly generates Zombies based on the difficulty. Viruses will not appear
		// until a difficulty of 2.
		if (randGenerator.nextInt(200) < difficulty) {
			Hacker hacker = new Hacker(new Point2D.Double((NUM_COLS + 3) * CELL_SIZE, randRow));
			actors.add(hacker);
		}
		if (randGenerator.nextInt(200) < (difficulty / 2)) {
			Virus virus = new Virus(new Point2D.Double((NUM_COLS + 3) * CELL_SIZE, randRow));
			actors.add(virus);
		}

		// Checks if there are any duplicated viruses to be placed and adds them to
		// actors. Then clears the list of viruses to add.
		for (Actor actor : Virus.virusesToAdd) {
			actors.add(actor);
		}
		Virus.virusesToAdd.clear();

		// This method is getting a little long, but it is mostly loop code
		// Increment their cooldowns and reset collision status
		for (Actor actor : actors) {
			actor.update();
		}

		// Try to attack
		for (Actor actor : actors) {
			for (Actor other : actors) {
				actor.attack(other);
			}
		}

		// Remove plants and zombies with low health
		ArrayList<Actor> nextTurnActors = new ArrayList<>();
		for (Actor actor : actors) {
			if (actor.isAlive())
				nextTurnActors.add(actor);
			else
				actor.removeAction(actors); // any special effect or whatever on removal
		}
		actors = nextTurnActors;

		// Check for collisions between zombies and plants and set collision status
		for (Actor actor : actors) {
			for (Actor other : actors) {
				actor.setCollisionStatus(other);
			}
		}

		// Move the actors.
		for (Actor actor : actors) {
			actor.move(); // for Zombie, only moves if not colliding.
		}

		// Checks the position of actors, if one has passed the end of the screen, the
		// game will end.
		for (Actor actor : actors) {
			if (actor.getPosition().getX() <= 0) {
				gameOver();
			}
		}

		// Redraw the new scene
		repaint();
	}

	/**
	 * Make the game and run it
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame app = new JFrame("Computers vs. Hackers");
				app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				Game panel = new Game();

				app.addMouseListener(panel); // Adds a MouseListener to panel for checking plant placement based of of
												// mouse clicks.
				app.setContentPane(panel);
				app.pack();
				app.setVisible(true);
			}
		});
	}

	/**
	 * 
	 * This is triggered by a mouse press. When the mouse is pressed, this will
	 * first check the position of the mouse to make sure it's within the game
	 * bounds. Then will check if a plant has been queued for placement. If a plant
	 * is queued, it will add the plant of the type of the button. Then will clear
	 * the que of that plant.
	 * 
	 * @param e
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getX() > CELL_SIZE && e.getX() < (CELL_SIZE * (NUM_COLS + 1)) && e.getY() > 150
				&& e.getY() < (CELL_SIZE * (NUM_ROWS + 3))) {
			if (oldComputerQueued) {
				OldComputer oldComputer = new OldComputer(new Point2D.Double(((int) (e.getX() / CELL_SIZE) * CELL_SIZE),
						((int) (e.getY() / (CELL_SIZE + ROW_SPACING))) * (CELL_SIZE + ROW_SPACING)));
				actors.add(oldComputer);
				oldComputerQueued = false;
			}

			if (newComputerQueued) {
				NewComputer newComputer = new NewComputer(new Point2D.Double(((int) (e.getX() / CELL_SIZE) * CELL_SIZE),
						((int) (e.getY() / (CELL_SIZE + ROW_SPACING))) * (CELL_SIZE + ROW_SPACING)));
				actors.add(newComputer);
				newComputerQueued = false;
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	/**
	 * 
	 * This is triggered by a keyboard press. If the space bar is pressed on the end
	 * game screen, it will start a new game, other wise it acts as a button press,
	 * selecting the most recent plant purchase for convenience.
	 * 
	 * @param e
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == ' ') {
			startGame();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}