class Solution {

  public int longestPalindromeSubseq(String s) {
    int n = s.length();
    if (n == 1) {
      return 1;
    }
    char[] chars = s.toCharArray();
    int dp[][] = new int[n][n];
    int ans = Integer.MIN_VALUE;

    for (int i = 0; i < n; i++) {
      dp[i][i] = 1;
    }

    for (int i = 1; i < n; i++) {
      for (int j = i - 1; j >= 0; j--) {
        dp[j][i] = chars[i] == chars[j] ? 2 + dp[j + 1][i - 1] : 1;
        dp[j][i] = Math.max(dp[j + 1][i], Math.max(dp[j][i], dp[j][i - 1]));
        ans = Math.max(ans, dp[j][i]);
      }
    }
    return ans;
  }
}
