package org.nestframework.commons.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeUtil
{
    private TreeNode rootNode;
    private Map<String, TreeNode> nodes = new HashMap<String, TreeNode>();
    
    
    public TreeUtil(String rootId, String rootTitle)
    {
        rootNode = new TreeNode(rootId, "", rootTitle);
        nodes.put(rootId, rootNode);
    }


    public TreeUtil addNode(TreeNode node) {
        TreeNode parentNode = nodes.get(node.getFatherId());
        if (parentNode != null) {
            node.setLevel(parentNode.getLevel() + 1);
            parentNode.getSubNodes().add(node);
            nodes.put(node.getId(), node);
        }
        return this;
    }
    
    public TreeNode getRootNode() {
        return rootNode;
    }
    
    public void getAllSubNodeList(List<TreeNode> list, TreeNode root) {
        for (TreeNode node: root.getSubNodes()) {
            list.add(node);
            if (node.getSubNodes().size() > 0) {
                getAllSubNodeList(list, node);
            }
        }
    }
    
    public List<TreeNode> getAllSubNodeList() {
        List<TreeNode> list = new ArrayList<TreeNode>();
        getAllSubNodeList(list, rootNode);
        return list;
    }
}
