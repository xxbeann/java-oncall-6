package oncall;

import camp.nextstep.edu.missionutils.Console;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class Application {
    private static final List<String> MONTHS = List.of(
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"
    );
    private static final List<String> DAYS_OF_WEEK = List.of(
            "월", "화", "수", "목", "금", "토", "일"
    );

    public static void main(String[] args) {
        // TODO: 법정 공유일 1월 1일, 3월 1일, 5월 1일, 6월 6일, 8월 15일, 10월 3일, 10월 9일, 12월 25일
        // TODO:1. 월과 시작요일 입력 기능
        String monthAndDay = repeatUntilSuccess(Application::readMonthAndDay);
        // TODO:2. 평일 비상 근무 순서 입력 기능
        // TODO:3. 휴일(토요일, 일요일, 공휴일) 비상 근무 순서 입력 기능
        // TODO:4. 평일 + 공휴일의 경유에만 요일 뒤에 (휴일) 표기 하기
        // TODO:5. 근무 배정 기능

    }

    private static String readMonthAndDay() {
        System.out.println("비상 근무를 배정할 월과 시작 요일을 입력하세요> ");
        String input = Console.readLine();
        validateMonthAndDay(input);
        return input;
    }

    private static void validateMonthAndDay(String input) {
        validateNotBlank(input);
        validateDelimiter(input, ",");
        List<String> temp = Arrays.stream(input.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toList();

        if (temp.size() != 2) {
            throw new IllegalArgumentException("[ERROR] 월과 시작 요일을 쉼표(,)로 구분하여 입력하세요.");
        }

        String month = temp.get(0);
        String day = temp.get(1);

        if (!MONTHS.contains(month)) {
            throw new IllegalArgumentException("[ERROR] 유효하지 않은 월입니다: " + month);
        }
        if (!DAYS_OF_WEEK.contains(day)) {
            throw new IllegalArgumentException("[ERROR] 유효하지 않은 요일입니다: " + day);
        }
        validateAlphanumericKorean(month);
        validateAlphanumericKorean(day);
    }

    // 빈 값 및 공백 검증
    private static void validateNotBlank(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 입력값이 비어있거나 공백입니다.");
        }
    }

    // 구분자 및 리스트 개수 검증 (예: 쉼표가 없거나 하나도 안 나뉠 때)
    private static void validateDelimiter(String input, String delimiter) {
        if (!input.contains(delimiter)) {
            throw new IllegalArgumentException("[ERROR] 입력값은(" + delimiter + ")로 구분되어야 합니다.");
        }
    }

    //  숫자 + 한글 + 영어만 허용 (특수문자 및 공백 불가)
    private static void validateAlphanumericKorean(String input) {
        if (!input.matches("^[a-zA-Z0-9가-힣]+$")) {
            throw new IllegalArgumentException("[ERROR] 숫자, 한글, 영어만 입력 가능합니다.");
        }
    }

    private static <T> T repeatUntilSuccess(Supplier<T> supplier) {
        while (true) {
            try {
                return supplier.get();
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
