package model;

import java.util.HashMap;

public sealed interface Card permits Master,Upgrade {
	int point();
	HashMap<Money,Integer> cost();
	Money advantage();
	String id();
	
	@Override
	String toString();
}
