package ex1.src;

import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.*;
import java.util.*;



public class WGraph_Algo implements weighted_graph_algorithms, Serializable {
   private weighted_graph graph;

 /*   public  WGraph_Algo()
    {
        this.graph=null;
    }*/
    @Override
    public void init(weighted_graph g) {
        if(g!=null)
            graph=g;
    }

    @Override
    public weighted_graph getGraph() {
        return graph;
    }

    @Override
    public weighted_graph copy() {
        return new WGraph_DS(graph);
    }

    @Override
    public boolean isConnected() {

            if (graph != null&&graph.getV()!=null)
            {
                resetVisited();//  reset the var "visited" in every vertex in order for the test will be normal
                return BFS();//check if the graph is connected by BFS algorithm and return the value

            } return false;
        }



    @Override
    public double shortestPathDist(int src, int dest) {
        resetVisitedAndTag();
        if(graph.getNode(src)!=null&&graph.getNode(dest)!=null)
        return dijkstra(src,dest);
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public List<node_info> shortestPath(int src, int dest) {
        if (graph != null&&graph.getV()!=null)//if there's graph with vertices
            {
            node_info s = graph.getNode(src);//s-the starting vertex
            node_info d = graph.getNode(dest);//d-the destination vertex
            if (s != null && d != null&&graph.getV().contains(s)&&graph.getV().contains(d)&&!(((WGraph_DS.NodeInfo)d).getNi().isEmpty())) {//if the vertices exist and belong to the graph
                //and if the destination vertex have any neighbors
                resetVisitedAndTagAndPre();//  reset the var "visited" , "tag" and "pre" in every vertex
                if (dijkstraPath(s,d)) { //do dijkstraPath function, if a path is found, do:
                    LinkedList<node_info> path = new LinkedList<>(); // create a list that illustrate the shortest path
                    node_info crawl = d;//save the last vertex= the dest of the path
                    path.addFirst(crawl); //add it to the list of path
                    while (((WGraph_DS.NodeInfo) (crawl)).getPre() != null) { //while isn't the start og the path
                        path.addFirst(((WGraph_DS.NodeInfo) (crawl)).getPre());// add to the list the previous vertex that we came from it to the current vertex
                        crawl = ((WGraph_DS.NodeInfo) (crawl)).getPre();// in the next iteration the tests will be on the previous vertex
                    }

                    return path;//return the list of the shortest path
                }
            }



            return null;// if there's no path or the vertices aren't exist, return null
        }
        return  null;

            }

    @Override
    public boolean save(String file) {
        try {
            FileOutputStream f = new FileOutputStream(new File(file));
            ObjectOutputStream o = new ObjectOutputStream(f);

            // Write objects to file

            o.writeObject(graph);


            o.close();
            f.close();
            return true;


        } catch (Exception e) {
             e.printStackTrace();
            return false;

        }
    }

    @Override
    public boolean load(String file)  {
        try{

            FileInputStream fi = new FileInputStream(file);
            ObjectInputStream oi = new ObjectInputStream(fi);
            graph = (weighted_graph) oi.readObject();
            oi.close();
            fi.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
            }
        }

    public double dijkstra(int src,int des)
    {
        WGraph_DS.NodeInfo n=((WGraph_DS.NodeInfo)graph.getNode(src)); //n= the starting vertex
        WGraph_DS.NodeInfo d=((WGraph_DS.NodeInfo)graph.getNode(des));//d= the destination vertex
        n.setTag(0); //the distance between vertex and itself is zero
        PriorityQueue<WGraph_DS.NodeInfo>queue=new PriorityQueue<>();
        // create priority queue of vertices, the priority is about the tag of every vertex
        queue.add(n);// adding the starting vertex
        while(!queue.isEmpty())//while there are no vertices to check in order to find the shortest path
        {
            WGraph_DS.NodeInfo current=queue.poll(); // get the minimum tag vertex , and remove it from the queue
            if(current.getKey()==d.getKey()) // if we through all the paths to the destination
                return current.getTag();//we have already scan all the paths and found the shortest
            for(int i: current.getNi())//pass the neighbors of current vertex
            {
                WGraph_DS.NodeInfo currentNi=((WGraph_DS.NodeInfo)graph.getNode(i)); //currentNi- the current neighbor of the current vertex

                if(!currentNi.isVisited())//if the neighbor hasn't visited yet
                {
                   boolean notExistInQueue=(currentNi.getTag()== Double.POSITIVE_INFINITY);//replace queue.contains
                        double t = current.getTag() + current.getWeight(i);
                        //t- the weight of the edge(current,currentNi) + the tag(total distance) of current
                        if (t < currentNi.getTag())//if we found another path to the neighbor with less total distance to it
                        {
                            currentNi.setTag(t);
                        }//update the tag to be t-the total distance of the last path

                    if(notExistInQueue)//if it's the first time to reach this neighbor
                        queue.add(currentNi);//add this neighbor to the queue

                }


            }

            current.setVisited(true);//we have done passing all the neighbors of the current vertex, so we are marking it.

        }
        return Double.POSITIVE_INFINITY;// if there's no path to the destination return the distance is infinity.

    }

    public boolean dijkstraPath(node_info src,node_info des)
    {
        WGraph_DS.NodeInfo n=((WGraph_DS.NodeInfo)src);//n= the starting vertex
        WGraph_DS.NodeInfo d=((WGraph_DS.NodeInfo)des);//d= the destination vertex
        n.setTag(0);//the distance between vertex and itself is zero
        PriorityQueue<WGraph_DS.NodeInfo>queue=new PriorityQueue<>();
        // create priority queue of vertices, the priority is about the tag of every vertex
        queue.add(n);// adding the starting vertex
        while(!queue.isEmpty())//while there are no vertices to check in order to find the shortest path
        {
            WGraph_DS.NodeInfo current=queue.poll();// get the minimum tag vertex , and remove it from the queue
            if(current==d) // if we through all the paths to the destination
                return true;//we have already scan all the paths and found the shortest so return true
            for(int i: current.getNi())//pass the neighbors of current vertex
            {
                WGraph_DS.NodeInfo currentNi=((WGraph_DS.NodeInfo)graph.getNode(i));//currentNi- the current neighbor of the current vertex
                if(!currentNi.isVisited())//if the neighbor hasn't visited yet
                {
                    boolean notExistInQueue=(currentNi.getTag()== Double.POSITIVE_INFINITY);//replace queue.contains
                    double t=current.getTag()+ current.getWeight(i);
                    //t- the weight of the edge(current,currentNi) + the tag(total distance) of current
                    if(t<currentNi.getTag())//if we found another path to the neighbor with less total distance to it
                    {currentNi.setTag(t);//update the tag to be t-the total distance of the last path
                        currentNi.setPre(current);//the "pre" of the neighbor will be a pointer on the current vertex that we came from it to the neighbor vertex
                    }
                    if(notExistInQueue)//if it's the first time to reach this neighbor
                        queue.add(currentNi);//add this neighbor to the queue


                }


            }

            current.setVisited(true);//we have done passing all the neighbors of the current vertex, so we are marking it.

        }
        return false;// if there's no path to the destination return false;

    }

    private void resetVisited()//reset the var "visited" and "tag" in every vertex at the graph in order for the next test will be normal
    {

        for (node_info nodes : graph.getV()) {
            ((WGraph_DS.NodeInfo)nodes).setVisited(false);


        }
    }
    private void resetVisitedAndTag()//reset the var "visited" and "tag" in every vertex at the graph in order for the next test will be normal
    {

        for (node_info nodes : graph.getV()) {
            ((WGraph_DS.NodeInfo)nodes).setVisited(false);
            nodes.setTag(Double.POSITIVE_INFINITY);

        }
    }

    private void resetVisitedAndTagAndPre()// //  reset the var "visited" and "pre" in every vertex in order for the next test will be normal
    {
        if (graph != null&&graph.getV()!=null) {
            for (node_info nodes : graph.getV()) {

                ((WGraph_DS.NodeInfo) (nodes)).setVisited(false);
                ((WGraph_DS.NodeInfo) (nodes)).setPre(null);
                nodes.setTag(Double.POSITIVE_INFINITY);

            }
        }
    }

    private boolean BFS() {
        //
        Collection<node_info> graphNodes = graph.getV();//get the list of the vertices in the graph
        if (graphNodes != null && graphNodes.size() != 0) {//if there are vertices
            node_info node = graphNodes.stream().findFirst().get();//find a starting vertex
            ((WGraph_DS.NodeInfo) (node)).setVisited(true);//mark this vertex as "visited"
            LinkedList<node_info> queue = new LinkedList<>();//create a queue of the vertices that we need to visit their neighbors
            queue.add(node);// add the starting vertex to the queue
            while (queue.size() != 0) {//while the queue isn't empty= there are neighbors that have not been visited yet
                node = queue.poll();//remove and get the next vertex from the queue
                LinkedList<Integer> nodes = ((WGraph_DS.NodeInfo) node).getNi();
                for (int keyNi : nodes) {//pass the neighbors list of this vertex
                    if (!nodes.isEmpty()) {
                        WGraph_DS.NodeInfo currentNi = ((WGraph_DS.NodeInfo) graph.getNode(keyNi));
                        if (!currentNi.isVisited()) {//if there's a neighbor and it isn't visited
                            currentNi.setVisited(true);//mark this neighbor as "visited"
                            queue.add(currentNi);//add to the queue the neighbor
                        }
                    }
                }
            }

            for (node_info nodes : graph.getV()) {//pass all the vertex in the graph
                if (!((WGraph_DS.NodeInfo) (nodes)).isVisited())//check if there's a vertex that we didn't visited
                    return false;//if there is vertex we didn't visit ->return false= the graph is disconnected
            }
        }

        return true;
    }


    }
