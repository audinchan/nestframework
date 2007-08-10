package org.nestframework.commons.hibernate;

import java.util.List;

public interface IPage<E>
{
    public boolean isFirstPage();
    public boolean isLastPage();
    public boolean hasNextPage();
    public boolean hasPreviousPage();
    public List<E> getPageElements();
    public int getTotalCount();
    public int getFirstPageNumber();
    public int getLastPageNumber();
    public int getNextPageNumber();
    public int getPreviousPageNumber();
    public int getCurrPageNumber();
    
    public int getPageSize();
}
