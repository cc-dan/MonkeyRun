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
	private Piedra piedra;
	private Timer timer_piedra = new Timer(120);
	private boolean bloqueado = false;
	private Image run;
	private Image saltando;
	private Image muerto;
	
	public Mono(int x, int y, Entorno entorno) {
		this.x = x;
		this.y = y;
		this.entorno = entorno;
		this.colision = false;
		
		this.piedra = new Piedra(this.x, this.y, entorno);
		
		this.run = Herramientas.cargarImagen("run.png");
		this.saltando = Herramientas.cargarImagen("saltando.png");
		this.muerto = Herramientas.cargarImagen("agila.jpg");
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
	public int getXpiedra() {
		return this.piedra.getX();
	}	
	public int getYpiedra() {
		return this.piedra.getY();
	}	
	public int getRadioPiedra() {
		return this.piedra.getRadio();
	}	
	
	public void saltar() {
		this.vel_vertical = -this.vel_salto;
	}
	public void mover(int spd) {
		this.x += spd;
	}
	
	public void lanzar_piedra() {
		this.piedra.setX(this.x);
		this.piedra.setY(this.y);
		this.piedra.cambiarEstado();
	}
	
	public void resetear_piedra() {
		this.piedra.setX(this.x);
		this.piedra.setY(this.y);
		if (this.piedra.getVel() > 0) {
			this.piedra.cambiarEstado();
		}
	}
	
	public boolean get_piedra_colision(int izq, int der, int hei, int wei) {
		return this.piedra.piedra_colision(izq, der, hei, wei);
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
		
		// Lanzar piedra
		if (this.entorno.sePresiono(entorno.TECLA_ESPACIO) && this.piedra.getVel() == 0) {
			if (this.timer_piedra.getContador() <= 0) {
				this.lanzar_piedra();
				this.timer_piedra.empezar();
			}
		}
		this.timer_piedra.actualizar();
		
		// Reposicionar piedra cuando se sale del mapa o choca un enemigo
		if ((this.piedra.getX() > this.entorno.ancho() || this.piedra.getY() > this.entorno.alto()) && this.piedra.getVel() > 0) {
			this.piedra.cambiarEstado();
		}
		
		if (this.piedra.getVel() > 0) {
			this.piedra.dibujar();
		}
		this.piedra.actualizar();
	}
	
	public void dibujar() {
		//this.entorno.dibujarRectangulo(this.x, this.y, this.w, this.h, 0, null);
		
		Image img = this.run;
		
		if (this.vel_vertical != 0) {
			img = this.saltando;
		}
		
		this.entorno.dibujarImagen(img, this.x, this.y, 0);
	}
}
