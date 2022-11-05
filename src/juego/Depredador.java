package juego;

import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class Depredador {
	private int x, y;
	private int w = 32;
	private int h = 32;
	private int velocidad = 6;
	private int vel_actual;
	private Image img_puma = Herramientas.cargarImagen("puma.png");	
	private Image img_serpiente = Herramientas.cargarImagen("serpiente.png");
	private int direccion = -1;
	
	// NUEVO CONSTRUCTOR
	public Depredador(int x, int y) {
		this.x = x;
		this.y = y;
		this.vel_actual = velocidad;
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
	public int get_direccion() {
		return this.direccion;
	}
	public void set_vel(int vel) {
		this.vel_actual = vel;
	}
	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	public void mover() {
		this.x += this.vel_actual * this.direccion;
	}
	
	public void huir() {
		this.cambiar_direccion();
		this.set_vel(vel_actual * 2);
	}
	
	public void cambiar_direccion() {
		this.direccion *= -1;
	}	
		
	public void dibujar(Entorno entorno) {
		Image img = img_puma;
		
		if (this.y < 464) {
			img = img_serpiente;
		} else {
			img = img_puma;
		}
		entorno.dibujarImagen(img, this.x, this.y, 0);
	}
}
