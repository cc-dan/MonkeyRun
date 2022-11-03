package juego;

import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class Depredador {
	private int x, y;
	private int w = 32;
	private int h = 32;
	boolean offscreen;
	private int velocidad = 6;
	private int vel_actual;
	private Image img_puma; //VARIABLE QUE VA ALMACENAR LA IMG PARA LOS PUMAS
	private Image img_serpiente;//VARIABLE QUE VA ALMACENAR LA IMG PARA LAS SERPIENTES
	private int direccion = -1;
	
	public Depredador(int x, int y, Entorno entorno) {
		this.x = x;
		this.y = y;
		this.vel_actual = velocidad;
		
		this.img_puma = Herramientas.cargarImagen("puma.png"); //CARGAMOS EL ARCHIVO EN LA VARIABLE "puma"
		
		this.img_serpiente = Herramientas.cargarImagen("serpiente.png"); //CARGAMOS EL ARCHIVO EN LA VARIABLE "serpiente"
	}
	// NUEVO CONSTRUCTOR
	public Depredador(int x, int y) {
		this.x = x;
		this.y = y;
		this.vel_actual = velocidad;
		
		this.img_puma = Herramientas.cargarImagen("puma.png"); //CARGAMOS EL ARCHIVO EN LA VARIABLE "puma"
		
		this.img_serpiente = Herramientas.cargarImagen("serpiente.png"); //CARGAMOS EL ARCHIVO EN LA VARIABLE "serpiente"
	}
	
	public void reposicionar(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	public void resetear(int y) {
		int diferencia = (int)(Math.random() * 256 + 64);
		
		this.vel_actual = this.velocidad;
		this.reposicionar(1900 + diferencia, y);
	}
	
	public void huir() {
		if (this.y < 464) {
			this.reposicionar(1900, 464);
			return;
		}
		
		this.set_vel(6);
	}
	
	public void dormir() {
		this.h = this.h / 2;
	}
	
	public void mover() {
		this.x += this.vel_actual * direccion;
	}
	public void huir2() {
		this.cambiar_direccion();
		this.set_vel(vel_actual * 2);
	}
	public void cambiar_direccion() {
		this.direccion *= -1;
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
		return this.vel_actual;
	}
	public int get_direccion() {
		return this.direccion;
	}
	public void set_vel(int vel) {
		this.vel_actual = vel;
	}
	
	public void dibujar(Entorno entorno) {
		Image img = this.img_puma;
		
		if (this.y < 464) {
			img = this.img_serpiente;
		}
		
		//this.entorno.dibujarRectangulo(this.x, this.y, this.w, this.h, 0, null);
		entorno.dibujarImagen(img, this.x, this.y, 0);
	}
}
