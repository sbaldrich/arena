import static java.time.temporal.ChronoUnit.*;

import java.time.*;
import java.util.*;

class DoublyLinkedList<T> {

  Node<T> head, tail;

  DoublyLinkedList() {
    this.head = Node.<T>of(null);
    this.tail = Node.<T>of(null);
    head.next = tail;
    tail.previous = head;
  }

  static class Node<T> {

    Node<T> previous;
    Node<T> next;
    T value;

    Node(T value, Node<T> previous, Node<T> next) {
      this.value = value;
      this.previous = previous;
      this.next = next;
    }

    static <T> Node<T> of(T value) {
      return new Node<>(value, null, null);
    }

    static <T> Node<T> of(T value, Node<T> prev) {
      return new Node<>(value, prev, null);
    }

    static <T> Node<T> of(T value, Node<T> prev, Node<T> next) {
      return new Node<>(value, prev, next);
    }

    public String toString() {
      return String.format("Node[%s]", Objects.toString(value));
    }
  }

  Node<T> append(T value) {
    var node = Node.<T>of(value);
    node.previous = tail.previous;
    node.next = tail;
    tail.previous.next = node;
    tail.previous = node;
    return node;
  }

  void moveToFront(Node<T> p) {
    if (p == head || p.previous == head || p == tail) {
      return;
    }
    p.previous.next = p.next;
    p.next.previous = p.previous;
    p.previous = head;
    p.next = head.next;
    head.next.previous = p;
    head.next = p;
  }

  T evict() {
    if (tail.previous == head) {
      return null;
    }
    var evicted = tail.previous;
    evicted.previous.next = tail;
    tail.previous = evicted.previous;
    return evicted.value;
  }

  void rotate() {
    moveToFront(tail.previous);
  }

  public String toString() {
    var curr = head;
    var sb = new StringBuilder();
    while (curr != null) {
      sb.append(
        String.format(
          "%s%s",
          curr == head ? "H" : curr == tail ? "T" : curr.value,
          curr != tail ? ", " : "\n"
        )
      );
      curr = curr.next;
    }
    return sb.toString();
  }
}

class LRUCache<K extends Comparable, V extends Comparable> {

  int limit;
  Map<K, DoublyLinkedList.Node<Map.Entry<K, V>>> map;
  DoublyLinkedList<Map.Entry<K, V>> queue;

  LRUCache(int capacity) {
    this.limit = capacity;
    this.map = new HashMap<>();
    this.queue = new DoublyLinkedList<>();
  }

  V get(K key) {
    if (map.containsKey(key)) {
      DoublyLinkedList.Node<Map.Entry<K, V>> node = map.get(key);
      queue.moveToFront(node);
      return node.value.getValue();
    }
    return null;
  }

  void put(K key, V value) {
    var node = map.computeIfAbsent(key, k ->
      queue.append(new HashMap.SimpleEntry<>(key, value))
    );
    queue.moveToFront(node);
    node.value.setValue(value);
    if (map.size() > limit) {
      map.remove(queue.evict().getKey());
    }
  }
}
