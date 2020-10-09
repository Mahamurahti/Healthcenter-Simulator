package simu;
import java.util.PriorityQueue;
/**
 * 
 * Tapahtumalista-luokan tarkoitus on pitää listaa tapahtumista. Tapahtumalista-luokan lista
 * on priorityQueue, joka pitää pitää tapahtumat niiden tapahtumaikojen mukaisessa järjestyksessä
 * 
 * @author Eric Keränen
 * @version 1.0
 *
 */
public class Tapahtumalista {
	private PriorityQueue<Tapahtuma> lista = new PriorityQueue<Tapahtuma>();
	
	public Tapahtumalista(){	}
	
	/**
	 * Poistaa tapahtumalistasta ensimmäisen tapahtuman
	 * @return poistettu tapahtuma
	 */
	public Tapahtuma poista(){
		Trace.out(Trace.Level.INFO,"Poisto " + lista.peek());
		return lista.remove();
	}
	
	/**
	 * Lisää tapahtumalistaan tapahtuman
	 * @param tapahtuma, joka lisätään listaan
	 */
	public void lisaa(Tapahtuma tapahtuma){
		lista.add(tapahtuma);
	}
	
	/**
	 * Hakee listassa ensimmäisenä olevan asiakkaan ajan
	 * @return ensimmäisen tapahtuman aika
	 */
	public double getSeuraavanAika(){
		return lista.peek().getAika();
	}
}
