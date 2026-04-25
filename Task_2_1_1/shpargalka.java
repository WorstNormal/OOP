import java.io.*;
import java.util.*;

public class Main {

    // ==========================================================
    // ТВОЙ КОД ЗДЕСЬ
    // ==========================================================
    public static void solve() {
        // Логика решения задачи

        // out.println("Ответ");
    }

    public static void main(String[] args) {
        sc = new FastScanner();
        out = new PrintWriter(new BufferedOutputStream(System.out));

        // int t = sc.nextInt();
        // while (t-- > 0) solve();

        solve();

        out.close();
    }

    // ==========================================================
    // ШПАРГАЛКА ПО JAVA (Синтаксис, типы, циклы, структуры)
    // ==========================================================
    private static void _cheatSheet() {

        // --- 1. ПРИМИТИВЫ И ТИПЫ ДАННЫХ ---
        byte b = 127;                // 8 bit: -128 .. 127
        short s = 32767;             // 16 bit
        int i = 2147483647;          // 32 bit: ~2 * 10^9 (Integer.MAX_VALUE / MIN_VALUE)
        long l = 9223372036854775807L; // 64 bit: (Long.MAX_VALUE) - обязательно 'L' на конце
        float f = 3.14f;             // 32 bit: обязательно 'f' на конце
        double d = 3.1415926535;     // 64 bit
        boolean bool = true;         // true / false
        char c = 'A';                // 16 bit Unicode

        // --- 2. МАТЕМАТИКА И ПРИВЕДЕНИЕ ТИПОВ ---
        int max = Math.max(10, 20);
        int min = Math.min(10, 20);
        int abs = Math.abs(-15);
        double pow = Math.pow(2, 3); // 2^3 = 8.0
        double sqrt = Math.sqrt(16); // 4.0
        long casted = (long) i;      // Приведение типов

        // --- 3. УСЛОВИЯ (If-Else, Switch) ---
        if (i > 10 && i < 100) {
            // И (AND)
        } else if (i == 10 || i == -10) {
            // ИЛИ (OR)
        } else if (i != 0) {
            // НЕ РАВНО (NOT EQUAL)
        } else {
            // Иначе
        }

        // Тернарный оператор (условие ? если_true : если_false)
        int minVal = (i < 10) ? i : 10;

        // Switch
        switch (c) {
            case 'A': break;
            case 'B': break;
            default:  break;
        }

        // --- 4. ЦИКЛЫ (Loops) ---
        // Обычный for
        for (int j = 0; j < 10; j++) {
            if (j == 2) continue; // Пропустить итерацию
            if (j == 5) break;    // Выйти из цикла
        }

        // Цикл while
        int k = 0;
        while (k < 10) {
            k++;
        }

        // --- 5. МАССИВЫ (Arrays) ---
        int[] arr = new int[5];                 // Пустой массив из 5 элементов (по умолчанию нули)
        int[] arrInit = {1, 2, 3, 4, 5};        // Инициализация значениями
        int[][] matrix = new int[5][5];         // Двумерный массив

        Arrays.sort(arrInit);                   // Сортировка по возрастанию (O(N log N))
        Arrays.fill(arr, -1);                   // Заполнить весь массив значением -1

        // Foreach (для перебора коллекций и массивов)
        for (int val : arrInit) {
            // Чтение val
        }

        // --- 6. СТРОКИ (Strings & StringBuilder) ---
        // Строки неизменяемы (immutable)! Операции типа s += "a" работают за O(N).
        String str = "Hello";
        int len = str.length();            // Длина строки
        char ch = str.charAt(0);           // 'H'
        String sub = str.substring(1, 4);  // "ell" (с 1 включительно по 4 не включительно)
        boolean eq = str.equals("Hello");  // Сравнение строк (НИКОГДА НЕ СРАВНИВАЙ ЧЕРЕЗ ==)

        // Для частых изменений строки используй StringBuilder (работает за O(1))
        StringBuilder sb = new StringBuilder();
        sb.append("A").append("B"); // "AB"
        sb.reverse();               // "BA"
        sb.deleteCharAt(0);         // "A"
        String res = sb.toString(); // Перевод обратно в String

        // --- 7. КОЛЛЕКЦИИ (Collections Framework) ---
        // СПИСКИ (Lists) - Динамические массивы
        List<Integer> list = new ArrayList<>();
        list.add(10);
        list.get(0);               // Получить элемент по индексу
        list.set(0, 20);           // Изменить элемент
        list.size();               // Размер
        Collections.sort(list);    // Сортировка списка
        Collections.reverse(list); // Разворот списка

        // СЛОВАРИ (Maps) - Ключ-Значение
        Map<String, Integer> map = new HashMap<>(); // O(1)
        map.put("A", 1);
        map.get("A");              // 1 (или null, если нет)
        map.getOrDefault("B", 0);  // Вернет 0, если ключа "B" нет (часто нужно для подсчета частоты)
        map.containsKey("A");      // true / false
        // Перебор мапы
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String key = entry.getKey();
            int value = entry.getValue();
        }

        // МНОЖЕСТВА (Sets) - Уникальные элементы
        Set<Integer> set = new HashSet<>(); // O(1)
        set.add(1);
        set.contains(1);           // true / false
        set.remove(1);

        // ДЕКИ И ОЧЕРЕДИ (Queues / Deque) - BFS и скользящие окна
        Deque<Integer> dq = new ArrayDeque<>();
        dq.addLast(1);  // push в конец
        dq.addFirst(2); // push в начало
        dq.pollFirst(); // pop из начала
        dq.pollLast();  // pop с конца
        dq.peekFirst(); // посмотреть первый элемент (не удаляя)

        // ОЧЕРЕДЬ С ПРИОРИТЕТОМ (PriorityQueue - Heap) - Дейкстра, K-th max
        // По умолчанию Min-Heap (наверху минимальный элемент)
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        // Max-Heap (наверху максимальный элемент)
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        minHeap.add(5);
        minHeap.poll(); // Достать и удалить минимальный элемент
        minHeap.peek(); // Посмотреть минимальный элемент
    }

    // ИНФРАСТРУКТУРА (Fast I/O)
    static FastScanner sc;
    static PrintWriter out;

    static class FastScanner {
        BufferedReader br;
        StringTokenizer st;

        public FastScanner() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() {
            while (st == null || !st.hasMoreElements()) {
                try {
                    String line = br.readLine();
                    if (line == null) return null;
                    st = new StringTokenizer(line);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() { return Integer.parseInt(next()); }
        long nextLong() { return Long.parseLong(next()); }
        double nextDouble() { return Double.parseDouble(next()); }
        String nextLine() {
            String str = "";
            try { str = br.readLine(); } catch (IOException e) { e.printStackTrace(); }
            return str;
        }
    }
}