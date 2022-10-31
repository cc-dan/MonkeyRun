package juego;

import entorno.Entorno;

public class Piedra {
	private int x, y;
	private int r = 8;
	private int vel = 10;
	private int vel_actual;
	private Entorno entorno;
	
	public Piedra(int x, int y, Entorno entorno) {
		this.x = x;
		this.y = y;
		this.entorno = entorno;
		this.vel_actual = 0;
	}
	
	public int getX() {
		return this.x;
	}
	public int getY() {
		return this.y;
	}
	public int getRadio() {
		return this.r;
	}
	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getVel() {
		return this.vel_actual;
	}
	
	public void cambiarEstado() {
		this.vel_actual = (this.vel_actual - this.vel) * -1;
	}
	
	public boolean piedra_colision(int izq, int der, int hei, int wei) {
		return izq <= this.x+this.r &&
			   der >= this.x-this.r &&
			   hei <= this.y-this.r &&
			   wei >= this.y+this.r;
	}
	
	
	public void actualizar() {
		this.x += vel_actual;
	}
	
	public void dibujar() {
		this.entorno.dibujarCirculo(this.x, this.y, this.r, null);
	}
}
