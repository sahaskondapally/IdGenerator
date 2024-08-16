SequenceGenerator Algorithm
Overview
The SequenceGenerator algorithm generates unique tracking numbers by combining a timestamp, a node identifier, and a sequence number. The design ensures that the generated IDs are unique, scalable, efficient, and fault-tolerant. The algorithm is based on the concept of a unique identifier with specific bit allocations for different components.

Key Components
1. Timestamp
Represents the current time in milliseconds since a custom epoch (January 1, 2024).
2. Node ID
A unique identifier for the machine or node generating the ID.
3. Sequence Number
A number that tracks the sequence of IDs generated within the same millisecond.
Algorithm Steps
Initialization
Epoch Definition

Define a custom epoch starting at January 1, 2024. This serves as the reference point for calculating timestamps.
Bit Allocation

The unique ID is structured with specific bits allocated for each component:
Timestamp: 64 bits
Node ID: 30 bits
Sequence Number: 12 bits
Unused Bits: 22 bits (reserved for future use)
Generate a Node ID
Retrieve System Information

Obtain network-related information from the local system, such as hostname, IP address, and MAC address.
Generate Node ID

Combine the retrieved system information to create a unique node identifier. If this process fails, use a random number generator as a fallback.
Generate a Unique ID
Timestamp Calculation

Calculate the current timestamp in milliseconds since the custom epoch.
Check Timestamp Validity

Compare the current timestamp with the last generated timestamp:
If the current timestamp is earlier, raise an error indicating an invalid system clock.
If the current timestamp is the same, increment the sequence number. If it overflows, wait for the next millisecond.
If the current timestamp is later, reset the sequence number to zero.
Wait for Next Millisecond

If the sequence number reaches its maximum value within the same millisecond, wait until the timestamp changes to avoid collisions.
Construct the ID

Combine the timestamp, node ID, and sequence number into a single ID:
High Part: Timestamp (64 bits)
Low Part: Node ID (30 bits) and Sequence Number (12 bits)
Format the Output

Convert the ID components into a hexadecimal string and return the concatenated result as the final unique ID.
Fault Tolerance
Invalid Clock Handling: If the system clock is not functioning correctly, an exception is thrown.
Fallback for Node ID Generation: If network information is unavailable, a random node ID is generated.
Scalability and Efficiency
Concurrent Handling: By synchronizing access to the sequence number and managing millisecond boundaries, the algorithm supports high concurrency.
Horizontal Scaling: Node IDs ensure that multiple instances can run across different machines, generating unique IDs without conflict.
Bit Mapping Explanation
The SequenceGenerator uses a bitwise approach to structure the unique ID as a 128-bit value. This involves allocating specific bits to different parts of the ID, ensuring uniqueness and efficiency.

Bit Fields
Timestamp Bits (64 bits)

Represents the time elapsed since the custom epoch.
Range: Provides a large range of timestamps for uniqueness over a long period.
Node ID Bits (30 bits)

Identifies the node (or machine) generating the ID.
Range: Up to 1,073,741,823 unique node IDs.
Sequence Bits (12 bits)

Tracks the sequence of IDs generated within the same millisecond.
Range: Allows for 4,096 unique sequence values per millisecond.
Unused Bits (22 bits)

Reserved for future expansion or additional purposes.
ID Structure
High Part (64 bits):

Timestamp (64 bits): Represents the time in milliseconds since the custom epoch.
Low Part (64 bits):

Node ID (30 bits): Positioned in the most significant 30 bits of the low part.
Sequence Number (12 bits): Positioned in the least significant 12 bits of the low part.
Unused Bits (22 bits): Reserved for future use.
