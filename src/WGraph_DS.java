package ex1.src;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;


public class WGraph_DS implements weighted_graph, Serializable {

    private HashMap<Integer, node_info> colNode;
    private int mc;

    private int edgesCounter;



    public WGraph_DS()
    {
        colNode=new HashMap<>();
    }

    public WGraph_DS(weighted_graph g) //copy constructor
    {
        if(g!=null&&g.getV()!=null) {
            colNode = new HashMap<>();
            for (node_info nodes : g.getV()) {
                colNode.put(nodes.getKey(), new NodeInfo(nodes));
            }//copy every vertex from vertices list of g
            edgesCounter = g.edgeSize();

        }
    }
    @Override
    public node_info getNode(int key) {
        if(colNode.containsKey(key))
            return colNode.get(key);
        return null;
    }


    @Override
    public boolean hasEdge(int node1, int node2) {
        return (colNode.containsKey(node1)&& colNode.containsKey(node2)
        &&((NodeInfo)colNode.get(node1)).hasNi(node2)&&((NodeInfo)colNode.get(node2)).hasNi(node1));
        //return true if node1 and node2 are exist in the graph and if they are neighbors
    }

    @Override
    public double getEdge(int node1, int node2) {
        if (colNode!=null&&hasEdge(node1,node2)) {
            node_info n1 = colNode.get(node1);

            return ((NodeInfo)n1).getWeight(node2);
        }
        return -1;
    }

    @Override
    public void addNode(int key) {
        if(!colNode.containsKey(key))
        {
            colNode.put(key,new NodeInfo(key));
            mc++;}

    }

    @Override
    public void connect(int node1, int node2, double w) {
        if(colNode.containsKey(node1)&&colNode.containsKey(node2)&&node1!=node2){
            node_info n1=colNode.get(node1);
            node_info n2=colNode.get(node2);
        if(!hasEdge(node1,node2))
        {


                ((NodeInfo) n1).addNi(node2, w);//add node2 to the neighbors of node1
                ((NodeInfo) n2).addNi(node1, w);//add node1 to the neighbors of node2
                edgesCounter++;

        }
        else {
            ((NodeInfo) n1).setWeight(node2, w);
            ((NodeInfo)n2).setWeight(node1,w);
        }
            mc++;

        }
    }

    @Override
    public Collection<node_info> getV() {
        if(colNode!=null)
        return colNode.values();
        return null;
    }

    @Override
    public Collection<node_info> getV(int node_id) {
        if(colNode.containsKey(node_id))
        {
            LinkedList<node_info> niObjects= new LinkedList<>();
            for(int currentKey: ((NodeInfo)(colNode.get(node_id))).nodeNi.keySet())
            {
                niObjects.add(colNode.get(currentKey));
            }
            return niObjects;
        }
        return null;
    }

    @Override
    public node_info removeNode(int key) {
        if(colNode.containsKey(key))//if the node is in the graph
        {
            node_info n=colNode.get(key);
            for(int currentKey: ((NodeInfo)n).nodeNi.keySet())
            {
                ((NodeInfo)colNode.get(currentKey)).nodeNi.remove(key);//for every neighbor, remove the given node from their neighbors list.
                edgesCounter--;//the nodes aren't neighbors anymore= there's no edge between them-> sub 1 from the number of the edges
                mc++;
            }
            // n.getNi().clear();// remove all the neighbors from the neighbors list of the given node(neighborhood is mutual)
            colNode.remove(key);//finally we remove the given node from the graph.
            mc++;
            return n;}

        return null;// if the node isn't exist, return null
    }

    @Override
    public void removeEdge(int node1, int node2) {
        if (colNode!=null&&colNode.containsKey(node1)&&colNode.containsKey(node2)) {//if the nodes are exist in the graph
            node_info n1 = colNode.get(node1);
            node_info n2 = colNode.get(node2);
            if(hasEdge(node1,node2)) {//if there's edge between the nodes
                ((NodeInfo)n1).nodeNi.remove(node2);//remove node2 from neighbors list of node1
                ((NodeInfo)n2).nodeNi.remove(node1);//remove node1 from neighbors list of node2
                edgesCounter--;// sub 1 from the numbers of edges
                mc++;
            }

        }
    }

