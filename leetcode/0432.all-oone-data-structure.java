import java.util.*;

class DList<T> {
    class Node {
        Node prev, next;
        T payload;

        Node(T payload) {
            this.payload = payload;
        }
    }

    Node head, tail;

    DList() {
        head = new Node(null);
        tail = new Node(null);
        head.next = tail;
        tail.prev = head;
    }

    Node add(Node pivot, T payload) {
        Node node = new Node(payload);
        node.prev = pivot;
        node.next = pivot.next;
        pivot.next.prev = node;
        pivot.next = node;
        return node;
    }

    Node remove(Node node) {
        if (node == head || node == tail) {
            throw new IllegalArgumentException("Cannot remove head or tail");
        }
        node.prev.next = node.next;
        node.next.prev = node.prev;
        return node;
    }

    Node last() {
        return tail.prev != head ? tail.prev : null;
    }

    Node first() {
        return head.next != tail ? head.next : null;
    }
}

class AllOne {

    class Bucket {

        int n;
        Set<String> keys = new HashSet<>();

        Bucket(int n) {
            this.n = n;
        }
    }

    Map<String, DList<Bucket>.Node> map = new HashMap<>();
    DList<Bucket> dlist = new DList<>();

    private void removeKey(DList<Bucket>.Node node, String key) {
        if (node == dlist.head || node == dlist.tail) return;
        node.payload.keys.remove(key);
        if (node.payload.keys.isEmpty()) dlist.remove(node);
    }

    public void inc(String key) {
        var pivot = map.getOrDefault(key, dlist.head);
        var next = pivot.next;
        int count = pivot == dlist.head ? 1 : pivot.payload.n + 1;
        if (next == dlist.tail || next.payload.n != count) {
            next = dlist.add(pivot, new Bucket(count));
        }
        removeKey(pivot, key);
        next.payload.keys.add(key);
        map.put(key, next);
    }

    public void dec(String key) {
        var pivot = map.getOrDefault(key, dlist.tail);
        if (pivot == dlist.tail) return;
        if (pivot.payload.n == 1) {
            map.remove(key);
        } else {
            var prev = pivot.prev;
            int count = pivot.payload.n - 1;
            if (prev == dlist.head || count != prev.payload.n) {
                prev = dlist.add(prev, new Bucket(count));
            }
            prev.payload.keys.add(key);
            map.put(key, prev);
        }
        removeKey(pivot, key);
    }

    public String getMaxKey() {
        var max = dlist.last();
        return max == null ? "" : max.payload.keys.iterator().next();
    }

    public String getMinKey() {
        var min = dlist.first();
        return min == null ? "" : min.payload.keys.iterator().next();
    }
}
