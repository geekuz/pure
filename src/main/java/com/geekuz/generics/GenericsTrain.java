package com.geekuz.generics;

import java.util.ArrayList;
import java.util.List;

public class GenericsTrain {
    
    public static void main(String[] args) {
        // Basic generic class usage
        Box<String> stringBox = new Box<>("Hello Generics");
        System.out.println(stringBox.getValue());
        
        Box<Integer> intBox = new Box<>(42);
        System.out.println(intBox.getValue());
        
        // Generic method example
        List<String> names = List.of("Alice", "Bob", "Charlie");
        printList(names);
        
        // Bounded type parameters
        NumberBox<Integer> intNumberBox = new NumberBox<>(100);
        System.out.println("Double value: " + intNumberBox.getDoubleValue());
        
        // Wildcard example
        List<Integer> numbers = List.of(1, 2, 3);
        printAnyList(numbers);
        printAnyList(names);

        // Type Erasure Examples
        TypeErasureExamples.demonstrate();
    }
    
    // Generic class
    static class Box<T> {
        private T value;
        
        public Box(T value) {
            this.value = value;
        }
        
        public T getValue() {
            return value;
        }
    }
    
    // Generic class with bounded type parameter
    static class NumberBox<T extends Number> {
        private T value;
        
        public NumberBox(T value) {
            this.value = value;
        }
        
        public double getDoubleValue() {
            return value.doubleValue();
        }
    }
    
    // Generic method
    static <E> void printList(List<E> list) {
        for (E element : list) {
            System.out.println("Element: " + element);
        }
    }
    
    // Wildcard usage
    static void printAnyList(List<?> list) {
        System.out.println("List contents: " + list);
    }

    // todo ------------------------------------------------------------------------------------------------------------------------------------------------------

    // Common type parameter names:
    // T - Type
    // E - Element
    // K - Key
    // V - Value
    // N - Number
    // S, U, V etc. - 2nd, 3rd, 4th types

    // Example with multiple type parameters
    static class Pair<K, V> {
        private K key;
        private V value;
        
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }
        
        // Getters
        public K getKey() { return key; }
        public V getValue() { return value; }
    }

    // Upper bound: T must be Number or a subclass of Number
    static class MathBox<T extends Number> {
        private T value;
        
        // Constructor
        public MathBox(T value) { this.value = value; }
        
        // Can use Number methods since T extends Number
        public double sqrt() { return Math.sqrt(value.doubleValue()); }
    }

    // Multiple bounds: T must implement Comparable AND Serializable
    static class SortableBox<T extends Comparable & java.io.Serializable> {
        private T value;
        
        public SortableBox(T value) { this.value = value; }
        
        public int compareTo(SortableBox<T> other) {
            return value.compareTo(other.value);
        }
    }

    // Unbounded wildcard: List<?> - any type of list
    static void printSize(List<?> list) {
        System.out.println("Size: " + list.size());
    }

    // Upper bounded wildcard: List<? extends Number> - List of Number or its subclasses
    static double sumOfList(List<? extends Number> list) {
        double sum = 0.0;
        for (Number n : list) {
            sum += n.doubleValue();
        }
        return sum;
    }

    // Lower bounded wildcard: List<? super Integer> - List of Integer or its superclasses
    static void addNumbers(List<? super Integer> list) {
        for (int i = 1; i <= 5; i++) {
            list.add(i); // Can add Integers safely
        }
    }

    // Type erasure means generics are removed at compile time
    // These methods would cause a compile error:

    /*
    // ERROR: Cannot overload methods that erase to same signature
    public static void process(List<String> list) { }
    public static void process(List<Integer> list) { }
    */

    // Type parameters can't be instantiated
    static <T> void incorrect() {
        // ERROR: Cannot instantiate type parameter T
        // T item = new T();
    }

    // Solution: Use Class<T> parameter
    static <T> T createInstance(Class<T> clazz) throws Exception {
        return clazz.getDeclaredConstructor().newInstance();
    }

    // "Producer Extends" - when you only read from a collection
    static void readOnly(List<? extends Number> numbers) {
        // Can read Numbers
        Number first = numbers.get(0);
        // But can't add - compiler doesn't know which specific subtype
        // numbers.add(new Integer(42)); // ERROR
    }

    // "Consumer Super" - when you only write to a collection
    static void writeOnly(List<? super Integer> numbers) {
        // Can add Integers
        numbers.add(42);
        // But reading gives only Objects
        Object obj = numbers.get(0); // Only know it's an Object
    }

    // Type Erasure Examples
    static class TypeErasureExamples {
        public static void demonstrate() {
            // Example 1: List<String> becomes List at runtime
            List<String> stringList = new ArrayList<>();
            List<Integer> intList = new ArrayList<>();
            
            // This prints true because at runtime both are just ArrayList
            System.out.println("Same class? " + 
                (stringList.getClass() == intList.getClass()));
            
            // Example 2: Generic type information is not available at runtime
            try {
                // This won't work - can't check if it's List<String>
                // if (stringList instanceof List<String>) {} // Compile error
                
                // Can only check if it's a List
                if (stringList instanceof List) {
                    System.out.println("It's a List (but type is erased)");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Type erasure examples continued
    static class ErasureDetails {
        // Before erasure: public <T> T getValue(List<T> list)
        // After erasure: public Object getValue(List list)
        public static <T> T getValue(List<T> list) {
            return list.get(0);
            // Compiler inserts cast: (T)list.get(0)
        }
        
        // Before erasure: Box<T extends Number>
        // After erasure: Box with T replaced by Number
        static class NumberBox<T extends Number> {
            private T value;
            
            public NumberBox(T value) {
                this.value = value;
            }
            
            public T getValue() {
                return value; // Compiler inserts cast: (T)value
            }
        }
    }

    // Limitations due to type erasure
    static class TypeErasureLimitations {
        // 1. Cannot overload methods that would have the same erasure
        public static void process(List<String> strings) {
            System.out.println("Processing strings");
        }
        
        // This would cause a compile error - same erasure as above
        // public static void process(List<Integer> integers) {
        //     System.out.println("Processing integers");
        // }
        
        // 2. Cannot create instances of type parameters
        public static <T> void createInstance() {
            // Cannot do this:
            // T instance = new T();  // Compile error
            
            // Workaround using Class<T>
            // T instance = clazz.newInstance();
        }
        
        // 3. Cannot use primitive types as type arguments
        // List<int> intList; // Compile error
        // Must use wrapper: List<Integer> intList;
        
        // 4. Cannot create arrays of parameterized types
        // Generic<String>[] array = new Generic<String>[10]; // Compile error
    }

    // Reifiable vs. Non-reifiable types
    static class ReifiableExample {
        public static void demonstrate() {
            // Reifiable types - type info available at runtime
            String[] strings = new String[10];
            Integer[] integers = new Integer[10];
            
            // This works - can check array component type
            System.out.println("Array component: " + 
                strings.getClass().getComponentType());
            
            // Non-reifiable types - type info lost at runtime
            List<String> stringList = new ArrayList<>();
            
            // Cannot do this:
            // System.out.println("List component: " + 
            //     stringList.getClass().getGenericComponentType()); // No such method
            
            // Can only get raw type
            System.out.println("List raw type: " + stringList.getClass());
        }
    }
}
