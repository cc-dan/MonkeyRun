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
	private Plataforma[] plataformas = new Plataforma[8];
	private int limite;
	
	// Variables y métodos propios de cada grupo
	// ...
	
	Juego()
	{
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Boss Rabbit Rabber - Grupo ... - v1", 800, 600);
		
		// Inicializar lo que haga falta para el juego
		// ...
		this.mono = new Mono(400, 0, this.entorno);
		
		this.piso = new Plataforma(400, this.entorno.alto()+8, 820, 16, this.entorno);
		this.flotante = new Plataforma(this.entorno.ancho()/3, this.entorno.alto()-64, 64, 16, this.entorno);
		this.flotante2 = new Plataforma(600, this.entorno.alto()-64, 64, 16, this.entorno);
		
		this.plataformas[0] = this.piso;
		this.plataformas[1] = this.flotante;
		this.plataformas[2] = this.flotante2;

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
				
				/* Funciona de forma que la plataforma con la que colisionamos siempre sea la ultima del loop, 
				para que ninguna otra pueda sobreescribir el valor de mono.colision */
				if (i >= this.limite) {
					mono.colision = mono.getX()-16 >= plataformas[i].getX()-plataformas[i].getW() && 
									mono.getX()+16 <= plataformas[i].getX()+plataformas[i].getW() &&
									mono.getY()+32+mono.get_vel() >= plataformas[i].getY()-plataformas[i].getH()/2 &&
									mono.get_vel() >= 0;					
					if (mono.colision) {
						this.limite = i;
					} else {
						this.limite = 0;
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
