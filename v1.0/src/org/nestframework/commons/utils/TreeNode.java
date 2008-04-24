package org.nestframework.commons.utils;

import java.util.ArrayList;
import java.util.List;

public class TreeNode
{
    /**
     * Node id.
     */
    private String id;
    
    /**
     * Parent node id.
     */
    private String fatherId;
    
    /**
     * Node title.
     */
    private String title;
    
    /**
     * Node level.
     */
    private int level;
    
    /**
     * Son nodes.
     */
    private List<TreeNode> subNodes = new ArrayList<TreeNode>();

    public TreeNode()
    {
    }

    public TreeNode(String id, String fatherId, String title)
    {
        this.id = id;
        this.fatherId = fatherId;
        this.title = title;
    }

    public String getFatherId()
    {
        return fatherId;
    }

    public void setFatherId(String fatherId)
    {
        this.fatherId = fatherId;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public int getLevel()
    {
        return level;
    }

    public void setLevel(int level)
    {
        this.level = level;
    }

    public List<TreeNode> getSubNodes()
    {
        return subNodes;
    }

    public void setSubNodes(List<TreeNode> subNodes)
    {
        this.subNodes = subNodes;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }
}
