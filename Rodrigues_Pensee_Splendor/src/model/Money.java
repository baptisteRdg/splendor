package model;

import java.awt.Color;

public enum Money {
	
	EMERALD("Émeraude"),
	SAPPHIRE("Saphir"),
	DIAMOND("Diamant"),
	RUBY("Rubis"),
	ONYX("Onyx"),
	GOLD("Gold");

	Money(String nom) {}
	
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
	public Color toColor() {
		switch (this) {
		case EMERALD: return Color.green;
        case SAPPHIRE: return Color.magenta;
        case DIAMOND: return Color.blue;
        case RUBY: return Color.red;
        case ONYX: return Color.gray;
        case GOLD: return Color.yellow;
		default:
			throw new IllegalArgumentException("Unexpected value: " + this);
	}}
	
	@Override
	public String toString() {
		return shortString();
	}
	
}
