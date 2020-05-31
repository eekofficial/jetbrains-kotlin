import java.util.Scanner

enum class State(action: String) {
    Action("choosing an action"),
    CoffeeType("choosing a variant of coffee"),
    AddWater("adding water in coffee machine"),
    AddMilk("adding milk in coffee machine"),
    AddCoffee("adding coffee beans in coffee machine"),
    AddCups("adding disposible cups in coffee machine");
}

enum class Drink(val water: Int, val milk: Int, val coffee: Int, val cost: Int) {
    Espresso(250, 0, 16, 4),
    Latte(350, 75, 20, 7),
    Cappucino(200, 100, 12, 6)
}

object CoffeeMachine {
    var WATER_IN_MACHINE = 400
    var MILK_IN_MACHINE = 540
    var COFFEE_IN_MACHINE = 120
    var CUPS_IN_MACHINE = 9
    var MONEY_IN_MACHINE = 550
    var CURRENT_STATE = State.Action

    fun enough_resources(drink: Drink) : Boolean {
        if (WATER_IN_MACHINE - drink.water < 0) {
            println("Sorry, not enough water!")
            return false
        }
        if (MILK_IN_MACHINE - drink.milk < 0) {
            println("Sorry, not enough milk!")
            return false
        }
        if (COFFEE_IN_MACHINE - drink.coffee < 0) {
            println("Sorry, not enough coffee beans!")
            return false
        }
        if (CUPS_IN_MACHINE <= 0) {
            println("Sorry, not enough disposable cups!")
            return false
        }
        return true
    }

    fun buyCoffee(drink: Drink) {
        if (enough_resources(drink)) {
            WATER_IN_MACHINE -= drink.water
            MILK_IN_MACHINE -= drink.milk
            COFFEE_IN_MACHINE -= drink.coffee
            CUPS_IN_MACHINE -= 1
            MONEY_IN_MACHINE += drink.cost
            println("I have enough resources, making you a coffee!")
        }
        CURRENT_STATE = State.Action
    }

    fun fill(unit: Int) {
        if (CURRENT_STATE == State.AddWater) {
            WATER_IN_MACHINE += unit
            CURRENT_STATE = State.AddMilk
        } else if (CURRENT_STATE == State.AddMilk) {
            MILK_IN_MACHINE += unit
            CURRENT_STATE = State.AddCoffee
        } else if (CURRENT_STATE == State.AddCoffee) {
            COFFEE_IN_MACHINE += unit
            CURRENT_STATE = State.AddCups
        } else if (CURRENT_STATE == State.AddCups) {
            CUPS_IN_MACHINE += unit
            CURRENT_STATE = State.Action
        }
    }

    fun take() {
        println("I gave you \$$MONEY_IN_MACHINE")
        MONEY_IN_MACHINE = 0
    }

    fun current_stock() {
        println("""
        The coffee machine has:
        $WATER_IN_MACHINE of water
        $MILK_IN_MACHINE of milk
        $COFFEE_IN_MACHINE of coffee beans
        $CUPS_IN_MACHINE of disposable cups
        $MONEY_IN_MACHINE of money
    """.trimIndent())
    }

    fun process_input(input: String) : Boolean {
        if (CURRENT_STATE == State.Action) {
            when (input) {
                "buy" -> CURRENT_STATE = State.CoffeeType
                "fill" -> CURRENT_STATE = State.AddWater
                "take" -> take()
                "remaining" -> current_stock()
                "exit" -> return false
            }
        } else if (CURRENT_STATE == State.CoffeeType) {
            when (input) {
                "1" -> buyCoffee(Drink.Espresso)
                "2" -> buyCoffee(Drink.Latte)
                "3" -> buyCoffee(Drink.Cappucino)
                "back" -> CURRENT_STATE = State.Action
            }
        } else if (CURRENT_STATE == State.AddWater ||
                CURRENT_STATE == State.AddMilk ||
                CURRENT_STATE == State.AddCoffee ||
                CURRENT_STATE == State.AddCups) {
            val units: Int = input.toInt()
            fill(units)
        }
        return true
}
}

val scanner = Scanner(System.`in`)

fun main() {
    do {
        if (CoffeeMachine.CURRENT_STATE == State.Action) {
            println("Write action (buy, fill, take, remaining, exit):")
        } else if (CoffeeMachine.CURRENT_STATE == State.CoffeeType) {
            println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino:")
        } else if (CoffeeMachine.CURRENT_STATE == State.AddWater) {
            println("Write how many ml of water do you want to add:")
        } else if (CoffeeMachine.CURRENT_STATE == State.AddMilk) {
            println("Write how many ml of milk do you want to add:")
        } else if (CoffeeMachine.CURRENT_STATE == State.AddCoffee) {
            println("Write how many grams of coffee beans do you want to add:")
        } else if (CoffeeMachine.CURRENT_STATE == State.AddCups) {
            println("Write how many disposable cups of coffee do you want to add:")
        }

        var option = scanner.next()
    } while (CoffeeMachine.process_input(option))
}

