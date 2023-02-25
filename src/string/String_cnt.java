import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class String_cnt {

    private static final String TEXT = "Be strong, be fearless, be beautiful. "
            + "And believe that anything is possible when you have the right "
            + "people there to support you. ";
    // Ӝ -> Unicode: \u04DC, Code Point: 1244
    // 💕 -> Unicode: \uD83D\uDC95, Code Point: 128149
    // 🎼 -> \uD83C\uDFBC, Code Point: 127932
    // 😍 ->\uD83D\uDE0D, Code Point: 128525
    private static final String TEXT_CP = TEXT + "😍 I love 💕 you Ӝ so much 💕 😍 🎼🎼🎼!";

    public static void main(String[] args) {

        System.out.println("Input text: \n" + TEXT + "\n");

        System.out.println("\n\nASCII or 16 bits Unicode characters (less than 65,535 (0xFFFF)) examples:\n");

        System.out.println("1) HashMap based solution:");
        long startTimeV1 = System.nanoTime();

        Map<Character, Integer> duplicatesV1 = countDuplicateCharacters_by_Map(TEXT);

        displayExecutionTime(System.nanoTime()-startTimeV1);
        System.out.println(Arrays.toString(duplicatesV1.entrySet().toArray()));
        // or: duplicatesV1.forEach( (k, v) -> System.out.print(k + "="+ v + ", "));

        System.out.println();
        System.out.println("2) Java 8, functional-style solution:");
        long startTimeV2 = System.nanoTime();

        Map<Character, Long> duplicatesV2 = countDuplicateCharacters_by_Stream(TEXT);

        displayExecutionTime(System.nanoTime()-startTimeV2);
        System.out.println(Arrays.toString(duplicatesV2.entrySet().toArray()));
        // or - duplicatesV2.forEach( (k, v) -> System.out.print(k + "="+ v + ", "));

        System.out.println("\n-----------------하단 : 아스키 이외에 유니코드 문자까지 처리하도록---------------------\n");
        System.out.println("Input text: \n" + TEXT_CP + "\n");
        System.out.println("\n\nIncluding Unicode surrogate pairs examples:\n");
        System.out.println("HashMap based solution:");
        long startTimeV3 = System.nanoTime();

        Map<String, Integer> duplicatesV3 = countDuplicateCharacters_by_Map_withUniCode(TEXT_CP);

        displayExecutionTime(System.nanoTime()-startTimeV3);
        System.out.println(Arrays.toString(duplicatesV3.entrySet().toArray()));
        // or: duplicatesV3.forEach( (k, v) -> System.out.print(k + "="+ v + ", "));

        System.out.println();
        System.out.println("Java 8, functional-style solution:");
        long startTimeV4 = System.nanoTime();

        Map<String, Long> duplicatesV4 = countDuplicateCharacters_by_Stream_withUniCode(TEXT_CP);

        displayExecutionTime(System.nanoTime()-startTimeV4);
        System.out.println(Arrays.toString(duplicatesV4.entrySet().toArray()));
        // or: duplicatesV4.forEach( (k, v) -> System.out.print(k + "="+ v + ", "));
    }

    private static void displayExecutionTime(long time) {
        System.out.println("Execution time: " + time + " ns" + " (" +
                TimeUnit.MILLISECONDS.convert(time, TimeUnit.NANOSECONDS) + " ms)");
    }

    //1) 문자열 내 문자를 순회해 Map을 사용하는 방법(문자 : 키, 빈도수: 값)
    public static Map<Character, Integer> countDuplicateCharacters_by_Map(String str) {
        Map <Character, Integer> result = new HashMap<>();

        for(char ch : str.toCharArray()){
            //map compute : 람다식을 통해 기존의 값에 어떻게 연산을 할지 지정하는 Map Interface의 메서드
            result.compute(ch, (key,value)-> (value == null)? 1 : ++value);
        }

        return result;
    }
    //유니코드 문자열까지 처리 가능한 버전
    public static Map<String, Integer> countDuplicateCharacters_by_Map_withUniCode(String str) {
        Map <String, Integer> result = new HashMap<>();

        for(int i = 0; i < str.length(); i++){
            int cp = str.codePointAt(i);//i번째 문자를 아스키로 변환
            String ch = String.valueOf(Character.toChars(cp));
            if(Character.charCount(cp) == 2) { //대리쌍을 의미
                i++;
            }
            result.compute(ch, (k, v) -> (v == null) ? 1 : ++v);
        }

        return result;
    }
    //2) 자바 8 이상의 스트림 기능 활용
    public static Map <Character, Long> countDuplicateCharacters_by_Stream(String str) {
        Map <Character, Long> result = str.chars() //str 안에 있는 문자들을 Intstream으로 변환
                                          .mapToObj( c -> (char) c) //변환한 Intstream을 문자 스트림으로 변환
                                          .collect( Collectors.groupingBy( c -> c, Collectors.counting()));
                                            //변환한 문자스트림을 분류하고 카운팅
        return result;
    }
    public static Map<String, Long> countDuplicateCharacters_by_Stream_withUniCode(String str) {
        Map <String, Long> result = str.codePoints()
                .mapToObj( c -> String.valueOf(Character.toChars(c)))
                .collect(Collectors.groupingBy( c -> c , Collectors.counting()));
        return result;
    }

}
