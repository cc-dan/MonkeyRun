package juego;

import java.awt.Color;
import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class Piedra {
	private int x, y;
	private int r = 8;
	private int vel = 10;
	private int vel_actual;
	private Entorno entorno;
	private Color c=new Color(0f,0f,0f,0f);
	private Image imagen;
	
	public Piedra(int x, int y, Entorno entorno) {
		this.x = x;
		this.y = y;
		this.entorno = entorno;
		this.vel_actual = 0;
		
		this.imagen = Herramientas.cargarImagen("piedra.png");
	}
	
	public Piedra(int x, int y) {
		this.x = x;
		this.y = y;
		this.vel_actual = 0;
		
		this.imagen = Herramientas.cargarImagen("piedra.png");
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
	public void setVel(int vel) {
		this.vel_actual = vel;
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
		this.x += vel;
	}
	
	public void dibujar() {
		this.entorno.dibujarCirculo(this.x, this.y, this.r, c);
		this.entorno.dibujarImagen(this.imagen, getX(), getY(), 0);
	}
	public void dibujar(Entorno entorno) {
		entorno.dibujarCirculo(this.x, this.y, this.r, c);
		entorno.dibujarImagen(this.imagen, getX(), getY(), 0);
	}
}
