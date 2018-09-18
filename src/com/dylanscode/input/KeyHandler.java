package com.dylanscode.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.dylanscode.life.Main;

public class KeyHandler implements KeyListener{
	public KeyHandler(Main game) {
		game.addKeyListener(this);
	}
	public class Key{
		boolean isPressed;
		public boolean isPressed() {
			return isPressed;
		}
		public void setPressed(boolean b) {
			isPressed = b;
		}
	}
	public Key ENTER = new Key();
	public Key ESC = new Key();
	public void toggleKey(int keyCode, boolean b) {
		if(keyCode == KeyEvent.VK_ENTER)
			ENTER.setPressed(b);
		if(keyCode == KeyEvent.VK_ESCAPE)
			ESC.setPressed(b);
	}
	@Override
	public void keyPressed(KeyEvent arg0) {
		toggleKey(arg0.getKeyCode(),true);
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		toggleKey(arg0.getKeyCode(),false);
	}
	@Override
	public void keyTyped(KeyEvent arg0) {}

}
