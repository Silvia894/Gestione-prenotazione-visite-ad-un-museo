package museo;
import java.util.*;
import java.io.*;

public class Visite {
	private Scanner input=new Scanner (System.in);
	private int id=1;
	private Vector <Prenotazione> visite=new Vector <Prenotazione>();
	private String nomefile; //nome del file
	private boolean modificato=false; //tenere traccia delle modifiche non salvate su file
	
	//costruttore
	public Visite(String nomefile) {
		this.nomefile=nomefile;
		
		try {
			ObjectInputStream file_input = new ObjectInputStream(new BufferedInputStream(new FileInputStream(nomefile)));
			// legge l'intero vettore da file
			visite = (Vector<Prenotazione>) file_input.readObject();
			System.out.println("File trovato e aperto.");
			id=(visite.get(visite.size()-1).getId())+1; //prende ID da ultimo oggetto del vettore e lo incrementa
			file_input.close();
		} catch (FileNotFoundException e) {
			// gestisce il caso in cui il file non sia presente 
			System.out.println("ATTENZIONE: Il file " + nomefile + " non esiste");
			System.out.println("Sara' creato al primo salvataggio");
			System.out.println();
		} catch (ClassNotFoundException e) {
			// gestisce il caso in cui il file non contenga un oggetto
			System.out.println("ERRORE di lettura");
			System.out.println(e);
		} catch (IOException e) {
			// gestisce altri errori di input/output
			System.out.println("ERRORE di I/O");
			System.out.println(e);
		}
		
		
	}
	
	//crea una nuova prenotazione e la aggiunge al vettore
	public void addPrenotazione() {
		boolean sconto=false;
		boolean guida=true;
		String nome="";
		String [] caratteriEvitare= {"1","2","3","4","5","6","7","8","9","0",",","!","?",":","(",")"}; //array di caratteri non validi come nome
		int controllo;
		boolean trovato=false;
		boolean ok;
		
		//chiediamo il nome all'utente
		do {
			ok=true;
			trovato=false;	
			System.out.println("Digita il nome");
			nome=(input.nextLine()).toUpperCase(); //nome scritto dall'utente
			if(nome.equals("")) { //controlla se scrive qualcosa
				System.out.println("Input non valido, riprova.");
				ok=false;
			}
			else {
				for (int i=0;i<caratteriEvitare.length && !trovato;i++) { //esce dal ciclo quando trova un carattere sbagliato
					controllo=nome.indexOf(caratteriEvitare[i]); //controlla uno alla volta se i caratteri dell'array sono nella stringa
					if(controllo!=-1) {
						trovato=true; //trovato un carattere non adeguato
						ok=false;
						nome=""; //azzera l'input
						System.out.println("Input non valido, riprova.");
					}
				}
			}
		}while(!ok); // se ok e' false, richiede il nome
		
		//chiediamo la data
		int[] giorniMese = {31,28,31,30,31,30,31,31,30,31,30,31};
		int a=0;
		int m=0;
		int gi=0;
		char c= ' ';
		do {
			ok=true;
			try {
				System.out.println("Digita l'anno in formato numerico");
				a =input.nextInt();
				if(a<2018)
					throw new Eccezione(a);
				
				System.out.println("Digita il mese in formato numerico");
				m = input.nextInt();
				if(m<1 || m>13)
					throw new Eccezione(m);
				
				System.out.println("Digita il giorno in formato numerico");
				gi=input.nextInt();
				if (gi<0 || gi>giorniMese[m-1])
					throw new Eccezione(gi);
			}
			catch(InputMismatchException | Eccezione e) {
				input.nextLine();
				System.out.println("Input non valido, riprova.");
				ok=false;
			}
		}while(!ok);
		//moltiplichiamo per mantenere la posizione
		a*=10000; 
		m*=100;
		int data=a+m+gi; //trasforma in formato int AAAAMMGG
		
		//chiediamo il numero dei visitatori
		int nV=0;
		do {
			ok=true;
			try {
				System.out.println("Digita il numero di visitatori");
				nV = input.nextInt();
				if (nV<1)
					throw new Eccezione(nV);
			}
			catch(InputMismatchException | Eccezione e) {
				input.nextLine();
				System.out.println("Input non valido, riprova.");
				ok=false;
			}	
		}while(!ok);
		
		String d = input.nextLine(); //prende l'invio in modo da evitare errori nell'input della stringa successiva
		if(nV>10) { //viene eseguito solo se il numero di visitatori e' maggiore di 10
			do {
				ok=true;
				System.out.println("Vuoi applicare lo sconto comitiva? (S/N)");
				String s = input.nextLine().toUpperCase();
				if(s.length()!=0) //gestisce la stringa vuota (a capo)
					c=s.charAt(0);
				else
					c=' ';
				switch(c) {
					case 'S':sconto=true;break;
					case 'N':sconto=false;break;
					default:System.out.println("Input non valido, riprova.\n");ok=false;break; // il booleano ok per far ripetere il ciclo
				}
				}while(!ok);
		}
		
		//chiediamo se l'utente voglia la guida
		do {
			ok=true;
			System.out.println("Vuoi la guida? (S/N)");
			String s = input.nextLine().toUpperCase();
			if(s.length()!=0) //gestisce la stringa vuota (a capo)
				c=s.charAt(0);
			else
				c=' ';
			switch(c) {
			case 'S':guida=true;break;
			case 'N':guida=false;break;
			default:System.out.println("Input non valido, riprova.\n");ok=false;break; // il booleano ok per far ripetere il ciclo
			}
			}while(!ok);
		
		//crea l'oggetto prenotazione e visualizza il prezzo totale
		if(sconto) {
			PrenotazioneGruppo pg = new PrenotazioneGruppo(nome,data,nV,guida,sconto,id);
			pg.addNome(); // richiama la funzione per inserire la lista dei nomi della comitiva
			visite.add(pg);
			id++;
			System.out.println(pg.getPrezzo()+" Il tuo ID e' "+ pg.getId());
		}
		else {
			Prenotazione p = new Prenotazione(nome,data,nV,guida,sconto,id);
			visite.add(p);
			id++;
			System.out.println(p.getPrezzo()+ " Il tuo ID e' "+p.getId());
		}		
		modificato=true; // per il salvataggio su file
	}
	
