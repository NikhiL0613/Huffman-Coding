import java.util.*;

class HuffmanNode implements Comparable<HuffmanNode> {
    char ch;
    int freq;
    HuffmanNode left, right;

    public HuffmanNode(char ch, int freq) {
        this.ch = ch;
        this.freq = freq;
    }

    public int compareTo(HuffmanNode node) {
        return this.freq - node.freq;
    }
}

public class HuffmanCoding {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter the length of the message: ");
        int length = scanner.nextInt();
        scanner.close();

        // Generate a random message of the specified length
        String message = generateRandomMessage(length);

        long startTime = System.nanoTime();
        
        Map<Character, String> codes = huffmanEncode(message);
        String encodedMessage = encodeMessage(message, codes);
        
        long endTime = System.nanoTime();

        printResults(message, codes, encodedMessage);
        
        double duration = (endTime - startTime) / 1_000_000.0;
        System.out.printf("\nTime taken: %.3f ms%n", duration);
        
        System.out.println("\nTime Complexity:");
        System.out.println("- Building frequency map: O(n), where n is the length of the message");
        System.out.println("- Building Huffman tree: O(k log k), where k is the number of unique characters");
        System.out.println("- Generating codes: O(k)");
        System.out.println("- Encoding message: O(n)");
        System.out.println("Overall time complexity: O(n + k log k)");
    }

    private static String generateRandomMessage(int length) {
        StringBuilder message = new StringBuilder(length);
        Random random = new Random();
        
        // Generate a random message using uppercase letters A-Z
        for (int i = 0; i < length; i++) {
            char randomChar = (char) ('A' + random.nextInt(26)); // Random char between A-Z
            message.append(randomChar);
        }
        
        return message.toString();
    }

    private static Map<Character, String> huffmanEncode(String message) {
        Map<Character, Integer> frequencyMap = buildFrequencyMap(message);
        PriorityQueue<HuffmanNode> pq = new PriorityQueue<>();
        
        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            pq.offer(new HuffmanNode(entry.getKey(), entry.getValue()));
        }

        while (pq.size() > 1) {
            HuffmanNode left = pq.poll();
            HuffmanNode right = pq.poll();
            HuffmanNode parent = new HuffmanNode('\0', left.freq + right.freq);
            parent.left = left;
            parent.right = right;
            pq.offer(parent);
        }

        Map<Character, String> codes = new HashMap<>();
        generateCodes(pq.peek(), "", codes);
        return codes;
    }

    private static Map<Character, Integer> buildFrequencyMap(String message) {
        Map<Character, Integer> frequencyMap = new HashMap<>();
        for (char c : message.toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }
        return frequencyMap;
    }

    private static void generateCodes(HuffmanNode node, String code, Map<Character, String> codes) {
        if (node != null) {
            if (node.ch != '\0') {
                codes.put(node.ch, code);
            }
            generateCodes(node.left, code + "0", codes);
            generateCodes(node.right, code + "1", codes);
        }
    }

    private static String encodeMessage(String message, Map<Character, String> codes) {
        StringBuilder encodedMessage = new StringBuilder();
        for (char c : message.toCharArray()) {
            encodedMessage.append(codes.get(c));
        }
        return encodedMessage.toString();
    }

    private static void printResults(String message, Map<Character, String> codes, String encodedMessage) {
        System.out.println("\nOriginal Message: " + message);
        System.out.println("Encoded Message: " + encodedMessage);
        
        System.out.println("\nHuffman Codes:");
        for (Map.Entry<Character, String> entry : codes.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        int originalSize = message.length() * 8; // Size in bits
        int encodedSize = encodedMessage.length(); // Size in bits
        int compressionRatio = (int) (100 - ((double) encodedSize / originalSize) * 100);

        System.out.println("\nOriginal size: " + originalSize + " bits");
        System.out.println("Encoded size: " + encodedSize + " bits");
        System.out.println("Compression ratio: " + compressionRatio + "%");
    }
}
