package com.dylanscode.life;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import com.dylanscode.input.KeyHandler;
import com.dylanscode.input.MouseHandler;
import com.dylanscode.input.MouseMotionHandler;

public class Main extends JFrame implements Runnable {
	private static final long serialVersionUID = 1L;
	public boolean simRunning = false;
	//Buffer Strategy prevents screen tearing
	BufferStrategy bs;
	Graphics g;
	Board board;
	//Input Handlers
	KeyHandler keyHandler;
	MouseHandler mouseHandler;
	MouseMotionHandler mouseMotionHandler;
	//Each iteration of a Main class needs a Cell[][] array
	Cell[][] cells;
	
	private boolean debug = false;
	public boolean isRunning = false;

	public static void main(String[] args) {
		new Main().start();
	}

	/**
	 * Main GameOfLife loop!
	 */
	@Override
	public void run() {
		long lastTime = System.nanoTime();
		//nanoseconds per tick (1 ^ 10^9 / 5)
		double nsPerTick = 1000000000 / 25D;
		//nanoseconds per input tick (1 ^ 10^9 / 60)
		double nsPerInputTick = 1000000000 / 60D;
		int frames = 0;
		int ticks = 0;
		int inputTicks = 0;
		long lastTimer = System.currentTimeMillis();
		double deltaTick = 0;
		double deltaInput = 0;
		initialize(); 
		while (isRunning) {
			long now = System.nanoTime();
			deltaTick += (now - lastTime) / nsPerTick;
			deltaInput += (now - lastTime) / nsPerInputTick;
			lastTime = now;
			boolean shouldRender = true;
			while (deltaTick >= 1) {
				ticks++;
				tick();
				deltaTick -= 1;
			}
			while (deltaInput >= 1) {
				inputTicks++;
				checkForInput();
				deltaInput -= 1;
			}
			if (shouldRender) {
				frames++;
				paint(g);
			}
			if (System.currentTimeMillis() - lastTimer > 1000) {
				lastTimer += 1000;
				System.out.println("Frames: " + frames + " | Ticks: " + ticks + " | Input Ticks: " + inputTicks);
				frames = 0;
				ticks = 0;
				inputTicks = 0;
			}
		}
	}
	/**
	 * Starts the game by setting running to true and starting the Main thread.
	 */
	public synchronized void start() {
		isRunning = true;
		new Thread(this).start();
	}
	/**
	 * Stops the game
	 */
	public synchronized void stop() {
		isRunning = false;
	}
	/**
	 * Called once at the very start of the Simulation
	 */
	public void initialize() {
		setTitle("GameOfLife");
		//Makes JFrame full screen
		setUndecorated(true);
		setVisible(true);
		setAlwaysOnTop(true);
		setResizable(false);
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		try {
			gd.setFullScreenWindow(this);
		} catch (Exception e) {
			gd.setFullScreenWindow(null);
		}
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screen = tk.getScreenSize();
		setMinimumSize(screen);
		setPreferredSize(screen);
		setMaximumSize(screen);
		
		pack();
		setLocationRelativeTo(null);
		//initialized bufferstrategy
		createBufferStrategy(2);
		bs = getBufferStrategy();
		g = getGraphics();
		//initialized board (100 width) and fills the cells array
		board = new Board(this, 100);
		cells = board.createCells();
		//initializes input handlers
		keyHandler = new KeyHandler(this);
		mouseHandler = new MouseHandler(this);
		mouseMotionHandler = new MouseMotionHandler(this, mouseHandler);
	}

	/**
	 * Runs 5 times a second to update the cells
	 */
	public void tick() {
		if (simRunning) {
			update(cells);
		}
	}
	/**
	 * Runs 60 times a second to check for input from keyboard and mouse
	 */
	public void checkForInput() {
		if (keyHandler != null && keyHandler.ESC.isPressed()) {
			System.exit(0);
		}
		if (mouseHandler != null
				&& (mouseHandler.LEFT_CLICK.isClicked() || mouseMotionHandler.handler.LEFT_CLICK.isDragged())
				&& (!simRunning)) {
			int eventX = mouseHandler.LEFT_CLICK.getX();
			int eventY = mouseHandler.LEFT_CLICK.getY();
			for (int y = 0; y < cells.length; y++) {
				for (int x = 0; x < cells[y].length; x++) {
					if (cells[y][x].isOnMouse(eventX, eventY))
						cells[y][x].setInhabited(true);
				}
			}
		}
		if (mouseHandler != null
				&& (mouseHandler.RIGHT_CLICK.isClicked() || mouseMotionHandler.handler.RIGHT_CLICK.isDragged())
				&& (!simRunning)) {
			int eventX = mouseHandler.RIGHT_CLICK.getX();
			int eventY = mouseHandler.RIGHT_CLICK.getY();
			for (int y = 0; y < cells.length; y++) {
				for (int x = 0; x < cells[y].length; x++) {
					if (cells[y][x].isOnMouse(eventX, eventY))
						cells[y][x].setInhabited(false);
				}
			}
		}
		//Turns on the simulation
		if (!simRunning && keyHandler.ENTER.isPressed()) {
			simRunning = true;
		}
	}
	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = null;
		if (bs != null) {
			do {
				try {
					g2 = (Graphics2D) bs.getDrawGraphics();
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2.setColor(Color.white);
					g2.fillRect(0, 0, getWidth(), getHeight());
					//draws board and each cell
					board.draw(g2);
					for (int y = 0; y < cells.length; y++) {
						for (int x = 0; x < cells[y].length; x++) {
							cells[y][x].draw(g2);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (g2 != null)
						g2.dispose();
				}
				bs.show();
			} while (bs.contentsLost());
		}
	}

	/**
	 * Updates the game of life using rules
	 * 
	 * @param cells
	 */
	public void update(Cell[][] cells) {
		board.findNeighbors(cells);
		for (int y = 0; y < cells.length; y++) {
			for (int x = 0; x < cells[y].length; x++) {
				if ((cells[y][x].getNeighbors() < 2) && cells[y][x].isInhabited()) {
					cells[y][x].setInhabited(false);
				} else if (cells[y][x].getNeighbors() > 3 && cells[y][x].isInhabited()) {
					cells[y][x].setInhabited(false);
				} else if ((cells[y][x].getNeighbors() == 2 || cells[y][x].getNeighbors() == 3)
						&& cells[y][x].isInhabited()) {
					cells[y][x].setInhabited(true);
				} else if ((cells[y][x].getNeighbors() == 3) && !(cells[y][x].isInhabited())) {
					cells[y][x].setInhabited(true);
				} else {
					cells[y][x].setInhabited(false);
				}

			}
		}
		board.resetNeighbors(cells);
	}

	public boolean isDebug() {
		return debug;
	}

}
