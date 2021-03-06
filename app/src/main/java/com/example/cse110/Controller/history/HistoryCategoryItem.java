package com.example.cse110.Controller.history;

/**
 * Wrapper class to display data in History page's list.
 * @author Peter Gonzalez
 * @version 4.23
 */
public class HistoryCategoryItem {

    //Declare our local variables, passed into constructor

    /**
     * Hold's the name of the Category this item displays.
     */
    private String name;

    /**
     * Hold's the budget of the Category this item displays.
     */
    private int budget;

    /**
     * Hold's the total value of expenses in the Category this item displays.
     */
    private  double totalExpenses;

    //Constructor

    /**
     * The only constructor for a HistoryItem. Hold's all information to display for one Category.
     * @param name The name of the Category.
     * @param budget The budget of the Category.
     * @param totalExpenses The sum of the total value of expenses for one Category.
     */
    public HistoryCategoryItem(String name, int budget, double totalExpenses){
        this.name = name;
        this.budget = budget;
        this.totalExpenses = totalExpenses;
    }

    //Getters


    /**
     * Get the value of the total expenses.
     * @return The value this item is holding for the Category's expenses.
     */
    public double getTotalExpenses() {
        return totalExpenses;
    }

    public String getFormattedTotalExpenses(){
        String costString = Double.toString(getTotalExpenses());

        // Add formatting for whole numbers
        if(costString.indexOf('.') == -1){
            costString = costString.concat(".00");
        }else{
            //Ensure only valid input
            int costLength = costString.length();
            int decimalPlace = costString.indexOf(".");

            // If the user inputs a number formatted as "<num>.", appends a 00 after the decimal
            if (costLength - decimalPlace == 1) {
                costString = costString.substring(0, decimalPlace + 1) +  "00";
            }
            // If the user inputs a number formatted as "<num>.1", where 1 could be any number,
            // appends a 0 to the end
            else if (costLength - decimalPlace == 2) {
                costString = costString.substring(0, decimalPlace + 1 + 1) + "0";
            }
            // If the user inputs a number with >= 2 decimal places, only displays up to 2
            else {
                costString = costString.substring(0, costString.indexOf(".") + 2 + 1);
            }
        }
        return costString;

    }

    /**
     * Get the value of the Category's budget.
     * @return The value this item is holding for the Category's budget.
     */
    public int getBudget() {
        return budget;
    }

    /**
     * Get the name of the Category this item displays.
     * @return The value this item is holding for the Category's budget.
     */
    public String getName(){
        return name;
    }
}
