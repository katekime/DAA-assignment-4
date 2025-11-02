Assignment 4: Smart City/Campus Scheduling — Graph Algorithms
Introduction

This project implements a comprehensive framework for analyzing and scheduling interdependent tasks in smart city and campus environments. It addresses problems such as planning maintenance, cleaning, or sensor servicing, where certain dependencies may form cycles or acyclic chains. The goal is to provide analytical tools for decomposing, organizing, and optimizing task execution leveraging core graph algorithms.

Algorithms and Methods

The solution integrates the following algorithms:

Strongly Connected Components (SCC) Detection (Tarjan’s algorithm): Used to identify and compress cyclic dependencies in the task graph. Each SCC represents a set of tasks that mutually depend on each other and must be handled as a group.

Condensation Graph Construction: After SCC detection, the graph is transformed into a condensation (a directed acyclic graph, DAG) where each node is an SCC. This simplifies further dependency analysis and scheduling.

Topological Sorting (Kahn’s algorithm): Applied to the condensation DAG to produce a valid order of component execution where all prerequisites are respected.

Shortest and Longest Paths in DAG: For the acyclic dependency graph, shortest-path algorithms (using dynamic programming over a topological order) produce both minimal and maximal execution sequence lengths. The longest path in a DAG identifies the critical path—the limiting factor for the project's overall duration.

Detailed timing and operation count metrics are collected for each algorithm to evaluate performance and identify bottlenecks.

Experimental Data

A set of nine datasets with varying graph sizes (from 6 to 50 tasks), densities, and structures (cyclic and acyclic, sparse and dense) were generated according to the assignment specification. Each dataset is provided in a uniform JSON format indicating nodes, edges, weights, and the entry-point task.

Testing and Code Quality

The project includes full JUnit coverage for all principal algorithms and edge cases. Source code is organized into clear packages with JavaDoc comments. The repository contains test datasets in a dedicated /data/ folder, and build/test instructions are provided in the README for reproduction.

Results

All experiments (algorithm runs) are documented in detail in the file output/analysis_report.txt. This document includes:

Per-dataset descriptions (size, cyclicity, SCC count)

SCC decompositions

Condensation graphs and their topological orders

Critical/shortest path reconstructions when applicable

Per-algorithm performance metrics: operation counts and execution time

A summary table with all performance metrics (in CSV format) for statistical or graphical analysis is provided in output/performance_results.csv.

Analysis and Key Findings

SCC detection enables the decomposition of cyclic dependencies, facilitating robust scheduling strategies even for complex, strongly connected graphs.

For DAGs, the scheduling order and critical path are successfully computed, enabling the identification of bottleneck task sequences.

All algorithms demonstrate linear or near-linear scaling with respect to the size of the task graph, and sub-millisecond processing times for all benchmarked cases.

Metrics collected allow in-depth benchmarking and comparison, confirming both theoretical and practical effectiveness.

Conclusion

The designed system fully meets the assignment's requirements for analyzing task graphs with arbitrary dependencies. It provides objective, reproducible measures of scheduling complexity and enables practical recommendations for project managers and engineers in smart city applications. The modular design supports further extension (e.g., node-weighted paths, real-world integration, visualization).

For comprehensive results, raw outputs, logs of all algorithm steps per dataset, and all detailed timing data, please refer to the automatically generated file:
output/analysis_report.txt
