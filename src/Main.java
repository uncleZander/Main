import java.io.*;

class StringToExample {
    public String value1;
    public char action;
    public String value2;

    /**
     * Конструктор класса StringToExample
     * @param value1 Первый операнд в виде строки
     * @param action Арифметическая операция в виде символа
     * @param value2 Второй операнд в виде строки
     */
    public StringToExample(String value1, char action, String value2) {
        this.value1 = value1;
        this.action = action;
        this.value2 = value2;
    }
}

class ReadyToCalculate {
    public int value1;
    public int value2;
    String numSet;

    /**
     * Конструктор класса ReadyToCalculate
     * @param value1 Первый операнд в виде целого числа
     * @param value2 Второй операнд в виде целого числа
     * @param numSet Система счисления операндов ("arab" или "roman")
     */
    public ReadyToCalculate(int value1, int value2, String numSet) {
        this.value1 = value1;
        this.value2 = value2;
        this.numSet = numSet;
    }
}

public class Main {
    private static final String[] ROMAN = { "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I" };
    private static final int[] ARAB = { 100, 90, 50, 40, 10, 9, 5, 4, 1 };

    /**
     * Преобразование строки в операнды и арифметический оператор
     * @param chInput Входная строка с заданием
     * @return Объект StringToExample, содержащий строковые выражения операндов и символьное значение операции
     */
    public static StringToExample stringTransform(String chInput) {
        int i = 0;
        int j = 0;
        char[] var1 = new char[chInput.length()];
        char[] var2 = new char[chInput.length()];
        char action = 0;

        // Определение первого операнда
        if (chInput.charAt(i) == '-') {
            var1[j++] = chInput.charAt(i++);
        }
        while (i < chInput.length() && chInput.charAt(i) != '+' && chInput.charAt(i) != '*' && chInput.charAt(i) != '/' && chInput.charAt(i) != '-') {
            var1[j++] = chInput.charAt(i++);
        }
        if (i < chInput.length()) {
            action = chInput.charAt(i++);
        }

        // Определение второго операнда
        j = 0;
        while (i < chInput.length()) {
            var2[j++] = chInput.charAt(i++);
        }

        String value1 = new String(var1).trim();
        String value2 = new String(var2).trim();
        return new StringToExample(value1, action, value2);
    }

    /**
     * Проверка корректности введенного задания, определение системы счисления операндов и преобразование строк в числа
     * @param example Объект StringToExample, содержащий строковые выражения операндов и символьное значение операции
     * @return Объект ReadyToCalculate, содержащий целочисленные значения операндов и систему исчисления задания
     * @throws IllegalArgumentException Если входные данные некорректны
     */
    public static ReadyToCalculate stringToInt(StringToExample example) {
        String num1 = example.value1;
        String num2 = example.value2;
        String numSet;
        int value1, value2;

        // Определение системы счисления
        if (isRoman(num1) && isRoman(num2)) {
            numSet = "roman";
        } else if (isArab(num1) && isArab(num2)) {
            numSet = "arab";
        } else {
            throw new IllegalArgumentException("Задание должно содержать только арабские или только римские числа");
        }

        // Преобразование в числа
        switch (numSet) {
            case "roman":
                value1 = getRomanValue(num1);
                value2 = getRomanValue(num2);
                break;
            case "arab":
                value1 = Integer.parseInt(num1);
                value2 = Integer.parseInt(num2);
                break;
            default:
                throw new IllegalArgumentException("Некорректная система счисления");
        }

        return new ReadyToCalculate(value1, value2, numSet);
    }

    private static boolean isRoman(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (!"IVX".contains(String.valueOf(s.charAt(i)))) {
                return false;
            }
        }
        return true;
    }

    private static boolean isArab(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Вспомогательный метод для преобразования римского числа в целое
     * @param romanNum Римское число в виде строки
     * @return Целое число, соответствующее римскому числу
     * @throws IllegalArgumentException Если входное число некорректно
     */
    private static int getRomanValue(String romanNum) {
        int result = 0;
        for (int i = 0; i < ROMAN.length; i++) {
            while (romanNum.startsWith(ROMAN[i])) {
                result += ARAB[i];
                romanNum = romanNum.substring(ROMAN[i].length());
            }
        }
        if (!romanNum.isEmpty()) {
            throw new IllegalArgumentException("Введенное число " + romanNum + " не является корректным римским числом");
        }
        return result;
    }

    /**
     * Проверка математических условий, вычисление результата, подготовка к выводу ответа
     * @param value1 Первый операнд
     * @param value2 Второй операнд
     * @param action Арифметическая операция
     * @param numSet Система счисления операндов ("arab" или "roman")
     * @return Результат вычислений в виде строки
     * @throws IllegalArgumentException Если входные данные некорректны
     * @throws ArithmeticException Если произошло деление на ноль
     */
    public static String calc(int value1, int value2, char action, String numSet) {
        int resultInt;
        String result = "";

        // Проверка на корректность входных данных
        if (numSet.equals("arab") && (Math.abs(value1) > 10 || Math.abs(value2) > 10)) {
            throw new IllegalArgumentException("Числа в задании должны быть от -10 до 10");
        }
        if (numSet.equals("roman") && (value1 < 1 || value1 > 10 || value2 < 1 || value2 > 10)) {
            throw new IllegalArgumentException("Числа в задании должны быть от I до X");
        }

        // Вычисление результата
        switch (action) {
            case '+':
                resultInt = value1 + value2;
                break;
            case '-':
                if (numSet.equals("roman") && value1 < value2) {
                    throw new IllegalArgumentException("Римские числа не могут быть меньше I");
                }
                resultInt = value1 - value2;
                break;
            case '*':
                resultInt = value1 * value2;
                break;
            case '/':
                if (value2 != 0) {
                    resultInt = value1 / value2;
                } else {
                    throw new ArithmeticException("На ноль делить нельзя!");
                }
                break;
            default:
                throw new IllegalArgumentException("Некорректная арифметическая операция");
        }

        // Формирование результата
        switch (numSet) {
            case "arab":
                result = Integer.toString(resultInt);
                break;
            case "roman":
                result = convertToRoman(resultInt);
                break;
            default:
                throw new IllegalArgumentException("Некорректная система счисления");
        }
        return result;
    }

    /**
     * Вспомогательный метод для преобразования целого числа в римское
     * @param number Целое число
     * @return Римское число в виде строки
     */
    private static String convertToRoman(int number) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ROMAN.length; i++) {
            int checkDiv = number / ARAB[i];
            if (checkDiv > 0) {
                sb.append(ROMAN[i].repeat(checkDiv));
                number -= ARAB[i] * checkDiv;
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("Введите задание в виде: 'Число1' 'арифметический оператор' 'Число2' и нажмите ENTER.");
        System.out.println("Допустимо использование арабских чисел от -10 до 10 или римских чисел от I до X.");
        System.out.println();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String chInput = br.readLine();
            chInput = chInput.replaceAll("\\s+", "");
            chInput = chInput.toUpperCase();
            StringToExample example = stringTransform(chInput);
            ReadyToCalculate dataForCalculate = stringToInt(example);
            int value1 = dataForCalculate.value1;
            int value2 = dataForCalculate.value2;
            char action = example.action;
            String numSet = dataForCalculate.numSet;
            String result = calc(value1, value2, action, numSet);
            System.out.println("Результат: " + result);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (ArithmeticException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Ошибка ввода-вывода: " + e.getMessage());
        }
    }
}