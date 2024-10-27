package me.zhengjie.utils;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
public class DateQuery {
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 将日期字符串列表解析为 Timestamp 列表
     *
     * @param dateStrs 日期字符串列表
     * @return Timestamp 列表
     */
    public static List<Timestamp> parseTimestamps(List<String> dateStrs) {
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        List<Timestamp> timestamps = new ArrayList<>();

        for (String dateStr : dateStrs) {
            try {
                timestamps.add(new Timestamp(sdf.parse(dateStr).getTime()));
            } catch (ParseException e) {
                throw new IllegalArgumentException("日期格式不正确: " + dateStr);
            }
        }

        return timestamps;
    }
}
