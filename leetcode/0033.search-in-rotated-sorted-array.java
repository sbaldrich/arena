class Solution {

  public int search(int[] nums, int target) {
    int n = nums.length;
    if (n == 1) return nums[0] == target ? 0 : -1;
    // find the pivot (the index where everything rotated)
    int lo = 0, hi = n - 1, mid = 0, piv = 0;
    while (lo <= hi) {
      mid = lo + ((hi - lo) >> 1);
      if (nums[mid] >= nums[0]) {
        piv = mid;
        lo = mid + 1;
      } else {
        hi = mid - 1;
      }
    }

    if (target == nums[piv]) return piv;
    if (target >= nums[0] && target < nums[piv]) {
      lo = 0;
      hi = piv - 1;
    } else {
      lo = piv + 1;
      hi = n - 1;
    }

    while (lo <= hi) {
      mid = lo + ((hi - lo) >>> 1);
      if (target == nums[mid]) return mid;
      if (target < nums[mid]) {
        hi = mid - 1;
      } else {
        lo = mid + 1;
      }
    }
    return -1;
  }
}
