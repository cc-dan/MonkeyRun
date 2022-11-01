package juego;

import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class Depredador {
	private int x, y;
	private int w = 32;
	private int h = 32;
	private Entorno entorno;
	boolean offscreen;
	private int vel_inicial = -1;
	private int vel_actual;
	private Image img_puma; //VARIABLE QUE VA ALMACENAR LA IMG PARA LOS PUMAS
	private Image img_serpiente;//VARIABLE QUE VA ALMACENAR LA IMG PARA LAS SERPIENTES
	
	public Depredador(int x, int y, Entorno entorno) {
		this.x = x;
		this.y = y;
		this.entorno = entorno;
		this.vel_actual = vel_inicial;
		
		this.img_puma = Herramientas.cargarImagen("puma.png"); //CARGAMOS EL ARCHIVO EN LA VARIABLE "puma"
		
		this.img_serpiente = Herramientas.cargarImagen("serpiente.png"); //CARGAMOS EL ARCHIVO EN LA VARIABLE "serpiente"
	}
	
	public void reposicionar(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void resetear(int y) {
		int diferencia = (int)(Math.random() * 256 + 64);
		
		this.vel_actual = this.vel_inicial;
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
		this.mover(0);
	}
	public void mover(int mod) {
		this.x += this.vel_actual - mod;
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
	public void set_vel(int vel) {
		this.vel_actual = vel;
	}
	
	public boolean depredador_colision(int derecha, int izquierda, int pies, int cabeza) {		
		boolean colision = false;
		
		if(this.getX() >= izquierda && 
		   this.getX() <= derecha && 
		   this.getY() >= cabeza && 
		   this.getY() <= pies) {
		 colision = true;
		}		
		
		return colision; 
	}
	
	public void actualizar() {
		this.offscreen = this.x < 0 || this.x > this.entorno.ancho();
	}
	
	public void dibujar() {
		Image img = this.img_puma;
		
		if (this.y < 464) {
			img = this.img_serpiente;
		}
		
		//this.entorno.dibujarRectangulo(this.x, this.y, this.w, this.h, 0, null);
		this.entorno.dibujarImagen(img, this.x, this.y, 0);
	}
}
