package com.github.hairless.plink.sql.model;

import lombok.Data;

import java.util.List;

/**
 * @author: silence
 * @date: 2020/7/17
 */
@Data
public class SqlParseResult {

    private List<Node> nodeList;

    private List<Link> linkList;

    @Data
    public static class Node {
        private String name;
        private String type;
        private List<Column> columnList;
        private String sql;
    }

    @Data
    public static class Link {
        private String source;
        private String target;
    }
}

