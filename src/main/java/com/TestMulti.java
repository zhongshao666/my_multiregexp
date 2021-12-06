package com;

import com.hylanda.lightgrep.*;

public class TestMulti {
    public static void main(String[] args) throws Exception {
        MultiPattern patterns = new MultiPattern();
        patterns.addPattern("敏感词", 0);
        patterns.addPattern("红牛", 0);
        patterns.addPattern("可乐雪碧", 0);
        patterns.addPattern("匹配", 0);

        Automation automation = patterns.toAutomation();

        Matcher matcher = automation.createMatcher();
        final GrepString r = new GrepString("今天，我们需要尝试一下敏感词的匹配，其中红牛和可乐雪碧都是我们要匹配的目标");
        matcher.match(r,  new HitCallback() {
            @Override
            public void match(HitItem item) {
                System.out.println(item.getId() + " " + r.subSequence(item.getStart(), item.getEnd()));
            }
        });
        r.close();
        patterns.close();

        matcher.close();

        automation.close();
    }
}
