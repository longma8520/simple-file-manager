package cn.tyson.simplefilemanager.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class PagerResult<T> {
    private List<T> items;
    private long total;

    public PagerResult(List<T> items, long total) {
        this.items = items;
        this.total = total;
    }
}
