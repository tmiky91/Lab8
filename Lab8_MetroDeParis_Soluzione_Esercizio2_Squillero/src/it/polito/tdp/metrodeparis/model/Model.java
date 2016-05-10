/*****************************************************************-*-java-*-*\
*               *  Classroom code for "Tecniche di Programmazione"           *
*    #####      *  (!) Andrea Marcelli <<andrea.marcelli@polito.it>          *
*   ######      *   &  Giovanni Squillero <giovanni.squillero@polito.it>     *
*   ###   \     *                                                            *
*    ##G  c\    *  Copying and distribution of this file, with or without    *
*    #     _\   *  modification, are permitted in any medium without royalty *
*    |   _/     *  provided this notice is preserved.                        *
*    |  _/      *  This file is offered as-is, without any warranty.         *
*               *  See: http://bit.ly/tecn-progr                             *
\****************************************************************************/

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

	// Due linee "virtuali" per l'inizio e la fine della corsa
	private Linea virtInizioCorsa = new Linea(-100, "*INIZIO*", 0, 0);
	private Linea virtFineCorsa = new Linea(-101, "*FINE*", 0, 0);

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

		// PATCH: Aggiungo le "meta fermate"
		for (Fermata fermata : fermate) {
			fermateSuLinea.add(new FermataSuLinea(fermata, virtInizioCorsa));
			fermateSuLinea.add(new FermataSuLinea(fermata, virtFineCorsa));
		}

		// Directed Weighted
		grafo = new DefaultDirectedWeightedGraph<FermataSuLinea, DefaultWeightedEdge>(DefaultWeightedEdge.class);

		// FASE1: Aggiungo un vertice per ogni fermata
		Graphs.addAllVertices(grafo, fermateSuLinea);

		// FASE2: Aggiungo tutte le connessioni tra tutte le fermate
		for (Connessione c : connessioni) {

			double velocita = c.getLinea().getVelocita();
			double distanza = LatLngTool.distance(c.getStazP().getCoords(), c.getStazA().getCoords(),LengthUnit.KILOMETER);
			double tempo = (distanza / velocita) * 60 * 60;

			// Cerco la fermataSuLinea corretta all'interno della lista delle
			// fermate
			FermataSuLinea fslPartenza = fermateSuLinea.get(fermateSuLinea.indexOf(new FermataSuLinea(c.getStazP(), c.getLinea())));
			FermataSuLinea fslArrivo = fermateSuLinea.get(fermateSuLinea.indexOf(new FermataSuLinea(c.getStazA(), c.getLinea())));

			if (fslPartenza != null && fslArrivo != null) {
				// Aggiungoun un arco pesato tra le due fermate
				Graphs.addEdge(grafo, fslPartenza, fslArrivo, tempo);
			} else {
				System.err.println("Non ho trovato fslPartenza o fslArrivo. Salto alle prossime");
			}
		}

		// FASE3: Aggiungo tutte le connessioni tra tutte le fermateSuLinee della stessa Fermata
		for (Fermata fermata : fermate) {
			
			FermataSuLinea metaFermataP = fermateSuLinea.get(fermateSuLinea.indexOf(new FermataSuLinea(fermata, virtInizioCorsa)));
			FermataSuLinea metaFermataA = fermateSuLinea.get(fermateSuLinea.indexOf(new FermataSuLinea(fermata, virtFineCorsa)));
			
			for (FermataSuLinea ft : fermata.getFermateSuLinea()) {
				if (debug) System.err.println("Pseudo arco da " + metaFermataP + " a " + ft);
				Graphs.addEdge(grafo, metaFermataP, ft, 0);
				if (debug) System.err.println("Pseudo arco da " + ft + " a " + metaFermataA);
				Graphs.addEdge(grafo, ft, metaFermataA, 0);
			}

			for (FermataSuLinea fslP : fermata.getFermateSuLinea()) {
				for (FermataSuLinea fslA : fermata.getFermateSuLinea()) {
					if (!fslP.equals(fslA)) {
						Graphs.addEdge(grafo, fslP, fslA, fslA.getLinea().getIntervallo() * 60 / 2);
					}
				}
			}
		}

		if (debug)
			System.out.println("Grafo creato: " + grafo.vertexSet().size() + " nodi, " + grafo.edgeSet().size() + " archi");
	}

	public void calcolaPercorso(Fermata partenza, Fermata arrivo) {
		
		FermataSuLinea fslP = fermateSuLinea.get(fermateSuLinea.indexOf(new FermataSuLinea(partenza, virtInizioCorsa)));
		FermataSuLinea fslA = fermateSuLinea.get(fermateSuLinea.indexOf(new FermataSuLinea(arrivo, virtFineCorsa)));

		System.out.println("Calcolo percorso da: " + fslP + " a " + fslA);

		DijkstraShortestPath<FermataSuLinea, DefaultWeightedEdge> dijkstra = new DijkstraShortestPath<FermataSuLinea, DefaultWeightedEdge>(grafo, fslP, fslA);

		pathEdgeList = dijkstra.getPathEdgeList();
		pathTempoTotale = dijkstra.getPathLength();

		if (pathEdgeList == null)
			throw new RuntimeException("Non è stato creato un percorso.");
	}

	public String getPercorsoEdgeList() {

		if (pathEdgeList == null)
			throw new RuntimeException("Non è stato creato un percorso.");

		StringBuilder risultato = new StringBuilder();
		risultato.append("Percorso:");

		Linea ultimaLinea = null;
		for (DefaultWeightedEdge edge : pathEdgeList) {
			if (!grafo.getEdgeTarget(edge).getLinea().equals(virtFineCorsa)) {
				if (!grafo.getEdgeTarget(edge).getLinea().equals(ultimaLinea)) {
					ultimaLinea = grafo.getEdgeTarget(edge).getLinea();
					risultato.append("\n\nPrendere Linea: " + ultimaLinea.getNome() + "\n");
				} else {
					risultato.append(", ");
				}
				risultato.append(grafo.getEdgeTarget(edge).getNome());
			}
		}

		return risultato.toString();
	}

	public double getPercorsoTempoTotale() {

		if (pathEdgeList == null)
			throw new RuntimeException("Non è stato creato un percorso.");

		return pathTempoTotale;
	}
}
