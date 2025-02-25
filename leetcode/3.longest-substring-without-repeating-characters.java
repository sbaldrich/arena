import java.util.*;

class Solution {

  public int lengthOfLongestSubstring(String s) {
    Set<Character> chars = new HashSet<>();
    int start = 0;
    int ans = 0;
    for (char c : s.toCharArray()) {
      while (chars.contains(c)) {
        chars.remove(s.charAt(start++));
      }
      chars.add(c);
      ans = Math.max(ans, chars.size());
    }
    return ans;
  }
}
