package juego;

import entorno.Entorno;
import entorno.Herramientas;
import entorno.InterfaceJuego;

import java.awt.Color;
import java.awt.Image;
import java.util.Random;

public class Juego extends InterfaceJuego
{
	private Entorno entorno;
	
	private Mono mono;
	private Piedra piedra;
	private Plataforma[] plataformas = new Plataforma[7];
	private Depredador[] depredadores = new Depredador[8];
	private Timer timer_aparicion_depredadores = new Timer(120);
	private Timer timer_lanzamiento_piedra = new Timer(120);
	private int plataforma_actual;
	private int vel_juego = 4;
	private int gravedad = 1;
	private int ultimo_y;
	private boolean mono_colisionando;
	
	private int nivel_piso = 464;
	
	private int puntaje = 0;
	
	Juego()
	{
		this.entorno = new Entorno(this, "Monkey Run", 1024, 480);
		
		this.mono = new Mono(64, nivel_piso-32);
		
		this.plataformas[0] = new Plataforma(this.entorno.ancho()/2, this.entorno.alto()+8, this.entorno.ancho(), 16, this.entorno);
		this.plataformas[1] = new Plataforma(0, this.entorno.alto()-64, 96, 16, this.entorno);
		this.plataformas[2] = new Plataforma(250, this.entorno.alto()-128, 96, 16, this.entorno);
		this.plataformas[3] = new Plataforma(900, this.entorno.alto()-80, 96, 16, this.entorno);
		this.plataformas[4] = new Plataforma(1100, this.entorno.alto()-70, 96, 16, this.entorno);
		this.plataformas[5] = new Plataforma(600, this.entorno.alto()-80, 96, 16, this.entorno);
		this.plataformas[6] = new Plataforma(750, this.entorno.alto()-70, 96, 16, this.entorno);
		
		this.plataforma_actual = 0;		
		this.ultimo_y = this.mono.getY();
		
		this.entorno.iniciar();
	}
	
	public void tick()
	{	
		entorno.dibujarImagen(Herramientas.cargarImagen("fondo.jpg"), entorno.ancho() / 2, entorno.alto() / 2 , 0);
		
		// PLATAFORMAS
		for (int i = plataformas.length-1; i >= 0; i--) {
			Plataforma plataforma = plataformas[i];
			// MOVER Y DIBUJAR PLATAFORMAS Y ARBOLES
			if (i > 0) {
				if (plataforma.getX()+plataformas[i].getW() < 0) {
					plataforma.setX(entorno.ancho()+plataforma.getW()/2 + 300);
					
					int nuevo_y = (int)(Math.random() * 48 + 24); 					// Unidades maximas y minimas de movimiento vertical
					if (plataforma.getY() + nuevo_y > entorno.alto()-32) { // Límite de altura para las plataformas
						nuevo_y = -nuevo_y;
					} 
					plataforma.setY(plataforma.getY() + nuevo_y);
				}
				plataforma.mover(vel_juego);
				
				// Arboles
				if (i % 2 == 0) {
					entorno.dibujarRectangulo(plataforma.getX() - 80, entorno.alto() - 100, 8, 200, 0, null);
					entorno.dibujarImagen(Herramientas.cargarImagen("arbol.png"), plataforma.getX() - 80, entorno.alto() - 161, 0);
				}
			}				
			plataforma.dibujar();
			
			// COLISION CON EL MONO
			if (i >= this.plataforma_actual) {			
				if (colision(mono, plataforma)) {
					this.plataforma_actual = i;
					mono_colisionando = true;

					// puntaje
					if (this.mono.getY() < 448) {
						if (this.mono.getY() != this.ultimo_y) {
							this.puntaje += 5;
						}
						this.ultimo_y = this.mono.getY();
					}
				} else {
					this.plataforma_actual = 0;
					mono_colisionando = false;
				}
			}			
		}
		
		// MONO
		if (mono_colisionando) {
			mono.set_vel(0);
			if (entorno.sePresiono(entorno.TECLA_ARRIBA)) {
				mono.saltar();
			}
		} else {
			mono.caer(gravedad);
		}
		mono.dibujar(entorno);
		
		// LANZAR PIEDRAS
		if (piedra == null) {
			if (timer_lanzamiento_piedra.get_contador() == 0) {
				if (entorno.sePresiono(entorno.TECLA_ALT)) {
					piedra = mono.lanzar_piedra();
					timer_lanzamiento_piedra.empezar();
				}
			}
		} else {
			piedra.actualizar();
			piedra.dibujar(entorno);
			
			if (piedra.getX() > entorno.ancho()) {
				piedra = null;
			}
		}
		timer_lanzamiento_piedra.actualizar();
		
		// SPAWNEAR DEPREDADORES
		if (timer_aparicion_depredadores.get_contador() == 0) {
			spawnear_depredador();			
			timer_aparicion_depredadores.set_tiempo((int)(Math.random() * 120 + 50));
			timer_aparicion_depredadores.empezar();
		}
		timer_aparicion_depredadores.actualizar();
		
		// DEPREDADORES
		for (int i = 0; i < depredadores.length; i++) {
			Depredador depredador = depredadores[i];
			if (depredador != null) {											
				depredador.mover();
				depredador.dibujar(entorno);
				
				// Si están en una plataforma se mueven a la misma velocidad
				if (depredador.getY() < nivel_piso) {
					depredador.set_vel(vel_juego);
				}
				
				// Game over
				if(colision(mono, depredador)) {
					this.reset();
				}
				
				// Eliminar a los depredadores cuando salen de la pantalla o les pega una piedra
				if (depredador.getX() < 0 || (depredador.getX() > entorno.ancho() && depredador.get_direccion() > 0)) {
					depredadores[i] = null;
				} else {
					if (piedra != null) {
						if (colision(depredador, piedra) ) {
							this.piedra = null;
							
							// Serpientes mueren, leones huyen
							if (depredador.getY() < nivel_piso) {
								depredadores[i] = null;
							} else {						
								depredador.huir();
							}
						}
					}
				}
			}
		}
	}

	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		Juego juego = new Juego();
	}

	private void reset() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		// Limpiar depredadores
		for (int i = 0; i < depredadores.length; i++) {
			depredadores[i] = null;
		}
		// Rotar plataformas
		for (int i = 1; i < plataformas.length; i++) {
			plataformas[i].setX(plataformas[i].getX() + entorno.ancho());
		}
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
