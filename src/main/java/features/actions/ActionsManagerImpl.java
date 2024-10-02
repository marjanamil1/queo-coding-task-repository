package features.actions;

import features.actions.ActionsManager;

import java.util.ArrayList;
import java.util.List;

public class ActionsManagerImpl implements ActionsManager {

    /**
     * Calculates the sum of all values in the provided list of float type numbers.
     *
     * @param inputList a list of Float values to be summed
     * @return the sum of the values in the input list or 0 if the list is empty or null
     */
    @Override
    public float sum(List<Float> inputList) {
        if (inputList == null || inputList.isEmpty()) {
            return 0;
        }
        float sum = 0;
        for (Float value : inputList) {
            if (value != null) {
                sum += value;
            }
        }
        return sum;
    }

    /**
     * Implementation of finding the minimum value in the provided list.
     *
     * @param inputList a list of Float values to search for the minimum
     * @return the minimum value in the input list or returns 0 if the list is empty or null
     */
    @Override
    public float min(List<Float> inputList) {
        if (inputList == null || inputList.isEmpty()) {
            return 0;
        }
        float minValue = Float.MAX_VALUE;
        for (Float value : inputList) {
            if (value != null && value < minValue) {
                minValue = value;
            }
        }
        return minValue == Float.MAX_VALUE ? 0 : minValue;
    }

    /**
     * Implementation of finding the maximum value in the provided list.
     *
     * @param inputList a list of Float values to search for the maximum
     * @return the maximum value in the input list or 0 if the list is empty or null
     */
    @Override
    public float max(List<Float> inputList) {
        if (inputList == null || inputList.isEmpty()) {
            return 0;
        }

        float maxValue = Float.MIN_VALUE;
        boolean foundValidValue = false;
        for (Float value : inputList) {
            if (value != null) {
                foundValidValue = true;
                if (value > maxValue) {
                    maxValue = value;
                }
            }
        }
        return foundValidValue ? maxValue : 0;
    }

    /**
     * Implementation of the filter method which filters the provided list
     * based on the condition LT4 (less than four)
     * and returns the list with the float values which satisfy the condition.
     *
     * @param inputList a list of Float values to be filtered
     * @return a filtered list result with the elements from the list that satisfy the condition,
     * or empty list if the input list is empty or null
     */
    @Override
    public List<Float> filter(List<Float> inputList) {
        List<Float> filteredList = new ArrayList<>();
        if (inputList == null || inputList.isEmpty()) {
            return filteredList;
        }
        for (Float value : inputList) {
            if (value != null && value < 4) {
                filteredList.add(value);
            }
        }
        return filteredList;
    }
}
