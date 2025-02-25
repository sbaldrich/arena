class Solution {

  public int minSubArrayLen(int target, int[] nums) {
    int n = nums.length;
    int[] sums = new int[n + 1];
    for (int i = 0; i < n; i++) {
      sums[i + 1] = nums[i] + sums[i];
    }
    int lead = 1, fol = 0, ans = -1;
    while (lead <= n) {
      int sum = sums[lead] - sums[fol];
      if (sum >= target) {
        ans = ans == -1 ? lead - fol : Math.min(ans, lead - fol);
        fol++;
      }
      if (sum < target || fol == lead) {
        lead++;
      }
    }
    return Math.max(0, ans);
  }
}
