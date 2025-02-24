import java.util.*;
import java.util.stream.*;

class DList<T> {

  Node<T> head;
  Node<T> tail;

  DList() {
    head = new Node<T>(null);
    tail = new Node<T>(null);
    head.next = tail;
    tail.prev = head;
  }

  DList<T> append(T value) {
    return add(tail.prev, value);
  }

  DList<T> append(Node<T> anchor, T value) {
    return add(anchor, value);
  }

  DList<T> prepend(T value) {
    return add(head, value);
  }

  DList<T> prepend(Node<T> anchor, T value) {
    return add(anchor.prev, value);
  }

  DList<T> remove(Node<T> node) {
    node.prev.next = node.next;
    node.next.prev = node.prev;
    return this;
  }

  DList<T> add(Node<T> anchor, T value) {
    var node = new Node<>(value);
    node.prev = anchor;
    node.next = anchor.next;
    anchor.next.prev = node;
    anchor.next = node;
    return this;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    var node = head;
    while (true) {
      if (node == tail) {
        sb.append("T");
        return sb.toString();
      }
      sb.append(node == head ? "H" : node.payload.toString()).append(", ");
      node = node.next;
    }
  }

  class Node<T> {

    T payload;
    Node<T> next;
    Node<T> prev;

    Node(T payload) {
      this.payload = payload;
    }
  }
}

class AllOne {

  DList<Bucket> dlist;
  Map<String, DList<Bucket>.Node<Bucket>> map;

  AllOne() {
    dlist = new DList<>();
    map = new HashMap<>();
  }

  class Bucket {

    int n;
    Set<String> keys;

    Bucket(int n) {
      this.n = n;
      keys = new HashSet<String>();
    }

    Bucket() {
      this(0);
    }

    public String toString() {
      return String.format("[ %s : %d ]", keys, n);
    }
  }

  void inc(String key) {
    if (map.containsKey(key)) {
      var cur = map.get(key);
      var nex = cur.next;
      if (nex == dlist.tail || nex.payload.n != cur.payload.n + 1) {
        dlist.append(cur, new Bucket(cur.payload.n + 1));
        nex = cur.next;
      }
      cur.payload.keys.remove(key);
      if (cur.payload.keys.isEmpty()) {
        dlist.remove(cur);
      }
      nex.payload.keys.add(key);
      map.put(key, nex);
      return;
    }

    var node = dlist.head.next;

    if (node == dlist.tail || node.payload.n != 1) {
      dlist.append(dlist.head, new Bucket(1));
      node = dlist.head.next;
    }

    node.payload.keys.add(key);
    map.put(key, node);
  }

  void dec(String key) {
    if (!map.containsKey(key)) {
      return;
    }

    var cur = map.get(key);
    var pre = cur.prev;

    cur.payload.keys.remove(key);

    if (
      cur.payload.n > 1 &&
      (pre == dlist.head || pre.payload.n != cur.payload.n - 1)
    ) {
      dlist.prepend(cur, new Bucket(cur.payload.n - 1));
      pre = cur.prev;
    }

    if (cur.payload.n == 1) {
      map.remove(key);
    } else {
      pre.payload.keys.add(key);
      map.put(key, pre);
    }

    if (cur.payload.keys.isEmpty()) {
      dlist.remove(cur);
    }
  }

  String getMaxKey() {
    var node = dlist.tail.prev;
    if (node == dlist.head) {
      return "";
    }
    return node.payload.keys.iterator().next();
  }

  String getMinKey() {
    var node = dlist.head.next;
    if (node == dlist.tail) {
      return "";
    }
    return node.payload.keys.iterator().next();
  }
}
