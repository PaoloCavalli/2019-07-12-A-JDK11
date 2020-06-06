package it.polito.tdp.food.model;

public class FoodAndCongiunte implements Comparable<FoodAndCongiunte> {

	Food f;
	Double calorieCongiunte;
	
	public FoodAndCongiunte(Food f, Double calorieCongiunte) {
		super();
		this.f = f;
		this.calorieCongiunte = calorieCongiunte;
	}

	public Food getF() {
		return f;
	}

	public void setF(Food f) {
		this.f = f;
	}

	public Double getCalorieCongiunte() {
		return calorieCongiunte;
	}

	public void setCalorieCongiunte(Double calorieCongiunte) {
		this.calorieCongiunte = calorieCongiunte;
	}

	@Override
	public int compareTo(FoodAndCongiunte o) {
		
		return - this.getCalorieCongiunte().compareTo(o.getCalorieCongiunte());
	}
	
}
