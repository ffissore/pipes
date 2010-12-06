/*
 * Copyright 2010 Federico Fissore
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

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
