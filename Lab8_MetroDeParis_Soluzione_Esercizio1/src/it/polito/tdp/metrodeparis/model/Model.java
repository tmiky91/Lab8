package it.polito.tdp.metrodeparis.model;

import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.metrodeparis.db.MetroDAO;

public class Model {

	private static boolean debug = true;

	private List<Linea> linee;
	private List<Fermata> fermate;
	private List<Connessione> connessioni;

	private List<DefaultWeightedEdge> pathEdgeList = null;
	private double pathTempoTotale = 0;

	private MetroDAO metroDAO;

	// Undirected Weighted Graph
	private SimpleWeightedGraph<Fermata, DefaultWeightedEdge> grafo = null;

	public Model() {
		if (debug)
			System.out.println("Building the graph.");

		metroDAO = new MetroDAO();
	}

	public List<Fermata> getStazioni() {

		if (fermate == null)
			throw new RuntimeException("Lista delle stazioni non disponibile!");

		return fermate;
	}

	public void creaGrafo() {

		fermate = metroDAO.getAllFermate();
		linee = metroDAO.getAllLinee();
		connessioni = metroDAO.getAllConnessione(fermate, linee);

		// Undirected Weighted
		grafo = new SimpleWeightedGraph<Fermata, DefaultWeightedEdge>(DefaultWeightedEdge.class);

		// Aggiungo un vertice per ogni fermata
		Graphs.addAllVertices(grafo, fermate);

		for (Connessione c : connessioni) {

			// IMPORTANTE:
			// Usando un grafo seplice non prendo in considerazione il caso
			// in cui due fermate siano collegate da più linee.

			double velocita = c.getLinea().getVelocita();
			double distanza = LatLngTool.distance(c.getStazP().getCoords(), c.getStazA().getCoords(), LengthUnit.KILOMETER);
			double tempo = (distanza / velocita) * 60 * 60;

			// Aggiungoun un arco pesato tra le due fermate
			Graphs.addEdge(grafo, c.getStazP(), c.getStazA(), tempo);
		}

		if (debug)
			System.out.println("Grafo creato: " + grafo.vertexSet().size() + " nodi, " + grafo.edgeSet().size() + " archi");
	}

	public void calcolaPercorso(Fermata partenza, Fermata arrivo) {

		DijkstraShortestPath<Fermata, DefaultWeightedEdge> dijkstra = new DijkstraShortestPath<Fermata, DefaultWeightedEdge>(grafo, partenza, arrivo);

		pathEdgeList = dijkstra.getPathEdgeList();
		pathTempoTotale = dijkstra.getPathLength();

		// Nel calcolo del tempo non tengo conto della prima e dell'ultima
		if (pathEdgeList.size() - 1 > 0) {
			pathTempoTotale += (pathEdgeList.size() - 1) * 30;
		}
	}

	public String getPercorsoEdgeList() {

		if (pathEdgeList == null)
			throw new RuntimeException("Non è stato creato un percorso.");

		StringBuilder risultato = new StringBuilder();
		risultato.append("Percorso: [ ");

		for (DefaultWeightedEdge edge : pathEdgeList) {
			risultato.append(grafo.getEdgeTarget(edge).getNome());
			risultato.append(", ");
		}
		risultato.setLength(risultato.length()-2);
		risultato.append("]");

		return risultato.toString();
	}

	public double getPercorsoTempoTotale() {

		if (pathEdgeList == null)
			throw new RuntimeException("Non è stato creato un percorso.");

		return pathTempoTotale;
	}
}
