public class Strains {

    /**
     * Mission 1. The smallest number of single-letter changes that turn a into b.
     */
    public static int editDistance(String a, String b) {
        int m = a.length();
        int n = b.length();
        int[][] d = new int[m + 1][n + 1];

        // Инициализация первой строки и первого столбца
        for (int i = 0; i <= m; i++) {
            d[i][0] = i; // Чтобы получить пустую строку из a, нужно удалить i символов
        }
        for (int j = 0; j <= n; j++) {
            d[0][j] = j; // Чтобы получить b из пустой строки, нужно вставить j символов
        }

        // Заполнение таблицы
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    d[i][j] = d[i - 1][j - 1]; // Символы совпадают, ничего менять не нужно
                } else {
                    int delete = d[i - 1][j] + 1;
                    int insert = d[i][j - 1] + 1;
                    int replace = d[i - 1][j - 1] + 1;
                    d[i][j] = Math.min(delete, Math.min(insert, replace));
                }
            }
        }

        return d[m][n];
    }

    /**
     * Mission 2. The length of the longest common subsequence.
     */
    public static int lcsLength(String a, String b) {
        int m = a.length();
        int n = b.length();
        int[][] d = new int[m + 1][n + 1];

        // Заполнение таблицы (первая строка и столбец остаются 0 по умолчанию)
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    d[i][j] = d[i - 1][j - 1] + 1; // Если символы совпали, увеличиваем длину подпоследовательности
                } else {
                    d[i][j] = Math.max(d[i - 1][j], d[i][j - 1]); // Иначе берем максимум из предыдущих вариантов
                }
            }
        }

        return d[m][n];
    }
}