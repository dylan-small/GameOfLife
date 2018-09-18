package com.dylanscode.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.SwingUtilities;

import com.dylanscode.life.Main;

public class MouseHandler implements MouseListener {
	public MouseHandler(Main game) {
		game.addMouseListener(this);
	}

	public class MousePart {
		private boolean clicked;
		private boolean pressed;
		private boolean released;
		private boolean dragged;
		private int x;
		private int y;

		public boolean isClicked() {
			return clicked;
		}

		public void setClicked(boolean clicked) {
			this.clicked = clicked;
		}

		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}

		public int getY() {
			return y;
		}

		public void setY(int y) {
			this.y = y;
		}

		public boolean isPressed() {
			return pressed;
		}

		public void setPressed(boolean pressed) {
			this.pressed = pressed;
		}

		public boolean isReleased() {
			return released;
		}

		public void setReleased(boolean released) {
			this.released = released;
		}

		public boolean isDragged() {
			return dragged;
		}

		public void setDragged(boolean dragged) {
			this.dragged = dragged;
		}
	}

	public MousePart LEFT_CLICK = new MousePart();
	public MousePart RIGHT_CLICK = new MousePart();
	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		if (SwingUtilities.isLeftMouseButton(arg0)) {
			LEFT_CLICK.setPressed(true);
			LEFT_CLICK.setX(arg0.getX());
			LEFT_CLICK.setY(arg0.getY());
		}
		if(SwingUtilities.isRightMouseButton(arg0)) {
			RIGHT_CLICK.setPressed(true);
			RIGHT_CLICK.setX(arg0.getX());
			RIGHT_CLICK.setY(arg0.getY());
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		if(SwingUtilities.isLeftMouseButton(arg0)) {
			LEFT_CLICK.setReleased(true);
		}
	}
}
