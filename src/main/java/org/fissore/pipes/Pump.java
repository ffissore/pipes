package org.fissore.pipes;

public interface Pump<I, O> {

	O draw(I in);

}
