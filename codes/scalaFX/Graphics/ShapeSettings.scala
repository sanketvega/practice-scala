/*
 This code is from a book 
 <Introduction to Programming and Problem-solving Using Scala>
 written by Mark C. Lewis
*/

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.paint._
import scalafx.scene.shape._

val app = new JFXApp {
    stage = new JFXApp.PrimaryStage {
      title = "Shape Setting"
      scene = new Scene(500, 300) {
        val polygon1 = Polygon(10, 10, 100, 190, 190, 100)
        polygon1.fill = Color.Green
        polygon1.stroke = Color.Black
        polygon1.strokeWidth = 5
        polygon1.strokeType = StrokeType.Centered
        polygon1.strokeLineJoin = StrokeLineJoin.Bevel

        val polygon2 = Polygon(160, 10, 250, 190, 340, 100)
        polygon2.fill = LinearGradient(160, 10, 340, 100, false, scalafx.scene.paint.CycleMethod.NoCycle,
          Stop(0.0, Color.White), Stop(0.3, Color.Cyan), Stop(0.7, Color.Blue), Stop(1.0, Color.Blue))
        polygon2.stroke = Color.Black
        polygon2.strokeWidth = 5
        polygon2.strokeType = StrokeType.Inside
        polygon2.strokeLineJoin = StrokeLineJoin.Miter

        val polygon3 = Polygon(310, 10, 400, 190, 490, 100)
        polygon3.fill = RadialGradient(45, 0.5, 400, 100, 50, false,
            scalafx.scene.paint.CycleMethod.Reflect, Stop(0.0, Color.White), Stop(0.5, Color.Red),
            Stop(1.0, Color.Black))
        polygon3.stroke = Color.Black
        polygon3.strokeWidth = 5
        polygon3.strokeType = StrokeType.Outside
        polygon3.strokeLineJoin = StrokeLineJoin.Round

        val line1 = Line(50, 220, 450, 220)
        line1.stroke = Color.Black
        line1.strokeWidth = 5
        line1.strokeLineCap = StrokeLineCap.Butt
        line1.strokeDashArray = List(30, 10, 20, 15)

        val line2 = Line(50, 250, 450, 250)
        line2.stroke = Color.Black
        line2.strokeWidth = 5
        line2.strokeLineCap = StrokeLineCap.Round
        line2.strokeDashArray = List(30, 10, 20, 15)

        val line3 = Line(50, 280, 450, 280)
        line3.stroke = Color.Black
        line3.strokeWidth = 5
        line3.strokeLineCap = StrokeLineCap.Square
        line3.strokeDashArray = List(30, 10, 20, 15)

        content = List(polygon1, polygon2, polygon3, line1, line2, line3)
      }
    }
  }

  app.main(args)