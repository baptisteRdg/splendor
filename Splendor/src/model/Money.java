package model;


public enum Money {

	EMERALD("Émeraude"),
	SAPPHIRE("Saphir"),
	DIAMOND("Diamant"),
	RUBY("Rubis"),
	ONYX("Onyx"),
	GOLD("Gold");

	Money(String nom) {
		name = nom;
	}
	
	/// Nom utilisé dans le jeu
	private final String name;
	
	public String shortString() {
		switch (this) {
		case EMERALD: return "E";
		case SAPPHIRE: return "S";
		case DIAMOND: return "D";
		case RUBY: return "R";
		case ONYX: return "O";
		case GOLD: return "G";
		default:
			throw new IllegalArgumentException("Unexpected value: " + this);
		}
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
