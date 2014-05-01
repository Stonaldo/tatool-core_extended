/*******************************************************************************
 * Copyright (c) 2011 Michael Ruflin, André Locher, Claudia von Bastian.
 * 
 * This file is part of Tatool.
 * 
 * Tatool is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published 
 * by the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 * 
 * Tatool is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Tatool. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package ch.tatool.core.element;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanNameAware;

import ch.tatool.element.Element;
import ch.tatool.element.Node;


/**
 * Default node implementation. Provides helper methods to assign a parent as well
 * as support for id chaining where the id is "{parentId}.{localId}"
 * 
 * @author Michael Ruflin
 */
public class NodeImpl extends AbstractPropertyHolder implements Node, BeanNameAware {
	
	private Logger logger = LoggerFactory.getLogger(NodeImpl.class);

    /** Node localId. */
    private String localId;
    
    /** Parent node. */
    private Node parent;
    
    public NodeImpl() {
    	this("node");
    }
    
    public NodeImpl(String defaultId) {
        setLocalId(defaultId + "-" + (1000 + (new Random().nextInt(8999))));
    }
    
    /**
     * Set the the id of this node to be the id attribute.
     * Don't set the id to an automatically generated id.
     * Rename the id "moduleHierarch" to "module".
     */
	public void setBeanName(String name) {
		if (name != null && name.contains("#")) {
			logger.warn("The following bean has an automatically generated id: {}", name);
		} else if (name.contains("moduleHierarchy")) {
			setId("module");
			logger.info("Bean with id moduleHierarchy will not be replaced");
		} else {
			setId(name);
		}
	}
 
    public void setId(String id) {
        this.localId = id;
    }
    
    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String id) {
        this.localId = id;
    }

    /**
     * Unique id of the element. This includes the localId of the parent elements as well
     * and has to be unique in a element/handler tree
     */
    public String getId() {
        LinkedList<Node> nodes = new LinkedList<Node>();
        Node u = this;
        while (u != null) {
            nodes.addFirst(u);
            u = u.getParent();
        }
        StringBuilder builder = new StringBuilder();
        Iterator<Node> it = nodes.iterator();
        
        // add first element (we know we have at least one!)
        NodeImpl n = (NodeImpl) it.next();
        builder.append(n.getLocalId());
        while (it.hasNext()) {
        	n = (NodeImpl) it.next();
            builder.append('.');
            builder.append(n.getLocalId());
        }
        return builder.toString();
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }
    
    /**
     * Sets the parent to the provided object if the object is type Node
     */
    protected static void assignParent(Node parent, Object object) {
    	if (object instanceof Node) {
    		((Node) object).setParent(parent);
    	}
    }
    
    /**
     * Sets the parent to the provided objects if they are of type Node
     */
    protected static void assignParent(Node parent, Collection<Object> objects) {
    	for (Object object : objects) {
    		assignParent(parent, object);
    	}
    }
    
    /**
     * Sets the parent to the provided objects if they are of type Node
     */
    //TODO: ugly but does the trick for now for ListElement
    protected static void assignParents(Node parent, Collection<Element> objects) {
    	for (Object object : objects) {
    		assignParent(parent, object);
    	}
    }
    
}
