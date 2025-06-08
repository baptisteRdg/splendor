package controller;

import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.PointerEvent;

import model.CoordClic;

public class graphicAction {
	ApplicationContext context;
	public graphicAction(ApplicationContext context) {
		this.context=context;
	}

	public CoordClic getClic() {
		var event = context.pollOrWaitEvent(1000000000);
		if(event==null){return null;}// Il existe d'autre type wait mais je ne m'en rappel plus
		switch(event) {
		case KeyboardEvent _ :return null;
		  // Recup la touche du clavier
		case PointerEvent pe:
		  var location = pe.location();
		  return new CoordClic(location.x(), location.y());
		default :
		  return null;
		}
	}
	
}
