
# Pipes
Pipes is an easy way to parallelize long running pieces of code that produce objects that other long running pieces of code will use as input.

For example, think about a web crawler, a parsing logic and a service layer.
What you do at the beginning is a repetition of these three steps: crawl, parse, save, for each URL you have to crawl. You soon understand you are wasting time: you are using just one core of your quad-core server. These steps can run independently: the crawler will crawl and feed the parsing logic ASAP; the parsing logic, as new web pages can be parsed, will produce objects for the service layer.

## What's in the name

                         in-pipe          out-pipe   in-pipe          out-pipe
    >> INCOMING DATA >>  ======= [ PUMP ] ======== - ======= | PUMP | ======== >> OUTGOING DATA >>
                                               pipes link

The building block of Pipes is the **pipe**: each pipe has a **pump** in the middle. The pump draws data from the incoming part of the pipe, elaborating it, to the outgoing part.
Each middle pipe has its outgoing part connected with the incoming part of the next pipe.

You can assemble the pipes on your own, or ask a **plumber** to do that for you: you need to give the plumber the beginning and ending parts of your pipes, and the collection of pumps that will elaborate/transform the data. The plumber will assemble the pipes and the pumps.
