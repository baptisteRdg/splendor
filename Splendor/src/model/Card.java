package model;

import java.util.HashMap;

public  interface Card {
	int point();
	HashMap<Money,Integer> cost();
	Money advantage();
	String id();
	
	@Override
	String toString();
}
