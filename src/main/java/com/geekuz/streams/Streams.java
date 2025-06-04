package com.geekuz.streams;

import java.util.Arrays;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Streams {
    public static void main(String[] args) {
        // From collection
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
        Stream<String> streamFromCollection = names.stream();

        // From array
        String[] array = {"Alice", "Bob", "Charlie"};
        Stream<String> streamFromArray = Arrays.stream(array);

        // Stream.of
        Stream<Integer> streamOfValues = Stream.of(1, 2, 3, 4, 5);

        // Generate infinite stream
        Stream<Double> randomStream = Stream.generate(Math::random).limit(5);

        // IntStream, LongStream, DoubleStream for primitives
        IntStream intStream = IntStream.range(1, 6); // 1, 2, 3, 4, 5
    }
    public static void demonstrateOperations() {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David", "Alice");
        
        // Intermediate operations (return a new stream)
        // filter, map, flatMap, distinct, sorted, peek, limit, skip
        
        // Terminal operations (produce a result)
        // forEach, toArray, reduce, collect, min, max, count, anyMatch, allMatch, noneMatch, findFirst, findAny
        
        // Example pipeline
        long count = names.stream()
                .filter(name -> name.startsWith("A"))
                .distinct()
                .count();
        System.out.println("Count of distinct names starting with A: " + count);
    }
    public static void demonstrateIntermediateOperations() {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David", "Eve");
        
        // filter - keeps elements that match the predicate
        List<String> filtered = names.stream()
                .filter(name -> name.length() > 3)
                .collect(Collectors.toList());
        System.out.println("Filtered names (length > 3): " + filtered);
        
        // map - transforms each element
        List<Integer> nameLengths = names.stream()
                .map(String::length)
                .collect(Collectors.toList());
        System.out.println("Name lengths: " + nameLengths);
        
        // flatMap - flattens nested collections
        List<List<Integer>> nestedNumbers = Arrays.asList(
                Arrays.asList(1, 2), 
                Arrays.asList(3, 4, 5)
        );
        List<Integer> flattenedNumbers = nestedNumbers.stream()
                .flatMap(List::stream)
                .toList();
        System.out.println("Flattened numbers: " + flattenedNumbers);
        
        // distinct - removes duplicates
        List<String> namesWithDuplicates = Arrays.asList("Alice", "Bob", "Alice", "David");
        List<String> distinctNames = namesWithDuplicates.stream()
                .distinct()
                .toList();
        System.out.println("Distinct names: " + distinctNames);
        
        // sorted - sorts elements
        List<String> sortedNames = names.stream()
                .sorted()
                .toList();
        System.out.println("Sorted names: " + sortedNames);
        
        // peek - performs action on each element without modifying the stream
        List<String> peekResult = names.stream()
                .peek(name -> System.out.println("Processing: " + name))
                .toList();
    }
    public static void demonstrateTerminalOperations() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David");
        
        // forEach - performs action on each element
        System.out.println("forEach example:");
        numbers.forEach(n -> System.out.print(n + " "));
        System.out.println();
        
        // collect - gathers elements into a collection
        Map<Integer, List<String>> namesByLength = names.stream()
                .collect(Collectors.groupingBy(String::length));
        System.out.println("Names grouped by length: " + namesByLength);
        
        // reduce - combines elements
        Optional<Integer> sum = numbers.stream()
                .reduce((a, b) -> a + b);
        System.out.println("Sum using reduce: " + sum.orElse(0));
        
        // count, min, max
        long count = names.stream().count();
        Optional<String> shortest = names.stream().min((a, b) -> a.length() - b.length());
        Optional<String> longest = names.stream().max(Comparator.comparing(String::length));
        System.out.println("Count: " + count + ", Shortest: " + shortest.orElse("") + 
                           ", Longest: " + longest.orElse(""));
        
        // anyMatch, allMatch, noneMatch
        boolean anyStartsWithA = names.stream().anyMatch(name -> name.startsWith("A"));
        boolean allLongerThan2 = names.stream().allMatch(name -> name.length() > 2);
        boolean noneStartWithZ = names.stream().noneMatch(name -> name.startsWith("Z"));
        System.out.println("Any starts with A: " + anyStartsWithA + 
                           ", All longer than 2: " + allLongerThan2 + 
                           ", None start with Z: " + noneStartWithZ);
        
        // findFirst, findAny
        Optional<String> first = names.stream().findFirst();
        Optional<String> any = names.stream().findAny();
        System.out.println("First: " + first.orElse("") + ", Any: " + any.orElse(""));
    }
    public static void demonstrateParallelStreams() {
        List<Integer> numbers = IntStream.rangeClosed(1, 1000000)
                .boxed()
                .toList();
        
        // Sequential processing
        long startSeq = System.currentTimeMillis();
        long sumSeq = numbers.stream()
                .filter(n -> n % 2 == 0)
                .mapToLong(Integer::longValue)
                .sum();
        long endSeq = System.currentTimeMillis();
        
        // Parallel processing
        long startPar = System.currentTimeMillis();
        long sumPar = numbers.parallelStream()
                .filter(n -> n % 2 == 0)
                .mapToLong(Integer::longValue)
                .sum();
        long endPar = System.currentTimeMillis();
        
        System.out.println("Sequential sum: " + sumSeq + " took " + (endSeq - startSeq) + "ms");
        System.out.println("Parallel sum: " + sumPar + " took " + (endPar - startPar) + "ms");
    }
    public static void demonstrateCollectors() {
        List<Product> products = Arrays.asList(
                new Product("Phone", "Electronics", 999.99),
                new Product("Laptop", "Electronics", 1999.99),
                new Product("Book", "Books", 19.99),
                new Product("Tablet", "Electronics", 499.99),
                new Product("Magazine", "Books", 9.99)
        );
        
        // Collecting to List
        List<String> productNames = products.stream()
                .map(Product::getName)
                .toList();
        System.out.println("Product names: " + productNames);
        
        // Collecting to Set (removes duplicates)
        Set<String> categories = products.stream()
                .map(Product::getCategory)
                .collect(Collectors.toSet());
        System.out.println("Categories: " + categories);
        
        // Collecting to Map
        Map<String, Double> priceByName = products.stream()
                .collect(Collectors.toMap(Product::getName, Product::getPrice));
        System.out.println("Prices by name: " + priceByName);
        
        // Grouping
        Map<String, List<Product>> productsByCategory = products.stream()
                .collect(Collectors.groupingBy(Product::getCategory));
        System.out.println("Products by category: " + productsByCategory);
        
        // Partitioning
        Map<Boolean, List<Product>> expensiveProducts = products.stream()
                .collect(Collectors.partitioningBy(p -> p.getPrice() > 500));
        System.out.println("Expensive products: " + expensiveProducts.get(true));
        
        // Summarizing
        DoubleSummaryStatistics priceStats = products.stream()
                .collect(Collectors.summarizingDouble(Product::getPrice));
        System.out.println("Price statistics: " + priceStats);
        
        // Joining
        String namesList = products.stream()
                .map(Product::getName)
                .collect(Collectors.joining(", "));
        System.out.println("Products: " + namesList);
    }

    // Simple Product class for examples
    static class Product {
        private String name;
        private String category;
        private double price;
        
        public Product(String name, String category, double price) {
            this.name = name;
            this.category = category;
            this.price = price;
        }
        
        public String getName() { return name; }
        public String getCategory() { return category; }
        public double getPrice() { return price; }
        
        @Override
        public String toString() {
            return name + " (" + price + ")";
        }
    }
}
