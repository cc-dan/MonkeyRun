package juego;

import java.awt.Color;

import entorno.Entorno;

public class Plataforma {
	private int x, y, w, h;
	private Entorno entorno;
	
	public Plataforma(int x, int y, int w, int h, Entorno entorno) {
		this.w = w;
		this.h = h;
		this.x = x;
		this.y = y;
		this.entorno = entorno;
	}
	
	public int getX() {
		return this.x;
	}
	public int getY() {
		return this.y;
	}
	public int getW() {
		return this.w;
	}
	public int getH() {
		return this.h;
	}
	
	public void mover() {
		this.x -= 2;
	}
	
	public void dibujar() {
		this.entorno.dibujarRectangulo(this.x, this.y, this.w, this.h, 0, null);
	}
}
