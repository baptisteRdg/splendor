package model;

public sealed interface Card permits Master,Upgrade {
	int point();
	Price getPrice();
	Money advantage();
	String id();
	int level();
	@Override
	String toString();
}
