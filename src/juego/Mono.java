package juego;

import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class Mono {
	private int x, y;
	private int w = 32;
	private int h = 64;
	private int gravedad = 1;
	private int vel_salto = 17;
	private int vel_vertical;
	private Entorno entorno;
	public boolean colision;
	private boolean bloqueado = false;
	private Image img_corriendo;
	private Image img_saltando;
	private Image img_actual;
	
	public Mono(int x, int y, Entorno entorno) {
		this.x = x;
		this.y = y;
		this.entorno = entorno;
		this.colision = false;		
		
		this.img_corriendo = Herramientas.cargarImagen("run.png");
		this.img_saltando = Herramientas.cargarImagen("saltando.png");
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
	
	public void bloquear_control() {
		this.bloqueado = true;
	}
	public void habilitar_control() {
		this.bloqueado = false;
	}
	
	public void actualizar() {
		if (this.bloqueado) {
			return;
		}
		
		if (!this.colision) {
			if (this.vel_vertical <= 5) {
				this.vel_vertical += this.gravedad;
			}
		} else {
			this.vel_vertical = 0;
			if (this.entorno.sePresiono(entorno.TECLA_ARRIBA)) {
				this.saltar();
			}
		}
		this.y += this.vel_vertical;
	}
	
	public void dibujar() {		
		if (this.vel_vertical != 0) {
			this.img_actual = this.img_saltando;
		} else {
			this.img_actual = this.img_corriendo;
		}
		
		this.entorno.dibujarImagen(this.img_actual, this.x, this.y, 0);
	}
}
