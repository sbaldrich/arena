class Solution {

  public int trap(int[] height) {
    int l = 0, r = height.length - 1, ans = 0, lmax, rmax;
    lmax = rmax = Integer.MIN_VALUE;
    while (l <= r) {
      lmax = Math.max(lmax, height[l]);
      rmax = Math.max(rmax, height[r]);
      if (lmax <= rmax) ans += lmax - height[l++];
      else ans += rmax - height[r--];
    }
    return ans;
  }
}