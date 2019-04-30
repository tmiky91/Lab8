package it.polito.tdp.metrodeparis.model;

public class Connessione {
	
	private int idConnessione;
	private int idLinea;
	private int idPartenza;
	private int idArrivo;
	public Connessione(int idConnessione, int idLinea, int idPartenza, int idArrivo) {
		super();
		this.idConnessione = idConnessione;
		this.idLinea = idLinea;
		this.idPartenza = idPartenza;
		this.idArrivo = idArrivo;
	}
	public int getIdConnessione() {
		return idConnessione;
	}
	public void setIdConnessione(int idConnessione) {
		this.idConnessione = idConnessione;
	}
	public int getIdLinea() {
		return idLinea;
	}
	public void setIdLinea(int idLinea) {
		this.idLinea = idLinea;
	}
	public int getIdPartenza() {
		return idPartenza;
	}
	public void setIdPartenza(int idPartenza) {
		this.idPartenza = idPartenza;
	}
	public int getIdArrivo() {
		return idArrivo;
	}
	public void setIdArrivo(int idArrivo) {
		this.idArrivo = idArrivo;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idConnessione;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Connessione other = (Connessione) obj;
		if (idConnessione != other.idConnessione)
			return false;
		return true;
	}

}
