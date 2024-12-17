package me.zhengjie.utils;
import java.util.Random;

/**
 * 昵称生成工具类
 */

public class NicknameUtil {
     /**
     * 生成昵称
     * 格式: 名字 (电话后4位或随机数)
     *
     * @param name 名字
     * @param contactInfo 联系方式
     * @return 生成的昵称
     */
    public static String generateNickname(String name, String contactInfo) {
        String phoneLastFour = "";

        if (contactInfo != null && contactInfo.length() >= 4 && contactInfo.matches("\\d+")) {
            phoneLastFour = contactInfo.substring(contactInfo.length() - 4);
        } else {
            Random rand = new Random();
            int randomNum = rand.nextInt(9000) + 1000;
            phoneLastFour = String.valueOf(randomNum);
        }

        return name + " (" + phoneLastFour + ")";
    }

}
