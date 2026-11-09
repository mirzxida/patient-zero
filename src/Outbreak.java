public class Outbreak {

    /**
     * Mission 5a. Minimum Weight Vertex Cover on a tree.
     */
    public static boolean[] cheapestTests(int n, int[][] nb, int[] cost) {
        int[] dpTested = new int[n]; // Минимальная стоимость, если человек v ТЕСТИРУЕТСЯ
        int[] dpNotTested = new int[n]; // Минимальная стоимость, если человек v НЕ тестируется

        // Этап 1: Считаем стоимости снизу вверх (от листьев к корню)
        dfsCosts(0, -1, nb, cost, dpTested, dpNotTested);

        // Этап 2: Восстанавливаем ответ сверху вниз (кто именно вошел в оптимальный выбор)
        boolean[] tested = new boolean[n];
        dfsReconstruct(0, -1, false, nb, dpTested, dpNotTested, tested);

        return tested;
    }

    private static void dfsCosts(int u, int p, int[][] nb, int[] cost, int[] dpTested, int[] dpNotTested) {
        dpTested[u] = cost[u];
        dpNotTested[u] = 0;

        for (int v : nb[u]) {
            if (v == p) continue; // Не идем назад к родителю

            dfsCosts(v, u, nb, cost, dpTested, dpNotTested);

            // Если родитель (u) НЕ тестируется, мы ОБЯЗАНЫ протестировать ребенка (v), чтобы покрыть связь
            dpNotTested[u] += dpTested[v];

            // Если родитель (u) тестируется, связь уже покрыта. Мы вольны выбирать самый дешевый вариант для ребенка
            dpTested[u] += Math.min(dpTested[v], dpNotTested[v]);
        }
    }

    private static void dfsReconstruct(int u, int p, boolean parentTested, int[][] nb, int[] dpTested, int[] dpNotTested, boolean[] tested) {
        if (!parentTested) {
            // Если родитель не тестировался, у нас нет выбора — нужно тестировать текущего
            tested[u] = true;
        } else {
            // У родителя есть тест, выбираем то, что вышло дешевле в DP
            tested[u] = (dpTested[u] <= dpNotTested[u]);
        }

        for (int v : nb[u]) {
            if (v == p) continue;
            dfsReconstruct(v, u, tested[u], nb, dpTested, dpNotTested, tested);
        }
    }

    /**
     * Mission 5b. The number of links on the longest chain of infections.
     */
    public static int longestChain(int n, int[][] nb) {
        int[] maxDiameter = {0}; // Используем массив из 1 элемента, чтобы изменять его внутри рекурсии
        dfsDiameter(0, -1, nb, maxDiameter);
        return maxDiameter[0];
    }

    // Метод возвращает максимальную глубину поддерева, одновременно обновляя глобальный максимум (диаметр)
    private static int dfsDiameter(int u, int p, int[][] nb, int[] maxDiameter) {
        int maxDepth1 = 0; // Самая длинная ветка вниз
        int maxDepth2 = 0; // Вторая по длине ветка вниз

        for (int v : nb[u]) {
            if (v == p) continue;

            int depth = dfsDiameter(v, u, nb, maxDiameter) + 1;

            if (depth > maxDepth1) {
                maxDepth2 = maxDepth1;
                maxDepth1 = depth;
            } else if (depth > maxDepth2) {
                maxDepth2 = depth;
            }
        }

        // Самая длинная цепь, проходящая через вершину u как через наивысшую точку
        if (maxDepth1 + maxDepth2 > maxDiameter[0]) {
            maxDiameter[0] = maxDepth1 + maxDepth2;
        }

        return maxDepth1; // Возвращаем наверх только одну самую длинную ветку
    }
}