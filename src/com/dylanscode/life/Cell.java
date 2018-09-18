package com.dylanscode.life;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Cell {
	private int x;
	private int y;
	private int size; //each cell is a square, so no need for width and height
	private boolean isInhabited;
	// Used for drawing cells, since each cell is essentially just a rectangle drawn on the screen
	private Rectangle rect;
	private int neighbors;
	public Cell(int x,int y,int size) {
		this.setX(x);
		this.setY(y);
		this.setSize(size);
		this.setInhabited(false);
		this.setNeighbors(0);
		rect = new Rectangle(x+1, y+1, size-2, size-2);
	}
	public boolean isInhabited() {
		return isInhabited;
	}
	public void setInhabited(boolean isInhabited) {
		this.isInhabited = isInhabited;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	/**
	 * Draws cell on JFrame
	 * @param g
	 */
	public void draw(Graphics2D g) {
		if(this.isInhabited()) {
			g.setColor(new Color(234, 117, 0));
			g.fill(this.rect);
			g.draw(this.rect);
		}
	}
	public boolean isOnMouse(int mouseX,int mouseY) {
		return rect.contains(mouseX, mouseY);
	}
	public int getNeighbors() {
		return neighbors;
	}
	public void setNeighbors(int neighbors) {
		this.neighbors = neighbors;
	}
}
