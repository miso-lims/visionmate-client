# VisionMate Client

A Java client for accessing a Thermo Scientific VisionMate scanner via TCP/IP.

## Compatibility

* Scanner model AB-1850, software v3.2.1.10: works as is
* Scanner model 312800, software v4.0.4.1: works with patches. There are bugs in this version of the VisionMate
  software. Thermo Fisher has provided these two patches, which are required in order for this library to communicate
  with the scanner software. Unfortunately, the patches have not yet been included in an official release. Installation
  instructions are included in the archives.
  * [TCPUpdate_dependancies.zip](patches/TCPUpdate_dependancies.zip)
  * [SBTCPServerV4.0.3.6.zip](patches/SBTCPServerV4.0.3.6.zip)

## Compile

	mvn clean install

This will run unit tests, and compile the project, creating two jars

* visionmate-client-\<version\>.jar
* visionmate-client-\<version\>-jar-with-dependencies.jar

## Testing

### Mock server

A mock server class is included to allow testing without interacting with a real server. The mock server is made to
imitate the real server as much as possible. Methods are provided to manipulate its current state and data before
issuing commands to it over TCP/IP.

### CLI

	java -jar <jar-with-dependencies> <server-ip> <server-port>

A small CLI application is included for testing with a real server. Run it and type 'help' for a list of commands.

### Unit tests

	mvn test

The unit tests make use of the mock server, so that a real server is not required.

## Usage

### Configuration

**Server Setup - V3 VisionMate Software**

1. Enable the TCP/IP server: Open the VisionMate software and go to Export > Enable TCP/IP Server
2. Display the TCP/IP tabs: Config > Show TCP/IP Tab
3. Set a suffix character \(TCP/IP Server tab\). A suffix character marks the end of response data and is required for
   the client to function correctly. It is ideal to use a non-printable character to ensure that it is not found in
   barcodes or other data that the server may send. The suggested character is ASCII control character 3 \(the end of
   text character\), which can be chosen by entering '\[3\]'

It is recommended to leave other settings at their default, but if you'd like to change anything else, ensure that the
option is supported in the client (see below).

**Server Setup - V4 VisionMate Software**

1. Enable the TCP/IP server: Export Options (yellow icon) > TCP Server \(a checkmark should appear to indicate it is
   enabled\)
2. Set a suffic character: Export Options > TCP Server > Config > Data Field Suffix. The suggested character is ASCII
   control character 3 \(the end of text character\), which can be chosen by entering '\[3\]'
3. Add a rack type: You will need a rack with tubes in it in order to set this up as there is no option to manually
   define a rack type. Click the gear icon > RAD and follow the on-screen instructions to scan the rack. Name it in the
   format MRRCC, replacing "RR" with the number of rows, and "CC" with the number of columns. e.g. "M0812" for a rack
   with 8 rows and 12 columns. Ensure that this rack type is selected in Rack Formats \(grid-looking icon\)

It is recommended to leave other settings at their default, but if you'd like to change anything else, ensure that the
option is supported in the client (see below).

**Client Setup**

A ServerConfig object holds configuration settings that MUST match the server's configuration. It is recommended to use
the default settings for everything EXCEPT for the suffix character. The default and recommended suffix character in
the client is control character 3, as noted above.

### Supported Operations

The following operations are available via the VisionMateClient

* get status
* reset status
* get current product
* set current product
* get scan data
* get last error

There are also convenience methods for monitoring status updates and getting scan data when a fresh scan is completed.

### Example

	try (VisionMateClient scanner = new VisionMateClient(<host-ip> <port>) {
		scanner.connect();
		// expect a matrix box with 8 rows and 12 columns
		scanner.setCurrentProduct(Manufacturer.MATRIX, 8, 12);
		
		// reset status, so we can observe status updates to find when a new scan has occurred
		scanner.resetStatus();
		
		System.out.println("Scan the rack now");
		Scan scan = scanner.waitForScan();
		if (scan == null) {
			// timed out; no rack was scanned
		}
		else {
			// rack was scanned
		}
	}