	//visualizza tutte le prenotazioni
	public void visualizza() {
		if(visite.isEmpty()) {
			System.out.println("Non esiste nessuna prenotazione.");
		}
		else {
			Vector<Prenotazione>visiteCopia = new Vector(visite.size());//copia il vettore per mantenere l'ordine progressivo degli ID (per non sovrascriverlo)
			visiteCopia.setSize(visite.size());
			Collections.copy(visiteCopia,visite);
			Collections.sort(visiteCopia); //ordina il vettore in base alla data
			for (Prenotazione x: visiteCopia) { 
				System.out.println(x+"\n"); //stampa l'elemento
			}
		}
	}
	
	//visualizza solo le prenotazioni delle comitive
	public void visualizzaComitive() { 
		boolean trovato=false; //se non esistono prenotazioni di gruppo
		if(!visite.isEmpty()) {
			Vector<Prenotazione>visiteCopia = new Vector(visite.size());//copia il vettore per mantenere l'ordine progressivo degli ID (per non sovrascriverlo)
			visiteCopia.setSize(visite.size());
			Collections.copy(visiteCopia,visite);
			Collections.sort(visiteCopia);
			for (Prenotazione x:visiteCopia) {
				if (x.getSconto()) {
					trovato=true; //cambia quando trova una prenotazione di gruppo
					System.out.println(x+"\n");	
				}
			}
		}	
		if (!trovato) { //il vettore non e' vuoto ma non ci sono prenotazioni di gruppo OPPURE il vettore e' vuoto
			System.out.println("Non esiste nessuna prenotazione di gruppo.");
		}
	}
	
