package oncall;

import camp.nextstep.edu.missionutils.Console;
import java.util.Arrays;
import java.util.HashSet;
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
        String weekdayStaff = repeatUntilSuccess(Application::readWeekdayStaff);
        // TODO:3. 휴일(토요일, 일요일, 공휴일) 비상 근무 순서 입력 기능
        String holidayStaff = repeatUntilSuccess(Application::readHolidayStaff);
        // TODO:4. 평일 + 공휴일의 경유에만 요일 뒤에 (휴일) 표기 하기
        // TODO:5. 근무 배정 기능

    }

    // TODO:1. 월과 시작요일 입력 기능
    private static String readMonthAndDay() {
        System.out.print("비상 근무를 배정할 월과 시작 요일을 입력하세요> ");
        String input = Console.readLine();
        validateMonthAndDay(input);
        return input;
    }

    private static void validateMonthAndDay(String input) {
        validateNotBlank(input);
        validateDelimiter(input, ",");
        List<String>temp= parseInput(input);

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

    // TODO:2. 평일 비상 근무 순서 입력 기능
    private static String readWeekdayStaff() {
        System.out.print("평일 비상 근무 순번대로 사원 닉네임을 입력하세요> ");
        String input = Console.readLine();
        validateWeekdayStaff(input);
        return input;
    }

    private static void validateWeekdayStaff(String input){
        validateNotBlank(input);
        validateDelimiter(input, ",");
        List<String>temp = parseInput(input);
        for (String temps : temp) {
            validateNicknameNumberOfCharacter(temps);
            validateBlankInElement(temps);
        }
        validateListSize(temp);
        validateDuplicate(temp);
    }

    // TODO:3. 휴일(토요일, 일요일, 공휴일) 비상 근무 순서 입력 기능
    private static String readHolidayStaff() {
        System.out.print("휴일 비상 근무 순번대로 사원 닉네임을 입력하세요> ");
        String input = Console.readLine();
        validateHolidayStaff(input);
        return input;
    }

    private static void validateHolidayStaff(String input){
        validateNotBlank(input);
        validateDelimiter(input, ",");
        List<String>temp = parseInput(input);
        for (String temps : temp) {
            validateNicknameNumberOfCharacter(temps);
            validateBlankInElement(temps);
        }
        validateListSize(temp);
        validateDuplicate(temp);
    }

    // TODO: 빈 값 및 공백 검증
    private static void validateNotBlank(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 입력값이 비어있거나 공백입니다.");
        }
    }

    // TODO: 구분자 및 리스트 개수 검증 (예: 쉼표가 없거나 하나도 안 나뉠 때)
    private static void validateDelimiter(String input, String delimiter) {
        if (!input.contains(delimiter)) {
            throw new IllegalArgumentException("[ERROR] 입력값은(" + delimiter + ")로 구분되어야 합니다.");
        }
    }

    // TODO: 숫자 + 한글 + 영어만 허용 (특수문자 및 공백 불가)
    private static void validateAlphanumericKorean(String input) {
        if (!input.matches("^[a-zA-Z0-9가-힣]+$")) {
            throw new IllegalArgumentException("[ERROR] 숫자, 한글, 영어만 입력 가능합니다.");
        }
    }

    // TODO: 닉네임 글자수 체크 로직
    private static void validateNicknameNumberOfCharacter(String element){
        if(element.length() > 5) {
            throw new IllegalArgumentException("[ERROR] 이름은 5자 이하만 가능합니다: " + element);
        }
    }

    // TODO: 리스트 요소에 공백이 포함되는지 검사
    private static void validateBlankInElement(String element){
        if (element.contains(" ")) {
            throw new IllegalArgumentException("[ERROR] 이름에 공백을 포함할 수 없습니다: " + element);
        }
    }

    // TODO: 리스트 사이즈 검사
    private static void validateListSize(List<String> list) {
        if (list.size() < 5 || list.size() > 35) {
            throw new IllegalArgumentException("[ERROR] 비상근무자는 5명에서 35명 사이어야 합니다.");
        }
    }

    // TODO: input 중복요소 검사
    private static void validateDuplicate(List<String> temp) {
        if (temp.size() != new HashSet<>(temp).size()) {
            throw new IllegalArgumentException("[ERROR] 중복된 닉네임이 존재합니다.");
        }
    }

    // TODO: 파싱 로직
    private static List<String> parseInput(String input) {
        return Arrays.stream(input.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toList();
    }

    // TODO: 재시도 로직
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
