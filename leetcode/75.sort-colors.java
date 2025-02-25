import java.util.*;

class Solution {

  public void sortColors(int[] nums) {
    int r, g, b;
    r = g = b = 0;
    for (int i = 0; i < nums.length; i++) {
      if (nums[i] == 0) r++;
      if (nums[i] == 1) g++;
      if (nums[i] == 2) b++;
    }
    for (int i = 0; i < nums.length; i++) {
      nums[i] = r-- > 0 ? 0 : g-- > 0 ? 1 : 2;
    }
  }
}
