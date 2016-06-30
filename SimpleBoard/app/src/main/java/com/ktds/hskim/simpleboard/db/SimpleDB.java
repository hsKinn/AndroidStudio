package com.ktds.hskim.simpleboard.db;

import com.ktds.hskim.simpleboard.vo.ArticleVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by 206-006 on 2016-06-08.
 */
public class SimpleDB {

    private static Map<String, ArticleVO> db = new HashMap<String, ArticleVO>();

    public static void addArticle(String index, ArticleVO articleVO) {
        db.put(index, articleVO);
    }

    public static ArticleVO getArticle(String index) {
        return db.get(index);
    }

    public static List<String> getIndexes() {
        Iterator<String> keys = db.keySet().iterator();

        List<String> keyList = new ArrayList<String>();
        String key = "";
        while ( keys.hasNext() ) {
            key = keys.next();
            keyList.add(key);
        }

        return keyList;
    }
}



