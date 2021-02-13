package museo;
import java.util.Scanner;

public class PrenotazioneGruppo extends Prenotazione {
	private String[] listaNomi;
	
	//costruttore
	public PrenotazioneGruppo(String nome,int data,int nV,boolean guida,boolean sconto,int id) {
		super(nome,data,nV,guida,sconto,id); //chiama il costruttore della superclasse
		listaNomi=new String [nV]; //crea un array di lunghezza di numero di visitatori
	}
	
	public PrenotazioneGruppo(int id) { //costruttore per fare la ricerca secondo ID
		super(id);
		listaNomi=new String [0];
	}
	
	//inserimento dei nomi nell'array
	public void addNome() {
		System.out.println("Ora ti viene richiesto di inserire uno alla volta i nomi dei componenti del gruppo");
		String nome="";
		Scanner input=new Scanner(System.in);
		boolean ok; //il nome dato e' ok
		boolean trovato; //trovato un carattere da evitare
		int controllo;
		String [] caratteriEvitare= {"1","2","3","4","5","6","7","8","9","0",",","!","?",":","(",")"}; //array di caratteri non validi come nome
	
		for (int i=0;i<listaNomi.length;i++) {
			do {
				ok=true;
				trovato=false;
				System.out.println("Digita il nome del componente della comitiva");
				nome=(input.nextLine()).toUpperCase(); //nome scritto dall'utente trasformato in maiuscolo
				if(nome.equals("")) { //controlla se scrive qualcosa
					System.out.println("Input non valido, riprova.");
					ok=false;
				}
				else {
					for (int j=0;j<caratteriEvitare.length-1 && !trovato;j++) { //esce dal ciclo quando ha trovato un carattere da evitare
						controllo=nome.indexOf(caratteriEvitare[j]); //controlla uno alla volta se i caratteri dell'array sono nella stringa
						if(controllo!=-1) {
							trovato=true; //trovato un carattere non adeguato
							ok=false;
							nome=""; //azzera l'input
							System.out.println("Input non valido, riprova.");
						}
					}
				}
			}while(!ok);
			listaNomi[i]=nome;
			nome=""; // azzera l'input
			
		}
	}
	
	//crea una stringa con i nomi della comitiva
	public String getListaNomi() {
		String lista="La comitiva e' composta da: \n";
		for (String x:listaNomi) {
			lista+=x.toUpperCase()+". ";
		}
		return lista+"\n";
	}
	
	public String toStringComitive() {
		String testo= "Prenotazione ID:"+getId()+"\nNome: "+getNome()+"\nData: "+getStringData()+"\nNumero visitatori: "+getnV();
		if (getGuida()) testo+=" con guida\n";
		else testo+= " senza guida\n";
		return testo + getListaNomi();
	}
	
	public boolean equals (Object o) {
		boolean trovato=false;
		if(o instanceof PrenotazioneGruppo) {
			trovato=((this.getNome()).equals(((PrenotazioneGruppo)o).getNome())) || ((this.getData())==((PrenotazioneGruppo)o).getData()) || ((this.getId())==((PrenotazioneGruppo)o).getId());
		}
		else if (o instanceof Prenotazione) { 
			trovato=((this.getNome()).equals(((Prenotazione)o).getNome())) || ((this.getData())==((Prenotazione)o).getData())|| ((this.getId())==((Prenotazione)o).getId());
		}
		return trovato;
	}
}
