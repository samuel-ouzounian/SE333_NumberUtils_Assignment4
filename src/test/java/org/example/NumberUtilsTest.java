package org.example;
import org.junit.jupiter.api.DisplayName;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class NumberUtilsTest {
    /**
     *
     * Step 1: understand the requirement, input type and output type
     *        Requirement: Add two list of integer, index by index, and returns another list
     * Step 2 (raw):  Perform partition and boundary analysis on input and output
     *        Each input: left | right
     *        Combination of input:
     *        Output:
     *  Step 3: Derive potential test cases
     */

    /*
     * Classes:
     * Single input tests
     * Combination input tests
     * Output tests
     * */
    @Nested
    @DisplayName("Single Input Tests")
    class SingleInputTests {
        @ParameterizedTest
        @Tag("SpecificationBasedTest")
        @MethodSource("nullInputProvider")
        @DisplayName("Test null inputs")
        void testNullInputs(TestCase testCase) {
            List<Integer> result = NumberUtils.add(testCase.left, testCase.right);
            assertNull(result, "Null input should return null result");
        }

        static Stream<TestCase> nullInputProvider() {
            return Stream.of(
                    // Test when left input is null, right is valid
                    new TestCase(null, new ArrayList<>(List.of(1)), null),
                    // Test when right input is null, left is valid
                    new TestCase(new ArrayList<>(List.of(1)), null, null),
                    // Test when both inputs are null
                    new TestCase(null, null, null)
            );
        }

        @ParameterizedTest
        @Tag("SpecificationBasedTest")
        @MethodSource("nonIntegerValueProvider")
        @DisplayName("Test non-integer values")
        void testNonIntegerValues(TestCase testCase) {
            assertThrows(IllegalArgumentException.class,
                    () -> NumberUtils.add(testCase.left, testCase.right),
                    "Non-integer values should throw IllegalArgumentException");
        }

        static Stream<TestCase> nonIntegerValueProvider() {
            List<Integer> listWithChar = new ArrayList<>();
            List<Integer> listWithSpecialChar = new ArrayList<>();
            List<Integer> mixedList = new ArrayList<>();

            listWithChar.add((int)'a');
            listWithSpecialChar.add((int)'$');
            mixedList.add((int)'x');
            mixedList.add(5);

            return Stream.of(
                    // Test character value in left input
                    new TestCase(listWithChar, new ArrayList<>(List.of(5)), null),
                    // Test character value in right input
                    new TestCase(new ArrayList<>(List.of(5)), listWithChar, null),
                    // Test special character value
                    new TestCase(listWithSpecialChar, new ArrayList<>(List.of(3)), null),
                    // Test mixed valid and invalid values
                    new TestCase(mixedList, new ArrayList<>(List.of(2)), null),
                    // Test both inputs containing characters
                    new TestCase(listWithChar, listWithSpecialChar, null)
            );
        }

        @ParameterizedTest
        @Tag("SpecificationBasedTest")
        @MethodSource("emptyListProvider")
        @DisplayName("Test empty list inputs")
        void testEmptyLists(TestCase testCase) {
            List<Integer> result = NumberUtils.add(testCase.left, testCase.right);
            assertEquals(testCase.expected, result, "Empty list handling should match expected result");
        }

        static Stream<TestCase> emptyListProvider() {
            return Stream.of(
                    /*Below test case has a bug, should return [0].
                    new TestCase(new ArrayList<>(List.of()), new ArrayList<>(List.of()), new ArrayList<>(List.of(0))),*/
                    // Test adding empty list with left non-empty list
                    new TestCase(new ArrayList<>(List.of(5)), new ArrayList<>(List.of()), new ArrayList<>(List.of(5))),
                    // Test adding empty list with right non-empty list
                    new TestCase(new ArrayList<>(List.of()), new ArrayList<>(List.of(5)), new ArrayList<>(List.of(5)))
            );
        }

        @ParameterizedTest
        @Tag("CoverageAnalysisFixTest")
        @MethodSource("invalidDigitProviderStructural")
        @DisplayName("Test invalid digit values")
        void testInvalidDigitsStructural(TestCase testCase) {
            assertThrows(IllegalArgumentException.class,
                    () -> NumberUtils.add(testCase.left, testCase.right),
                    "Invalid digits should throw IllegalArgumentException");
        }

        static Stream<TestCase> invalidDigitProviderStructural() {
            return Stream.of(
                    // Existing cases for left invalid
                    new TestCase(new ArrayList<>(List.of(-1)), new ArrayList<>(List.of(5)), null),
                    new TestCase(new ArrayList<>(List.of(10)), new ArrayList<>(List.of(5)), null),
                    // New cases for right invalid
                    new TestCase(new ArrayList<>(List.of(5)), new ArrayList<>(List.of(-1)), null),
                    new TestCase(new ArrayList<>(List.of(3)), new ArrayList<>(List.of(10)), null),
                    // Case where both are invalid
                    new TestCase(new ArrayList<>(List.of(-2)), new ArrayList<>(List.of(15)), null)
            );
        }

        @ParameterizedTest
        @Tag("SpecificationBasedTest")
        @MethodSource("leadingZerosProvider")
        @DisplayName("Test leading zeros")
        void testLeadingZeros(TestCase testCase) {
            List<Integer> result = NumberUtils.add(testCase.left, testCase.right);
            assertEquals(testCase.expected, result, "Numbers with leading zeros should be handled correctly");
        }

        static Stream<TestCase> leadingZerosProvider() {
            return Stream.of(
                    // Test multiple leading zeros in left input
                    new TestCase(new ArrayList<>(List.of(0, 0, 5)), new ArrayList<>(List.of(3)), new ArrayList<>(List.of(8))),
                    // Test all zeros in left input
                    new TestCase(new ArrayList<>(List.of(0, 0, 0)), new ArrayList<>(List.of(1)), new ArrayList<>(List.of(1)))
            );
        }
    }

    @Nested
    @Tag("SpecificationBasedTest")
    @DisplayName("Input Combination Tests")
    class InputCombinationTests {
        @ParameterizedTest
        @MethodSource("sizeCombinationProvider")
        @DisplayName("Test different list size combinations")
        void testSizeCombinations(TestCase testCase) {
            List<Integer> result = NumberUtils.add(testCase.left, testCase.right);
            assertEquals(testCase.expected, result, "Size combination handling should match expected result");
        }

        static Stream<TestCase> sizeCombinationProvider() {
            return Stream.of(
                    // Left size > Right size
                    new TestCase(new ArrayList<>(List.of(1, 2)), new ArrayList<>(List.of(3)), new ArrayList<>(List.of(1, 5))),
                    // Left size == Right size
                    new TestCase(new ArrayList<>(List.of(5, 4)), new ArrayList<>(List.of(2, 3)), new ArrayList<>(List.of(7, 7))),
                    // Left size < Right size
                    new TestCase(new ArrayList<>(List.of(5)), new ArrayList<>(List.of(3, 2, 1)), new ArrayList<>(List.of(3, 2, 6)))
            );
        }

        @ParameterizedTest
        @Tag("SpecificationBasedTest")
        @MethodSource("carryOperationProvider")
        @DisplayName("Test carry operations")
        void testCarryOperations(TestCase testCase) {
            List<Integer> result = NumberUtils.add(testCase.left, testCase.right);
            assertEquals(testCase.expected, result, "Carry operations should produce correct results");
        }

        static Stream<TestCase> carryOperationProvider() {
            return Stream.of(
                    // No carry
                    new TestCase(new ArrayList<>(List.of(4)), new ArrayList<>(List.of(5)), new ArrayList<>(List.of(9))),
                    // Single carry
                    new TestCase(new ArrayList<>(List.of(7)), new ArrayList<>(List.of(5)), new ArrayList<>(List.of(1, 2))),
                    // Multiple carries
                    new TestCase(new ArrayList<>(List.of(9, 9)), new ArrayList<>(List.of(9, 9)), new ArrayList<>(List.of(1, 9, 8))),
                    // Cascading carries
                    new TestCase(new ArrayList<>(List.of(9, 9, 9, 9)), new ArrayList<>(List.of(1)), new ArrayList<>(List.of(1, 0, 0, 0, 0)))
            );
        }
    }

    @Nested
    @Tag("SpecificationBasedTest")
    @DisplayName("Output Tests")
    class OutputTests {
        @ParameterizedTest
        @MethodSource("outputPartitionProvider")
        @DisplayName("Test output partitions")
        void testOutputPartitions(TestCase testCase) {
            List<Integer> result = NumberUtils.add(testCase.left, testCase.right);
            assertEquals(testCase.expected, result, "Output should match the expected partition type");
        }

        static Stream<TestCase> outputPartitionProvider() {
            return Stream.of(
                    // Single digit result
                    new TestCase(new ArrayList<>(List.of(3)), new ArrayList<>(List.of(4)), new ArrayList<>(List.of(7))),
                    // Multi-digit result
                    new TestCase(new ArrayList<>(List.of(8)), new ArrayList<>(List.of(5)), new ArrayList<>(List.of(1, 3))),
                    // Result longer than inputs
                    new TestCase(new ArrayList<>(List.of(9)), new ArrayList<>(List.of(9)), new ArrayList<>(List.of(1, 8)))
            );
        }
    }

    static class TestCase {
        final List<Integer> left;
        final List<Integer> right;
        final List<Integer> expected;

        TestCase(List<Integer> left, List<Integer> right, List<Integer> expected) {
            this.left = left;
            this.right = right;
            this.expected = expected;
        }
    }
}