package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {

	private FoodDao dao;
	private Graph<Food,DefaultWeightedEdge> grafo;
	private Map<Integer,Food> idMap;
	
	public Model() {
		dao = new FoodDao();
		idMap = new HashMap<>();
	}
	
	public void creaGrafo(Integer porzioni) {
	grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
	
	
	
	Graphs.addAllVertices(this.grafo, dao.getVertici(porzioni, idMap));
	
	List <Adiacenza> adiacenze =	dao.getAdiacenze(idMap);
	for(Adiacenza a: adiacenze) {
		
	if(this.grafo.containsVertex(a.getF1()) && this.grafo.containsVertex(a.getF2()) && a.getPeso()!= null) {	
		
	     Graphs.addEdge(this.grafo, a.getF1(), a.getF2(), a.getPeso());
	     
    	}
	
	}
	System.out.println(String.format("Grafo creato con %d vertici e %d archi", this.grafo.vertexSet().size(), this.grafo.edgeSet().size()));	
	}
	
	public List<Food> getFoods(Integer portions){
		return dao.getVertici(portions, idMap);
	}
	
	public int nVertici() {
		return	this.grafo.vertexSet().size();
		}
		public int nArchi() {
			return this.grafo.edgeSet().size();
		}
	//Punto 1D
	public	List <FoodAndCongiunte> getCongiunti(Food sorgente){
			
			
			List <FoodAndCongiunte> congiunti = new ArrayList<>();
			
			for(Food adiacente : Graphs.neighborListOf(this.grafo, sorgente)) {
		     
			  DefaultWeightedEdge e = this.grafo.getEdge(sorgente, adiacente);
		      Double calorie =  this.grafo.getEdgeWeight(e);
		      
		      congiunti.add(new FoodAndCongiunte (adiacente, calorie ));
		      
		     }
			
			Collections.sort(congiunti);
			return congiunti;		
		}
	//Punto 2 Simulazione
	public String simula(Food cibo, int k) {
		Simulator sim = new Simulator(this.grafo,this);
		sim.setK(k);
		sim.init(cibo);
        sim.run();
      String messaggio = String.format("preparati %d cibi in %f minuti \n", sim.getCibiPreparati(), sim.getTempoPreparazione() );
	 return messaggio;
	}
		
		}
