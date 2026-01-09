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
    private static final List<String> LEGAL_HOLIDAY = List.of(
            "1-1", "3-1", "5-5", "6-6", "8-15", "10-3", "10-9", "12-25"
    );

    public static void main(String[] args) {
        // TODO: 법정 공유일 1월 1일, 3월 1일, 5월 1일, 6월 6일, 8월 15일, 10월 3일, 10월 9일, 12월 25일
        // TODO:1. 월과 시작요일 입력 기능
        List<String> monthAndDay = repeatUntilSuccess(Application::readMonthAndDay);
        String month = monthAndDay.get(0);
        String day = monthAndDay.get(1);
        System.out.println("[Debug]입력받은 월: " + month);
        System.out.println("[Debug]입력받은 날: " + day);
        System.out.println("[Debug]day index: " + DAYS_OF_WEEK.indexOf(day));
        // TODO:2. 비상근무 사원 입력
        List<List<String>> emergencyStaff = repeatUntilSuccess(Application::readEmergencyStaff);
        List<String> weekdayStaff = emergencyStaff.get(0);
        List<String> holidayStaff = emergencyStaff.get(1);
        System.out.println("[Debug]평일 비상근무: " + weekdayStaff);
        System.out.println("[Debug]평일 비상근무: " + holidayStaff);
        // TODO:5. 근무 배정 기능
        staffAssign(month, day, weekdayStaff, holidayStaff);
        // TODO:4. 평일 + 공휴일의 경유에만 요일 뒤에 (휴일) 표기 하기
    }

    // TODO:5. 근무 배정 기능
    private static void staffAssign(String month, String day, List<String> weekdayStaff, List<String> holidayStaff) {
        //TODO: 법정 공휴일 1월 1일, 3월 1일, 5월 5일, 6월 6일, 8월 15일, 10월 3일, 10월 9일, 12월 25일
        int dayOfWeekIndex = DAYS_OF_WEEK.indexOf(day);

        if (month.equals("1") || month.equals("3") || month.equals("5") || month.equals("7") || month.equals("8")
                || month.equals("10") || month.equals("12")) {
            for (int i = 1; i <= 31; i++) {
                String currentDayOfWeek = DAYS_OF_WEEK.get((dayOfWeekIndex + i - 1) % 7);
                if (LEGAL_HOLIDAY.contains(month+"-"+i)){
                    System.out.println(month + "월 " + i + "일 " + currentDayOfWeek + "(휴일)");
                }
                else
                    System.out.println(month + "월 " + i + "일 " + currentDayOfWeek);
            }
        }
        if (month.equals("4") || month.equals("6") || month.equals("9") || month.equals("11")) {
            for (int i = 1; i <= 30; i++) {
                String currentDayOfWeek = DAYS_OF_WEEK.get((dayOfWeekIndex + i - 1) % 7);
                if (LEGAL_HOLIDAY.contains(month+"-"+i)){
                    System.out.println(month + "월 " + i + "일 " + currentDayOfWeek + "(휴일)");
                }
                else
                    System.out.println(month + "월 " + i + "일 " + currentDayOfWeek);
            }
        }
        if (month.equals("2")) {
            for (int i = 1; i <= 28; i++) {
                String currentDayOfWeek = DAYS_OF_WEEK.get((dayOfWeekIndex + i - 1) % 7);
                if (LEGAL_HOLIDAY.contains(month+"-"+i)){
                    System.out.println(month + "월 " + i + "일 " + currentDayOfWeek + "(휴일)");
                }
                else
                    System.out.println(month + "월 " + i + "일 " + currentDayOfWeek);
            }
        }
    }

    // TODO:1. 월과 시작요일 입력 기능
    private static List<String> readMonthAndDay() {
        System.out.print("비상 근무를 배정할 월과 시작 요일을 입력하세요> ");
        String input = Console.readLine();
        return validateMonthAndDay(input);
    }

    private static List<String> validateMonthAndDay(String input) {
        validateNotBlank(input);
        validateDelimiter(input, ",");
        List<String> temp = parseInput(input);

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

        return temp;
    }

    // TODO:2. 비상근무 사원 입력
    private static List<List<String>> readEmergencyStaff() {
        System.out.print("평일 비상 근무 순번대로 사원 닉네임을 입력하세요> ");
        String weekdayStaffInput = Console.readLine();
        List<String> weekdayStaff = validateStaff(weekdayStaffInput);
        System.out.print("휴일 비상 근무 순번대로 사원 닉네임을 입력하세요> ");
        String holidayStaffInput = Console.readLine();
        List<String> holidayStaff = validateStaff(holidayStaffInput);
        validateStaffConsistency(weekdayStaff, holidayStaff);
        return List.of(weekdayStaff, holidayStaff);
    }

    private static List<String> validateStaff(String input) {
        validateNotBlank(input);
        validateDelimiter(input, ",");
        List<String> temp = parseInput(input);
        for (String temps : temp) {
            validateNicknameNumberOfCharacter(temps);
            validateBlankInElement(temps);
        }
        validateListSize(temp);
        validateDuplicate(temp);
        return temp;
    }

    private static void validateStaffConsistency(List<String> weekdayStaff, List<String> holidayStaff) {
        if (!(new HashSet<>(holidayStaff).equals(new HashSet<>(weekdayStaff)))) {
            throw new IllegalArgumentException("[ERROR] 비상 근무자가 평일, 휴일 순번에 각각 1회 편성되지 않았습니다.");
        }
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
    private static void validateNicknameNumberOfCharacter(String element) {
        if (element.length() > 5) {
            throw new IllegalArgumentException("[ERROR] 이름은 5자 이하만 가능합니다: " + element);
        }
    }

    // TODO: 리스트 요소에 공백이 포함되는지 검사
    private static void validateBlankInElement(String element) {
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
