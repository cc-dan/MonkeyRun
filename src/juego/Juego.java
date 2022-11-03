package juego;

import entorno.Entorno;
import entorno.Herramientas;
import entorno.InterfaceJuego;

import java.awt.Color;
import java.awt.Image;

public class Juego extends InterfaceJuego
{
	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;
	
	private Mono mono;
	private Piedra piedra;
	private Plataforma[] plataformas = new Plataforma[7];
	private Depredador[] depredadores = new Depredador[8];
	private Timer timer_aparicion_depredadores = new Timer(200);
	private int limite;
	private int vel_juego = 4;
	private int ultimo_y;
	
	private int nivel_piso = 464;
	
	private int puntaje = 0;
	
	Juego()
	{
		this.entorno = new Entorno(this, "Monkey Run", 1024, 480);
		
		this.mono = new Mono(64, nivel_piso-32, this.entorno);
		
		this.plataformas[0] = new Plataforma(this.entorno.ancho()/2, this.entorno.alto()+8, this.entorno.ancho(), 16, this.entorno);
		this.plataformas[1] = new Plataforma(0, this.entorno.alto()-64, 96, 16, this.entorno);
		this.plataformas[2] = new Plataforma(250, this.entorno.alto()-128, 96, 16, this.entorno);
		this.plataformas[3] = new Plataforma(900, this.entorno.alto()-80, 96, 16, this.entorno);
		this.plataformas[4] = new Plataforma(1100, this.entorno.alto()-70, 96, 16, this.entorno);
		this.plataformas[5] = new Plataforma(600, this.entorno.alto()-80, 96, 16, this.entorno);
		this.plataformas[6] = new Plataforma(750, this.entorno.alto()-70, 96, 16, this.entorno);
		
		this.limite = 0;		
		this.ultimo_y = this.mono.getY();
		
		this.entorno.iniciar();
	}
	
	private void spawnear_depredador() {
		for (int i = 0; i < depredadores.length; i++) {
			if (depredadores[i] == null) {
				int pos_x = 2000;
				int pos_y = nivel_piso;
				for (int x = 1; x < plataformas.length; x++ ) {
					if (plataformas[x].getX() > entorno.ancho() && plataformas[x].getY() % 9 == 0) {
						pos_x = plataformas[x].getX();
						pos_y = plataformas[x].getY();
						break;
					}
				}				
				depredadores[i] = new Depredador(pos_x, pos_y);
				break;
			}
		}
	}
	
