import java.util.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class Trie {
		static class TrieNode {
				boolean isWord;
				Map<Character, TrieNode> edges;
				public TrieNode(){
						this.edges = new HashMap<>();
				}
		}
		final TrieNode root = new TrieNode();

		Trie add(String word){
				var node = root;
				for(char c :  word.toCharArray()){
						node.edges.computeIfAbsent(c, k -> new TrieNode());
						node = node.edges.get(c);
				}
				node.isWord = true;
				return this;
		}

		boolean find(String word){
				var node = root;
				for ( char c : word.toCharArray() ){
						if( node.edges.containsKey(c) ){
								node = node.edges.get(c);
						} else {
								return false;
						}
				}
				return true;
		}
}

class TrieTests{ 

		@Nested
		@DisplayName("A trie")
		class BasicOperations {

				@Test
				@DisplayName("can have words added and searched for")
				void addWord(){
						var trie = new Trie();
						trie.add("banana").add("bank");

						assertTrue(trie.find("banana"));
						assertTrue(trie.find("bank"));
				}
		}
}
