package model;

public enum Money {

	EMERALD("Émeraude"),
	SAPPHIRE("Saphir"),
	DIAMOND("Diamant"),
	RUBY("Rubis"),
	ONYX("Onyx"),
	GOLD("Or");

	Money(String nom) {
		name = nom;
	}
	
	/// Nom utilisé dans le jeu
	private final String name;
	

	@Override
	public String toString() {
		return name;
	}
	
	
}
