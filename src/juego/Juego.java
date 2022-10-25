package juego;

import entorno.Entorno;
import entorno.InterfaceJuego;
import java.util.Random;

public class Juego extends InterfaceJuego
{
	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;
	
	private Mono mono;
	private Plataforma piso;
	private Plataforma flotante;
	private Plataforma flotante2;
	private Plataforma flotante3;
	private Plataforma flotante4;
	private Plataforma[] plataformas = new Plataforma[8];
	private Depredador leon;
	private Depredador leon2;
	private Depredador leon3;
	private Depredador leon4;
	private Depredador[] depredadores = new Depredador[4];
	private int limite;
	private int vel_juego = 3;
	private Random rand = new Random();
	
	// Variables y métodos propios de cada grupo
	// ...
	
	Juego()
	{
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Boss Rabbit Rabber - Grupo ... - v1", 1024, 480);
		
		// Inicializar lo que haga falta para el juego
		// ...
		this.mono = new Mono(400, 0, this.entorno);
		
		this.piso = new Plataforma(0+this.entorno.ancho()/2, this.entorno.alto()+8, this.entorno.ancho(), 16, this.entorno);
		this.flotante = new Plataforma(0, this.entorno.alto()-64, 64, 16, this.entorno);
		this.flotante2 = new Plataforma(250, this.entorno.alto()-128, 64, 16, this.entorno);
		this.flotante3 = new Plataforma(500, this.entorno.alto()-80, 64, 16, this.entorno);
		this.flotante4 = new Plataforma(650, this.entorno.alto()-70, 64, 16, this.entorno);
		
		this.plataformas[0] = this.piso;
		this.plataformas[1] = this.flotante;
		this.plataformas[2] = this.flotante2;
		this.plataformas[3] = this.flotante3;
		this.plataformas[4] = this.flotante4;

		this.leon = new Depredador(300, 448, entorno);
		this.leon2 = new Depredador(400, 448, entorno);
		this.leon3 = new Depredador(580, 448, entorno);
		this.leon4 = new Depredador(660, 448, entorno);
		this.depredadores[0] = this.leon;
		this.depredadores[1] = this.leon2;
		this.depredadores[2] = this.leon3;
		this.depredadores[3] = this.leon4;
		
		this.limite = 0;
		
		// Inicia el juego!
		this.entorno.iniciar();
	}

	/**
	 * Durante el juego, el método tick() será ejecutado en cada instante y 
	 * por lo tanto es el método más importante de esta clase. Aquí se debe 
	 * actualizar el estado interno del juego para simular el paso del tiempo 
	 * (ver el enunciado del TP para mayor detalle).
	 */
	public void tick()
	{	
		// colisiones jugador
		// array de todas las plataformas para checkear colision con cada una
		for (int i = plataformas.length-1; i >= 0; i--) {
			if (plataformas[i] != null) {
				plataformas[i].dibujar();
				// Puntos de interés para calcular colisiones
				int superficie_plataforma = plataformas[i].getY()-plataformas[i].getH()/2;
				int extremod_plataforma = plataformas[i].getX()+plataformas[i].getW();
				int extremoi_plataforma = plataformas[i].getX()-plataformas[i].getW();
;				
				/* Funciona de forma que la plataforma con la que colisionamos siempre sea la ultima del loop, 
				para que ninguna otra pueda sobreescribir el valor de mono.colision */
				if (i >= this.limite) {
					mono.colision = mono.getX()-16 >= extremoi_plataforma && 
									mono.getX()+16 <= extremod_plataforma &&
									mono.getY()+32+mono.get_vel() >= superficie_plataforma &&
									(mono.get_vel() >= 0 && mono.getY()+32 <= superficie_plataforma); // Si está cayendo, que esté por arriba
									
					if (mono.colision) {
						this.limite = i;
					} else {
						this.limite = 0;
					}
				}
				
				// Scrolling
				if (i > 0) { // No aplica al piso
					if (extremod_plataforma < 0) {
						// Cambio de las posiciones verticales y horizontales
						plataformas[i].setX(this.entorno.ancho()+plataformas[i].getW()/2);
						
						int nuevo_y = (int)(Math.random() * 48 + 24); // Unidades maximas y minimas de movimiento vertical
						//System.out.println(nuevo_y);
						if (plataformas[i].getY() + nuevo_y > this.entorno.alto()-64) { // Limite de altura. Max altura = (entorno-64 - maximo movimiento vertical) 
							nuevo_y = -nuevo_y;
						} 
						plataformas[i].setY(plataformas[i].getY() + nuevo_y);
						
						// Spawnear depredadores en las plataformas
						if (plataformas[i].getY() % 5 == 0) {
							System.out.println("yep");
							for (int x = depredadores.length-1; x >= 0; x--) {
								if (depredadores[x] != null) {
									if (depredadores[x].offscreen) {
										depredadores[x].reposicionar(plataformas[i].getX(), plataformas[i].getY());
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
					}
				}								
			}
		}
		for (int i = depredadores.length-1; i >= 0; i--) {
			if (depredadores[i] != null) {
				int vel_depredador = vel_juego;
				if (depredadores[i].getY() >= 448) {
					vel_depredador += 1;
				}
				depredadores[i].mover(vel_depredador);
				
				if (depredadores[i].getX() < 0) {
					depredadores[i].reposicionar(1900, 448);
				}
				
				depredadores[i].actualizar();
				depredadores[i].dibujar();
			}
		}
		
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
