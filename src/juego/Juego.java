//first commit github

package juego;

import entorno.Entorno;
import entorno.Herramientas;
import entorno.InterfaceJuego;

import java.awt.Color;
import java.awt.Image;
import java.util.concurrent.TimeUnit;

public class Juego extends InterfaceJuego
{
	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;
	
	private Mono mono;
	private Plataforma[] plataformas = new Plataforma[8];
	private Depredador[] depredadores = new Depredador[4];
	private int limite;
	private int vel_juego = 4;
	private int ultimo_y;
	
	private int nivel_piso = 464;
	
	private int puntaje = 0;
	
	private Image fondo; //VARIABLE QUE VA ALMACENAR LA IMG PARA EL FONDO
	private Image arbol;
	
	// Variables y métodos propios de cada grupo
	// ...
	
	Juego()
	{
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Monkey Run", 1024, 480);
		
		// Inicializar lo que haga falta para el juego
		// ...
		this.mono = new Mono(64, nivel_piso-32, this.entorno);
		
		this.plataformas[0] = new Plataforma(0+this.entorno.ancho()/2, this.entorno.alto()+8, this.entorno.ancho(), 16, this.entorno);
		this.plataformas[1] = new Plataforma(0, this.entorno.alto()-64, 96, 16, this.entorno);
		this.plataformas[2] = new Plataforma(250, this.entorno.alto()-128, 96, 16, this.entorno);
		this.plataformas[3] = new Plataforma(900, this.entorno.alto()-80, 96, 16, this.entorno);
		this.plataformas[4] = new Plataforma(1100, this.entorno.alto()-70, 96, 16, this.entorno);
		this.plataformas[5] = new Plataforma(600, this.entorno.alto()-80, 96, 16, this.entorno);
		this.plataformas[6] = new Plataforma(750, this.entorno.alto()-70, 96, 16, this.entorno);

		this.depredadores[0] = new Depredador(this.entorno.ancho()+300, nivel_piso, entorno);
		this.depredadores[1] = new Depredador(this.entorno.ancho()+600, nivel_piso, entorno);
		this.depredadores[2] = new Depredador(this.entorno.ancho()+780, nivel_piso, entorno);
		this.depredadores[3] = new Depredador(this.entorno.ancho()+1000, nivel_piso, entorno);
		
		this.limite = 0;
		
		Color colorFont = new Color(689);	
		this.entorno.cambiarFont("ITALIC", 50, colorFont);
		this.entorno.escribirTexto("Score: " + puntaje, 30, 50);
		
		this.fondo = Herramientas.cargarImagen("fondo.jpg"); //CARGAMOS EL ARCHIVO EN LA VARIABLE "fondo"
		this.arbol = Herramientas.cargarImagen("arbol.png"); //CARGAMOS EL ARCHIVO EN LA VARIABLE "arbol"
		
		this.ultimo_y = this.mono.getY();
		
		// Inicia el juego!
		this.entorno.iniciar();
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

	/**
	 * Durante el juego, el método tick() será ejecutado en cada instante y 
	 * por lo tanto es el método más importante de esta clase. Aquí se debe 
	 * actualizar el estado interno del juego para simular el paso del tiempo 
	 * (ver el enunciado del TP para mayor detalle).
	 */
	public void tick()
	{	
		entorno.dibujarImagen(fondo, entorno.ancho() / 2, entorno.alto() / 2 , 0);
		
		// puntos para calcular colision con el mono, para usar con las plataformas y enemigos
		int mono_derecha = mono.getX()+mono.getW()/2;
		int mono_izquierda = mono.getX()-mono.getW()/2;
		int mono_pies = mono.getY()+mono.getH()/2;
		int mono_cabeza = mono.getY()-mono.getH()/2;
		
		int piedra_right = mono.getXpiedra() + mono.getRadioPiedra()/2;
		int piedra_left = mono.getXpiedra() - mono.getRadioPiedra()/2;
		int piedra_bottom = mono.getYpiedra() + mono.getRadioPiedra()/2;
		int piedra_top = mono.getYpiedra() - mono.getRadioPiedra()/2;
		
		// colisiones jugador
		// array de todas las plataformas para checkear colision con cada una
		for (int i = plataformas.length-1; i >= 0; i--) {
			if (plataformas[i] != null) {
				// Puntos de interés para calcular colisiones
				int superficie_plataforma = plataformas[i].getY()-plataformas[i].getH()/2;
				int extremod_plataforma = plataformas[i].getX()+plataformas[i].getW();
				int extremoi_plataforma = plataformas[i].getX()-plataformas[i].getW();
;				
				/* Funciona de forma que la plataforma con la que colisionamos siempre sea la ultima del loop, 
				para que ninguna otra pueda sobreescribir el valor de mono.colision */
				if (i >= this.limite) {
					mono.colision = mono_izquierda >= extremoi_plataforma && 
									mono_derecha <= extremod_plataforma &&
									mono_pies+mono.get_vel() >= superficie_plataforma &&
									(mono.get_vel() >= 0 && mono.getY()+32 <= superficie_plataforma); // Si está cayendo, que esté por arriba
									
					if (mono.colision) {
						this.limite = i;
						
						// puntaje
						if (this.mono.getY() < 448) {
							if (this.mono.getY() != this.ultimo_y) {
								this.puntaje += 5;
								System.out.println(puntaje);
							}
							this.ultimo_y = this.mono.getY();
						}
					} else {
						this.limite = 0;
					}
				}
				
				// Scrolling
				if (i > 0) { // No aplica al piso
					if (extremod_plataforma < 0) {
						// Cambio de las posiciones verticales y horizontales
						plataformas[i].setX(this.entorno.ancho()+plataformas[i].getW()/2 + 300);
						
						int nuevo_y = (int)(Math.random() * 48 + 24); // Unidades maximas y minimas de movimiento vertical
						if (plataformas[i].getY() + nuevo_y > this.entorno.alto()-32) { // Limite de altura. Max altura = (entorno-64 - maximo movimiento vertical) 
							nuevo_y = -nuevo_y;
						} 
						plataformas[i].setY(plataformas[i].getY() + nuevo_y);
						
						// Spawnear depredadores en las plataformas. Para que no ocurra tan comunmente, solo ocurre cuando la coordenada Y de la plataforma es divisible por 5
						if (plataformas[i].getY() % 5 == 0) {
							for (int x = depredadores.length-1; x >= 0; x--) {
								if (depredadores[x] != null) {
									if (depredadores[x].offscreen) {
										depredadores[x].reposicionar(plataformas[i].getX(), plataformas[i].getY() - 16);
										break;
									}
								}
							}
						}
 					}
					plataformas[i].mover(vel_juego);
					
					// Dibujar arboles. Uno por cada dos plataformas
					if (i % 2 == 0) {
						this.entorno.dibujarRectangulo(plataformas[i].getX() - 80, this.entorno.alto() - 100, 8, 200, 0, null);
						this.entorno.dibujarImagen(arbol, plataformas[i].getX() - 80, this.entorno.alto() - 161, 0);
					}
				}	
				plataformas[i].dibujar();
			}
		}
		// Mover los depredadores
		int ultimo_x = 0;
		for (int i = depredadores.length-1; i >= 0; i--) {
			if (depredadores[i] != null) {
				int depredador_right = depredadores[i].getX() + depredadores[i].getW()/2;
				int depredador_left = depredadores[i].getX() - depredadores[i].getW()/2;
				int depredador_top = depredadores[i].getY() - depredadores[i].getH()/2;
				int depredador_bottom = depredadores[i].getY() + depredadores[i].getH()/2;	
				
				//Testing berna
				//colision Depredador con mono
				if(depredadores[i].depredador_colision(mono_derecha, mono_izquierda, mono_pies, mono_cabeza)) {
					System.out.println("******");
					this.reset();
				}
				
				//colision Piedra con depredador
				if (piedra_right >= depredador_left && 
					piedra_left <= depredador_right && 
					piedra_top <= depredador_bottom && 
					piedra_bottom >= depredador_top &&
					(this.mono.get_vel_piedra() > 0)){ 
					depredadores[i].huir();
					mono.resetear_piedra();
				}
				
				// Que se queden quietos si están en una plataforma
				if (depredadores[i].getY() < nivel_piso) {
					depredadores[i].set_vel(0);
				}
				if (!(vel_juego == 0)) {
					depredadores[i].mover(vel_juego);
				}
				
				// Reposicionar cuando se escapen
				if (depredadores[i].getX() < 0 || (depredadores[i].offscreen && depredadores[i].get_vel() > 0)) {
					depredadores[i].resetear(nivel_piso);
					
					// Asegurarse de que no se sobrepongan
					if (Math.abs(ultimo_x - depredadores[i].getX()) <= depredadores[i].getW()*2 && depredadores[i].getY() == nivel_piso) {
						depredadores[i].reposicionar(depredadores[i].getX()+depredadores[i].getW()*2, depredadores[i].getY());
					}
				}
				ultimo_x = depredadores[i].getX();
				
				depredadores[i].actualizar();
				depredadores[i].dibujar();
			}
		}
		
		//System.out.println(this.mono.getY());
		
		// Procesamiento de un instante de tiempo
		// ...
		mono.actualizar();
		mono.dibujar();
	}

	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		Juego juego = new Juego();
	}
}
