package juego;

import entorno.Entorno;

public class Mono {
	private int x, y;
	private int gravedad = 1;
	private int vel_salto = 15;
	private int vel_vertical;
	private Entorno entorno;
	
	public Mono(int x, int y, Entorno entorno) {
		this.x = x;
		this.y = y;
		this.entorno = entorno;
	}
	
	public void saltar() {
		this.vel_vertical = -this.vel_salto;
	}
	
	public void actualizar() {
		if (this.y < this.entorno.ancho() - 400) {
			if (this.vel_vertical <= 8) {
				this.vel_vertical += this.gravedad;
			}
		} else {
			this.vel_vertical = 0;
			if (this.entorno.estaPresionada(entorno.TECLA_ARRIBA)) {
				this.saltar();
			}
		}
		this.y += this.vel_vertical;
	}
	
	public void dibujar() {
		this.entorno.dibujarRectangulo(this.x, this.y, 32, 64, 0, null);
	}
}
