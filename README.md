Smart City Scheduling — README Report
Assignment Goal
This project implements a set of graph algorithms for "Smart City / Smart Campus Scheduling," handling city-service tasks (e.g., street cleaning, repairs, sensor maintenance, analytics). The main algorithms are:

Strongly Connected Components (SCC) detection (Tarjan's algorithm)

Condensation graph construction to obtain a DAG from SCCs

Topological sorting (Kahn's algorithm)

Shortest and longest paths in DAGs (dynamic programming on a topological order)

All solutions are evaluated on multiple generated datasets of varying size and density.

Project Structure
text
data/
  small/    # 3 small test graphs (6–10 nodes)
  medium/   # 3 medium test graphs (10–20 nodes)
  large/    # 3 large test graphs (20–50 nodes)

src/main/java/
  graph/model         # Graph and Edge data structures
  graph/metrics       # Metrics for timing and operation counts
  graph/scc           # TarjanSCC and CondensationGraph classes
  graph/topo          # KahnTopologicalSort
  graph/dagsp         # DAGShortestPath (handles both shortest and longest paths)
  util/               # JSONLoader and ReportGenerator
  Main.java           # Main runner and result analyzer

src/test/java/
  # Automated tests for correctness (SCC, topological sort, DAG paths)

output/
  analysis_report.txt         # Detailed run log and explanations
  performance_results.csv     # Summary of metrics for spreadsheet/plotting
Algorithms
SCC Detection (Tarjan):
Finds all strongly connected components in a directed graph, efficiently decomposing cycles.

Condensation Graph:
Collapses each SCC into a single node, yielding a Directed Acyclic Graph (DAG).

Topological Sort (Kahn):
Produces a valid linear order of DAG components and a compressed ordering of original tasks.

DAG Shortest/Longest Path:
For DAGs, computes shortest and critical (longest) paths from a source node using DP over a topological order.

Operation counters and timings are collected for each algorithm to analyze bottlenecks and scalability.

Input Format
Each dataset is a JSON file:

json
{
  "directed": true,
  "n": 10,
  "edges": [
    { "u": 0, "v": 1, "w": 4 },
    { "u": 1, "v": 2, "w": 2 }
    // ...
  ],
  "source": 0,
  "weight_model": "edge"
}
Nine datasets were created:

Small (6–10 nodes), Medium (10–20), Large (20–50)

Mix of sparse/dense, cyclic and acyclic structures

Outputs:

output/analysis_report.txt (detailed algorithm results, SCC structure, topo orders, path details)

output/performance_results.csv (summary table for plotting/Excel import)

Results and Performance
Example Table (from CSV):
Dataset	Nodes	Edges	SCCs	SCC (ms)	Topo (ms)	Path (ms)	Type
graph_small_01.json	6	7	6	0.050	0.037	0.020	DAG
...	...	...	...	...	...	...	...
SCC and topo sorting run in under 0.1 ms even for largest test graphs (50 nodes).

Path algorithms are applied only to acyclic (DAG) graphs; metrics show efficient scaling.

For cyclic graphs, path algorithms are skipped as per assignment requirements.

Number of SCCs and tasks structure influences topological order and scheduling complexity.

Insights
SCC detection is vital for any scenario with potential cyclic task dependencies; it enables the transformation into a manageable DAG and identifies parallelizable components.

Topological ordering is essential for scheduling in acyclic systems: tasks can be assigned, executed, or analyzed optimally.

The longest path in a DAG detects the "critical path" — the main bottleneck for project duration and resource planning.

Analysis of metrics helps pinpoint the sources of computational complexity and guides future scaling.

References
R. Tarjan, “Depth-First Search and Linear Graph Algorithms,” SIAM J. Comput. (1972)
Kahn, A. B. “Topological sorting of large networks,” Communications of the ACM (1962)

JavaDocs, GeeksForGeeks, Baeldung, and other resources for graph algorithms
