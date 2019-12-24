package nl.iprwc.Utils;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

public class Paginated<T extends Collection> {
    private T response;
    private long pageSize;
    private long page;
    private long total;

    public Paginated(long pageSize, long page, long total)
    {
        this(pageSize, page, total, null);
    }

    public Paginated(long pageSize, long page, long total, T response)
    {
        this.pageSize = pageSize;
        this.page = page;
        this.total = total;
        this.response = response;
    }

    @JsonProperty
    public T getData() {
        return response;
    }

    @JsonProperty
    public long getTotalCount()
    {
        return total;
    }

    @JsonProperty
    public long getResultCount()
    {
        return response.size();
    }

    @JsonProperty
    public long getPageSize()
    {
        return pageSize;
    }

    @JsonProperty
    public long getPage()
    {
        return page;
    }

    @JsonProperty
    public long getOffset()
    {
        return pageSize * (page - 1);
    }

    @JsonProperty
    public long getPageCount()
    {
        return (long) Math.ceil(total * 1.0 / pageSize);
    }
}
