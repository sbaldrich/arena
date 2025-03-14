class ZAlgorithm {
    static int[] z(String input) {
        int l, r, k, m = input.length();
        int[] z = new int[m];
        l = r = 0;
        for (int i = 1; i < m; i++) {
            if (i > r) {
                l = r = i;
                while (r < m && input.charAt(r) == input.charAt(r - l)) {
                    r++;
                }
                z[i] = r - l;
                r--;
            } else {
                k = i - l;
                if (z[k] < r - i + 1) {
                    z[i] = z[k];
                } else {
                    l = i;
                    while (r < m && input.charAt(r) == input.charAt(r - l)) {
                        r++;
                    }
                    z[i] = r - l;
                    r--;
                }
            }
        }
        return z;
    }
}