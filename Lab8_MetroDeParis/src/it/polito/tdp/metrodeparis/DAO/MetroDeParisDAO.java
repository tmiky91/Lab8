package it.polito.tdp.metrodeparis.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.metrodeparis.model.Fermata;

public class MetroDeParisDAO {

	public List<Fermata> getAllFermate() {
		final String sql =	"select f.id_fermata, f.nome, f.coordX, f.coordY " + 
							"from fermata as f";
		List<Fermata> fermate = new LinkedList<Fermata>();
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			ResultSet res = st.executeQuery() ;
			while(res.next()) {
				int idFermata = res.getInt("id_fermata");
				String nome = res.getString("nome");
				double coordX = res.getDouble("coordX");
				double coordY = res.getDouble("coordY");
				
				Fermata f = new Fermata(idFermata, nome, coordX, coordY);
				fermate.add(f);
			}
			conn.close();
			return fermate;
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore db");
			
		}
	}
	
	public boolean esisteConnessione(Fermata partenza, Fermata arrivo) {
		final String sql =	"select count(*) as cnt " + 
							"from connessione as c " + 
							"where c.id_stazP=? " + 
							"and c.id_stazA=?";
		
		
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, partenza.getIdFermata());
			st.setInt(2, arrivo.getIdFermata());
			ResultSet res = st.executeQuery() ;
			res.next();
			int numero = res.getInt("cnt");
			
			conn.close();
			return(numero>0);
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore db");
			
		}
	}

	public List<Fermata> stazioniArrivo(Fermata partenza) {
		final String sql = 	"select c.id_stazA " + 
							"from connessione as c " + 
							"where c.id_stazP=?";
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, partenza.getIdFermata());
			ResultSet res = st.executeQuery() ;
			List<Fermata> result = new ArrayList<Fermata>();
			while(res.next()) {
				result.add(new Fermata(res.getInt("id_stazA")));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore db");
		}
	}

}
