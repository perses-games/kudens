package games.perses.text

import games.perses.game.Game

/**
 * Created by rnentjes on 16-5-16.
 */
object Texts {

  // TODO: use same coords for webgl and canvas 2d
  fun drawText(
      x: Float,
      y: Float,
      message: String,
      font: String = "bold 24pt Arial",
      fillStyle: String = "white"
  ) {
    var yy = y
    var xx = x
    if (yy < 0) {
      yy += Game.view.height
    }
    if (xx < 0) {
      xx += Game.view.width
    }
    yy = Game.view.height - yy

    Game.html.canvas2d.fillStyle = fillStyle
    Game.html.canvas2d.font = font
    Game.html.canvas2d.fillText(message, x.toDouble(), yy.toDouble())
  }

  fun drawLeftTop(
      left: Float,
      top: Float,
      message: String,
      font: String = "bold 24pt Arial",
      fillStyle: String = "white"
  ) {
    drawText(
        left,
        /* Game.view.height - */ top,
        message,
        font,
        fillStyle
    )
  }

}
