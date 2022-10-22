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
	private int limite;
	private int vel_juego = 3;
	
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
						plataformas[i].setX(this.entorno.ancho()+plataformas[i].getW()/2);
					}
					
					plataformas[i].mover(vel_juego);
					
					// Dibujar arboles. Uno por cada dos plataformas
					if (i % 2 == 0) {
						this.entorno.dibujarRectangulo(plataformas[i].getX() - 80, this.entorno.alto() - 100, 8, 200, 0, null);
					}
				}								
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
