package it.polito.tdp.metrodeparis.model;

import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.metrodeparis.db.MetroDAO;

public class Model {

	private static boolean debug = true;

	private List<Linea> linee;
	private List<Fermata> fermate;
	private List<Connessione> connessioni;
	private List<FermataSuLinea> fermateSuLinea;

	private List<DefaultWeightedEdge> pathEdgeList = null;
	private double pathTempoTotale = 0;

	private MetroDAO metroDAO;

	// Directed Weighted Graph
	private DefaultDirectedWeightedGraph<FermataSuLinea, DefaultWeightedEdge> grafo = null;

	public Model() {
		if (debug)
			System.out.println("Costruisco il grafo.");

		metroDAO = new MetroDAO();
	}

	public List<Fermata> getStazioni() {

		if (fermate == null)
			throw new RuntimeException("Lista delle fermate non disponibile!");

		return fermate;
	}

	public void creaGrafo() {

		fermate = metroDAO.getAllFermate();
		linee = metroDAO.getAllLinee();
		connessioni = metroDAO.getAllConnessioni(fermate, linee);
		fermateSuLinea = metroDAO.getAllFermateSuLinea(fermate, linee);

		// Directed Weighted
		grafo = new DefaultDirectedWeightedGraph<FermataSuLinea, DefaultWeightedEdge>(DefaultWeightedEdge.class);

		// FASE1: Aggiungo un vertice per ogni fermata
		Graphs.addAllVertices(grafo, fermateSuLinea);

		// FASE2: Aggiungo tutte le connessioni tra tutte le fermate
		for (Connessione c : connessioni) {

			double velocita = c.getLinea().getVelocita();
			double distanza = LatLngTool.distance(c.getStazP().getCoords(), c.getStazA().getCoords(),LengthUnit.KILOMETER);
			double tempo = (distanza / velocita) * 60 * 60;

			// Cerco la fermataSuLinea corretta all'interno della lista delle fermate
			List<FermataSuLinea> fermateSuLineaPerFermata = c.getStazP().getFermateSuLinea();
			FermataSuLinea fslPartenza = fermateSuLineaPerFermata.get(fermateSuLineaPerFermata.indexOf(new FermataSuLinea(c.getStazP(), c.getLinea())));

			// Cerco la fermataSuLinea corretta all'interno della lista delle fermate
			fermateSuLineaPerFermata = c.getStazA().getFermateSuLinea();
			FermataSuLinea fslArrivo = fermateSuLineaPerFermata.get(fermateSuLineaPerFermata.indexOf(new FermataSuLinea(c.getStazA(), c.getLinea())));

			if (fslPartenza != null && fslArrivo != null) {
				// Aggiungoun un arco pesato tra le due fermate
				Graphs.addEdge(grafo, fslPartenza, fslArrivo, tempo);
			} else {
				System.err.println("Non ho trovato fslPartenza o fslArrivo. Salto alle prossime");
			}
		}

		// FASE3: Aggiungo tutte le connessioni tra tutte le fermateSuLinee
		// della stessa Fermata
		for (Fermata fermata : fermate) {
			for (FermataSuLinea fslP : fermata.getFermateSuLinea()) {
				for (FermataSuLinea fslA : fermata.getFermateSuLinea()) {
					if (!fslP.equals(fslA)) {
						Graphs.addEdge(grafo, fslP, fslA, fslA.getLinea().getIntervallo() * 60);
					}
				}
			}
		}

		if (debug)
			System.out.println("Grafo creato: " + grafo.vertexSet().size() + " nodi, " + grafo.edgeSet().size() + " archi");
	}

	public void calcolaPercorso(Fermata partenza, Fermata arrivo) {

		DijkstraShortestPath<FermataSuLinea, DefaultWeightedEdge> dijkstra;

		// Usati per salvare i valori temporanei
		double pathTempoTotaleTemp;

		// Usati per salvare i valori migliori
		List<DefaultWeightedEdge> bestPathEdgeList = null;
		double bestPathTempoTotale = Double.MAX_VALUE;

		for (FermataSuLinea fslP : partenza.getFermateSuLinea()) {
			for (FermataSuLinea fslA : arrivo.getFermateSuLinea()) {
				dijkstra = new DijkstraShortestPath<FermataSuLinea, DefaultWeightedEdge>(grafo, fslP, fslA);

				pathTempoTotaleTemp = dijkstra.getPathLength();

				if (pathTempoTotaleTemp < bestPathTempoTotale) {
					bestPathTempoTotale = pathTempoTotaleTemp;
					bestPathEdgeList = dijkstra.getPathEdgeList();
				}
			}
		}

		pathEdgeList = bestPathEdgeList;
		pathTempoTotale = bestPathTempoTotale;

		if (pathEdgeList == null)
			throw new RuntimeException("Non è stato creato un percorso.");

		// Nel calcolo del tempo non tengo conto della prima e dell'ultima
		if (pathEdgeList.size() - 1 > 0) {
			pathTempoTotale += (pathEdgeList.size() - 1) * 30;
		}
	}

	public String getPercorsoEdgeList() {

		if (pathEdgeList == null)
			throw new RuntimeException("Non è stato creato un percorso.");

		StringBuilder risultato = new StringBuilder();
		risultato.append("Percorso:\n\n");

		Linea lineaTemp = grafo.getEdgeTarget(pathEdgeList.get(0)).getLinea();
		risultato.append("Prendo Linea: " + lineaTemp.getNome() + "\n[");

		for (DefaultWeightedEdge edge : pathEdgeList) {
			
			risultato.append(grafo.getEdgeTarget(edge).getNome());

			if (!grafo.getEdgeTarget(edge).getLinea().equals(lineaTemp)) {
				risultato.append("]\n\nCambio su Linea: " + grafo.getEdgeTarget(edge).getLinea().getNome() + "\n[");
				lineaTemp = grafo.getEdgeTarget(edge).getLinea();
				
			} else {
				risultato.append(", ");
			}
		}
		risultato.setLength(risultato.length() - 2);
		risultato.append("]");

		return risultato.toString();
	}

	public double getPercorsoTempoTotale() {

		if (pathEdgeList == null)
			throw new RuntimeException("Non è stato creato un percorso.");

		return pathTempoTotale;
	}
}
