package com.yiyan.boot.common.utils.fakevalue;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.yiyan.boot.common.utils.fakevalue.model.enums.RandomTypeEnum;
import net.datafaker.Faker;

import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Fake 数据工具类
 *
 * @author Sparkler
 * @createDate 2022 /12/5
 */
public class FakerUtils {

    private static final Faker ZH_FAKER = new Faker(new Locale("zh-CN"));

    private static final Faker EN_FAKER = new Faker();

    private final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取随机值
     *
     * @param randomTypeEnum the random type enum
     * @return random value
     */
    public static String getRandomValue(RandomTypeEnum randomTypeEnum) {
        String defaultValue = RandomUtil.randomString(RandomUtil.randomInt(2, 6));
        return generateRandomValue(randomTypeEnum, defaultValue);
    }

    public static String getRandomValue(RandomTypeEnum randomTypeEnum, String defaultValue) {
        return generateRandomValue(randomTypeEnum, defaultValue);
    }

    private static String generateRandomValue(RandomTypeEnum randomTypeEnum, String defaultValue) {
        if (randomTypeEnum == null) {
            return defaultValue;
        }
        switch (randomTypeEnum) {
            case NAME:
                return ZH_FAKER.name().name();
            case CITY:
                return ZH_FAKER.address().city();
            case EMAIL:
                return EN_FAKER.internet().emailAddress();
            case URL:
                return EN_FAKER.internet().url();
            case IP:
                return ZH_FAKER.internet().ipV4Address();
            case INTEGER:
                return String.valueOf(ZH_FAKER.number().randomNumber());
            case DECIMAL:
                return String.valueOf(RandomUtil.randomDouble(0, 10000, 4, RoundingMode.HALF_UP));
            case UNIVERSITY:
                return ZH_FAKER.university().name();
            case DATE:
                return EN_FAKER.date()
                        .between(Timestamp.valueOf("2022-01-01 00:00:00"), Timestamp.valueOf("2023-01-01 00:00:00"))
                        .toLocalDateTime().format(DATE_TIME_FORMATTER);
            case TIMESTAMP:
                return String.valueOf(EN_FAKER.date()
                        .between(Timestamp.valueOf("2022-01-01 00:00:00"), Timestamp.valueOf("2023-01-01 00:00:00"))
                        .getTime());
            case PHONE:
                return ZH_FAKER.phoneNumber().cellPhone();
            case ADDRESS:
                return ZH_FAKER.address().fullAddress();
            case ID:
                return IdUtil.getSnowflake().nextIdStr();
            case UUID:
                return IdUtil.fastSimpleUUID();
            case AGE:
                return String.valueOf(RandomUtil.randomInt(0, 120));
            case BIRTHDAY:
                return EN_FAKER.date().birthday(0, 120).toString();
            case FOOD:
                return food();
            case JOB:
                return ZH_FAKER.job().title();
            case COLOR:
                return ZH_FAKER.color().name();
            case COLOR_HEX:
                return EN_FAKER.color().hex();
            case ID_CARD:
                return ZH_FAKER.idNumber().validZhCNSsn();
            case BARCODE:
                return String.valueOf(ZH_FAKER.barcode().gtin13());
            case MEDICINE:
                return ZH_FAKER.medical().medicineName();
            case DISEASE:
                return ZH_FAKER.medical().diseaseName();
            case HOSPITAL:
                ZH_FAKER.medical().symptoms();
                return ZH_FAKER.medical().hospitalName();
            default:
                return defaultValue;
        }
    }

    private static String food() {
        switch (RandomUtil.randomInt(1, 5)) {
            case 1:
                return ZH_FAKER.food().fruit();
            case 2:
                return ZH_FAKER.food().ingredient();
            case 3:
                return ZH_FAKER.food().spice();
            case 4:
                return ZH_FAKER.food().sushi();
            case 5:
                return ZH_FAKER.food().vegetable();
            default:
                return ZH_FAKER.food().dish();
        }
    }
}
