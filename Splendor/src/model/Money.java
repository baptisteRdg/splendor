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
		case EMERALD: return "🟢";
        case SAPPHIRE: return "🟣";
        case DIAMOND: return "🔵";
        case RUBY: return "🔴";
        case ONYX: return "⚫️";
        case GOLD: return "🟡";
		default:
			throw new IllegalArgumentException("Unexpected value: " + this);
		}
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