	public void tick()
	{	
		entorno.dibujarImagen(Herramientas.cargarImagen("fondo.jpg"), entorno.ancho() / 2, entorno.alto() / 2 , 0);
		
		// LANZAR PIEDRAS
		if (this.piedra == null) {
			if (this.entorno.sePresiono(entorno.TECLA_ALT)) {
				this.piedra = mono.lanzar_piedra();
			}
		} else {
			this.piedra.actualizar();
			this.piedra.dibujar(entorno);
			
			if (this.piedra.getX() > this.entorno.ancho()) {
				this.piedra = null;
			}
		}
		
		// Plataformas: movimiento, colision y dibujo
		for (int i = plataformas.length-1; i >= 0; i--) {
			// Puntos de interés para calcular colisiones
			int extremod_plataforma = plataformas[i].getX()+plataformas[i].getW();

			/* Funciona de forma que la plataforma con la que colisionamos siempre sea la ultima del loop, 
			para que ninguna otra pueda sobreescribir el valor de mono.colision */
			if (i >= this.limite) {			
				if (colision(mono, plataformas[i])) {
					this.limite = i;
					mono.colision = true;
					
					// puntaje
					if (this.mono.getY() < 448) {
						if (this.mono.getY() != this.ultimo_y) {
							this.puntaje += 5;
						}
						this.ultimo_y = this.mono.getY();
					}
				} else {
					this.limite = 0;
					mono.colision = false;
				}
			}
			
			if (i > 0) {
				// MOVER PLATAFORMAS
				if (extremod_plataforma < 0) {
					plataformas[i].setX(this.entorno.ancho()+plataformas[i].getW()/2 + 300);
					
					int nuevo_y = (int)(Math.random() * 48 + 24); 					// Unidades maximas y minimas de movimiento vertical
					if (plataformas[i].getY() + nuevo_y > this.entorno.alto()-32) { // Límite de altura para las plataformas
						nuevo_y = -nuevo_y;
					} 
					plataformas[i].setY(plataformas[i].getY() + nuevo_y);
				}
				plataformas[i].mover(vel_juego);
				
				// DIBUJAR ARBOLES 
				if (i % 2 == 0) { // Cada uno tiene dos plataformas
					this.entorno.dibujarRectangulo(plataformas[i].getX() - 80, this.entorno.alto() - 100, 8, 200, 0, null);
					this.entorno.dibujarImagen(Herramientas.cargarImagen("arbol.png"), plataformas[i].getX() - 80, this.entorno.alto() - 161, 0);
				}
			}	
			plataformas[i].dibujar();
		}
		
		
		// Mover los depredadores
		if (timer_aparicion_depredadores.getContador() == 0) {
			spawnear_depredador();
			timer_aparicion_depredadores.empezar();
		}
		timer_aparicion_depredadores.actualizar();
		
		for (int i = depredadores.length-1; i >= 0; i--) {			
			if (depredadores[i] != null) {
				if(colision(mono, depredadores[i])) {
					System.out.println("******");
					//this.reset();
				}
								
				depredadores[i].mover2();
				depredadores[i].actualizar();
				depredadores[i].dibujar(entorno);
				
				if (depredadores[i].getY() < nivel_piso) {
					depredadores[i].set_vel(vel_juego);
				}
				
				// Eliminar a los depredadores
				if (depredadores[i].getX() < 0 || (depredadores[i].getX() > this.entorno.ancho() && depredadores[i].get_vel() < 0)) {
					depredadores[i] = null;
				} else {
					if (piedra != null) {
						if (colision(depredadores[i], piedra) ) {
							this.piedra = null;
							
							// Serpientes mueren, leones huyen
							if (depredadores[i].getY() < 448) {
								depredadores[i] = null;
							} else {						
								depredadores[i].huir2();
							}
						}
					}
				}
			}
		}
		
		mono.actualizar();
		mono.dibujar();
	}

	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		Juego juego = new Juego();
	}
	
	private void pausar() {
		for (int i = 0; i < this.depredadores.length-1; i++) {
			this.depredadores[i].set_vel(vel_juego);
		}
		this.mono.bloquear_control();
		vel_juego = 0;
	}
	private void resumir() {
		for (int i = 0; i <= this.depredadores.length-1; i++) {
			this.depredadores[i].resetear(nivel_piso);
		}
		this.mono.habilitar_control();
		vel_juego = 4;
	}
	private void reset() {
		this.pausar();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			Thread.currentThread().interrupt();
		}
		this.resumir();
	}
	
	private boolean colision(Mono mono, Plataforma plataforma) {
		// Colisión con plataformas; de un solo sentido
		
		int mono_derecha 	= mono.getX()+mono.getW()/2;
		int mono_izquierda 	= mono.getX()-mono.getW()/2;
		int mono_pies 		= mono.getY()+mono.getH()/2;
		
		int plataforma_derecha 	= plataforma.getX()+plataforma.getW();
		int plataforma_izquierda= plataforma.getX()-plataforma.getW();
		int plataforma_cabeza 	= plataforma.getY()-plataforma.getH()/2;
				
		return 	mono_izquierda 			>= plataforma_izquierda && 
				mono_derecha 			<= plataforma_derecha 	&&
				mono_pies+mono.get_vel()>= plataforma_cabeza 	&&
				(mono.get_vel() >= 0 && mono.getY()+32 <= plataforma_cabeza); // Si está cayendo, que esté por arriba
				//TODO: revisar getY
	}
	private boolean colision(Mono mono, Depredador depredador) {
		// Colisión con depredadores; márgen reducido
		
		int mono_derecha 	= mono.getX()+mono.getW()/2;
		int mono_izquierda 	= mono.getX()-mono.getW()/2;
		int mono_pies 		= mono.getY()+mono.getH()/2;
		int mono_cabeza 	= mono.getY()-mono.getH()/2;

		return 	(depredador.getX() >= mono_izquierda) 	&& 
				(depredador.getX() <= mono_derecha) 	&& 
				(depredador.getY() >= mono_cabeza) 		&& 
				(depredador.getY() <= mono_pies);
	}
	private boolean colision(Depredador depredador, Piedra piedra) {
		if (piedra == null) return false;
		
		int depredador_derecha 	= depredador.getX()+depredador.getW()/2;
		int depredador_izquierda= depredador.getX()-depredador.getW()/2;
		int depredador_cabeza 	= depredador.getY()-depredador.getH()/2;
		int depredador_pies 	= depredador.getY()+depredador.getH()/2;
		
		return 	(piedra.getX() >= depredador_izquierda) && 
				(piedra.getX() <= depredador_derecha) 	&& 
				(piedra.getY() >= depredador_cabeza) 	&& 
				(piedra.getY() <= depredador_pies);
	}
}
