package it.polito.tdp.metrodeparis.model;

import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import it.polito.tdp.metrodeparis.MetroDeParisController;
import it.polito.tdp.metrodeparis.DAO.MetroDeParisDAO;

public class Model {
	
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

}
