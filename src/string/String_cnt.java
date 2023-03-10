import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class String_cnt {

    private static final String TEXT = "Be strong, be fearless, be beautiful. "
            + "And believe that anything is possible when you have the right "
            + "people there to support you. ";
    // Σ -> Unicode: \u04DC, Code Point: 1244
    // π -> Unicode: \uD83D\uDC95, Code Point: 128149
    // πΌ -> \uD83C\uDFBC, Code Point: 127932
    // π ->\uD83D\uDE0D, Code Point: 128525
    private static final String TEXT_CP = TEXT + "π I love π you Σ so much π π πΌπΌπΌ!";

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

        System.out.println("\n-----------------νλ¨ : μμ€ν€ μ΄μΈμ μ λμ½λ λ¬ΈμκΉμ§ μ²λ¦¬νλλ‘---------------------\n");
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

    //1) λ¬Έμμ΄ λ΄ λ¬Έμλ₯Ό μνν΄ Mapμ μ¬μ©νλ λ°©λ²(λ¬Έμ : ν€, λΉλμ: κ°)
    public static Map<Character, Integer> countDuplicateCharacters_by_Map(String str) {
        Map <Character, Integer> result = new HashMap<>();

        for(char ch : str.toCharArray()){
            //map compute : λλ€μμ ν΅ν΄ κΈ°μ‘΄μ κ°μ μ΄λ»κ² μ°μ°μ ν μ§ μ§μ νλ Map Interfaceμ λ©μλ
            result.compute(ch, (key,value)-> (value == null)? 1 : ++value);
        }

        return result;
    }
    //μ λμ½λ λ¬Έμμ΄κΉμ§ μ²λ¦¬ κ°λ₯ν λ²μ 
    public static Map<String, Integer> countDuplicateCharacters_by_Map_withUniCode(String str) {
        Map <String, Integer> result = new HashMap<>();

        for(int i = 0; i < str.length(); i++){
            int cp = str.codePointAt(i);//iλ²μ§Έ λ¬Έμλ₯Ό μμ€ν€λ‘ λ³ν
            String ch = String.valueOf(Character.toChars(cp));
            if(Character.charCount(cp) == 2) { //λλ¦¬μμ μλ―Έ
                i++;
            }
            result.compute(ch, (k, v) -> (v == null) ? 1 : ++v);
        }

        return result;
    }
    //2) μλ° 8 μ΄μμ μ€νΈλ¦Ό κΈ°λ₯ νμ©
    public static Map <Character, Long> countDuplicateCharacters_by_Stream(String str) {
        Map <Character, Long> result = str.chars() //str μμ μλ λ¬Έμλ€μ IntstreamμΌλ‘ λ³ν
                                          .mapToObj( c -> (char) c) //λ³νν Intstreamμ λ¬Έμ μ€νΈλ¦ΌμΌλ‘ λ³ν
                                          .collect( Collectors.groupingBy( c -> c, Collectors.counting()));
                                            //λ³νν λ¬Έμμ€νΈλ¦Όμ λΆλ₯νκ³  μΉ΄μ΄ν
        return result;
    }
    public static Map<String, Long> countDuplicateCharacters_by_Stream_withUniCode(String str) {
        Map <String, Long> result = str.codePoints()
                .mapToObj( c -> String.valueOf(Character.toChars(c)))
                .collect(Collectors.groupingBy( c -> c , Collectors.counting()));
        return result;
    }

}
