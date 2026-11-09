import java.util.*;

public class Marker {

    /**
     * Mission 3a. The prefix function of p.
     */
    public static int[] prefixFunction(String p) {
        int n = p.length();
        int[] pi = new int[n];

        for (int i = 1; i < n; i++) {
            int j = pi[i - 1];
            while (j > 0 && p.charAt(i) != p.charAt(j)) {
                j = pi[j - 1]; // Откатываемся назад по массиву префиксов
            }
            if (p.charAt(i) == p.charAt(j)) {
                j++;
            }
            pi[i] = j;
        }

        return pi;
    }

    /**
     * Mission 3b. KMP. All start positions where p occurs in text.
     */
    public static int[] findAll(String text, String p) {
        if (p.length() == 0 || text.length() < p.length()) {
            return new int[0];
        }

        int[] pi = prefixFunction(p);
        List<Integer> positions = new ArrayList<>();
        int j = 0; // Указатель для шаблона (p)

        for (int i = 0; i < text.length(); i++) {
            while (j > 0 && text.charAt(i) != p.charAt(j)) {
                j = pi[j - 1]; // Если не совпало, сдвигаем указатель по таблице, не возвращая i назад
            }
            if (text.charAt(i) == p.charAt(j)) {
                j++;
            }
            if (j == p.length()) {
                positions.add(i - p.length() + 1); // Нашли полное совпадение
                j = pi[j - 1]; // Чтобы найти перекрывающиеся вхождения (overlap)
            }
        }

        int[] result = new int[positions.size()];
        for (int i = 0; i < positions.size(); i++) {
            result[i] = positions.get(i);
        }
        return result;
    }

    /**
     * Mission 4. Rabin-Karp. The number of positions i in a such that a[i..i+k-1] occurs in b.
     */
    public static int sharedWords(String a, String b, int k) {
        if (a.length() < k || b.length() < k) return 0;

        int prime = 31; // Простое число для основания хэша
        long highestPower = 1;

        // Предрасчет (prime^(k-1)) для удаления самого старого символа из окна в O(1)
        for (int i = 0; i < k - 1; i++) {
            highestPower *= prime;
        }

        // Карта для хранения [Хэш -> Список стартовых позиций в строке b]
        Map<Long, List<Integer>> bHashes = new HashMap<>();
        long currentHashB = 0;

        // Хэш для самого первого окна в b
        for (int i = 0; i < k; i++) {
            currentHashB = currentHashB * prime + b.charAt(i);
        }
        bHashes.computeIfAbsent(currentHashB, x -> new ArrayList<>()).add(0);

        // Rolling hash (двигаем окно по b)
        for (int i = 1; i <= b.length() - k; i++) {
            currentHashB = currentHashB - b.charAt(i - 1) * highestPower; // Убираем первую букву
            currentHashB = currentHashB * prime + b.charAt(i + k - 1);    // Добавляем новую в конец
            bHashes.computeIfAbsent(currentHashB, x -> new ArrayList<>()).add(i);
        }

        int sharedCount = 0;
        long currentHashA = 0;

        // Хэш для самого первого окна в a
        for (int i = 0; i < k; i++) {
            currentHashA = currentHashA * prime + a.charAt(i);
        }

        if (checkAndCompare(a, 0, b, bHashes, currentHashA, k)) {
            sharedCount++;
        }

        // Rolling hash (двигаем окно по a)
        for (int i = 1; i <= a.length() - k; i++) {
            currentHashA = currentHashA - a.charAt(i - 1) * highestPower;
            currentHashA = currentHashA * prime + a.charAt(i + k - 1);

            if (checkAndCompare(a, i, b, bHashes, currentHashA, k)) {
                sharedCount++;
            }
        }

        return sharedCount;
    }

    // Вспомогательный метод: если хэши совпали, мы ОБЯЗАНЫ сравнить буквы, чтобы избежать коллизий
    private static boolean checkAndCompare(String a, int indexA, String b, Map<Long, List<Integer>> bHashes, long hashA, int k) {
        if (bHashes.containsKey(hashA)) {
            for (int startB : bHashes.get(hashA)) {
                boolean match = true;
                for (int x = 0; x < k; x++) {
                    if (a.charAt(indexA + x) != b.charAt(startB + x)) {
                        match = false;
                        break;
                    }
                }
                if (match) return true; // Если хотя бы одно реальное совпадение найдено
            }
        }
        return false;
    }
}