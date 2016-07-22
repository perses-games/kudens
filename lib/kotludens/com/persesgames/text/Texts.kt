package com.persesgames.text

import com.persesgames.game.Game

/**
 * Created by rnentjes on 16-5-16.
 */

object Texts {

    fun drawText(x: Float, y: Float, message: String, font: String = "bold 24pt Arial", fillStyle: String = "white") {
        Game.html.canvas2d.fillStyle = fillStyle
        Game.html.canvas2d.font = font
        Game.html.canvas2d.fillText(message, x.toDouble(), y.toDouble())
    }

    fun drawLeftTop(left: Float, top: Float, message: String, font: String = "bold 24pt Arial", fillStyle: String = "white") {
        drawText(
          Game.view.gameToScreenCoordX(-Game.view.width / 2f + left),
          Game.view.gameToScreenCoordY(Game.view.height / 2f - top),
          message,
          font,
          fillStyle
        )
    }
}
