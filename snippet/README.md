cmdchat
=======

A command line group chat built on Akka.

### Topology
*	only one central server (bind to `127.0.0.1:2552`)
*	zero or multiple clients connected to server, clients will take any free port. 

### Features
*	client side login: need to enter a non-used name
*	client side logout: type `/exit` to quit
*	client side logout: type `/list` to show all the members
*	membership change notification: any membership change(join or leave) event will be sent to all existing clients
*	public message: every public message will be sent to all registered clients except the sender
*	private message: every private message should start with `@<client name>`, then the following message will be only sent to the specified target
*	crash detection: thanks to `akka-remote`, an unexpected crash can be detected by TCP heart-beat.
*	layout: out-messages are always on the left side, while in-messages are on the right side.
*	prompt: prompt display is optimized for UX

### Test
*	type `sbt clean coverage test` to run the test with a html report
*	make sure the coverage is higher than 80%

