package com.dylanscode.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.SwingUtilities;

import com.dylanscode.life.Main;

public class MouseMotionHandler implements MouseMotionListener {
	public MouseHandler handler;
	public MouseMotionHandler(Main game,MouseHandler handler) {
		game.addMouseMotionListener(this);
		this.handler = handler;
	}
	@Override
	public void mouseDragged(MouseEvent arg0) {
		if(SwingUtilities.isLeftMouseButton(arg0)) {
			handler.LEFT_CLICK.setDragged(true);
			handler.LEFT_CLICK.setX(arg0.getX());
			handler.LEFT_CLICK.setY(arg0.getY());
		}
		if(SwingUtilities.isRightMouseButton(arg0)) {
			handler.RIGHT_CLICK.setDragged(true);
			handler.RIGHT_CLICK.setX(arg0.getX());
			handler.RIGHT_CLICK.setY(arg0.getY());
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		
	}

}
