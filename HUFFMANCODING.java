import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

class HuffmanNode {
    int frequency;
    char symbol;
    HuffmanNode left, right;

    HuffmanNode(char symbol, int frequency) {
        this.symbol = symbol;
        this.frequency = frequency;
        this.left = null;
        this.right = null;
    }

    HuffmanNode(int frequency) {
        this.symbol = '-'; // No symbol for internal nodes
        this.frequency = frequency;
        this.left = null;
        this.right = null;
    }
}

class HuffmanComparator implements Comparator<HuffmanNode> {
    public int compare(HuffmanNode node1, HuffmanNode node2) {
        return node1.frequency - node2.frequency;
    }
}

public class HuffmanCoding {
    public static Map<Character, String> buildHuffmanTree(char[] symbols, int[] frequencies) {
        PriorityQueue<HuffmanNode> pq = new PriorityQueue<>(new HuffmanComparator());

        for (int i = 0; i < symbols.length; i++) {
            pq.add(new HuffmanNode(symbols[i], frequencies[i]));
        }
        while (pq.size() > 1) {
            HuffmanNode left = pq.poll();
            HuffmanNode right = pq.poll();

            HuffmanNode newNode = new HuffmanNode(left.frequency + right.frequency);
            newNode.left = left;
            newNode.right = right;

            pq.add(newNode);
        }

        HuffmanNode root = pq.poll();

        Map<Character, String> huffmanCodes = new HashMap<>();
        generateCodes(root, "", huffmanCodes);

        return huffmanCodes;
    }

    private static void generateCodes(HuffmanNode node, String code, Map<Character, String> huffmanCodes) {
        if (node == null) {
            return;
        }

        if (node.left == null && node.right == null) {
            huffmanCodes.put(node.symbol, code);
        }

        generateCodes(node.left, code + "0", huffmanCodes);
        generateCodes(node.right, code + "1", huffmanCodes);
    }

    public static void main(String[] args) {
        int[] inputSizes = {10, 100, 1000, 10000, 100000};
        Random random = new Random();

        for (int size : inputSizes) {
            // Generate random symbols (A, B, C, ..., Z) and random frequencies
            char[] symbols = new char[size];
            int[] frequencies = new int[size];

            for (int i = 0; i < size; i++) {
                symbols[i] = (char) ('A' + (i % 26)); // Reuse letters A-Z
                frequencies[i] = random.nextInt(100) + 1; // Random frequency between 1 and 100
            }

            long startTime = System.nanoTime();
            Map<Character, String> huffmanCodes = buildHuffmanTree(symbols, frequencies);
            long endTime = System.nanoTime();
            long duration = endTime - startTime; // Duration in nanoseconds

            System.out.printf("Input Size: %d | Time taken to build Huffman tree: %d nanoseconds%n", size, duration);
        }

        System.out.println("Estimated Time Complexity: O(n log n)");
    }
}
