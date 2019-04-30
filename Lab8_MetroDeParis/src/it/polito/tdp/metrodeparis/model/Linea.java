package it.polito.tdp.metrodeparis.model;

public class Linea {
	
	private int idLinea;
	private String name;
	private int velocita;
	private int intervallo;
	private String colore;
	public Linea(int idLinea, String name, int velocita, int intervallo, String colore) {
		super();
		this.idLinea = idLinea;
		this.name = name;
		this.velocita = velocita;
		this.intervallo = intervallo;
		this.colore = colore;
	}
	public int getIdLinea() {
		return idLinea;
	}
	public void setIdLinea(int idLinea) {
		this.idLinea = idLinea;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getVelocita() {
		return velocita;
	}
	public void setVelocita(int velocita) {
		this.velocita = velocita;
	}
	public int getIntervallo() {
		return intervallo;
	}
	public void setIntervallo(int intervallo) {
		this.intervallo = intervallo;
	}
	public String getColore() {
		return colore;
	}
	public void setColore(String colore) {
		this.colore = colore;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idLinea;
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
		Linea other = (Linea) obj;
		if (idLinea != other.idLinea)
			return false;
		return true;
	}

}
