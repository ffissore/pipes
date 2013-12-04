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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PipesTest {

	private Pump<Integer, Integer> multiplierPump;
	private Pump<Integer, Integer> summerPump;
	private Queue<Integer> incoming;
	private Queue<Integer> outgoing;
	private ExecutorService executor;

	public PipesTest() {
		multiplierPump = new Pump<Integer, Integer>() {

			@Override
			public Integer draw(Integer in) {
				return Integer.valueOf(in.intValue() * 2);
			}
		};

		summerPump = new Pump<Integer, Integer>() {

			@Override
			public Integer draw(Integer in) {
				return Integer.valueOf(in.intValue() + 2);
			}
		};

	}

	@Before
	public void setup() {
		this.incoming = new ConcurrentLinkedQueue<Integer>();
		this.outgoing = new ConcurrentLinkedQueue<Integer>();

		this.executor = Executors.newCachedThreadPool();
	}

	@After
	public void teardown() {
		this.executor.shutdown();
	}

	@Test
	public void shouldReturnSummedNumbers() throws Exception {
		incoming.add(Integer.valueOf(1));
		incoming.add(Integer.valueOf(2));

		assertEquals(0, outgoing.size());

		Pipe<Integer, Integer> pipe = new Pipe<Integer, Integer>(incoming, outgoing, summerPump);
		executor.execute(pipe);

		Thread.sleep(1000);

		assertEquals(0, incoming.size());
		assertEquals(2, outgoing.size());
		assertTrue(outgoing.contains(Integer.valueOf(3)));
		assertTrue(outgoing.contains(Integer.valueOf(4)));

		incoming.add(Integer.valueOf(3));

		Thread.sleep(1000);

		assertEquals(1, incoming.size());
		assertTrue(outgoing.contains(Integer.valueOf(5)));

		pipe.close();
	}

	@Test
	public void shouldReturnSummedAndMultipliedNumbers() throws Exception {
		List<Pump<?, ?>> pumps = new LinkedList<Pump<?, ?>>();
		pumps.add(summerPump);
		pumps.add(multiplierPump);

		incoming.add(Integer.valueOf(1));
		incoming.add(Integer.valueOf(2));

		Plumber<Integer, Integer> plumber = new Plumber<Integer, Integer>(executor);
		List<Pipe<?, ?>> pipes = plumber.connectAndOpenPipes(incoming, outgoing, pumps);

		Thread.sleep(1000);

		plumber.closeAll(pipes);

		Thread.sleep(500);

		assertEquals(2, outgoing.size());
		assertTrue(outgoing.contains(Integer.valueOf(6)));
		assertTrue(outgoing.contains(Integer.valueOf(8)));
	}
}