    @Override
    public int nodeSize() {
        return colNode.size();
    }

    @Override
    public int edgeSize() {
        return edgesCounter;
    }

    @Override
    public int getMC() {
        return mc;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof WGraph_DS))
            return false;
        if (obj != null) {
            WGraph_DS wg = ((WGraph_DS) obj);
            if (wg.colNode.size() == colNode.size() && wg.edgesCounter == edgesCounter)
            {
                for( int i: colNode.keySet()) {
                    if (!(wg.colNode.containsKey(i)))
                        return false;
                    else {
                        NodeInfo thisNode= (NodeInfo) getNode(i);
                        NodeInfo wgNode= (NodeInfo) getNode(i);
                        for (int j : thisNode.nodeNi.keySet())
                            if(!(wgNode.nodeNi.containsKey(j)))
                                return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public class NodeInfo implements node_info,Comparable<node_info>,Serializable{

        private int key;
        private String info;
        private double tag= Double.POSITIVE_INFINITY; //for dijkstra algorithm,
        //the distance of every vertex from a given vertex
        private HashMap<Integer,Double> nodeNi;//all the vertices that connected to this vertex
        private boolean visited=false; //for BFS & dijkstra algorithm
        private node_info pre=null; //for the shortestPath function,
        // this var save the previous node of this node in the shortest path between 2 nodes.




        protected node_info getPre()
        {return  pre;}
        protected void setPre(node_info n)
        {this.pre=n;}
        protected boolean isVisited()
        {
            return visited;
        }
        protected void setVisited(boolean b)
        {
            this.visited=b;
        }



        public NodeInfo(int key)
        {
            this.key=key;
            nodeNi= new HashMap<>();

        }

        public NodeInfo(node_info n)
        {
            this.key=n.getKey();
            this.info=n.getInfo();
            nodeNi=new HashMap<>();
            for(int i: ((NodeInfo)n).nodeNi.keySet())
            {
                nodeNi.put(i,((NodeInfo)n).nodeNi.get(i));
            }
        }
        public LinkedList<Integer> getNi() { //get all the nodes are connected to this node
            LinkedList<Integer>l=new LinkedList<>();
            for(int currentKey: nodeNi.keySet())
            {
                l.add(currentKey);//for every neighbor, remove the given node from their neighbors list.

            }
            return l;

        }
        public void addNi(int kNode,double weight) // add node to neighbors list of this node and the weight of the edge between them, by key node
        {
            if(nodeNi!=null &&!nodeNi.containsKey(kNode))
                nodeNi.put(kNode,weight);
        }

        public boolean hasNi(int key)// check if there's node between this node and a given node(by key)
        {
            return nodeNi.containsKey(key);
        }

        public double getWeight(int kNode) //get the weight of the edge between this node and kNode
        {
           return nodeNi.get(kNode);
        }
        public void setWeight(int kNode,double weight) //get the weight of the edge between this node and kNode
        {
            nodeNi.replace(kNode,getWeight(kNode),weight);
        }
        @Override
        public int getKey() {
            return key;
        }

        @Override
        public String getInfo() {
            return info;
        }

        @Override
        public void setInfo(String s) {
            this.info=s;
        }

        @Override
        public double getTag() {
            return tag;
        }

        @Override
        public void setTag(double t) {
            this.tag=t;
        }
        public String toString()
        {
            return ""+key;
        }


        @Override
        public int compareTo(node_info o) { //for priority queue in dijkstra algorithm,
            //compare between 2 nodes by their tag. the maximal tag node is the bigger one.
            int ans=0;
            if(this.tag-o.getTag()>0)
                ans=1;
            else if (this.tag-o.getTag()<0)
                ans=-1;
            return ans;
        }
    }
}
