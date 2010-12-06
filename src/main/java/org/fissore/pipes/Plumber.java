package org.fissore.pipes;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;

public class Plumber<I, O> {

	private final ExecutorService executor;

	public Plumber(ExecutorService executor) {
		this.executor = executor;
	}

	@SuppressWarnings("unchecked")
	public List<Pipe<?, ?>> connectAndOpenPipes(Queue<I> input, Queue<O> output, List<Pump<?, ?>> pumps) {
		List<Pipe<?, ?>> pipes = new LinkedList<Pipe<?, ?>>();

		Queue<?> incoming = input;
		for (int i = 0; i < pumps.size(); i++) {
			Queue<?> outgoing;
			if (i == (pumps.size() - 1)) {
				outgoing = output;
			} else {
				outgoing = new ConcurrentLinkedQueue();
			}

			Pipe<?, ?> pipe = new Pipe(incoming, outgoing, pumps.get(i));
			pipe.open();
			pipes.add(pipe);
			executor.execute(pipe);
			incoming = outgoing;
		}

		return pipes;
	}

	public void closeAll(List<Pipe<?, ?>> pipes) {
		for (Pipe<?, ?> pipe : pipes) {
			pipe.close();
		}
	}
}
