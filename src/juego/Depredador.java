package juego;

import entorno.Entorno;

public class Depredador {
	private int x, y;
	private int w = 32;
	private int h = 32;
	private Entorno entorno;
	boolean offscreen;
	private int vel_inicial = -1;
	private int vel_actual;
	
	public Depredador(int x, int y, Entorno entorno) {
		this.x = x;
		this.y = y;
		this.entorno = entorno;
		this.vel_actual = vel_inicial;
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
	
	public void actualizar() {
		this.offscreen = this.x < 0 || this.x > this.entorno.ancho();
	}
	
	public void dibujar() {
		this.entorno.dibujarRectangulo(this.x, this.y, this.w, this.h, 0, null);
	}
}
