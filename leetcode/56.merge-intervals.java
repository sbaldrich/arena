import java.util.*;

class Solution {

  public int[][] merge(int[][] intervals) {
    Arrays.sort(intervals, (a, b) ->
      a[0] - b[0] != 0 ? a[0] - b[0] : a[1] - b[1]
    );
    int ranges = 0, left = -1, right = -1;
    for (int i = 0; i < intervals.length; i++) {
      if (left == -1) {
        left = intervals[i][0];
        right = intervals[i][1];
        continue;
      }
      if (intervals[i][0] <= right) {
        right = Math.max(right, intervals[i][1]);
      } else {
        intervals[ranges][0] = left;
        intervals[ranges++][1] = right;
        left = intervals[i][0];
        right = intervals[i][1];
      }
    }
    if (left >= 0) {
      intervals[ranges][0] = left;
      intervals[ranges++][1] = right;
    }
    return Arrays.copyOfRange(intervals, 0, ranges);
  }
}
