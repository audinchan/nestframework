package org.nestframework.commons.hibernate;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPage<E> implements IPage<E> {
	protected List<E> pageElements = new ArrayList<E>();

	protected int pageSize = 10;

	protected int totalCount = 0;

	protected int currPageNumber = 0;

	protected int startPage = 1;

	public AbstractPage(int pageNumber, int pageSize) {
		this.pageSize = pageSize;
		this.currPageNumber = pageNumber;
	}

	protected void computePage() {
		if (pageSize < 1) {
			pageSize = 1;
		}
        int lastPageNumber = getLastPageNumber();
		if (currPageNumber > lastPageNumber) {
			currPageNumber = lastPageNumber;
		}
		if (currPageNumber < startPage) {
			currPageNumber = startPage;
		}
	}

	public int getFirstPageNumber() {
		return startPage;
	}

	public int getLastPageNumber() {
		double lastPage = (double) totalCount / pageSize;
		return (int) Math.ceil(lastPage);
	}

	public int getNextPageNumber() {
		return hasNextPage() ? currPageNumber + 1 : currPageNumber;
	}

	public List<E> getPageElements() {
		return hasNextPage() ? pageElements.subList(0, pageSize) : pageElements;
	}

	public int getPreviousPageNumber() {
		return hasPreviousPage() ? currPageNumber - 1 : currPageNumber;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public boolean hasNextPage() {
		return pageElements.size() > pageSize;
	}

	public boolean hasPreviousPage() {
		return currPageNumber > startPage;
	}

	public boolean isFirstPage() {
		return currPageNumber == startPage;
	}

	public boolean isLastPage() {
		return currPageNumber >= getLastPageNumber();
	}

	public int getCurrPageNumber() {
		return (getLastPageNumber() == 0) ? 0 : currPageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

    @Override
    public String toString()
    {
        return new StringBuffer()
            .append("Page Object.")
            .append("\nTotal count: ").append(getTotalCount())
            .append("\nTotal page: ").append(getLastPageNumber())
            .append("\nCurrent page: ").append(getCurrPageNumber())
            .append("\nPage Size: ").append(getPageSize())
            .append("\nList size: ").append(getPageElements().size())
            .toString();
    }

}
