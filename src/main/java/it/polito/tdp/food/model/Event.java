package it.polito.tdp.food.model;

public class Event implements Comparable<Event> {

	public enum EventType {
		INIZIO_PREPARAZIONE, //viene assegnato un cibo ad una stazione
		FINE_PREPARAZIONE //la stazione ha completato la prep. di un cibo
		
	}
	private Double time ; //tempo in minuti
	private EventType type;
	private Stazione stazione;
	private Food food;
	
	
	
	/**
	 * @param time
	 * @param stazione
	 * @param food
	 */
	public Event(Double time, EventType type, Stazione stazione, Food food) {
		super();
		this.time = time;
		this.type=type;
		this.stazione = stazione;
		this.food = food;
	}
	public Double getTime() {
		return time;
	}
	
	public Stazione getStazione() {
		return stazione;
	}
	
	public Food getFood() {
		return food;
	}
	@Override
	public int compareTo(Event o) {
		
		return this.time.compareTo(o.time);
	}
	public EventType getType() {
		return type;
	}
	
	
	
	/*
	 *0[F1]  1[F2]  2[ F3]
	 *FINE_PREPARAZIONE (F2 , stazione 1)
	 *INIZIO_PREPARAZIONE(F2 cibo concluso , stazione 1)
	 *   scelgo il prox cibo 
	 *    calcolo la durata  
	 *     schedulo evento FINE (cibo nuovo appena scelto) 
	 * FINE_PREPARAZIONE(F4, stazione 1) T+DELTA DELTA(peso di F1---F4)
	 *     rischedulare l'evento di INIZIO allo stesso istante, ricordando il cibo terminato
	 * 
	 * */
}
