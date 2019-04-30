package it.polito.tdp.metrodeparis.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import it.polito.tdp.metrodeparis.model.Fermata;

public class MetroDAO {

	public List<Fermata> getTutteLeFermate() {
		final String sql = "SELECT * FROM fermata";

		List<Fermata> fermate = new LinkedList<Fermata>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				int id = rs.getInt("id_fermata");
				String nome = rs.getString("nome");
				double coordX = rs.getDouble("coordX");
				double coordY = rs.getDouble("coordY");

				Fermata f = new Fermata(id, nome, coordX, coordY);
				fermate.add(f);
			}
			conn.close();

			return fermate;

		} catch (SQLException e) {
			throw new RuntimeException("Errore Db");
		}
	}

	public void popolaGrafo(SimpleGraph<Fermata, DefaultEdge> grafo) {
		final String sql = "select partenza.id_fermata as idPartenza, partenza.nome as nomePartenza, arrivo.id_fermata as idArrivo, arrivo.nome as nomeArrivo " + 
							"from fermata as partenza, fermata as arrivo, connessione as conn " + 
							"where partenza.id_fermata=conn.id_stazP " + 
							"and arrivo.id_fermata=conn.id_stazA";
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				int idPartenza = rs.getInt("idPArtenza");
				String nomePartenza = rs.getString("nomePartenza");
				int idArrivo = rs.getInt("idArrivo");
				String nomeArrivo = rs.getString("nomeArrivo");

				Fermata partenza = new Fermata(idPartenza, nomePartenza);
				Fermata arrivo = new Fermata(idArrivo, nomeArrivo);
				
				if(!grafo.containsVertex(partenza)) {
					grafo.addVertex(partenza);
				}
				if(!grafo.containsVertex(arrivo)) {
					grafo.addVertex(arrivo);
				}
				if(!grafo.containsEdge(partenza, arrivo) && !grafo.containsEdge(arrivo, partenza)) {
					grafo.addEdge(partenza, arrivo);
				}
			}

		} catch (SQLException e) {
			throw new RuntimeException("Errore Db");
		}
	}

}
