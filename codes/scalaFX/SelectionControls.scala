val app = new JFXApp {
    stage = new application.JFXApp.PrimaryStage {
      title = "Selection Controls"
      scene = new Scene(667, 200) {
        // Label
        val label = new Label("For display purposes.")
        label.layoutX = 20
        label.layoutY = 20

        // ChoiceBox
        val choiceBox = new ChoiceBox(ObservableBuffer("Choice 1", "Choice 2", "Choice 3"))
        choiceBox.layoutX = 20
        choiceBox.layoutY = 50

        // ComboBox
        val comboBox = new ComboBox(List("Combo 1", "Combo 2", "Combo 3"))
        comboBox.layoutX = 20
        comboBox.layoutY = 80

        // ListView
        val listView = new ListView(List("List 1", "List 2", "List 3"))
        listView.layoutX = 353
        listView.layoutY = 20
        listView.prefHeight = 160

        content = List(label, choiceBox, comboBox, listView)

        choiceBox.value.onChange {
          label.text = "Choice selected : " + choiceBox.value.value
        }

        comboBox.onAction = (e:ActionEvent) => {
          label.text = "Combo box selected : " + comboBox.value.value
        }

        listView.selectionModel.value.selectedItem.onChange {
          label.text = "List view selected : " +
            listView.selectionModel.value.selectedItem.value
        }
      }
    }
  }

  app.main(args)