package johar.idf;

import java.util.ArrayList;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class IdfNodeList implements NodeList {
    private ArrayList<Node> _theNodeList;

    public IdfNodeList() {
	_theNodeList = new ArrayList<Node>();
    }

    // For NodeList
    public int getLength() {
	return _theNodeList.size();
    }

    // For NodeList
    public Node item(int i) {
	return _theNodeList.get(i);
    }

    public void add(Node n) {
	_theNodeList.add(n);
    }

}
