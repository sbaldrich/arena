import java.math.BigInteger;
import java.util.*;

class Solution {

  long[] primes = primes();

  public List<List<String>> groupAnagrams(String[] strs) {
    var groups = new HashMap<Long, List<String>>();
    for (String s : strs) {
      var x = hash(s);
      System.out.printf("x: %d (%s)\n", x, s);
      groups.putIfAbsent(x, new ArrayList<>());
      groups.get(x).add(s);
    }

    return new ArrayList<>(groups.values());
  }

  BigInteger hash(String s) {
    var h = BigInteger.ONE;
    for (char c : s.toCharArray()) {
      h = h.multiply(BigInteger.valueOf(primes[c - 'a']));
    }
    return h;
  }

  long[] primes() {
    int n = 113;
    boolean sieve[] = new boolean[n + 1];
    Arrays.fill(sieve, true);
    sieve[0] = sieve[1] = false;
    for (int p = 2; p * p <= n; p++) {
      if (sieve[p]) {
        for (int i = p * p; i <= n; i += p) {
          sieve[i] = false;
        }
      }
    }
    long[] out = new long[30];
    for (int i = 0, cur = 0; i <= n; i++) {
      if (sieve[i]) {
        out[cur++] = (long) i;
      }
    }
    return out;
  }

  public static void main(String[] args) {
    var sol = new Solution();
    System.out.println(Arrays.toString(sol.primes()));
  }
}
