package juego;

public class Timer {
	int tiempo;
	int contador;
	
	public Timer(int tiempo) {
		this.tiempo = tiempo;
		this.contador = 0;
	}
	
	public int getContador() {
		return this.contador;
	}
	
	public int get_tiempo() {
		return this.tiempo;
	}
	
	public void set_tiempo(int tiempo) {
		this.tiempo = tiempo;
	}
	
	public void empezar() {
		if (this.contador <= 0) {
			this.contador = this.tiempo;
		}
	}
	
	public void actualizar() {
		if (this.contador > 0) {
			this.contador--;
		}
	}
}