	//metodo per poter rimuovere una prenotazione
	public void elimina() {
		System.out.println("Digita l'ID della prenotazione che vuoi rimuovere");
		int id=0;
		boolean ok;
		do {
			ok=true;
			try {
			id=input.nextInt(); //l'ID e' preso dall'utente
			}
			catch(InputMismatchException e) {
				input.nextLine();
				System.out.println("Input non valido, riprova.");
				ok=false;
			}
		}while(!ok);
		
		int num=visite.indexOf(new PrenotazioneGruppo(id)); //restituisce l'indice nel vettore della prenotazione
		if(num!=-1) { //controlla ci sia una prenotazione
			visite.remove(num); //elimina l'oggetto nella posizione num
			System.out.println("Prenotazione rimossa");
			modificato=true;
		}
		
		else
			System.out.println("Nessuna prenotazione associata all'id, impossibile rimuovere");
	}
	
	//ricerca la prenotazione in base al nome o alla data
	public void ricerca() throws Eccezione {
		String s="";
		char c=' ';
		boolean ok=false;
		do {	
			System.out.println("Digita N se vuoi cercare per nome, D se vuoi cercare per data e A per annullare.");
			s=input.nextLine().toUpperCase();
			if(s.length()!=0) //gestisce la stringa vuota (a capo)
				c=s.charAt(0);
			else
				c=' '; //azzera il valore precedentemente preso
			switch(c) {
			case 'N':ricercaNome();ok=true;break;
			case 'D':ricercaData();ok=true;break;
			case 'A': break; //per evitare messaggio di default
			default:System.out.println("Input non valido, riprova.\n");break;
			}
		} while(c!='A' && !ok);
	}
	
	public void ricercaNome() {
		String n="";
		while(n.equals("")) {
			System.out.println("Inserisci il nome associato alla prenotazione");
			n=(input.nextLine()).toUpperCase(); //nome scritto dall'utente
		}
		int num=visite.indexOf(new PrenotazioneGruppo(n,0,0,false,false,0)); //restituisce l'indice nel vettore della prenotazione
		if(num!=-1) //controlla ci sia una prenotazione
			System.out.println(((visite.get(num))).toStringComitive());
		else
			System.out.println("Nessuna prenotazione associata al nome");
	}
	
	public void ricercaData() throws Eccezione {
		int[] giorniMese = {31,28,31,30,31,30,31,31,30,31,30,31};
		int a=0;
		int m=0;
		int gi=0;
		boolean ok;
		do {
			ok=true;
			try {
				System.out.println("Digita l'anno");
				a =input.nextInt();
				if(a<2018)
					throw new Eccezione(a);
				
				System.out.println("Digita il mese in formato numerico");
				m = input.nextInt();
				if(m<1 || m>13)
					throw new Eccezione(m);
				
				System.out.println("Digita il giorno");
				gi=input.nextInt();
				if (gi<0 || gi>giorniMese[m-1])
					throw new Eccezione(gi);
			}
			catch(InputMismatchException | Eccezione e) {
				input.nextLine();
				System.out.println("Input non valido, riprova.");
				ok=false;
			}
		}while(!ok);
		
		//moltiplichiamo per mantenere la posizione
		a*=10000;
		m*=100;
		int data=a+m+gi; // formato AAAAMMGG
		int num=visite.indexOf(new PrenotazioneGruppo("",data,0,false,false,0)); //restituisce l'indice nel vettore della prenotazione
		if(num!=-1) //controlla ci sia una prenotazione
			System.out.println(visite.get(num));
		else
			System.out.println("Nessuna prenotazione associata alla data");
	}
	
	// verifica se ci sono modifiche non salvate
	public boolean daSalvare() {
		return modificato;
	}
	
	// salva il registro nel file
	// restituisce true se il salvataggio e' andato a buon fine
	public boolean salva() {
		if (daSalvare()) { // salva solo se necessario (se ci sono modifiche)
			try {
				ObjectOutputStream file_output = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(nomefile)));
				// salva l'intero oggetto (vettore visite) nel file
				file_output.writeObject(visite);
				file_output.close();
				modificato = false; // le modifiche sono state salvate
				return true;
			} catch (IOException e) {
				System.out.println("ERRORE di I/O");
				System.out.println(e);
				return false;
			}		
		} else return true;
	}
}
