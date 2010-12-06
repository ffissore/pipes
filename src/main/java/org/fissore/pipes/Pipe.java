package org.fissore.pipes;

import java.util.Queue;

public class Pipe<I, O> implements Runnable {

	private final Queue<I> incoming;
	private final Queue<O> outgoing;
	private final Pump<I, O> pump;
	private volatile boolean open;

	public Pipe(Queue<I> incoming, Queue<O> outgoing, Pump<I, O> pump) {
		this.incoming = incoming;
		this.outgoing = outgoing;
		this.pump = pump;
		this.open = true;
	}

	@Override
	public void run() {
		while (open) {
			I in = null;
			while ((in = incoming.poll()) != null) {
				O out = pump.draw(in);
				outgoing.offer(out);
			}
			if (open) {
				sleep(100);
			}
		}
	}

	public void close() {
		this.open = false;
	}

	public void open() {
		this.open = true;
	}

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
