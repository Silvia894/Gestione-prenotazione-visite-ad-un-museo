package museo;
import java.util.Scanner;

public class TestMuseo {

	public static void main(String[] args) throws Eccezione {
		Scanner input = new Scanner(System.in);
		System.out.println("Inserisci il nome del file da aprire o dove vuoi salvare:");
		String nomefile=input.nextLine(); // variabile in cui e' salvato il nome del file
		Visite v=new Visite(nomefile);
		
		//menu
		String r="";
		char c=' ';		
		do {
			System.out.println("Premi\n"
					+ "[N]uova prenotazione\n"
					+ "[E]limina una prenotazione\n"
					+ "[V]isualizza le prenotazioni\n"
					+ "Visualizza le prenotazioni di [C]omitiva\n"
					+ "[R]icerca una prenotazione inserendo nome o data\n"
					+ "[S]alva le prenotazioni su file\n"
					+ "[T]ermina");
		    r=input.nextLine().toUpperCase(); //prendiamo il valore dall'utente e lo mettiamo maiuscolo
		    if(r.length()!=0) //gestisce la stringa vuota (a capo)
		    	c=r.charAt(0); //prendo il primo carattere
		    else
		    	c=' '; //azzera il valore precedentemente preso
		    switch(c) {
			    case 'N':v.addPrenotazione();break;
			    case 'E':v.elimina();break;
			    case 'V':v.visualizza();break;
			    case 'C':v.visualizzaComitive();break;
			    case 'R':v.ricerca();break;
			    case 'S':
			    	boolean ok=v.salva();
			    	if(ok)
			    		System.out.println("Salvataggio completato.");
			    	else
			    		System.out.println("Problema durante il salvataggio.");
			    	break;
			    case 'T':
			    	if(v.daSalvare()) {
			    		do {
							ok=true;
							System.out.println("Vuoi salvare le modifiche? (S/N)");
							String s = input.nextLine().toUpperCase();
							if(s.length()!=0) //gestisce la stringa vuota (a capo)
								c=s.charAt(0);
							else
								c=' ';
							switch(c) {
								case 'S':
									ok=v.salva();
									if(ok)
										System.out.println("Salvataggio completato.");
									else
										System.out.println("Problema durante il salvataggio.");
									break;
								case 'N':break;
								default:System.out.println("Input non valido, riprova.");ok=false;break; // il booleano per far ripetere il ciclo
							}
						}while(!ok);
			    		c='T'; // ritorna al comando dell'utente
			    	}
			    	break; //per evitare messaggio di default
			    default:System.out.println("Input non valido, riprova.\n");break;
		    }
		}while(c!='T');
		System.out.println("Operazione terminata."); //avviso di termine programma
	}

}
