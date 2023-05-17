package com.backend.reclamation.services;

public class CountEtat {
	private Long count;
	private int etat;
	public CountEtat() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CountEtat(Long count, int etat) {
		super();
		this.count = count;
		this.etat = etat;
	}
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public int getEtat() {
		return etat;
	}
	public void setEtat(int etat) {
		this.etat = etat;
	}
	
	

}
