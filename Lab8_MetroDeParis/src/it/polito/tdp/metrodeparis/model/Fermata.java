package it.polito.tdp.metrodeparis.model;

public class Fermata {
	
	private int id_fermata;
	private String nomeFermata;
	private double coordX;
	private double coordY;
	
	public Fermata(int id_fermata, String nomeFermata, double coordX, double coordY) {
		this.id_fermata = id_fermata;
		this.nomeFermata = nomeFermata;
		this.coordX = coordX;
		this.coordY = coordY;
	}
	
	public Fermata(int id_fermata, String nomeFermata) {
		this.id_fermata = id_fermata;
		this.nomeFermata = nomeFermata;
	}

	public int getId_fermata() {
		return id_fermata;
	}

	public void setId_fermata(int id_fermata) {
		this.id_fermata = id_fermata;
	}

	public String getNomeFermata() {
		return nomeFermata;
	}

	public void setNomeFermata(String nomeFermata) {
		this.nomeFermata = nomeFermata;
	}

	public double getCoordX() {
		return coordX;
	}

	public void setCoordX(double coordX) {
		this.coordX = coordX;
	}

	public double getCoordY() {
		return coordY;
	}

	public void setCoordY(double coordY) {
		this.coordY = coordY;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nomeFermata == null) ? 0 : nomeFermata.hashCode());
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
		Fermata other = (Fermata) obj;
		if (nomeFermata == null) {
			if (other.nomeFermata != null)
				return false;
		} else if (!nomeFermata.equals(other.nomeFermata))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Fermata: "+id_fermata+" "+nomeFermata;
	}
	
	
	

}
