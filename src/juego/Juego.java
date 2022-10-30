package juego;

import entorno.Entorno;
import entorno.InterfaceJuego;

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
	private int vel_juego = 4;
	
	private int nivel_piso = 464;
	
	// Variables y métodos propios de cada grupo
	// ...
	
	Juego()
	{
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Monkey Run", 1024, 480);
		
		// Inicializar lo que haga falta para el juego
		// ...
		this.mono = new Mono(400, 0, this.entorno);
		
		this.piso = new Plataforma(0+this.entorno.ancho()/2, this.entorno.alto()+8, this.entorno.ancho(), 16, this.entorno);
		this.flotante = new Plataforma(0, this.entorno.alto()-64, 96, 16, this.entorno);
		this.flotante2 = new Plataforma(250, this.entorno.alto()-128, 96, 16, this.entorno);
		this.flotante3 = new Plataforma(500, this.entorno.alto()-80, 96, 16, this.entorno);
		this.flotante4 = new Plataforma(650, this.entorno.alto()-70, 96, 16, this.entorno);
		
		this.plataformas[0] = this.piso;
		this.plataformas[1] = this.flotante;
		this.plataformas[2] = this.flotante2;
		this.plataformas[3] = this.flotante3;
		this.plataformas[4] = this.flotante4;

		this.leon = new Depredador(this.entorno.ancho()+300, nivel_piso, entorno);
		this.leon2 = new Depredador(this.entorno.ancho()+600, nivel_piso, entorno);
		this.leon3 = new Depredador(this.entorno.ancho()+780, nivel_piso, entorno);
		this.leon4 = new Depredador(this.entorno.ancho()+1000, nivel_piso, entorno);
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
		// puntos para calcular colision con el mono, para usar con las plataformas y enemigos
		int mono_derecha = mono.getX()+mono.getW()/2;
		int mono_izquierda = mono.getX()-mono.getW()/2;
		int mono_pies = mono.getY()+mono.getH()/2;
		int mono_cabeza = mono.getY()-mono.getH()/2;
		
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
						if (plataformas[i].getY() + nuevo_y > this.entorno.alto()-64) { // Limite de altura. Max altura = (entorno-64 - maximo movimiento vertical) 
							nuevo_y = -nuevo_y;
						} 
						plataformas[i].setY(plataformas[i].getY() + nuevo_y);
						
						// Spawnear depredadores en las plataformas. Para que no ocurra tan comunmente, solo ocurre cuando la coordenada Y de la plataforma es divisible por 5
						if (plataformas[i].getY() % 5 == 0) {
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
				plataformas[i].dibujar();
			}
		}
		// Mover los depredadores
		int ultimo_x = 0;
		for (int i = depredadores.length-1; i >= 0; i--) {
			if (depredadores[i] != null) {
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
		
		if (this.entorno.sePresiono(this.entorno.TECLA_ENTER)) {
			depredadores[0].huir();
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
