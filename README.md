#VisionMate Client

A Java client for accessing a Thermo Scientific VisionMate scanner via TCP/IP.

##Compile

	```
	mvn clean install
	```

This will run unit tests, and compile the project, creating two jars

* visionmate-client-<version\>.jar
* visionmate-client-<version\>-jar-with-dependencies.jar

##Testing

### Mock server

A mock server class is included to allow testing without interacting with a real server. The mock server is made to imitate the real server as much as possible, including the odd things that it does.

### CLI

	```
	java -jar \<jar-with-dependencies\> <\server-ip\> \<server-port\>
	```

A small CLI application is included for testing with a real server. Run it and type 'help' for a list of commands.

### Unit tests

	```
	mvn test
	```

The unit tests make use of the mock server, so that a real server is not required.

##Usage

###Configuration

A ServerConfig object holds configuration settings that MUST match the server's configuration. It is recommended to use the default settings for everything EXCEPT for the suffix character. The suffix character is used to mark end of line, and is required for the client to function. Default suffix character on the server is not specified, so this must be configured before this client can be used.

Not all server configuration options are supported, so it is recommended to leave any options which are not available via ServerConfig at their default settings.

###Supported Operations

The following operations are available via the VisionMateClient

* get status
* reset status
* get current product
* set current product
* get scan data
* get last error

There are also convenience methods for monitoring status updates and getting scan data when a fresh scan is completed.

###Example

	```
	try (VisionMateClient scanner = new VisionMateClient\(\<host-ip\> \<port\>) {
		scanner.connect();
		scanner.setCurrentProduct(Manufacturer.MATRIX, 8, 12); // expect a matrix box with 8 rows and 12 columns
		scanner.resetStatus(); // reset status, so we can observe status updates to find when a new scan has occurred
		System.out.println("Scan the rack now");
		Scan scan = scanner.waitForScan(); // returns a scan if a rack is scanned before the wait time expires; otherwise returns null
	}
	```
