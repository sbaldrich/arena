import java.util.Arrays;

class Solution {

  public void merge(int[] nums1, int m, int[] nums2, int n) {
    int i = n + m - 1, x;
    n--;
    m--;
    while (n >= 0) {
      if (m > 0 && nums1[m] > nums2[n]) {
        x = nums1[m--];
      } else {
        x = nums2[n--];
      }
      nums1[i--] = x;
    }
  }
}
