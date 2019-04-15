package it.polito.tdp.metrodeparis.model;

import java.util.List;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.metrodeparis.DAO.MetroDAO;

public class Model {
	
	private SimpleGraph<Fermata, DefaultEdge> grafo;

	public static List<Fermata> getTutteLeFermate() {
		MetroDAO dao = new MetroDAO();
		return dao.getTutteLeFermate();
	}

	public String calcolaPercorso(Fermata partenza, Fermata arrivo) {
		grafo=new SimpleGraph<>(DefaultEdge.class);
		String risultato="";
		MetroDAO dao = new MetroDAO();
		dao.popolaGrafo(grafo);
		for(Fermata f: grafo.vertexSet()) {
			risultato+= f.toString()+"\n";
		}
		return risultato;
	}

}
