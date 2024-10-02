package features.actions;

import java.util.List;

/**
 * The ActionsManager interface defines methods for performing the operations on a list
 * of floating-point numbers defined in the Queo's Coding Game Task: sum, min, max and filter
 */
public interface ActionsManager {

    /**
     * Calculates the sum of all floating point numbers in the provided list.
     *
     * @param inputList a List of Float values which are to be summed
     * @return the sum of the floating point numbers of float type
     * @throws IllegalArgumentException if the inputList is null or empty
     */
    float sum(List<Float> inputList);

    /**
     * Finds the minimum value in the provided list.
     *
     * @param inputList a list of Float values to search for the minimum
     * @return the minimum value in the input list or returns 0 if the list is empty or null
     */
    float min(List<Float> inputList);

    /**
     * Finds the maximum value in the provided list.
     *
     * @param inputList a list of Float values to search for the maximum
     * @return the maximum value in the input list or 0 if the list is empty or null
     */
    float max(List<Float> inputList);

    /**
     * Filters the provided list based on certain condition
     * and returns the list with the float values which satisfy the condition.
     *
     * @param inputList a list of Float values to be filtered
     * @return a filtered list result with the elements from the list that satisfy the condition,
     * or empty list if the input list is empty or null
     */
    List<Float> filter(List<Float> inputList);
}
