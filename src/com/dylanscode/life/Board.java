package com.dylanscode.life;

import java.awt.Color;
import java.awt.Graphics2D;

public class Board {
	Main game;
	private int amountOfCellsX;
	private int amountOfCellsY;
	private int cellWidth;
	private int cellHeight;
	//2D Cell array initially created with all null values
	private Cell[][] cells;
	// Positions of cells next to current cell on board
	public int[][] positions = { { -1, 1 }, { 0, 1 }, { 1, 1 }, { -1, 0 }, { 1, 0 }, { -1, -1 }, { 0, -1 }, { 1, -1 } };

	public Board(Main game, int amountOfCellsX) {
		this.game = game;
		this.setAmountOfCellsX(amountOfCellsX);
		//Using ratios we can calculate the amount of cells in the y direction
		this.setAmountOfCellsY((game.getHeight() * amountOfCellsX) / game.getWidth());
		this.cellWidth = game.getWidth() / amountOfCellsX;
		this.cellHeight = cellWidth;
	}
	/**
	 * createCells initializes cells based upon cell width of board
	 * @return cells
	 */
	public Cell[][] createCells() {
		cells = new Cell[++amountOfCellsY][++amountOfCellsX];
		int xCoord = 0;
		int yCoord = 0;
		for (int y = 0; y < cells.length; y++) {
			for (int x = 0; x < cells[y].length; x++) {
				cells[y][x] = new Cell(xCoord, yCoord, cellWidth);
				xCoord += cellWidth;
			}
			xCoord = 0;
			yCoord += cellWidth;
		}
		return cells;
	}
	/**
	 * Draws graphics onto JFrame by setting the color to black, and drawing lines where cell borders are
	 * @param g
	 */
	public void draw(Graphics2D g) {
		g.setColor(Color.BLACK);
		int y = 0;
		int x = 0;
		while (y < game.getHeight()) {
			g.drawLine(0, y, game.getWidth(), y);
			y += cellHeight;
		}
		while (x < game.getWidth()) {
			g.drawLine(x, 0, x, game.getHeight());
			x += cellWidth;
		}

	}
	/**
	 * sets the neighbor int in the cell class to its amount of surrounding neighbors on the board. Using three nested for loops, the 
	 * method loops through y values and x values of the array, then compares each cell to its neighbor in the array, and sets the total amount of neighbors in each cell
	 * @param cells
	 */
	public void findNeighbors(Cell[][] cells) {
		for (int y = 0; y < cells.length; y++) {
			for (int x = 0; x < cells[y].length; x++) {
				for (int i = 0; i < positions.length; i++) {
					int neighborY = positions[i][0] + y;
					int neighborX = positions[i][1] + x;
					if (neighborY < 0)
						neighborY = this.getAmountOfCellsY() - 1;
					if (neighborY >= this.getAmountOfCellsY())
						neighborY = 0;
					if (neighborX < 0)
						neighborX = this.getAmountOfCellsX() - 1;
					if (neighborX >= this.getAmountOfCellsX())
						neighborX = 0;
					if (cells[neighborY][neighborX].isInhabited()) {
						cells[y][x].setNeighbors(cells[y][x].getNeighbors() + 1);
					}	
				}
				if(game.isDebug())
					System.out.println("Cell at ("+x+","+y+") has " + cells[y][x].getNeighbors()+ " neighbors!");
			}
		}
	}
	/**
	 * loops through a 2D cell array and resets the amount of neighbors
	 * @param cells
	 */
	public void resetNeighbors(Cell[][] cells) {
		for(int y=0;y<cells.length;y++) {
			for(int x=0;x<cells[y].length;x++) {
				cells[y][x].setNeighbors(0);
			}
		}
	}
	public int getAmountOfCellsX() {
		return amountOfCellsX;
	}

	public void setAmountOfCellsX(int amountOfCellsX) {
		this.amountOfCellsX = amountOfCellsX;
	}

	public int getAmountOfCellsY() {
		return amountOfCellsY;
	}

	public void setAmountOfCellsY(int amountOfCellsY) {
		this.amountOfCellsY = amountOfCellsY;
	}
}
