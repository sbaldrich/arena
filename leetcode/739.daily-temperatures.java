import java.util.*;

class Solution {

  public int[] dailyTemperatures(int[] temperatures) {
    var queue = new ArrayDeque<Integer>();
    var answer = new int[temperatures.length];
    for (int i = 0; i < temperatures.length; i++) {
      while (!queue.isEmpty() && temperatures[i] > temperatures[queue.peek()]) {
        var top = queue.pop();
        answer[top] = i - top;
      }
      queue.push(i);
    }
    return answer;
  }
}
