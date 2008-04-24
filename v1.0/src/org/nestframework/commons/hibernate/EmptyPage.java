package org.nestframework.commons.hibernate;

/**
 * An empty page implementation.
 * @author audin
 */
public class EmptyPage<E> extends AbstractPage<E> {
    
    public EmptyPage(int pageSize) {
        super(0, pageSize);
        totalCount = 0;
        computePage();
    }
}
