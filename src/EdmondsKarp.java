//CALVIN TRAN
//GRAPH THEORY EXAM
//Edmonds Karp Algorithm - breadth first search

import java.util.*;

public class EdmondsKarp {
    private static class Edge { //this class represents the edges in a graph
        int to, capacity;
        Edge reverse;
        public Edge(int to, int capacity) {
            this.to = to;
            this.capacity = capacity;
        }
    }

    private List<List<Edge>> graph;
    private int[] parent;
    private Edge[] parentEdge;

    public EdmondsKarp(int n) {
        graph = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        parent = new int[n];
        parentEdge = new Edge[n];
    }

    public void addEdge(int from, int to, int capacity) {  // this class adds directed edges to the graph
        Edge forward = new Edge(to, capacity);
        Edge backward = new Edge(from, 0); // this part accounts for the reverse edge for residual capacity
        forward.reverse = backward;
        backward.reverse = forward;
        graph.get(from).add(forward);
        graph.get(to).add(backward);
    }

    public int maxFlow(int source, int sink) { //calculates the maximum flow from the source vertex to the sink vertex in the network
        int flow = 0; //sets starting flow to 0
        while (true) {
            Arrays.fill(parent, -1);
            Queue<Integer> queue = new LinkedList<>(); //i use a queue data structure to perform breadth first search
            queue.offer(source);
            parent[source] = source;
            boolean isPathFound = false;

            while (!queue.isEmpty() && !isPathFound) { //each vertex u dequeued it checks all outgoing edges. if an edge leads to an unvisited vertex (v) and has positive capacity, it marks v as visited, records the path (parent[v] and parentEdge[v]), and enqueues v. if v is the sink, breaks the loop meaning a path has been found.
                int u = queue.poll();
                for (Edge edge : graph.get(u)) {
                    if (parent[edge.to] == -1 && edge.capacity > 0) {
                        parent[edge.to] = u;
                        parentEdge[edge.to] = edge;
                        queue.offer(edge.to);
                        if (edge.to == sink) {
                            isPathFound = true;
                            break;
                        }
                    }
                }
            }

            if (!isPathFound) break;

            int pathCapacity = Integer.MAX_VALUE; //if no augmenting path is found, exits the loop. otherwise finds the minimum capacity along the path from the sink back to the source (bottleneck capacity).updates the capacities along the path and their reverse edges to reflect the flow being pushed through. adds the bottleneck capacity to the total flow
            for (int v = sink; v != source; v = parent[v]) {
                Edge edge = parentEdge[v];
                pathCapacity = Math.min(pathCapacity, edge.capacity);
            }

            for (int v = sink; v != source; v = parent[v]) {
                Edge edge = parentEdge[v];
                edge.capacity -= pathCapacity;
                edge.reverse.capacity += pathCapacity;
            }

            flow += pathCapacity;
        }

        return flow;
    }

    public static void main(String[] args) { //this part of the code prints out the 1st graph that was shown in the youtube video for edmonds karps algorithm, to test out for other graphs read below
        EdmondsKarp ek = new EdmondsKarp(5); // n = # of vertices (make sure to update n as needed for whatever graph is constructed!)
        ek.addEdge(0, 1, 20); //this part of the code can be edited to construct the graph to find max flow
        ek.addEdge(1, 4, 8);// just change out "from", and "to", and the "capacity" of each edge
        ek.addEdge(1, 2, 18); //add or delete however many edges are needed
        ek.addEdge(0, 2, 15); // <---- for example if you dont need an edge connecting from 0 to 2 just delete this line of code
        ek.addEdge(2, 3, 10);
        ek.addEdge(2, 4, 3);
        ek.addEdge(3, 4, 12);

        System.out.println("Max Flow: " + ek.maxFlow(0, 4)); //make sure to change the sink to the right number, should be n-1 if source is 0
    }
}
