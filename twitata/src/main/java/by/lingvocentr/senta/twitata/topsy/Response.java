package by.lingvocentr.senta.twitata.topsy;

public class Response {

    private Number hidden;
    private Number last_offset;
    private java.util.List<List> list;
    private Number offset;
    private Number page;
    private Number perpage;
    private Number total;
    private String window;

    public Number getHidden() {
        return this.hidden;
    }

    public void setHidden(Number hidden) {
        this.hidden = hidden;
    }

    public Number getLast_offset() {
        return this.last_offset;
    }

    public void setLast_offset(Number last_offset) {
        this.last_offset = last_offset;
    }

    public java.util.List<List> getList() {
        return list;
    }

    public void setList(java.util.List<List> list) {
        this.list = list;
    }

    public Number getOffset() {
        return this.offset;
    }

    public void setOffset(Number offset) {
        this.offset = offset;
    }

    public Number getPage() {
        return this.page;
    }

    public void setPage(Number page) {
        this.page = page;
    }

    public Number getPerpage() {
        return this.perpage;
    }

    public void setPerpage(Number perpage) {
        this.perpage = perpage;
    }

    public Number getTotal() {
        return this.total;
    }

    public void setTotal(Number total) {
        this.total = total;
    }

    public String getWindow() {
        return this.window;
    }

    public void setWindow(String window) {
        this.window = window;
    }
}
