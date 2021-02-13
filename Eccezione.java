package museo;

// eccezione creata da noi
public class Eccezione extends Exception {
	int numero;
	
	public Eccezione (int n) {
		this.numero=n;
	}

}
