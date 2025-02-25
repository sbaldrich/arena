import java.util.*;

class Solution {

  public int[] canSeePersonsCount(int[] heights) {
    var queue = new ArrayDeque<Integer>();
    var answer = new int[heights.length];
    for (int i = 0; i < heights.length; i++) {
      int acc = 0;
      while (!queue.isEmpty() && heights[queue.peek()] < heights[i]) {
        answer[queue.pop()]++;
      }
      if (!queue.isEmpty()) {
        answer[queue.peek()]++;
      }
      queue.push(i);
    }
    return answer;
  }
}
