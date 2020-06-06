package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.food.model.Event.EventType;
import it.polito.tdp.food.model.Food.StatoPreparazione;

public class Simulator {
   //MODELLO DEL MONDO
	private List<Stazione> stazioni ;
	private List<Food> cibi;
	
	//Grafo 
	private Graph <Food,DefaultWeightedEdge> grafo;
	private Model model;
	
	
	//PARAMETRI DI SIMULAZIONE
	private Integer K =5; //numero stazioni disponibili
	
	
	
	
	//RISULTATI CALCOLATI
	private Double tempoPreparazione;
	private int cibiPreparati;
	
	
	
	//CODA DEGLI EVENTI
	private PriorityQueue<Event> queue;
	
	//costruttore 
	public Simulator(Graph <Food,DefaultWeightedEdge> grafo, Model model) {
		this.grafo=grafo;
		this.model=model;
	}
	
    public void init(Food partenza) {
    	
    	
    	this.cibi = new ArrayList<>(this.grafo.vertexSet());
    	
    	for(Food cibo : this.cibi) {
    		cibo.setPreparazione(StatoPreparazione.DA_PREPARARE);
    	}
    	this.stazioni = new ArrayList<>();
    	this.cibiPreparati=0;
    	
    	for(int i = 0; i<K; i++) {
    		this.stazioni.add(new Stazione(true, null));
    	}
    	this.tempoPreparazione=0.0;
    	
    	this.queue = new PriorityQueue<>();
    	List<FoodAndCongiunte> vicini = this.model.getCongiunti(partenza);
    	
    	for(int i = 0; i<K && i<vicini.size();i++) {
    	   this.stazioni.get(i).setLibera(false);
    	   this.stazioni.get(i).setFood(vicini.get(i).getF());
    	   
    	   Event e = new Event(vicini.get(i).getCalorieCongiunte(),
    			   EventType.FINE_PREPARAZIONE,
    			   this.stazioni.get(i),
    			   vicini.get(i).getF());
    	   queue.add(e);
    	   
    	}
    	
    }
    
    public void run() {
    	while(!queue.isEmpty()) {
    		Event e = queue.poll();
    		processEvent(e);
    		
    	}
    }
    private void processEvent(Event e) {
    	switch(e.getType()) {
    	case INIZIO_PREPARAZIONE:
    		List<FoodAndCongiunte> vicini = this.model.getCongiunti(e.getFood());
    		FoodAndCongiunte prossimo = null;
    		for(FoodAndCongiunte vicino : vicini) {
    			if(vicino.getF().getPreparazione() == (StatoPreparazione.DA_PREPARARE)) {
    				prossimo = vicino;
    				break;
    			}
    		}
    		if(prossimo != null) {
    			prossimo.getF().setPreparazione(StatoPreparazione.IN_CORSO);
    			e.getStazione().setLibera(false);
    			e.getStazione().setFood(prossimo.getF());
    			
    			Event e2 = new Event(e.getTime()+prossimo.getCalorieCongiunte(),
    					EventType.FINE_PREPARAZIONE,
    					e.getStazione(),
    					prossimo.getF());
    			this.queue.add(e2);
    		}
    		break;
    	case FINE_PREPARAZIONE:
    		this.cibiPreparati++;
    		this.tempoPreparazione= e.getTime();
    		
    		e.getStazione().setLibera(true);
    	    e.getFood().setPreparazione(StatoPreparazione.PREPARATO);
    		
    		Event e2 = new Event(e.getTime(), EventType.INIZIO_PREPARAZIONE, e.getStazione(), e.getFood());
    		this.queue.add(e2);
    		break;
    	}
    }
	public Double getTempoPreparazione() {
		return tempoPreparazione;
	}
    public int getCibiPreparati () {
    	return cibiPreparati;
    }
	public Integer getK() {
		return K;
	}

	public void setK(Integer k) {
		K = k;
	}
	
}
