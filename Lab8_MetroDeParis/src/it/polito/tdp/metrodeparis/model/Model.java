package it.polito.tdp.metrodeparis.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.metrodeparis.MetroDeParisController;
import it.polito.tdp.metrodeparis.DAO.MetroDeParisDAO;

public class Model {
	
	public class EdgeTraversedGraphListener implements TraversalListener<Fermata, DefaultEdge>{
		
	}
	
	private Graph<Fermata, DefaultEdge> grafo;
	private List<Fermata> fermate;
	
	public void creaGrafo() {
		//crea l'oggetto grafo
		this.grafo = new SimpleDirectedGraph<>(DefaultEdge.class);
		
		//Aggiungi i vertici
		MetroDeParisDAO dao = new MetroDeParisDAO();
		this.fermate = dao.getAllFermate();
		Graphs.addAllVertices(this.grafo, this.fermate);
		
		//Aggiungi gli archi (versione 1)
		for(Fermata partenza: this.grafo.vertexSet()) {
			for(Fermata arrivo: this.grafo.vertexSet()) {
				if(dao.esisteConnessione(partenza, arrivo)) {
					this.grafo.addEdge(partenza, arrivo);
				}
			}
		}
		//Aggiungi gli archi (versione 2)
		for(Fermata partenza : this.grafo.vertexSet()) {
			List<Fermata> arrivi = dao.stazioniArrivo(partenza);
			for(Fermata arrivo: arrivi) {
				this.grafo.addEdge(partenza, arrivo);
			}
		}
	}

	public Graph<Fermata, DefaultEdge> getGrafo() {
		return grafo;
	}
	
	public List<Fermata> fermateRaggiungibili(Fermata source) {
		
		List<Fermata> result = new ArrayList<Fermata>() ;
		Map<Fermata, Fermata> back = new HashMap<>() ;
		
		GraphIterator<Fermata, DefaultEdge> it = new BreadthFirstIterator<>(this.grafo, source) ;
//		GraphIterator<Fermata, DefaultEdge> it = new DepthFirstIterator<>(this.grafo, source) ;
		
		while(it.hasNext()) {
			result.add(it.next()) ;
		}
				
		return result ;
		
	}
	
	public List<Fermata> getFermate() {
		return fermate;
	}
	
	public List<Fermata> percorsoFinoA(Fermata target){
		if(!backVisit.containsKey(target)) {
			return null;
		}
		List<Fermata> percorso = new LinkedList<>();
		Fermata f = target;
		while(f!=null) {
			
		}
		percorso.add(f);
		
	}

}
