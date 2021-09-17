package org.gmdev.pdftrick;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static org.assertj.core.api.Assertions.assertThat;

public class SumTest {

    @ParameterizedTest
    @CsvSource({
            "11, 4",
            "11.0, 4",
            "11.000, 4",
            "011, 4",
            "011.0, 4",
            "16.25, 1",
            "17.25, 2",
            "0.72, 1",
            "999, 0",
    })
    void itShouldFindTheSubsetsWithPositiveNumbers(BigDecimal sumToFind, int expectedSubsets) {
        // Given
        List<BigDecimal> amountList = getAmountListPositive();
        BigDecimal sum = sumToFind.setScale(2,RoundingMode.CEILING);

        // When
        Subset subset = new Subset(amountList, sum);
        BigDecimal zero = BigDecimal.ZERO.setScale(2, RoundingMode.CEILING);
        subset.solve(zero, 0);
        List<List<BigDecimal>> solutionSubsets = subset.getSolutionSubsets();

        // Then
        assertThat(solutionSubsets).hasSize(expectedSubsets);
    }

    @ParameterizedTest
    @CsvSource({
            "-12, 2",
            "-12.0, 2",
            "-12.00, 2",
            "-0012.00, 2",
            "-153.30, 1",
            "-9.21, 1",
            "-9.210, 1",
            "-09.21, 1",
            "-999, 0",
    })
    void itShouldFindTheSubsetsWithNegativeNumbers(BigDecimal sumToFind, int expectedSubsets) {
        // Given
        List<BigDecimal> amountList = getAmountListNegative();
        BigDecimal sum = sumToFind.setScale(2,RoundingMode.CEILING);

        // When
        Subset subset = new Subset(amountList, sum);
        BigDecimal zero = BigDecimal.ZERO.setScale(2, RoundingMode.CEILING);
        subset.solve(zero, 0);
        List<List<BigDecimal>> solutionSubsets = subset.getSolutionSubsets();

        // Then
        assertThat(solutionSubsets).hasSize(expectedSubsets);
    }

    static class Subset {
        List<BigDecimal> amountList;
        BigDecimal sum;
        Stack<BigDecimal> solutionStack;
        List<List<BigDecimal>> solutionSubsets;
        boolean foundSubset;
        boolean positive;

        public Subset(List<BigDecimal> amountList, BigDecimal sum) {
            this.amountList = amountList;
            this.sum = sum;
            this.solutionStack = new Stack<>();
            this.foundSubset = false;
            this.solutionSubsets = new ArrayList<>();
            this.positive = sum.compareTo(BigDecimal.ZERO) > 0;
        }

        public void solve(BigDecimal actualSum, int index) {
            // return false if actualSum value exceed sum
            if (positive && actualSum.compareTo(sum) > 0) return;
            if (!positive && actualSum.compareTo(sum) < 0) return;

            // check if stack has the right subsets of numbers to match the expected sum
            if (actualSum.equals(sum)) {
                foundSubset = true;
                solutionSubsets.add(new ArrayList<>(solutionStack));

                return;
            }

            for (int i = index; i < amountList.size(); i++) {
                // add element to the stack
                BigDecimal element = amountList.get(i).setScale(2, RoundingMode.CEILING);
                solutionStack.push(element);

                // add amountList[i] to the 'actualSum' and recursively start from next number
                solve(actualSum.add(element), i + 1);

                // remove element from stack (backtracking)
                solutionStack.pop();
            }
        }

        public List<List<BigDecimal>> getSolutionSubsets() {
            return solutionSubsets;
        }
    }

    List<BigDecimal> getAmountListPositive() {
        List<BigDecimal> amountList = new ArrayList<>();
        amountList.add(new BigDecimal("4.30"));
        amountList.add(new BigDecimal("10"));
        amountList.add(new BigDecimal("7.00"));
        amountList.add(new BigDecimal("6"));
        amountList.add(new BigDecimal("12.15"));
        amountList.add(new BigDecimal("4.0"));
        amountList.add(new BigDecimal("01.0"));
        amountList.add(new BigDecimal("1.10"));
        amountList.add(new BigDecimal("003"));
        amountList.add(new BigDecimal("0.18"));
        amountList.add(new BigDecimal("0.54"));
        return amountList;
    }

    List<BigDecimal> getAmountListNegative() {
        List<BigDecimal> amountList = new ArrayList<>();
        amountList.add(new BigDecimal("-04.00"));
        amountList.add(new BigDecimal("-7"));
        amountList.add(new BigDecimal("-6.00"));
        amountList.add(new BigDecimal("-12.0"));
        amountList.add(new BigDecimal("-1"));
        amountList.add(new BigDecimal("-03"));
        amountList.add(new BigDecimal("-150.30"));
        amountList.add(new BigDecimal("-3.40"));
        amountList.add(new BigDecimal("-5.81"));
        return amountList;
    }

}
