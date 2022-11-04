package juego;

import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class Mono {
	private int x, y;
	private int w = 32;
	private int h = 64;
	private int vel_salto = 17;
	private int vel_vertical;
	private Image img_corriendo = Herramientas.cargarImagen("run.png");
	private Image img_saltando = Herramientas.cargarImagen("saltando.png");
	private Image img_actual;
	
	public Mono(int x, int y) {
		this.x = x;
		this.y = y;
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
	public int get_vel() {
		return this.vel_vertical;	
	}
	public void set_vel(int vel) {
		this.vel_vertical = vel;
	}
	
	public void saltar() {
		this.vel_vertical = -this.vel_salto;
	}
	public void mover(int spd) {
		this.x += spd;
	}
	
	public Piedra lanzar_piedra() {
		return new Piedra(this.x, this.y);
	}
	
	public void caer(int velocidad) {
		this.vel_vertical += velocidad;
		this.y += this.vel_vertical;
	}
	
	public void dibujar(Entorno entorno) {		
		if (this.vel_vertical != 0) {
			this.img_actual = this.img_saltando;
		} else {
			this.img_actual = this.img_corriendo;
		}
		
		entorno.dibujarImagen(this.img_actual, this.x, this.y, 0);
	}
}
