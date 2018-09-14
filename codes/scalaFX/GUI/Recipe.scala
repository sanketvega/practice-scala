/*
 This code is from a book 
 <Introduction to Programming and Problem-solving Using Scala>
 written by Mark C. Lewis
*/

import scalafx.Includes._
import scalafx.scene.Scene
import scalafx.application.JFXApp
import scalafx.scene.control._
import scalafx.scene.layout._
import scalafx.event.ActionEvent
import scalafx.scene.input.KeyCode
import scalafx.scene.input.KeyCombination
import scalafx.scene.input.KeyCodeCombination
import scalafx.stage.FileChooser
import scalafx.collections.ObservableBuffer
import java.io.PrintWriter

case class Ingredient(name:String, amount:String)
case class Recipe(name:String, ingredients:List[Ingredient], directions:String)
  val app = new JFXApp {
    stage = new JFXApp.PrimaryStage {
      title = "Recipes"
      scene = new Scene(600, 400) {
        val menuBar = new MenuBar
        val fileMenu = new Menu("File")
        val openItem = new MenuItem("Open")
        openItem.accelerator = new KeyCodeCombination(KeyCode.O,
          KeyCombination.ControlDown)
        openItem.onAction = (e:ActionEvent) => { openFile }
        val saveItem = new MenuItem("Save")
        saveItem.accelerator = new KeyCodeCombination(KeyCode.S,
          KeyCombination.ControlDown)
        saveItem.onAction = (e:ActionEvent) => { saveFile }
        val exitItem = new MenuItem("Exit")
        exitItem.accelerator = new KeyCodeCombination(KeyCode.X,
          KeyCombination.ControlDown)
        exitItem.onAction = (e:ActionEvent) => { System.exit(0)}
        fileMenu.items = List(openItem, saveItem, new SeparatorMenuItem, exitItem)

        val recipeMenu = new Menu("Recipe")
        val addItem = new MenuItem("Add")
        addItem.onAction = (e:ActionEvent) => { addRecipe }
        val removeItem = new MenuItem("Remove")
        removeItem.onAction = (e:ActionEvent) => { removeRecipe }
        recipeMenu.items = List(addItem, removeItem)
        menuBar.menus = List(fileMenu, recipeMenu)

        // Recipe List
        var recipes = Array(Recipe("Pop Tarts", List(Ingredient("Pop Tart",
          "1 packet")), "Toast the poptarts ...\nor don't"))

        val recipeList = new ListView(recipes.map(_.name))
        recipeList.selectionModel.value.selectedIndex.onChange {
          val index = recipeList.selectionModel.value.selectedIndex.value
          if (index >= 0) setFields(recipes(index))
        }

        // Ingredients stuff
        val addButton = new Button("Add")
        addButton.onAction = (ae:ActionEvent) => addIngredient
        val removeButton = new Button("Remove")
        removeButton.onAction = (ae:ActionEvent) => removeIngredient
        val ingredientsList = new ListView[String]()
        val ingredientNameField = new TextField
        val amountField = new TextField
        val ingredientsGrid = new GridPane
        ingredientsGrid.children += addButton
        GridPane.setConstraints(addButton,0,0)
        ingredientsGrid.children += removeButton
        GridPane.setConstraints(removeButton,1,0)
        ingredientsGrid.children += ingredientsList
        GridPane.setConstraints(ingredientsList,0,1,2,3)
        val nameLabel = new Label("Name:")
        ingredientsGrid.children += nameLabel
        GridPane.setConstraints(nameLabel,3,0)
        val amountLabel = new Label("Amount:")
        ingredientsGrid.children += amountLabel
        GridPane.setConstraints(amountLabel,3,2)
        ingredientsGrid.children += ingredientNameField
        GridPane.setConstraints(ingredientNameField,4,0)
        ingredientsGrid.children += amountField
        GridPane.setConstraints(amountField,4,2)
        ingredientsList.selectionModel.value.selectedItem.onChange {
          val recipeIndex = recipeList.selectionModel.value.selectedIndex.value
          val ingredientIndex = ingredientsList.selectionModel.value.selectedIndex.value
          if (recipeIndex>=0 && ingredientIndex>=0) {
            ingredientNameField.text = recipes(recipeIndex).ingredients(ingredientIndex).name
            amountField.text = recipes(recipeIndex).ingredients(ingredientIndex).amount
          }
        }
        ingredientNameField.text.onChange {
          val newName = ingredientNameField.text.value
          alterSelectedIngredient(i => i.copy(name = newName))
          val ingredientIndex = ingredientsList.selectionModel.value.selectedIndex.value
          if (ingredientIndex >= 0) ingredientsList.items.value(ingredientIndex) = newName
        }
        amountField.text.onChange {
          alterSelectedIngredient(i => i.copy(amount = amountField.text.value))
        }
        // Directions
        val directionArea = new TextArea
        directionArea.text.onChange {
          val recipeIndex = recipeList.selectionModel.value.selectedIndex.value
          if (recipeIndex>=0) {
            recipes(recipeIndex) = recipes(recipeIndex).copy(directions = directionArea.text.value)
          }
        }

        val splitPane = new SplitPane
        splitPane.orientation = scalafx.geometry.Orientation.Vertical
        splitPane.items += ingredientsGrid
        splitPane.items += directionArea

        // Top level layout
        val topBorderPane = new BorderPane
        topBorderPane.top = menuBar
        topBorderPane.left = recipeList
        topBorderPane.center = splitPane

        root = topBorderPane

        def openFile:Unit = {
          val chooser = new FileChooser
          val selected = chooser.showOpenDialog(stage)
          if (selected != null) {
            val src = io.Source.fromFile(selected)
            val lines = src.getLines
            recipes = Array.fill(lines.next.toInt)(Recipe(
              lines.next,
              List.fill(lines.next.toInt)(Ingredient(lines.next,lines.next)),
              {
                var dir = ""
                var line = lines.next
                while (line!=".") {
                  dir += (if (dir.isEmpty) "" else "\n")+line
                  line = lines.next
                }
                dir
              }
            ))
            src.close()
            recipeList.items = ObservableBuffer(recipes.map(_.name):_*)
            recipeList.selectionModel.value.selectFirst
            setFields(recipes.head)
          }
        }

        def saveFile:Unit = {
          var chooser = new FileChooser
          val selected = chooser.showSaveDialog(stage)
          if (selected!=null) {
            val pw = new PrintWriter(selected)
            pw.println(recipes.length)
            for (r<-recipes) {
              pw.println(r.name)
              pw.println(r.ingredients.length)
              for (ing <- r.ingredients) {
                pw.println(ing.name)
                pw.println(ing.amount)
              }
              pw.println(r.directions)
              pw.println(".")
            }
            pw.close()
          }
        }

        def addRecipe:Unit = {
          val dialog = new TextInputDialog
          dialog.title = "Recipe Name"
          dialog.headerText = "Question?"
          dialog.contentText = "What is the name of the new recipe?"
          dialog.showAndWait().foreach { name =>
            recipes = recipes :+ Recipe(name,
              List(Ingredient("ingredient", "amount")), "Directions")
            recipeList.items = ObservableBuffer(recipes.map(_.name):_*)
            recipeList.selectionModel.value.clearAndSelect(recipes.length-1)
            setFields(recipes.last)
          }
        }

        def removeRecipe:Unit = {
          if (!recipeList.selectionModel.value.selectedItems.isEmpty) {
            recipes = recipes.patch(recipeList.selectionModel.value.selectedIndex.value, Nil, 1)
            if (recipes.isEmpty) {
              recipes = Array(Recipe("New recipe",
                List(Ingredient("ingredient", "amount")), "Directions"))
            }
            recipeList.items = ObservableBuffer(recipes.map(_.name):_*)
            recipeList.selectionModel.value.clearAndSelect(0)
            setFields(recipes.head)
          }
        }

        def addIngredient:Unit = {
          val recipeIndex = recipeList.selectionModel.value.selectedIndex.value
          if (recipeIndex>=0) {
            val newIngr = Ingredient("Stuff", "Some")
            recipes(recipeIndex) = recipes(recipeIndex).copy(
              ingredients = recipes(recipeIndex).ingredients :+ newIngr)
            ingredientsList.items.value += newIngr.name
          }
        }

        def removeIngredient:Unit = {
          val recipeIndex = recipeList.selectionModel.value.selectedIndex.value
          val ingredientIndex = ingredientsList.selectionModel.value.selectedIndex.value
          if (recipeIndex >= 0 && ingredientIndex>=0) {
            recipes(recipeIndex) = recipes(recipeIndex).copy(ingredients =
              recipes(recipeIndex).ingredients.patch(ingredientIndex,Nil,1))
            setFields(recipes(recipeIndex))
          }
        }

        def setFields(r:Recipe):Unit = {
          ingredientsList.items.value.clear
          ingredientsList.items.value ++= r.ingredients.map(_.name)
          directionArea.text = r.directions
          ingredientNameField.text = ""
          amountField.text = ""
        }

        def alterSelectedIngredient(diff: Ingredient => Ingredient):Unit = {
          val recipeIndex = recipeList.selectionModel.value.selectedIndex.value
          val ingredientIndex = ingredientsList.selectionModel.value.selectedIndex.value
          if (recipeIndex>=0 && ingredientIndex>=0) {
            val newIngredient = diff(recipes(recipeIndex).ingredients(ingredientIndex))
            val newRecipe = recipes(recipeIndex).copy(ingredients =
              recipes(recipeIndex).ingredients.updated(ingredientIndex, newIngredient))
            recipes(recipeIndex) = newRecipe
          }
        }
      }
    }
  }

  app.main(args)