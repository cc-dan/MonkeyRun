package juego;

import entorno.Entorno;

public class Depredador {
	private int x, y;
	private int w = 32;
	private int h = 32;
	private Entorno entorno;
	boolean offscreen;
	
	public Depredador(int x, int y, Entorno entorno) {
		this.x = x;
		this.y = y;
		this.entorno = entorno;
	}
	
	public void reposicionar(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void mover(int vel) {
		this.x -= vel;
	}
	
	public int getX() {
		return this.x;
	}
	public int getY() {
		return this.y;
	}
	
	public void actualizar() {
		this.offscreen = this.x < 0 || this.x > this.entorno.ancho();
	}
	
	public void dibujar() {
		this.entorno.dibujarRectangulo(this.x, this.y, this.w, this.h, 0, null);
	}
}
