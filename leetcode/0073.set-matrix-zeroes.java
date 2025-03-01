class Solution {

  public void setZeroes(int[][] matrix) {
    int m = matrix.length, n = matrix[0].length;
    boolean first_row = false, first_col = false;
    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++) {
        if (matrix[i][j] == 0) {
          first_col |= j == 0;
          first_row |= i == 0;
          matrix[0][j] = matrix[0][j] != 0 ? 0 : matrix[0][j];
          matrix[i][0] = matrix[i][0] != 0 ? 0 : matrix[i][0];
        }
      }
    }

    for (int i = m - 1; i >= 0; i--) {
      for (int j = n - 1; j >= 0; j--) {
        if (i == 0 || j == 0) {
          if ((first_row && i == 0) || (first_col && j == 0)) {
            matrix[i][j] = 0;
          }
          continue;
        }
        if (matrix[0][j] == 0 || matrix[i][0] == 0) {
          matrix[i][j] = 0;
        }
      }
    }
  }
}
