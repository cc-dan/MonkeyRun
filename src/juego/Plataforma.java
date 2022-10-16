package juego;

import entorno.Entorno;

public class Plataforma {
	private int x, y;
	private Entorno entorno;
	
	public Plataforma(int x, int y, Entorno entorno) {
		this.x = x;
		this.y = y;
		this.entorno = entorno;
	}
	
	public void dibujar() {
		this.entorno.dibujarRectangulo(this.x, this.y, 64, 16, 0, null);
	}
}
