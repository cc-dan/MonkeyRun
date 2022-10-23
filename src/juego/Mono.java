package juego;

import entorno.Entorno;

public class Mono {
	private int x, y;
	private int gravedad = 1;
	private int vel_salto = 17;
	private int vel_vertical;
	private Entorno entorno;
	public boolean colision;
	
	public Mono(int x, int y, Entorno entorno) {
		this.x = x;
		this.y = y;
		this.entorno = entorno;
		this.colision = false;
	}
	
	public int getX() {
		return this.x;
	}
	public int getY() {
		return this.y;
	}
	public int get_vel() {
		return this.vel_vertical;
	}
	
	public void saltar() {
		this.vel_vertical = -this.vel_salto;
	}
	public void mover(int spd) {
		this.x += spd;
	}
	
	public void actualizar() {
		if (this.entorno.estaPresionada(entorno.TECLA_DERECHA)) {
			this.mover(4);
		} else if (this.entorno.estaPresionada(entorno.TECLA_IZQUIERDA)){
			this.mover(-4);
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
		this.entorno.dibujarRectangulo(this.x, this.y, 32, 64, 0, null);
	}
}
