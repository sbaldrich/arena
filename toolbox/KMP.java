import java.util.List;

class KMP {
    static List<Integer> kmp(String pattern, String text) {
        int n = text.length(), m = pattern.length();
        int[] T = new int[m + 1];
        T[0] = -1;
        for (int i = 1, pos; i <= m; i++) {
            pos = T[i - 1];
            while (pos != -1 && pattern.charAt(pos) != pattern.charAt(i - 1)) {
                pos = T[pos];
            }
            T[i] = pos + 1;
        }
        int tp, pp;
        tp = pp = 0;
        List<Integer> matches = new ArrayList<>();
        while (tp < n) {
            while (pp != -1 && (m == pp || pattern.charAt(pp) != text.charAt(tp))) {
                pp = T[pp];
            }
            pp++;
            tp++;
            if (pp == m) {
                matches.add(tp - m);
            }
        }
        return matches;
    }
}
