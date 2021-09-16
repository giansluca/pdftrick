package org.gmdev.pdftrick;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static org.assertj.core.api.Assertions.assertThat;

public class SumTest {

    @Test
    void itShouldDoTheJob() {
        // Given
        List<Integer> amountList = getAmountList();
        Integer sum = 11;

        // When
        Subset subset = new Subset(amountList, sum);
        subset.solve(0, 0);
        List<List<Integer>> solutionSubsets = subset.getSolutionSubsets();

        // Then
        assertThat(solutionSubsets).isNotEmpty().hasSize(7);
    }

    @Test
    void itShouldDoTheJobWithNegativeNumbers() {
        // Given
        List<Integer> amountList = getAmountListNegative();
        Integer sum = -12;

        // When
        Subset subset = new Subset(amountList, sum);
        subset.solve(0, 0);
        List<List<Integer>> solutionSubsets = subset.getSolutionSubsets();

        // Then
        assertThat(solutionSubsets).isNotEmpty().hasSize(2);
    }

    static class Subset {
        List<Integer> amountList;
        Integer sum;
        Stack<Integer> solutionStack;
        List<List<Integer>> solutionSubsets;
        boolean foundSubset;
        boolean positive;

        public Subset(List<Integer> amountList, Integer sum) {
            this.amountList = amountList;
            this.sum = sum;
            this.solutionStack = new Stack<>();
            this.foundSubset = false;
            this.solutionSubsets = new ArrayList<>();
            this.positive = sum >= 0;
        }

        public void solve(Integer actualSum, int index) {
            // return false if actualSum value exceed sum
            if (positive && actualSum > sum) return;
            if (!positive && actualSum < sum) return;

            // check if stack has the right subsets of numbers
            if (actualSum.equals(sum)) {
                foundSubset = true;
                solutionSubsets.add(new ArrayList<>(solutionStack));

                return;
            }

            for (int i = index; i < amountList.size(); i++) {
                // add element to the stack
                Integer element = amountList.get(i);
                solutionStack.push(element);

                // add amountList[i] to the 'actualSum' and recursively start from next number
                solve(actualSum + element, i + 1);

                // remove element from stack (Backtracking)
                solutionStack.pop();
            }
        }

        public List<List<Integer>> getSolutionSubsets() {
            return solutionSubsets;
        }

    }

    List<Integer> getAmountList() {
        List<Integer> amountList = new ArrayList<>();
        amountList.add(4);
        amountList.add(10);
        amountList.add(7);
        amountList.add(6);
        amountList.add(12);
        amountList.add(4);
        amountList.add(1);
        amountList.add(3);
        return amountList;
    }

    List<Integer> getAmountListNegative() {
        List<Integer> amountList = new ArrayList<>();
        amountList.add(-4);
        amountList.add(-7);
        amountList.add(-6);
        amountList.add(-12);
        amountList.add(-1);
        amountList.add(-3);
        return amountList;
    }
}
