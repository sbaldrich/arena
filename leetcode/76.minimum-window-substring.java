import java.util.*;

class Solution {

  public String minWindow(String s, String t) {
    if (s.length() < t.length()) return "";
    Map<Character, Integer> sCount = new HashMap<>();
    Map<Character, Integer> tCount = new HashMap<>();

    for (char c : t.toCharArray()) {
      tCount.put(c, tCount.getOrDefault(c, 0) + 1);
    }

    int start = 0, end = 0, ansStart = 0, minLength =
      Integer.MAX_VALUE, charsLeft = tCount.size();
    while (end < s.length()) {
      char c = s.charAt(end);
      sCount.put(c, sCount.getOrDefault(c, 0) + 1);

      if (tCount.containsKey(c) && sCount.get(c).equals(tCount.get(c))) {
        charsLeft--;
      }

      while (charsLeft == 0) {
        if (end - start + 1 < minLength) {
          minLength = end - start + 1;
          ansStart = start;
        }
        char leftChar = s.charAt(start);
        if (
          tCount.containsKey(leftChar) &&
          tCount.get(leftChar).equals(sCount.get(leftChar))
        ) {
          charsLeft++;
        }
        sCount.put(leftChar, sCount.get(leftChar) - 1);
        start++;
      }
      end++;
    }
    return minLength < Integer.MAX_VALUE
      ? s.substring(ansStart, ansStart + minLength)
      : "";
  }
}
