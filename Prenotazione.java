package museo;
import java.util.*;
import java.io.Serializable;

public class Prenotazione implements Comparable<Object>, Serializable{
	private int id; //numero identificativo
	private String nome; //nome associato alla prenotazione
	private Integer data; //formato AAAAMMGG non int e non Integer per il toString
	private int nVisitatori; //numero dei visitatori previsti
	private boolean guida; //facoltativo, se true aumento di prezzo
	private double prezzo; //prezzo totale
	private boolean sconto; //facoltativo, se true applico sconto ed e' anche una comitiva
	
	static final long serialVersionUID=1; //richiesta da Serializable

	//costruttore
	public Prenotazione(String nome,int data,int nV,boolean guida,boolean sconto,int id){
		this.id=id;
		this.nome=nome;
		this.data=data;
		nVisitatori=nV;
		this.guida=guida;
		this.sconto=sconto;
		calcolaPrezzo();		
	}
	
	public Prenotazione (int id) { // costruttore per fare la ricerca secondo ID
		this.id=id;
		nome="";
		data=00000000;
		nVisitatori=0;
		guida=false;
		sconto=false;
		prezzo=0.0;
	}
	
	
	//set prezzo
	private void calcolaPrezzo() { 
		if (sconto) {
			prezzo=3.00*nVisitatori;
			if (guida) prezzo+=40.00;
		}
		else {
			prezzo=5.00*nVisitatori;
			if (guida) prezzo+=50.00;
		}		
	}
	
	//restituisce il prezzo come stringa
	public String getPrezzo() {
		return "Il prezzo totale e' "+prezzo+" euro.";
	}
	
	public int getData() {
		return data;
	}
	
	public String getStringData() {
		String dataS=Integer.toString(data); //cast da Integer a String
		String t="Giorno "+dataS.substring(6)+" Mese "+dataS.substring(4, 6)+" Anno "+dataS.substring(0,4);
		return t;
	}
	
	public String getNome() {
		return nome;
	}
	
	public int getId(){
		return id;
	}
	
	public int getnV() {
		return nVisitatori;
	}
	
	public boolean getGuida(){
		return guida;
	}
	
	public boolean getSconto() {
		return sconto;
	}
	
	//per scrivere a video
	public String toString() {
		String testo= "Prenotazione ID:"+id+"\nNome: "+nome+"\nData: "+getStringData()+"\nNumero visitatori: "+nVisitatori;
		if (guida) testo+=" con guida\n";
		else testo+= " senza guida\n";
		return testo;
	}
	
	// overriding del metodo compareTo in base alla data
	public int compareTo(Object p) {
		Prenotazione o = (Prenotazione)p;
		if(this.data<o.data)
			return -1; 
		else if(this.data>o.data)
			return 1;
		else 
			return 0;
	}

	public String toStringComitive() { //per evitare errori ma funziona come toString
		String testo= "Prenotazione ID:"+getId()+"\nNome: "+getNome()+"\nData: "+getStringData()+"\nNumero visitatori: "+getnV();
		if (getGuida()) testo+=" con guida\n";
		else testo+= " senza guida\n";
		return testo;
	}

}
