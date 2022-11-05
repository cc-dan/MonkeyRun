package juego;

import entorno.Entorno;
import java.awt.Image;
import entorno.Herramientas;

public class Piedra {
	private int x, y;
	private int vel = 10;
	private Image imagen = Herramientas.cargarImagen("piedra.png");;
	
	public Piedra(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return this.x;
	}
	public int getY() {
		return this.y;
	}
	
	public void mover() {
		this.x += vel;
	}
	public void dibujar(Entorno entorno) {
		entorno.dibujarImagen(this.imagen, getX(), getY(), 0);
	}
}
