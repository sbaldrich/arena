import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Clock;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

interface LetterBoxedSolver {
    static void main(String... args) {
        if(args.length != 1) {
            System.err.println("Usage: java LetterBoxedSolver <comma separated game configuration e.g SME,OIQ,UHT,JAR>");
            return;
        }
        var sides = args[0].toLowerCase().split(",");
        try (var words = Files.lines(Paths.get("words_alpha.txt"))) {
            var trie = new Trie();
            words.forEach(trie::add);
            var extractor = new Extractor(sides);
            trie.dfs(extractor);
            var nodes =
                    extractor.words.stream()
                            .sorted(Comparator.comparingInt(String::length).reversed())
                            .filter(w -> w.length() > 2);

            Solver.solve(nodes.toList(), sides).forEach(System.out::println);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    class Trie {
        char character;
        boolean isWord;
        int depth;
        int descendants;
        final Trie[] next = new Trie[26];

        Trie() {
            this('✨', 0);
        }

        private Trie(char c, int depth) {
            this.character = c;
            this.depth = depth;
        }

        void dfs(TrieVisitor v) {
            if (v.preVisit(this)) {
                v.visit(this);
                for (Trie t : next) {
                    if (t != null)
                        t.dfs(v);
                }
                v.postVisit(this);
            }
        }

        Trie add(String word) {
            add(word.toCharArray(), 0);
            return this;
        }

        void add(char[] word, int pos) {
            if (pos >= word.length) {
                isWord = true;
                return;
            }
            char nc = (char) (word[pos] - 'a');
            if (next[nc] == null) {
                next[nc] = new Trie(word[pos], depth + 1);
                descendants++;
            }
            next[nc].add(word, pos + 1);
        }
    }
}

class Solver {
    static long mask(String s) {
        long m = 0L;
        for (char c : s.toCharArray()) {
            m |= 1L << (c - 'a');
        }
        return m;
    }

    static List<List<String>> solve(final List<String> words, final String[] sides) {
        long goal = 0;
        for (String side : sides) {
            goal |= mask(side);
        }
        record SW(int index, long mask, char c) {
        }
        record S(long mask, List<Integer> moves) {
        }
        record W(int pos, long mask) {
        }

        Map<Character, List<W>> index = new HashMap<>();

        int best = Integer.MAX_VALUE;
        var queue = new ArrayDeque<S>();
        IntStream.range(0, words.size())
                .mapToObj(i -> {
                    var w = words.get(i);
                    return new SW(i, mask(w), w.charAt(0));
                })
                .forEach(sw -> {
                    queue.add(new S(sw.mask, List.of(sw.index)));
                    index.putIfAbsent(sw.c, new ArrayList<>());
                    index.get(sw.c).add(new W(sw.index, sw.mask));
                });
        var solutions = new ArrayList<List<String>>();

        while (!queue.isEmpty()) {
            var next = queue.poll();
            assert next != null;
            if (next.moves.size() > best) {
                continue;
            }
            if (next.mask == goal) {
                best = Math.min(best, next.moves.size());
                solutions.add(next.moves.stream().map(words::get).collect(Collectors.toList()));
                continue;
            }
            var word = words.get(next.moves.get(next.moves.size() - 1));
            index.getOrDefault(word.charAt(word.length() - 1), Collections.emptyList())
                    .stream()
                    .map(w -> new S(next.mask | w.mask, Stream.concat(next.moves.stream(), Stream.of(w.pos)).collect(Collectors.toList())))
                    .filter(s -> s.mask > next.mask) // only add it if we're making progress
                    .forEach(queue::add);
        }
        return solutions;
    }
}

interface TrieVisitor {
    boolean preVisit(LetterBoxedSolver.Trie t);

    void visit(LetterBoxedSolver.Trie t);

    void postVisit(LetterBoxedSolver.Trie t);
}

class OutputVisitor implements TrieVisitor {
    private void write(String s) {
        try {
            out.write(s);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    final BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
    static int MAX_DEPTH = 30;
    final int[] nodesLeft = new int[MAX_DEPTH];
    final int[] depth = new int[MAX_DEPTH];
    int currentLevel = 0;
    boolean newLevel = true;

    public boolean preVisit(LetterBoxedSolver.Trie t) {
        if (currentLevel == 0) {
            nodesLeft[currentLevel] = 1;
            nodesLeft[currentLevel + 1] = t.descendants;
        } else if (t.descendants > 1 || (t.descendants == 1 && t.isWord)) {
            nodesLeft[currentLevel + 1] = t.descendants;
            depth[currentLevel] = depth[currentLevel - 1] + currentLevel + (t.depth - depth[currentLevel - 1]);
        }
        return true;
    }

    public void visit(LetterBoxedSolver.Trie t) {
        if (newLevel) {
            newLevel = false;
            if (currentLevel > 0) {
                for (int i = 0; i < currentLevel - 1; i++) {
                    write(nodesLeft[i + 1] > 0 ? "│" : " ");
                    write(" ".repeat(depth[i + 1] - depth[i] - 1));
                }

                if (currentLevel > 0) {
                    write(nodesLeft[currentLevel] <= 1 ? "└─" : "├─");
                }
            }
            nodesLeft[currentLevel]--;
        }

        write("%s%s".formatted(t.character, t.isWord && t.descendants > 0 ? " *" : ""));
        if (currentLevel == 0 || t.descendants > 1 || (t.descendants == 1 && t.isWord)) {
            currentLevel++;
            newLevel = true;
            write("\n");
        } else if (t.isWord) {
            write("\n");
        }
    }

    public void postVisit(LetterBoxedSolver.Trie t) {
        if (t.descendants > 1 || (t.descendants == 1 && t.isWord)) {
            currentLevel--;
        }
        newLevel = true;
    }
};

class Extractor implements TrieVisitor {

    private Integer blocked;
    private StringBuilder sb;
    private Map<String, Integer> index;
    public List<String> words;

    public Extractor(String... lines) {
        index = IntStream.range(0, lines.length)
                .mapToObj(i -> Map.entry(lines[i], i))
                .flatMap(e -> e.getKey().codePoints().mapToObj(c -> Map.entry(Character.toString(c), e.getValue())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        words = new ArrayList<>();
        sb = new StringBuilder();
    }

    @Override
    public boolean preVisit(LetterBoxedSolver.Trie t) {
        if (t.depth == 0 || t.depth == 1 && index.containsKey(String.valueOf(t.character))) {
            return true;
        }
        return index.containsKey(String.valueOf(t.character)) && !index.getOrDefault(String.valueOf(t.character), blocked).equals(blocked);
    }

    @Override
    public void visit(LetterBoxedSolver.Trie t) {
        if (t.depth > 0) sb.append(t.character);
        if (t.isWord) {
            words.add(sb.toString());
        }
        blocked = index.get(String.valueOf(t.character));
        assert null != blocked;
    }

    @Override
    public void postVisit(LetterBoxedSolver.Trie t) {
        if (t.depth > 0) {
            sb.deleteCharAt(sb.length() - 1);
            if (!sb.isEmpty()) {
                blocked = index.get(String.valueOf(sb.charAt(sb.length() - 1)));
            }
        }
    }
}