package com.garfield.weishu.contact.model;


import com.garfield.weishu.contact.item.AbsContactItem;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * 基类，根据特殊字符和26个字母进行分组
 * 只有简单的分组名
 */
public class ContactGroupStrategy implements Comparator<String> {
    public static final String GROUP_SHARP = "#";

    public static final String GROUP_TEAM = "@";

    public static final String GROUP_NULL = "?";

    private static final class Group {
        private final int order;

        // 联系人界面就是26个字符、搜索界面是好友+群组+聊天记录
        private final String name;

        public Group(int order, String name) {
            this.order = order;
            this.name = name;
        }
    }

    private final Map<String, Group> groups = new HashMap<String, Group>();

    public String belongs(AbsContactItem item) {
        return item.belongsGroup();
    }

    /**
     * 特殊分组，id是除26个字母以外的分组名"#", "@", "?"
     */
    protected final void add(String id, int order, String name) {
        groups.put(id, new Group(order, name));
    }

    /**
     * 字母分组，order是字母A的起始位置
     */
    protected final int addABC(int order) {
        String id = ContactGroupStrategy.GROUP_SHARP;

        add(id, order++, id);

        for (char i = 0; i < 26; i++) {
            id = Character.toString((char) ('A' + i));

            add(id, order++, id);
        }

        return order;
    }

    public final String getName(String id) {
        Group group = groups.get(id);
        String name = group != null ? group.name : null;
        return name != null ? name : "";
    }

    // 传入id
    @Override
    public int compare(String lhs, String rhs) {
        if (lhs == null) {
            lhs = ContactGroupStrategy.GROUP_NULL;
        }

        if (rhs == null) {
            rhs = ContactGroupStrategy.GROUP_NULL;
        }

        Integer lhsO = toOrder(lhs);
        Integer rhsO = toOrder(rhs);

        if (lhsO == rhsO) {
            return 0;
        }

        if (lhsO == null) {
            return -1;
        }

        if (rhsO == null) {
            return 1;
        }

        return lhsO - rhsO;
    }

    private Integer toOrder(String id) {
        Group group = groups.get(id);
        return group != null ? group.order : null;
    }
}
