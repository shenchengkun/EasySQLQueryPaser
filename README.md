###This project parse the sql querry statement and return the result in a txt file

The top level of the class is SQLParser.

The logical operators are located in the logicalOperators folder.
The physical operators are located in the operators folder.
The PhysicalPlanBuilder is located in the util folder.

There are no known issues with the program.

The parition reset for SMJ involves calling reset(index) on the right child operator the SMJ. It will recurse down the tree and when it 
sees a scan operator or external sort operator (both of which use the binary reader to read tuples), it will call the reset(index) method
in the binary reader. This class keeps track of a list of indices to return to and uses the position function for file channels to return
to a previous line. There is definitely no unbounded state here. The distinct logic is the same as P2. Sort and iterate through the tuples 
removing duplicates. In memory sort obviously has unbounded state. External sort does not. So as long as the config has sort as 0 X where 
X is the number of buffer pages, distinct will not have unbounded state.
