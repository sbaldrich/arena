import java.util.*;

class Solution {

  public List<Integer> spiralOrder(int[][] matrix) {
    int m = matrix.length, n = matrix[0].length;
    int l = 0, r = n - 1, t = 0, b = m - 1;
    var ans = new ArrayList<Integer>();
    while (l <= r && t <= b) {
      for (int i = l; i <= r; i++) {
        ans.add(matrix[t][i]);
      }
      t++;
      for (int i = t; i <= b; i++) {
        ans.add(matrix[i][r]);
      }
      r--;
      for (int i = r; t <= b && i >= l; i--) {
        ans.add(matrix[b][i]);
      }
      b--;
      for (int i = b; l <= r && i >= t; i--) {
        ans.add(matrix[i][l]);
      }
      l++;
    }
    return ans;
  }
}
