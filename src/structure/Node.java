package src.structure;

import src.parser.Parser;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Node.
 * @author Hannes Schniz
 * @version 1.0
 */
public class Node {

    private final IP tag;
    private int gradiant;
    private boolean isRoot;
    private List<Node> connections;
    private Node upperNode;
    private int layer;

    /**
     * Instantiates a new Node.
     *
     * @param tag         the tag
     * @param gradiant    the gradiant
     * @param isRoot      the is root
     * @param connections the connections
     * @param upperNode   the upper node
     */
    public Node(IP tag, int gradiant, boolean isRoot, List<Node> connections, Node upperNode) {
        this.tag = tag;
        this.gradiant = gradiant;
        this.isRoot = isRoot;
        this.connections = connections;
        this.upperNode = upperNode;
        if (this.upperNode == null) {
            this.layer = 0;
        }
        else {
            this.layer = upperNode.getLayer() + 1;
        }
    }

    /**
     * Instantiates a new Node.
     *
     * @param tag the tag
     */
    public Node(IP tag) {
        this.tag = tag;
        this.gradiant = 0;
        this.isRoot = false;
        this.connections = new ArrayList<>();
        this.upperNode = null;
        this.layer = 0;
    }

    /**
     * Gets tag.
     *
     * @return the tag
     */
    public IP getTag() {
        return tag;
    }

    /**
     * Gets gradiant.
     *
     * @return the gradiant
     */
    public int getGradiant() {
        return gradiant;
    }

    /**
     * Sets gradiant.
     *
     * @param gradiant the gradiant
     */
    public void setGradiant(int gradiant) {
        this.gradiant = gradiant;
    }

    /**
     * Is root boolean.
     *
     * @return the boolean
     */
    public boolean isRoot() {
        return isRoot;
    }

    /**
     * Sets root.
     *
     * @param root the root
     */
    public void setRoot(boolean root) {
        isRoot = root;
    }

    /**
     * Gets connections.
     *
     * @return the connections
     */
    public List<Node> getConnections() {
        return connections;
    }

    /**
     * Sets connections.
     *
     * @param connections the connections
     */
    public void setConnections(List<Node> connections) {
        this.connections = connections;
    }

    /**
     * Init new connections.
     *
     * @param connections the connections
     */
    public void initNewConnections(List<IP> connections) {
        for (IP connection : connections) {
            Node node = new Node(connection, 1, false, new ArrayList<>(), this);
            node.setLayer(this.layer + 1);
            this.connections.add(node);
        }
        this.setGradiant(connections.size());
    }

    public void addConnection(Node connection) {
        if (this.connections == null) {
            this.connections = new ArrayList<>();
        }
        this.setGradiant(this.gradiant + 1);
        this.connections.add(connection);
    }

    public void addAllConnections( List<Node> connections) {
        if (connections == null) {
            return;
        }
        for (Node node: connections) {
            boolean notFound = true;
            for (Node connection: this.connections) {
                if (node.getTag().compareTo(connection.getTag()) == 0) {
                    notFound = false;
                }
            }
            if (notFound) {
                this.connections.add(node);
            }
        }
    }

    /**
     * Gets upper node.
     *
     * @return the upper node
     */
    public Node getUpperNode() {
        return upperNode;
    }

    /**
     * Sets upper node.
     *
     * @param upperNode the upper node
     */
    public void setUpperNode(Node upperNode) {
        this.upperNode = upperNode; //need to check if there was a upper node before for gradiant
    }

    /**
     * Sets all upper nodes.
     *
     * @param upperNode the upper node
     */
    public void setAllUpperNodes(Node upperNode) {
        for (Node connection: connections) {
            connection.setUpperNode(upperNode);
        }
    }

    /**
     * Gets layer.
     *
     * @return the layer
     */
    public int getLayer() {
        return layer;
    }

    public List<String> getLayerNodes() {
        List<String> output = new ArrayList<>();
        for (IP ip: getLayerIps()) {
            output.add(Parser.parseToString(ip.getAddress()));
        }
        return output;
    }

    public void removeConnection(IP toRemove) {
        for (Node connection: this.getConnections()) {
            if (connection.getTag().equals(toRemove)) {
                this.connections.remove(connection);
                return;
            }
        }
    }

    public List<IP> getLayerIps() {
        List<IP> layer = new ArrayList<>();
        if (this.connections != null) {
            layer.add(this.tag);
            for (Node connection: this.connections) {
                layer.add(connection.getTag());
            }
            return layer;
        }
        return null;
    }

    /**
     * Sets layer.
     *
     * @param layer the layer
     */
    public void setLayer(int layer) {
        this.layer = layer;
    }

    public int hasConnection(IP tag) {
        for (int i = 0; i < this.connections.size(); i++) {
            if (this.connections.get(i).getTag().compareTo(tag) == 0) {
                return i;
            }
        }
        return -1;
    }

    public void correctLayers() {
        if (this.upperNode != null) {
            this.layer = upperNode.getLayer() + 1;
        }
        else {
            this.layer = 0;
        }
        for (Node node: this.connections) {
            node.setLayer(this.layer + 1);
        }
    }

    public void correctGradiant() {
        int grad = this.connections.size();
        if (this.upperNode != null) {
            grad++;
        }
        setGradiant(grad);
    }
}
