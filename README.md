# Pipes
Pipes is an easy way to parallelize long running pieces of code that produce objects that other long running pieces will of code will use as input.

For esample, think about a web crawler, a parsing logic and a persistence layer: you can parallelize them making the crawler feeding the parsing logic, while that logic is feeding the persistence layer.

You don't need to change your code (apart from implementing a tiny interface)

## What's in the name

The building block of Pipes is the pipe: each pipe has a pump in the middle. The pump draws data from the incoming part of the pipe, elaborating it, to the outgoing part.
Each middle pipe has its outgoing part connected with the incoming part of the next pipe.

You can assemble the pipes on your own, or ask a plumber to do that for you: you need to give the plumber the beginning and ending parts of your pipes, and the collection of pumps that will elaborate/transform the data. The plumber will assemble the pipes and the pumps.